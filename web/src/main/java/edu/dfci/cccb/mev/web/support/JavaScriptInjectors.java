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
package edu.dfci.cccb.mev.web.support;

import java.util.ArrayList;

import lombok.extern.log4j.Log4j;

import edu.dfci.cccb.mev.api.client.support.injectors.InjectorRegistry;

/**
 * @author levk
 *
 */
@Log4j
public class JavaScriptInjectors extends ArrayList<String> implements InjectorRegistry {
  private static final long serialVersionUID = 1L;
  
  /* (non-Javadoc)
   * @see java.util.ArrayList#add(java.lang.Object)
   */
  @Override
  public boolean add (String e) {
    log.info ("Adding javascript injector at " + e);
    return super.add (e);
  }
}
