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
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvSerializer;

/**
 * @author levk
 */
@Provider
@Produces (TAB_SEPARATED_VALUES)
public class TsvDatasetMessageWriter implements MessageBodyWriter<Dataset<String, Double>> {

  private @Inject DatasetTsvSerializer serializer;

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType) */
  @Override
  public boolean isWriteable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return Dataset.class.equals (type) && TAB_SEPARATED_VALUES_TYPE.equals (mediaType);
  }

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object,
   * java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType) */
  @Override
  public long getSize (Dataset<String, Double> t,
                       Class<?> type,
                       Type genericType,
                       Annotation[] annotations,
                       MediaType mediaType) {
    return -1;
  }

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object,
   * java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.OutputStream) */
  @Override
  public void writeTo (Dataset<String, Double> t,
                       Class<?> type,
                       Type genericType,
                       Annotation[] annotations,
                       MediaType mediaType,
                       MultivaluedMap<String, Object> httpHeaders,
                       OutputStream entityStream) throws IOException, WebApplicationException {
    serializer.serialize (t, entityStream);
  }
}
