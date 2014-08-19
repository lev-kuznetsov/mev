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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.api.services.drive.model.CommentReply;

/**
 * @author levk
 * @since CRYSTAL
 */
public class CommentReplySerializer extends JsonSerializer<CommentReply> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<CommentReply> handledType () {
    return CommentReply.class;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  public void serialize (CommentReply value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                             JsonProcessingException {
    jgen.writeStartObject ();
    provider.defaultSerializeField ("author", value.getAuthor (), jgen);
    jgen.writeStringField ("content", value.getContent ());
    provider.defaultSerializeField ("created", value.getCreatedDate (), jgen);
    jgen.writeStringField ("id", value.getReplyId ());
    jgen.writeEndObject ();
  }
}
