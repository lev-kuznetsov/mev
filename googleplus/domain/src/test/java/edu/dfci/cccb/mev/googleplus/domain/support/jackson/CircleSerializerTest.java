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
import com.google.api.services.plusDomains.model.Circle;

public class CircleSerializerTest extends JsonSerializerTest {

  private Circle circle;

  public CircleSerializerTest () {
    super (Circle.class, CircleSerializer.class);
  }

  @Before
  public void setUpCircle () {
    circle = new Circle ();

    circle.setDescription ("mockDescription");
    circle.setDisplayName ("mockDisplay");
    circle.setId ("mockId");
    circle.setPeople (null);
  }

  @Test
  public void circle () throws JsonProcessingException, JSONException {
    assertEquals ("{description:\"mockDescription\",display:\"mockDisplay\",id:\"mockId\",people:null}",
                  mapper.writeValueAsString (circle), true);
  }
}
