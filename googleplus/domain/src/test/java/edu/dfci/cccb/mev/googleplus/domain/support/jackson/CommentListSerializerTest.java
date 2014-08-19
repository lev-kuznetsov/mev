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
import com.google.api.services.drive.model.Comment;
import com.google.api.services.drive.model.CommentList;

public class CommentListSerializerTest extends JsonSerializerTest {

  private CommentList comments;
  private CommentList commentsNoNext;

  public CommentListSerializerTest () {
    super (CommentList.class, CommentListSerializer.class);
  }

  @Before
  public void setUpComments () {
    comments = new CommentList ();
    comments.setItems (new ArrayList<Comment> ());
    comments.setNextPageToken ("mockNext");

    commentsNoNext = new CommentList ();
    commentsNoNext.setItems (new ArrayList<Comment> ());
  }

  @Test
  public void comments () throws JsonProcessingException, JSONException {
    assertEquals ("{comments:[],next:\"mockNext\"}", mapper.writeValueAsString (comments), true);
  }

  @Test
  public void commentsNoNext () throws JsonProcessingException, JSONException {
    assertEquals ("{comments:[]}", mapper.writeValueAsString (commentsNoNext), true);
  }
}
