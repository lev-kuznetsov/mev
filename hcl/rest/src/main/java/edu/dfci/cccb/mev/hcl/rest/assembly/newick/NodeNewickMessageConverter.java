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
package edu.dfci.cccb.mev.hcl.rest.assembly.newick;

import static java.nio.charset.Charset.forName;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

import lombok.ToString;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import edu.dfci.cccb.mev.hcl.domain.contract.Branch;
import edu.dfci.cccb.mev.hcl.domain.contract.Leaf;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;

/**
 * @author levk
 * 
 */
@ToString
public class NodeNewickMessageConverter extends AbstractHttpMessageConverter<Node> {

  private static final Charset DEFAULT_CHARSET = forName ("UTF-8");
  public static final String NEWICK_EXTENSION = "newick";
  private static final String NEWICK_TYPE = "application";
  public static final MediaType NEWICK_MEDIA_TYPE = new MediaType (NEWICK_TYPE,
                                                                   "x-" + NEWICK_EXTENSION,
                                                                   DEFAULT_CHARSET);
  private static final String LEAF_DISTANCE = "0.1"; // TODO: check with Kevin!
                                                     // this is a hack

  /**
   * 
   */
  public NodeNewickMessageConverter () {
    super (NEWICK_MEDIA_TYPE);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#supports
   * (java.lang.Class) */
  @Override
  protected boolean supports (Class<?> clazz) {
    return Node.class.isAssignableFrom (clazz);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal
   * (java.lang.Object, org.springframework.http.HttpOutputMessage) */
  @Override
  protected void writeInternal (Node t, HttpOutputMessage outputMessage) throws IOException,
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
  protected Node readInternal (Class<? extends Node> clazz,
                               HttpInputMessage inputMessage) throws IOException,
                                                             HttpMessageNotReadableException {
    throw new UnsupportedOperationException ("nyi");
  }

  private void write (OutputStream out, Node node) throws IOException {
    if (node instanceof Leaf)
      write (out, (Leaf) node);
    else if (node instanceof Branch)
      write (out, (Branch) node);
  }

  private void write (OutputStream out, Leaf leaf) throws IOException {
    out.write ((leaf.name () + ":" + LEAF_DISTANCE).getBytes ());
  }

  private void write (OutputStream out, Branch branch) throws IOException {
    out.write ("(".getBytes ());
    for (Iterator<Node> children = branch.children ().iterator (); children.hasNext ();) {
      write (out, children.next ());
      if (children.hasNext ())
        out.write (",".getBytes ());
    }
    out.write (("):" + branch.distance ()).getBytes ());
  }
}
