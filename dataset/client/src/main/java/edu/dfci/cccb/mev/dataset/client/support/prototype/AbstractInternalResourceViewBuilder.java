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

import static java.lang.System.arraycopy;
import lombok.Getter;

import org.springframework.web.servlet.view.InternalResourceView;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("unchecked")
public abstract class AbstractInternalResourceViewBuilder <T extends AbstractInternalResourceViewBuilder<?>> extends AbstractUrlBasedViewBuilder<T> {

  public @Getter boolean alwaysInclude = false;
  public @Getter boolean exposeContextBeansAsAttributes = false;
  public @Getter String[] exposedContextBeanNames;
  public @Getter boolean preventDispatchLoop;

  public T setAlwaysInclude (boolean flag) {
    alwaysInclude = flag;
    return (T) this;
  }

  public T setExposeContextBeansAsAttributes (boolean flag) {
    exposeContextBeansAsAttributes = flag;
    return (T) this;
  }

  public T setExposedContextBeans (String... names) {
    exposedContextBeanNames = names;
    return (T) this;
  }

  public T addExposedContextBeans (String... names) {
    if (exposedContextBeanNames == null)
      return setExposedContextBeans (names);
    else {
      String[] tmp = new String[names.length + exposedContextBeanNames.length];
      arraycopy (exposedContextBeanNames, 0, tmp, 0, exposedContextBeanNames.length);
      arraycopy (names, 0, tmp, exposedContextBeanNames.length, names.length);
      exposedContextBeanNames = tmp;
      return (T) this;
    }
  }

  public T setPreventDispatchLoop (boolean flag) {
    preventDispatchLoop = flag;
    return (T) this;
  }

  protected <V extends InternalResourceView> V initialize (V view) {
    super.initialize (view);
    view.setAlwaysInclude (alwaysInclude);
    view.setExposeContextBeansAsAttributes (exposeContextBeansAsAttributes);
    if (exposedContextBeanNames != null)
      view.setExposedContextBeanNames (exposedContextBeanNames);
    view.setPreventDispatchLoop (preventDispatchLoop);
    return view;
  }
}
