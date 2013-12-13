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

import org.springframework.web.servlet.view.AbstractTemplateView;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("unchecked")
public abstract class AbstractTemplateViewBuilder <T extends AbstractTemplateViewBuilder<?>> extends AbstractUrlBasedViewBuilder<T> {

  private @Getter boolean allowRequestOverride = false;
  private @Getter boolean allowSessionOverride = false;
  private @Getter boolean exposeRequestAttributes = false;
  private @Getter boolean exposeSessionAttributes = false;
  private @Getter boolean exposeSpringMacroHelpers = true;

  public T setAllowRequestOverride (boolean flag) {
    allowRequestOverride = flag;
    return (T) this;
  }

  public T setAllowSessionOverride (boolean flag) {
    allowSessionOverride = flag;
    return (T) this;
  }

  public T setExposeRequestAttributes (boolean flag) {
    exposeRequestAttributes = flag;
    return (T) this;
  }

  public T setExposeSessionAttributes (boolean flag) {
    exposeSessionAttributes = flag;
    return (T) this;
  }

  public T setExposeSpringMacroHelpers (boolean flag) {
    exposeSpringMacroHelpers = flag;
    return (T) this;
  }

  protected <V extends AbstractTemplateView> V initialize (V view) {
    super.initialize (view);
    view.setAllowRequestOverride (allowRequestOverride);
    view.setAllowSessionOverride (allowSessionOverride);
    view.setExposeRequestAttributes (exposeRequestAttributes);
    view.setExposeSessionAttributes (exposeSessionAttributes);
    view.setExposeSpringMacroHelpers (exposeSpringMacroHelpers);
    return view;
  }
}
