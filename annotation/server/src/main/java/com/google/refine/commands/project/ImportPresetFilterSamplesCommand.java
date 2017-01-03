package com.google.refine.commands.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.refine.ProjectManager;
import com.google.refine.ProjectMetadata;
import com.google.refine.browsing.Engine;
import com.google.refine.browsing.FilteredRows;
import com.google.refine.browsing.RowVisitor;
import com.google.refine.commands.Command;
import com.google.refine.model.Column;
import com.google.refine.model.Project;
import com.google.refine.model.Row;
import com.google.refine.operations.row.ImportPresetsRowRemovalOperation;
import com.google.refine.process.Process;

public class ImportPresetFilterSamplesCommand extends Command {
  final static protected Logger logger = LoggerFactory.getLogger("ImportPresetDatasetCommand");

  @Override
  public void doPost (final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    logger.info (String.format ("******************* Import Dataset: %s *******************",
                                request.getParameter ("import-preset")));
    ProjectManager.getSingleton ().setBusy (true);
    try {

      Project project = getProject (request);
      final Engine engine = getEngine (request, project);      
            
      final String newDatasetName = UUID.randomUUID ().toString ();
      final List<String> keys = new ArrayList<String> ();
      final List<Integer> unmatchedRowIndices = new ArrayList<Integer>();
      
      RowVisitor visitor = new RowVisitor () {
        Column theIdColumn;

        @Override
        public void start (Project project) {
            theIdColumn = project.getKeyColumn("annotationId", "id");
        }

        @Override
        public boolean visit (Project project, int rowIndex, Row row) {
          String cellData = row.getCell (theIdColumn.getCellIndex ()).value.toString ();
          if (cellData != null) {
            keys.add (cellData);
          }
          return false;
        }

        @Override
        public void end (Project project) {
//          Selection sourceSelection = new SimpleSelection (newDatasetName, properties, keys);
//          Dataset dataset=null;
          //File datafile = new File("/tmp/textxxx/presets/"+sourceDatasetName+"/"+sourceDatasetName+".tsv");
//          PresetDescriptor descriptor = (PresetDescriptor)request.getAttribute ("descriptor");
//          try {            
//            logger.info (String.format ("***Import Dataset: %s *******************", descriptor.dataUrl ().toString ()));
//            dataset = ProjectManager.getSingleton ().getDatasetBuilder ().build (descriptor, newDatasetName, sourceSelection);
            
//          } catch (DatasetBuilderException | InvalidDatasetNameException | InvalidDimensionTypeException e) {
//          } catch (PresetException e) {
//            e.printStackTrace();
//          }
          
          try {
            
//            ProjectManager.getSingleton ().getWorkspace ().put (dataset);
            ProjectMetadata pm = project.getMetadata ();;          
            pm.setName(newDatasetName);
                      
            ImportPresetsRowRemovalOperation op = new ImportPresetsRowRemovalOperation(getEngineConfig(request), unmatchedRowIndices);
            Process process = op.createProcess(project, new Properties());            
            project.processManager.queueProcess(process);            
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        @Override
        public boolean pass (Project project, int rowIndex, Row row) {
          unmatchedRowIndices.add(rowIndex);
          return false;
        }
      };

      
      
      FilteredRows filteredRows = engine.getAllFilteredRows ();
      filteredRows.accept (project, visitor);
      ProjectManager.getSingleton ().save (true);
      logger.info (String.format ("***Import Dataset Respone: OK"));
//      respond (response, "{ \"code\" : \"ok\" }");
      try {
          response.setCharacterEncoding("UTF-8");
          response.setHeader("Content-Type", "application/json");
          
          JSONWriter writer = new JSONWriter(response.getWriter());
          
          writer.object();
          writer.key("keys");
          writer.array ();
          for (String key : keys) {                            
              writer.value(key);              
          }
          writer.endArray();
          writer.key ("samplesprojname");
          writer.value(newDatasetName);
          writer.key("code");
          writer.value("ok");
          writer.endObject();
          
          request.getSession ().setAttribute ("samples", keys);
          request.getSession ().setAttribute ("samplesprojname", newDatasetName);
                    
      } catch (JSONException e) {
          respondException(response, e);
      }
      
    } catch (Exception e) {
      // Use generic error handling rather than our JSON handling
      // throw new ServletException(e);
      respondException (response, e);
    } finally {
      ProjectManager.getSingleton ().setBusy (false);
    }
  
  }
}
