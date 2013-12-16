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
package edu.dfci.cccb.mev.dataset.client.support.prototype;

import lombok.Getter;

import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * @author levk
 * 
 */
public abstract class AbstractUrlBasedViewBuilder <T extends AbstractUrlBasedViewBuilder<?>> extends AbstractViewBuilder<T> {

  private @Getter String url;

  @SuppressWarnings ("unchecked")
  public T setUrl (String url) {
    this.url = url;
    return (T) this;
  }

  protected <V extends AbstractUrlBasedView> V initialize (V view) {
    super.initialize (view);
    view.setUrl (url);
    return view;
  }
}
