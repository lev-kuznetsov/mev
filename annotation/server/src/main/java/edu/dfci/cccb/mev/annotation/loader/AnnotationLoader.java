package edu.dfci.cccb.mev.annotation.loader;

import com.google.refine.ProjectManager;
import com.google.refine.ProjectMetadata;
import com.google.refine.SessionWorkspaceDir;
import com.google.refine.importers.SeparatorBasedImporter;
import com.google.refine.importing.ImportingJob;
import com.google.refine.importing.ImportingManager;
import com.google.refine.importing.ImportingUtilities;
import com.google.refine.io.FileProjectManager;
import com.google.refine.model.Project;
import com.google.refine.util.JSONUtilities;
import com.google.refine.util.ParsingUtilities;
import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import lombok.extern.log4j.Log4j;
import org.apache.tools.tar.TarOutputStream;
import org.h2.store.fs.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static edu.dfci.cccb.mev.annotation.loader.AnnotationLoaderApp.gzipTarToOutputStream;

/**
 * Created by antony on 5/25/16.
 */
@Log4j
public class AnnotationLoader {
    private FileProjectManager projectManager;

    public AnnotationLoader() throws IOException {
        projectManager = new FileProjectManager ();
        projectManager.setWorkspaceDir (new SessionWorkspaceDir());
        ProjectManager.setSingleton (projectManager);
        ImportingManager.registerFormat(
                "text/line-based/*sv", "CSV / TSV / separator-based files",
                "SeparatorBasedParserUI",
                new SeparatorBasedImporter()
        );
    }

    private ImportingJob initJob() throws Exception {
        ImportingJob job = ImportingManager.createJob();
        JSONObject config = job.getOrCreateDefaultConfig();
        JSONObject retrievalRecord = new JSONObject ();
        JSONUtilities.safePut (config, "retrievalRecord", retrievalRecord);

        if (!("new".equals(config.getString("state")))) {
            throw new Exception("Job already started; cannot load more data");
        }

        return job;
    }

    private void stageData(ImportingJob job, URL importUrl) throws IOException {
        JSONObject metadataJson = ParsingUtilities.evaluateJsonStringToObject("{\"name\":\""+FileUtils.getName(importUrl.getFile())+"\",\"created\":\"2015-04-16T04:47:46Z\",\"modified\":\"2015-04-16T04:47:46Z\",\"customMetadata\":{},\"password\":null,\"encoding\":\"UTF-8\",\"encodingConfidence\":0,\"preferences\":{\"entries\":{\"scripting.starred-expressions\":{\"class\":\"com.google.refine.preference.TopList\",\"top\":2147483647,\"list\":[]},\"scripting.expressions\":{\"class\":\"com.google.refine.preference.TopList\",\"top\":100,\"list\":[]}}}}");
        job.metadata = ProjectMetadata.loadFromJSON(metadataJson);

        JSONObject config = job.getOrCreateDefaultConfig();
        JSONUtilities.safePut (config, "state", "loading-raw-data");

        final JSONObject progress = new JSONObject ();
        JSONUtilities.safePut (config, "progress", progress);
        addFile(job, importUrl);
        config.remove ("progress");

        JSONObject retrievalRecord = job.getRetrievalRecord();
        JSONUtilities.safePut (retrievalRecord, "uploadCount", 0);
        JSONUtilities.safePut (retrievalRecord, "downloadCount", 0);
        JSONUtilities.safePut (retrievalRecord, "clipboardCount", 1);
        JSONUtilities.safePut (retrievalRecord, "archiveCount", 0);

        JSONUtilities.safePut (config, "state", "ready");
        JSONUtilities.safePut (config, "hasData", true);

        JSONArray fileSelectionIndexes = new JSONArray ();
        JSONUtilities.safePut (config, "fileSelection", fileSelectionIndexes);
        String bestFormat = ImportingUtilities.autoSelectFiles (job, retrievalRecord, fileSelectionIndexes);

        JSONUtilities.safePut (config, "state", "ready");
        JSONUtilities.safePut (config, "hasData", true);

        job.touch();
        job.updating=false;

    }

