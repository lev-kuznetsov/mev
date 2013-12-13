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
package edu.dfci.cccb.mev.dataset.client.simple;

import java.util.ArrayList;

import lombok.ToString;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.client.contract.JavascriptInjectorRegistry;

/**
 * @author levk
 * 
 */
@ToString
@Log4j
public class ArrayListJavascriptRegistryInjector extends ArrayList<String> implements JavascriptInjectorRegistry {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.client.contract.JavascriptInjectorRegistry#add
   * (java.lang.String) */
  @Override
  public ArrayListJavascriptRegistryInjector register (String first, String... rest) {
    log.info ("Adding javascript injector at " + first);
    add (first);
    return this;
  }
}
