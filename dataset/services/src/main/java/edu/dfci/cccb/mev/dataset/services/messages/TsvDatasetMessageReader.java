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

package edu.dfci.cccb.mev.dataset.services.messages;

import static edu.dfci.cccb.mev.dataset.services.controllers.DatasetController.TAB_SEPARATED_VALUES;
import static edu.dfci.cccb.mev.dataset.services.controllers.DatasetController.TAB_SEPARATED_VALUES_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvDeserializer;

/**
 * @author levk
 * @since CRYSTAL
 */
@Provider
@Consumes (TAB_SEPARATED_VALUES)
public class TsvDatasetMessageReader implements MessageBodyReader<Dataset<String, Double>> {

  private @Inject DatasetTsvDeserializer builder;
  private @Context UriInfo uri;

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType) */
  @Override
  public boolean isReadable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return type.equals (Dataset.class) && TAB_SEPARATED_VALUES_TYPE.equals (mediaType);
  }

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.InputStream) */
  @Override
  public Dataset<String, Double> readFrom (Class<Dataset<String, Double>> type,
                                           Type genericType,
                                           Annotation[] annotations,
                                           MediaType mediaType,
                                           MultivaluedMap<String, String> httpHeaders,
                                           InputStream entityStream) throws IOException, WebApplicationException {
    return builder.deserialize (uri.getPathParameters ().getFirst ("dataset"), entityStream);
  }
}
