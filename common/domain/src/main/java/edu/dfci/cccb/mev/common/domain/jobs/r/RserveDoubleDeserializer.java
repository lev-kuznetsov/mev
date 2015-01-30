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

package edu.dfci.cccb.mev.common.domain.jobs.r;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.valueOf;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author levk
 * @since CRYSTAL
 */
public class RserveDoubleDeserializer extends JsonDeserializer<Double> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonDeserializer#handledType() */
  @Override
  public Class<?> handledType () {
    return Double.class;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml
   * .jackson.core.JsonParser,
   * com.fasterxml.jackson.databind.DeserializationContext) */
  @Override
  public Double deserialize (JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    String value = jp.readValueAs (String.class);
    if ("NA".equals (value))
      return null;
    else if ("Inf".equals (value))
      return POSITIVE_INFINITY;
    else if ("-Inf".equals (value))
      return NEGATIVE_INFINITY;
    else
      return valueOf (value);
  }
}
