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

import java.util.Locale;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * @author levk
 * 
 */
public class JsonViewResolver implements ViewResolver, Ordered {

  private @Getter @Setter int order = LOWEST_PRECEDENCE;

  private @Inject AutowireCapableBeanFactory beanFactory;

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.ViewResolver#resolveViewName(java.lang
   * .String, java.util.Locale) */
  @Override
  public View resolveViewName (String viewName, Locale locale) throws Exception {
    MappingJackson2JsonView view = new MappingJackson2JsonView ();
    beanFactory.autowireBean (view);
    return view;
  }
}
