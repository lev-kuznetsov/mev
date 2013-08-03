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
package edu.dfci.cccb.mev.domain;

import lombok.Getter;

/**
 * @author levk
 *
 */
// TODO: attach internationalization
public class SelectionNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  private final @Getter String id;
  
  public SelectionNotFoundException (String id) {
    super ("Selection keyed " + id + " not found");
    this.id = id;
  }
  
  @Override
  public String getLocalizedMessage () {
    // TODO: use LocaleContextHolder to get the request locale
    return super.getLocalizedMessage ();
  }
}
