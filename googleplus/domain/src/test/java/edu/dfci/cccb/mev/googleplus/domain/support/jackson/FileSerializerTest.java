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

package edu.dfci.cccb.mev.googleplus.domain.support.jackson;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Property;

public class FileSerializerTest extends JsonSerializerTest {

  private File file;

  public FileSerializerTest () {
    super (File.class, FileSerializer.class);
  }

  @Before
  public void setUpFile () {
    file = new File ();
    file.setTitle ("mockTitle");
    file.setDescription ("mockDescription");
    file.setFileSize (12L);
    file.setId ("mockId");
    file.setOwners (null);
    file.setLabels (null);
    file.setMimeType ("application/x-mock");
    file.setProperties (new ArrayList<Property> ());
  }

  @Test
  public void file () throws Exception {
    assertEquals ("{title:\"mockTitle\",description:\"mockDescription\",size:12,id:\"mockId\",owners:null,labels:null,mimetype:\"application/x-mock\",properties:[]}",
                  mapper.writeValueAsString (file),
                  true);
  }
}
