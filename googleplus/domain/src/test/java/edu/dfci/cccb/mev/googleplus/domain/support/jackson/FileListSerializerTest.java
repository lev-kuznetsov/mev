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
import com.google.api.services.drive.model.FileList;

public class FileListSerializerTest extends JsonSerializerTest {

  private FileList list;
  private FileList listNoNext;

  public FileListSerializerTest () {
    super (FileList.class, FileListSerializer.class);
  }

  @Before
  public void setUpList () {
    list = new FileList ();
    list.setItems (new ArrayList<File> ());
    list.setNextPageToken ("mockNext");

    listNoNext = new FileList ();
    listNoNext.setItems (new ArrayList<File> ());
  }

  @Test
  public void list () throws Exception {
    assertEquals ("{files:[],next:\"mockNext\"}", mapper.writeValueAsString (list), true);
  }

  @Test
  public void listNoNext () throws Exception {
    assertEquals ("{files:[]}", mapper.writeValueAsString (listNoNext), true);
  }
}