    private void addFile(ImportingJob job, URL importUrl) throws IOException {
        JSONObject retrievalRecord = job.getRetrievalRecord();
        JSONArray fileRecords = new JSONArray ();
        JSONUtilities.safePut (retrievalRecord, "files", fileRecords);

        InputStream stream = importUrl.openStream();
        String encoding = "UTF-8";

        //save file
        File rawDataDir = job.getRawDataDir ();
        File file = ImportingUtilities.allocateFile (rawDataDir, "clipboard.txt");

        JSONObject fileRecord = new JSONObject ();
        JSONUtilities.safePut (fileRecord, "origin", "clipboard");
        JSONUtilities.safePut (fileRecord, "declaredEncoding", encoding);
        JSONUtilities.safePut (fileRecord, "declaredMimeType", (String) null);
        JSONUtilities.safePut (fileRecord, "format", "text");
        JSONUtilities.safePut (fileRecord, "fileName", "clipboard.txt");
        JSONUtilities.safePut (fileRecord, "location", ImportingUtilities.getRelativePath (file, rawDataDir));

        JSONUtilities.safePut (fileRecord, "size", ImportingUtilities.saveStreamToFile (stream, file, null));
        JSONUtilities.append (fileRecords, fileRecord);
    }

    private long createProject(ImportingJob job){
        job.touch();
        job.updating = true;
        String format = "text/line-based/*sv";
        JSONObject optionObj = ParsingUtilities.evaluateJsonStringToObject("{\"encoding\":\"\",\"separator\":\"\\\\t\",\"ignoreLines\":-1,\"headerLines\":1,\"skipDataLines\":0,\"limit\":-1,\"storeBlankRows\":true,\"guessCellValueTypes\":false,\"processQuotes\":true,\"storeBlankCellsAsNulls\":true,\"includeFileSources\":false,\"projectName\":\""+job.metadata.getName()+"\"}");
        List<Exception> exceptions = new LinkedList<Exception>();
        long projectId = ImportingUtilities.createProject(job, format, optionObj, exceptions, true);
        return projectId;
    }

    private void gzipTarToOutputStream(Project project, OutputStream os) throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        try {
            tarToOutputStream(project, gos);
        } finally {
            gos.close();
        }
    }

    private void tarToOutputStream(Project project, OutputStream os) throws IOException {
        TarOutputStream tos = new TarOutputStream(os);
        try {
            ProjectManager.getSingleton().exportProject(project.id, tos);
        } finally {
            tos.close();
        }
    }

    private void exportProject(long projectId, URL outputUrl, String keyColumnName) throws IOException, URISyntaxException {
        Project project = projectManager.getProject(projectId);
        if(keyColumnName!=null)
            project.getMetadata().setCustomMetadata("keyColumnName", keyColumnName);
        projectManager.save(true);
        Files.createDirectories(Paths.get(outputUrl.toURI()).getParent());
        OutputStream os = new FileOutputStream(outputUrl.getFile());
        gzipTarToOutputStream(project, os);
    }

    public void load(URL importUrl, URL outputUrl, String keyColumnName) throws Exception {
        ImportingJob job = initJob();
        stageData(job, importUrl);
        long id = createProject(job);
        exportProject(id, outputUrl, keyColumnName);
    }
    public void load(URL importUrl, URL outputUrl) throws Exception {
        load(importUrl, outputUrl, null);
    }

    public void load() throws Exception {
        load(
                new URL("file:///home/antony/work/danafarber/mev/prod-server/data/tcga/tcga_cleaned_datasets/LUAD/20150821-LUAD-RNAseqGene-Clinical.txt"),
//                (new File(projectManager.getWorkspaceDir(), "export.tar.gz")).toURI().toURL()
                new URL("file:///home/antony/mev/data/tcga2/openrefine/clinical/UCEC-clinical_annotations-tsv.openrefine.tar.gz")
        );

    }
}
