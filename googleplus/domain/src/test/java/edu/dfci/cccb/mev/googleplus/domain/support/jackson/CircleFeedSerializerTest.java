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

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.services.plusDomains.model.Circle;
import com.google.api.services.plusDomains.model.CircleFeed;

public class CircleFeedSerializerTest extends JsonSerializerTest {

  private CircleFeed feed;
  private CircleFeed feedNoNext;

  public CircleFeedSerializerTest () {
    super (CircleFeed.class, CircleFeedSerializer.class);
  }

  @Before
  public void setUpFeed () {
    feed = new CircleFeed ();
    feed.setNextPageToken ("mockNext");
    feed.setItems (new ArrayList<Circle> ());

    feedNoNext = new CircleFeed ();
    feedNoNext.setItems (new ArrayList<Circle> ());
  }

  @Test
  public void serialize () throws JsonProcessingException, JSONException {
    assertEquals ("{circles:[], next:\"mockNext\"}", mapper.writeValueAsString (feed), true);
  }

  @Test
  public void serializeNoNext () throws JsonProcessingException, JSONException {
    assertEquals ("{circles:[]}", mapper.writeValueAsString (feedNoNext), true);
  }
}
