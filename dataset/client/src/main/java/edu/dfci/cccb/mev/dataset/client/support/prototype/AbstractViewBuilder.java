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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import lombok.Getter;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("unchecked")
public abstract class AbstractViewBuilder <T extends AbstractViewBuilder<?>> {

  private @Getter Map<String, Object> attributes = new HashMap<> ();
  private @Getter ApplicationContext applicationContext;
  private @Getter String beanName;
  private @Getter String contentType;
  private @Getter boolean exposePathVariables = true;
  private @Getter String requestContextAttribute;
  private @Getter ServletContext servletContext;

  public T setAttributes (Map<String, Object> attributes) {
    this.attributes = attributes;
    return (T) this;
  }

  public T putAttributes (Map<String, ?> attributes) {
    this.attributes.putAll (attributes);
    return (T) this;
  }

  public <V> T putAttribute (String key, V value) {
    attributes.put (key, value);
    return (T) this;
  }

  public T setApplicationContext (ApplicationContext context) {
    applicationContext = context;
    return (T) this;
  }

  public T setBeanName (String name) {
    beanName = name;
    return (T) this;
  }

  public T setContentType (String type) {
    contentType = type;
    return (T) this;
  }

  public T setExposePathVariables (boolean flag) {
    exposePathVariables = flag;
    return (T) this;
  }

  public T setRequestContextAttribute (String attribute) {
    requestContextAttribute = attribute;
    return (T) this;
  }

  public T setServletContext (ServletContext context) {
    this.servletContext = context;
    return (T) this;
  }

  protected <V extends AbstractView> V initialize (V view) {
    if (attributes != null && !attributes.isEmpty ())
      view.setAttributesMap (attributes);
    if (applicationContext != null)
      view.setApplicationContext (applicationContext);
    if (beanName != null)
      view.setBeanName (beanName);
    if (contentType != null)
      view.setContentType (contentType);
    view.setExposePathVariables (exposePathVariables);
    if (requestContextAttribute != null)
      view.setRequestContextAttribute (requestContextAttribute);
    if (servletContext != null)
      view.setServletContext (servletContext);
    return view;
  }
}
