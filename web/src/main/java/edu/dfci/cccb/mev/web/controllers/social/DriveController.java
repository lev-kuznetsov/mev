/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.web.controllers.social;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import lombok.Synchronized;

import org.springframework.context.annotation.Scope;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.drive.DriveFile;
import org.springframework.social.google.api.drive.DriveFilesPage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.web.domain.social.Drive;
import edu.dfci.cccb.mev.web.domain.social.SecurityContext;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/import/google")
@Scope ("session")
public class DriveController {

  private Map<String, DriveFile> files = null;
  private @Inject Google google;
  private @Inject Workspace workspace;
  private @Inject DatasetBuilder builder;

  private void loadFiles () {
    files = new HashMap<> ();
    String nextPageToken = "";
    do {
      DriveFilesPage page = google.driveOperations ().getRootFiles (nextPageToken);
      List<DriveFile> files = page.getItems ();
      for (DriveFile file : files)
        if (!file.isFolder ())
          this.files.put (file.getTitle (), file);
      nextPageToken = page.getNextPageToken ();
    } while (nextPageToken != null);
  }

  @RequestMapping (method = RequestMethod.GET)
  @Synchronized
  public Drive drive () {
    if (SecurityContext.userSignedIn ()) {
      if (files == null)
        loadFiles ();
      return new Drive (true, files.keySet ().toArray (new String[0]));
    } else {
      return new Drive (false, null);
    }
  }

  @RequestMapping (method = RequestMethod.POST, value = "/{id}")
  public void load (@PathVariable ("id") String id) throws DatasetBuilderException,
                                                   InvalidDatasetNameException,
                                                   InvalidDimensionTypeException,
                                                   IOException {
    workspace.put (builder.build (new MockTsvInput (id, google.driveOperations ()
                                                              .downloadFile (files.get (id))
                                                              .getFile ())));
  }
}
