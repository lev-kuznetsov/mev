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

import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;

public class PeopleFeedSerializerTest extends JsonSerializerTest {

  private PeopleFeed feed;
  private PeopleFeed feedNoNext;

  public PeopleFeedSerializerTest () {
    super (PeopleFeed.class, PeopleFeedSerializer.class);
  }

  @Before
  public void setUpFeed () {
    feed = new PeopleFeed ();
    feed.setNextPageToken ("mockNext");
    feed.setItems (new ArrayList<Person> ());

    feedNoNext = new PeopleFeed ();
    feedNoNext.setItems (new ArrayList<Person> ());
  }

  @Test
  public void feed () throws Exception {
    assertEquals ("{people:[],next:\"mockNext\"}", mapper.writeValueAsString (feed), true);
  }

  @Test
  public void feedNoNext () throws Exception {
    assertEquals ("{people:[]}", mapper.writeValueAsString (feedNoNext), true);
  }
}
