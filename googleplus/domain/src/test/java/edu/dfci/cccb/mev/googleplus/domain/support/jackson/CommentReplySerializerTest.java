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
import com.google.api.services.drive.model.CommentReply;

public class CommentReplySerializerTest extends JsonSerializerTest {

  public CommentReply reply;
  
  public CommentReplySerializerTest () {
    super (CommentReply.class, CommentReplySerializer.class);
  }

  @Before
  public void setUpReply () {
    reply = new CommentReply ();
    reply.setAuthor (null);
    reply.setContent ("mockContent");
    reply.setCreatedDate (null);
    reply.setReplyId ("mockId");
  }
  
  @Test
  public void reply () throws JsonProcessingException, JSONException {
    assertEquals ("{author:null,content:\"mockContent\",created:null,id:\"mockId\"}", mapper.writeValueAsString (reply), true);
  }
}
