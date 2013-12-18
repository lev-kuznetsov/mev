/*

Copyright 2010, Google Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
    * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,           
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY           
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.google.refine.commands.project;

import static com.google.refine.io.FileProjectManager.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.google.refine.ProjectManager;
import com.google.refine.ProjectMetadata;
import com.google.refine.commands.Command;
import com.google.refine.model.Project;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;

public class GetProjectMetadataCommand extends Command {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Project project;
            try {
                project = getProject(request);
            } catch (ServletException e) {
                respond(response, "error", e.getLocalizedMessage());
                return;
            }
            
            ProjectMetadata metadata = ProjectManager.getSingleton().getProjectMetadata(project.id);
            metadata.setCustomMetadata ("selectionName", "");
            metadata.setCustomMetadata ("selectionDescription", "");
            metadata.setCustomMetadata ("selectionColor", "");
            metadata.setCustomMetadata ("selectionDescription", "");
            metadata.setCustomMetadata ("selectionFacetLink", "");                    
            
            Dataset dataset = (Dataset) request.getAttribute (REQUEST_ATTEIBUTE_DATASET);
            if(dataset!=null){
              metadata.setCustomMetadata ("datasetName", dataset.name ());
              Dimension.Type dimensionType = Dimension.Type.from ((String)request.getAttribute (REQUEST_ATTEIBUTE_DIMENSION));              
              Dimension dimension = dataset.dimension (dimensionType);
              if(dimension!=null){
                metadata.setCustomMetadata ("dimension", dimension.type ().name ());                
                String selectionName = (String)request.getAttribute (REQUEST_ATTEIBUTE_SELECTIONNAME);
                if(selectionName!=null){
                  metadata.setCustomMetadata ("selectionName", selectionName);
                  Selection selection = dimension.selections ().get (selectionName);
                  if(selectionName!=null){
                    metadata.setCustomMetadata ("selectionDescription", selection.properties ().getProperty ("selectionDescription"));
                    metadata.setCustomMetadata ("selectionColor", selection.properties ().getProperty ("selectionColor"));
                    metadata.setCustomMetadata ("selectionDescription", selection.properties ().getProperty ("selectionDescription"));
                    metadata.setCustomMetadata ("selectionFacetLink", selection.properties ().getProperty ("selectionFacetLink"));                    
                  }
                }
              }
            }
            
            respondJSON(response, metadata);
        } catch (JSONException e) {
            respondException(response, e);
        } catch (SelectionNotFoundException e) {          
          e.printStackTrace();
          respondException(response, e);
        } catch (InvalidDimensionTypeException e) {
          e.printStackTrace();
          respondException(response, e);
        }
    }
}
