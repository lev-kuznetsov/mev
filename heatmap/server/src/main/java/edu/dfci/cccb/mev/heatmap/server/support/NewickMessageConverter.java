/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.heatmap.server.support;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import edu.dfci.cccb.mev.heatmap.server.mock.HeatmapHierarchicalClusteringInternalNode;
import edu.dfci.cccb.mev.heatmap.server.mock.HeatmapHierarchicalClusteringLeaf;
import edu.dfci.cccb.mev.heatmap.server.mock.HeatmapHierarchicalClusteringNode;

/**
 * @author levk
 * 
 */
public class NewickMessageConverter extends AbstractHttpMessageConverter<HeatmapHierarchicalClusteringNode> {

  private static final Charset DEFAULT_CHARSET = Charset.forName ("UTF-8");
  public static final String NEWICK_EXTENSION = "newick";
  private static final String NEWICK_TYPE = "application";
  public static final MediaType NEWICK_MEDIA_TYPE = new MediaType (NEWICK_TYPE,
                                                                   "x-" + NEWICK_EXTENSION,
                                                                   DEFAULT_CHARSET);

  /**
   * 
   */
  public NewickMessageConverter () {
    super (NEWICK_MEDIA_TYPE);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#canWrite
   * (java.lang.Class, org.springframework.http.MediaType) */
  @Override
  public boolean canWrite (Class<?> clazz, MediaType mediaType) {
    return super.canWrite (clazz, mediaType);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#supports
   * (java.lang.Class) */
  @Override
  protected boolean supports (Class<?> clazz) {
    return HeatmapHierarchicalClusteringNode.class.isAssignableFrom (clazz);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal
   * (java.lang.Object, org.springframework.http.HttpOutputMessage) */
  @Override
  protected void writeInternal (HeatmapHierarchicalClusteringNode t, HttpOutputMessage outputMessage) throws IOException,
                                                                                                     HttpMessageNotWritableException {
    try (OutputStream out = new BufferedOutputStream (outputMessage.getBody ())) {
      write (out, t);
    }
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#readInternal
   * (java.lang.Class, org.springframework.http.HttpInputMessage) */
  @Override
  protected HeatmapHierarchicalClusteringNode readInternal (Class<? extends HeatmapHierarchicalClusteringNode> clazz,
                                                            HttpInputMessage inputMessage) throws IOException,
                                                                                          HttpMessageNotReadableException {
    throw new UnsupportedOperationException ("nyi");
  }

  private void write (OutputStream out, HeatmapHierarchicalClusteringNode node) throws IOException {
    /* (A:0.1,B:0.2,(C:0.3,D:0.4):0.5); */
    if (node instanceof HeatmapHierarchicalClusteringLeaf)
      out.write ((((HeatmapHierarchicalClusteringLeaf) node).getKey () + ":" + node.getDistance ()).getBytes ());
    else if (node instanceof HeatmapHierarchicalClusteringInternalNode) {
      HeatmapHierarchicalClusteringInternalNode internal = (HeatmapHierarchicalClusteringInternalNode) node;
      out.write ("(".getBytes ());
      write (out, internal.getLeft ());
      out.write (",".getBytes ());
      write (out, internal.getRight ());
      out.write (("):" + node.getDistance ()).getBytes ());
    }
  }
}
