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

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.services.plus.model.Person.Cover.CoverPhoto;

public class CoverPhotoSerializerTest extends JsonSerializerTest {

  private CoverPhoto photo;

  public CoverPhotoSerializerTest () {
    super (CoverPhoto.class, CoverPhotoSerializer.class);
  }

  @Before
  public void setUpCoverPhoto () {
    photo = new CoverPhoto ();
    photo.setHeight (11);
    photo.setWidth (22);
    photo.setUrl ("mock://mock");
  }

  @Test
  public void coverPhoto () throws JsonProcessingException, JSONException {
    assertEquals ("{height:11,width:22,url:\"mock://mock\"}", mapper.writeValueAsString (photo), true);
  }
}
