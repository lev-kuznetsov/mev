/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.googleplus.domain.utilities;

import static edu.dfci.cccb.mev.googleplus.domain.utilities.Mixins.execute;

import org.junit.Test;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import edu.dfci.cccb.mev.googleplus.domain.contract.UnauthorizedException;

/**
 * @author levk
 */
public class MixinsTest {

  @Test (expected = UnauthorizedException.class)
  public void unauthorizedExecute () throws Exception {
    execute (new Drive.Builder (new NetHttpTransport (),
                                new JacksonFactory (),
                                new GoogleCredential ()).setApplicationName ("test")
                                                        .build ()
                                                        .files ()
                                                        .insert (new File ()));
  }

}
