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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
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
import edu.dfci.cccb.mev.io.implementation.TemporaryFile;
import edu.dfci.cccb.mev.web.domain.social.Drive;
import edu.dfci.cccb.mev.web.domain.social.SecurityContext;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/import/google")
public class DriveController {

  private @Inject Google google;
  private @Inject Workspace workspace;
  private @Inject DatasetBuilder builder;

  @RequestMapping (method = RequestMethod.GET)
  public Drive drive () {
    if (SecurityContext.userSignedIn ()) {
      List<edu.dfci.cccb.mev.web.domain.social.Drive.DriveFile> result = new ArrayList<> ();
      List<DriveFile> files;
      String nextPageToken = "";
      do {
        DriveFilesPage page = google.driveOperations ().getRootFiles (nextPageToken);
        files = page.getItems ();
        for (DriveFile file : files)
          if (!file.isFolder ())
            result.add (new edu.dfci.cccb.mev.web.domain.social.Drive.DriveFile (file.getTitle (), file.getId ()));
        nextPageToken = page.getNextPageToken ();
      } while (nextPageToken != null);
      return new Drive (true, result.toArray (new edu.dfci.cccb.mev.web.domain.social.Drive.DriveFile[0]));
    } else {
      return new Drive (false, null);
    }
  }

  @RequestMapping (method = RequestMethod.POST, value = "/{id}/load")
  public void load (@PathVariable ("id") String id) throws DatasetBuilderException,
                                                   InvalidDatasetNameException,
                                                   InvalidDimensionTypeException,
                                                   IOException {
    try (TemporaryFile file = new TemporaryFile ()) {
      try (FileOutputStream copy = new FileOutputStream (file)) {
        IOUtils.copy (google.driveOperations ().downloadFile (id).getInputStream (), copy);
      }
      workspace.put (builder.build (new MockTsvInput (google.driveOperations ().getFile (id).getTitle (), file)));
    }
  }
}
