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
package edu.dfci.cccb.mev.dataset.domain.simple;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Synchronized;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selections;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDimension;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode (callSuper = true)
@ToString
@Accessors (fluent = true)
@Log4j
public class SimpleDimension extends AbstractDimension {

  private @Getter List<String> keys;
  private @Getter final Selections selections;
  private @Getter final Annotation annotation;

  /**
   * 
   */
  public SimpleDimension (Type type, List<String> keys, Selections selections, Annotation annotation) {
    super (type);
    if (log.isDebugEnabled ())
      log.debug ("Type=" + type);
    this.annotation = annotation;
    this.keys = keys;
    this.selections = selections;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Dimension#reorder(java.util.List) */

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Dimension#reorder(java.util.List) */
  @Override
  @Synchronized
  public void reorder (List<String> keys) throws DatasetException {
    List<String> old = keys ();
    if (old.size () != keys.size ())
      throw new DatasetException (); // TODO: add args
    else
      for (String key : old)
        if (!keys.contains (key))
          throw new DatasetException (); // TODO: add args
    this.keys = keys;
  }
}
