package edu.dfci.cccb.mev.presets.tools.loader;

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
import edu.dfci.cccb.mev.annotation.loader.AnnotationLoader;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsFilesConfiguration;
import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetDomainBuildersConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import edu.dfci.cccb.mev.presets.util.timer.Timer;
import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.*;
import org.apache.tools.tar.TarOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * Created by antony on 5/24/16.
 */
@Log4j
@Component
public class AnnotationLoaderApp {

    @Inject Presets presets;
    AnnotationLoader loader;
    static AnnotationConfigApplicationContext springContext;
    public static void main (String[] args) throws Exception {
        try(final AnnotationConfigApplicationContext springContext = new AnnotationConfigApplicationContext ()){
            AnnotationLoaderApp.springContext=springContext;
//      springContext.register(DatasetDomainBuildersConfiguration.class);
//      springContext.register (DatasetRestConfiguration.class);
//      springContext.register (PresetsRestConfiguration.class);
            springContext.register (PresetsLoaderConfiguration.class);
            springContext.register (AnnotationLoaderConfiguration.class);
//            springContext.register (PresetsRestConfiguration.class);
            springContext.register (ProbeAnnotationsFilesConfiguration.class);
//      springContext.register (ProbeAnnotationsConfigurationMain.class);

            springContext.refresh ();
            final AnnotationLoaderApp app = springContext.getBean (AnnotationLoaderApp.class);
            app.run (args);
        }
    }
    private boolean checkInclude(String[] includeFilters, Preset preset){
        for(String filter : includeFilters){
            if(preset.descriptor ().dataUrl ().toString ().contains (filter))
                return true;
        }
        return false;
    }
    public void run(String[] args) throws IOException, ParseException {
        // create Options object
        Options options = new Options();
        // add t option
        options.addOption("k", "key-column-name", true, "name of the unique key column");
        options.addOption("f", "file-mask", true, "comma-separated list of files to include");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);

        String keyColumnName = cmd.getOptionValue("k");
        Iterator<Option> it = cmd.iterator();
        while(it.hasNext()){
            Option option = it.next();
            log.info(String.format("Argument %s: %s", option.getId(), option.getValue()));
        }
        String[] includeFilters = cmd.getOptionValues("f");
        if(includeFilters==null)
            includeFilters = new String[]{};

        loader = springContext.getBean(AnnotationLoader.class);
        Timer timerTotal = Timer.start("total import time");
        for(Preset preset : presets.getAll ()){
            if(includeFilters.length>0 && checkInclude (includeFilters, preset)==false)
                continue;
            try {
                log.info (preset.descriptor ().columnUrl());
                Timer timer = Timer.start ("import "+preset.descriptor ().columnUrl());
                loader.load (preset.descriptor().columnSourceUrl(), preset.descriptor().columnUrl(), keyColumnName);
                timer.read();
            } catch (Exception e) {
                log.error ("Error while loading preset "+preset.descriptor ().columnUrl ());
                e.printStackTrace();
            }
        }

        timerTotal.read ();
        log.info ("exit");
    }

}
