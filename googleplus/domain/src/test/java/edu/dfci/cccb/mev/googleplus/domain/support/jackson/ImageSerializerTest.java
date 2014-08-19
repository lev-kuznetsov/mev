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

import org.junit.Before;
import org.junit.Test;

import com.google.api.services.plus.model.Person.Image;

public class ImageSerializerTest extends JsonSerializerTest {

  private Image image;

  public ImageSerializerTest () {
    super (Image.class, ImageSerializer.class);
  }

  @Before
  public void setUpImage () {
    image = new Image ();
    image.setIsDefault (true);
    image.setUrl ("mock://mock");
  }

  @Test
  public void image () throws Exception {
    assertEquals ("{default:true,url:\"mock://mock\"}", mapper.writeValueAsString (image), true);
  }
}
