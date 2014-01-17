package com.google.refine.commands.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.refine.ProjectManager;
import com.google.refine.ProjectMetadata;
import com.google.refine.commands.Command;
import com.google.refine.model.Project;
import com.google.refine.util.ParsingUtilities;

public class ImportServerProjectCommand extends Command {

  @Override
  public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    ProjectManager.getSingleton().setBusy(true);
    try {
        Properties options = ParsingUtilities.parseUrlParameters(request);

        long projectID = Project.generateID();
        logger.info("Importing existing project using new ID {}", projectID);
        File file = new File("/tmp/textxxx/test.gz");
        String fileName = file.getName().toLowerCase();
        InputStream stream = new FileInputStream (file);
        
        ProjectManager.getSingleton().importProject(projectID, stream, !fileName.endsWith(".tar"));
        ProjectManager.getSingleton().loadProjectMetadata(projectID);

        ProjectMetadata pm = ProjectManager.getSingleton().getProjectMetadata(projectID);
        if (pm != null) {
            if (options.containsKey("project-name")) {
                String projectName = options.getProperty("project-name");
                if (projectName != null && projectName.length() > 0) {
                    pm.setName(projectName);
                }
            }

            redirect(response, "/annotations/openrefine/project?project=" + projectID);
        } else {
            respondWithErrorPage(request, response, "Failed to import project. Reason unknown.", null);
        }
    } catch (Exception e) {
        respondWithErrorPage(request, response, "Failed to import project", e);
    } finally {
        ProjectManager.getSingleton().setBusy(false);
    }
  }
  
}
