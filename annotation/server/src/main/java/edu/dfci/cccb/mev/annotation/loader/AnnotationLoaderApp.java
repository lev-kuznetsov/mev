package edu.dfci.cccb.mev.annotation.loader;

import com.google.refine.ProjectManager;
import com.google.refine.SessionWorkspaceDir;
import com.google.refine.importers.SeparatorBasedImporter;
import com.google.refine.importing.ImportingJob;
import com.google.refine.importing.ImportingManager;
import com.google.refine.importing.ImportingUtilities;
import com.google.refine.io.FileProjectManager;
import com.google.refine.model.Project;
import com.google.refine.util.JSONUtilities;
import com.google.refine.util.ParsingUtilities;
import org.apache.tools.tar.TarOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * Created by antony on 5/24/16.
 */
public class AnnotationLoaderApp {

    private static void importData(ImportingJob job, JSONObject retrievalRecord) throws IOException {
        JSONObject config = job.getOrCreateDefaultConfig();

        final JSONObject progress = new JSONObject ();
        JSONUtilities.safePut (config, "progress", progress);

        JSONArray fileRecords = new JSONArray ();
        JSONUtilities.safePut (retrievalRecord, "files", fileRecords);

//        InputStream stream = new URL("file:///home/antony/mev/data/tcga2/tcga_data/UNC_UCEC_AgilentG4502A_07_3_l3/annotation.tsv").openStream();
        InputStream stream = new URL("file:///home/antony/work/danafarber/mev/prod-server/data/tcga/tcga_cleaned_datasets/LUAD/20150821-LUAD-RNAseqGene-Clinical.txt").openStream();
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

        JSONUtilities.safePut (config, "state", "ready");
        JSONUtilities.safePut (config, "hasData", true);
        config.remove ("progress");

        JSONUtilities.safePut (retrievalRecord, "uploadCount", 0);
        JSONUtilities.safePut (retrievalRecord, "downloadCount", 0);
        JSONUtilities.safePut (retrievalRecord, "clipboardCount", 1);
        JSONUtilities.safePut (retrievalRecord, "archiveCount", 0);
    }

    protected static void gzipTarToOutputStream(Project project, OutputStream os) throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        try {
            tarToOutputStream(project, gos);
        } finally {
            gos.close();
        }
    }


    protected static void tarToOutputStream(Project project, OutputStream os) throws IOException {
        TarOutputStream tos = new TarOutputStream(os);
        try {
            ProjectManager.getSingleton().exportProject(project.id, tos);
        } finally {
            tos.close();
        }
    }

    public static void main(String[] args) throws Exception {

        FileProjectManager projectManager = new FileProjectManager ();
        projectManager.setWorkspaceDir (new SessionWorkspaceDir());
        ProjectManager.setSingleton (projectManager);

        ImportingJob job = ImportingManager.createJob();
        JSONObject config = job.getOrCreateDefaultConfig();
        if (!("new".equals(config.getString("state")))) {
            throw new Exception("Job already started; cannot load more data");
        }

        JSONObject retrievalRecord = new JSONObject ();
        JSONUtilities.safePut (config, "retrievalRecord", retrievalRecord);
        JSONUtilities.safePut (config, "state", "loading-raw-data");

        importData(job, retrievalRecord );

        JSONArray fileSelectionIndexes = new JSONArray ();
        JSONUtilities.safePut (config, "fileSelection", fileSelectionIndexes);
        String bestFormat = ImportingUtilities.autoSelectFiles (job, retrievalRecord, fileSelectionIndexes);

        JSONUtilities.safePut (config, "state", "ready");
        JSONUtilities.safePut (config, "hasData", true);

        ImportingManager.registerFormat("text/line-based/*sv", "CSV / TSV / separator-based files", "SeparatorBasedParserUI",
                new SeparatorBasedImporter());

        job.touch();
        job.updating=false;

        job.touch();
        job.updating = true;
        String format = "text/line-based/*sv";
        JSONObject optionObj = ParsingUtilities.evaluateJsonStringToObject("{\"encoding\":\"\",\"separator\":\"\\\\t\",\"ignoreLines\":-1,\"headerLines\":1,\"skipDataLines\":0,\"limit\":-1,\"storeBlankRows\":true,\"guessCellValueTypes\":false,\"processQuotes\":true,\"storeBlankCellsAsNulls\":true,\"includeFileSources\":false,\"projectName\":\"mouse_test_data.tsvrow\"}");
        List<Exception> exceptions = new LinkedList<Exception>();
        long projectId = ImportingUtilities.createProject(job, format, optionObj, exceptions, true);
        projectManager.save(true);
        OutputStream os = new FileOutputStream(
                new File(projectManager.getWorkspaceDir(), "export.tar.gz"), true
        );
       gzipTarToOutputStream(projectManager.getProject(projectId), os);

    }
}
