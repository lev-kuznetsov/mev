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
package edu.dfci.cccb.mev.dataset.domain.prototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionBuilder;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true, chain = true)
public abstract class AbstractSelectionBuilder implements SelectionBuilder {

  private @Getter @Setter String name;
  private @Getter final Properties properties = new Properties ();
  private @Getter final List<String> keys = new ArrayList<String> ();

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.SelectionBuilder#property(java
   * .lang.String, java.lang.String) */
  @Override
  public SelectionBuilder property (String key, String value) {
    properties.put (key, value);
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.SelectionBuilder#properties(
   * java.util.Properties) */
  @Override
  public SelectionBuilder properties (Properties properties) {
    this.properties.putAll (properties);
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.SelectionBuilder#keys(java.util
   * .List) */
  @Override
  public SelectionBuilder keys (List<String> keys) {
    this.keys.addAll (keys);
    return this;
  }
}
