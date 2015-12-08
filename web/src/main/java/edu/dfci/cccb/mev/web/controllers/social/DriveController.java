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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.drive.DriveFile;
import org.springframework.social.google.api.drive.DriveFilesPage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.rest.google.SecurityContext;
import edu.dfci.cccb.mev.web.domain.social.Drive;
import edu.dfci.cccb.mev.web.domain.social.Entry;
import edu.dfci.cccb.mev.web.domain.social.File;
import edu.dfci.cccb.mev.web.domain.social.Folder;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/import/google")
public class DriveController {

  private @Inject Google google;

  @RequestMapping (method = RequestMethod.GET)
  public Drive drive () throws UnsupportedEncodingException {
    return SecurityContext.userSignedIn () ? new Drive (true, list ("root")) : new Drive (false, null);
  }

  private Entry[] list (String parent) throws UnsupportedEncodingException {
    List<Entry> items = new ArrayList<> ();
    List<DriveFile> list;
    String nextToken = "";
    do {
      DriveFilesPage page = google.driveOperations ().getFiles (URLEncoder.encode (parent, "UTF-8"),
                                                                URLEncoder.encode (nextToken, "UTF-8"));
      list = page.getItems ();
      for (DriveFile file : list)
        if (!file.isHidden ())
          items.add (file.isFolder ()
                                     ? new Folder (file.getTitle (), list (file.getId ()))
                                     : new File (file.getTitle (), file.getId ()));
      nextToken = page.getNextPageToken ();
    } while (nextToken != null);
    return items.toArray (new Entry[0]);
  }
}
