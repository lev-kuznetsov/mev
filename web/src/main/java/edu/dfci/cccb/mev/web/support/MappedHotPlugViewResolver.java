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

import static java.lang.Integer.MAX_VALUE;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * @author levk
 * 
 */
@Log4j
public class MappedHotPlugViewResolver extends AbstractCachingViewResolver implements Ordered {

  private @Getter @Setter int order = MAX_VALUE;
  private @Getter @Setter boolean allowChaining = true;
  private @Getter @Setter boolean warnOnOverride = true;
  private final Map<String, View> views = new HashMap<String, View> () {
    private static final long serialVersionUID = 1L;

    @Synchronized
    public View put (String key, View value) {
      if (value == null) throw new NullPointerException ("View value cannto be null");
      View result = super.put (key, value);
      if (result != null && warnOnOverride && !result.equals (value))
        log.warn ("Overriden view name " + key + " old value " + result + " new value " + value);
      log.info ("Associated view " + key + " with " + value);
      return result;
    }
  };

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView
   * (java.lang.String, java.util.Locale) */
  @Override
  protected View loadView (String viewName, Locale locale) throws Exception {
    View result = views.get (viewName);
    if (result == null && !allowChaining)
      throw new NoSuchBeanDefinitionException (viewName);
    else
      return result;
  }

  public void addXmlBeanDefinitionResources (Resource... resources) {
    for (Resource resource : resources)
      try (GenericWebApplicationContext context = createContext ()) {
        XmlBeanDefinitionReader reader = createXmlReader (context);

        reader.loadBeanDefinitions (resource);

        context.refresh ();

        mergeViews (context);
      }
  }

  public void addPropertiesBeanDefinitionResources (Resource... resources) {
    for (Resource resource : resources)
      try (GenericWebApplicationContext context = createContext ()) {
        PropertiesBeanDefinitionReader reader = createPropertiesReader (context);

        reader.loadBeanDefinitions (resource);

        context.refresh ();

        mergeViews (context);
      }
  }

  public void addAnnotatedClasses (Class<?>... classes) {
    try (GenericWebApplicationContext context = createContext ()) {
      AnnotatedBeanDefinitionReader reader = createAnnotatedReader (context);

      reader.register (classes);

      context.refresh ();

      mergeViews (context);
    }
  }

  protected GenericWebApplicationContext createContext () {
    GenericWebApplicationContext result = new GenericWebApplicationContext ();
    result.setParent (getApplicationContext ());
    result.setServletContext (getServletContext ());
    return result;
  }

  protected XmlBeanDefinitionReader createXmlReader (GenericWebApplicationContext context) {
    XmlBeanDefinitionReader result = new XmlBeanDefinitionReader (context);
    result.setEntityResolver (new ResourceEntityResolver (getApplicationContext ()));
    result.setEnvironment (getApplicationContext ().getEnvironment ());
    return result;
  }

  protected PropertiesBeanDefinitionReader createPropertiesReader (GenericWebApplicationContext context) {
    PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader (context);
    reader.setEnvironment (getApplicationContext ().getEnvironment ());
    return reader;
  }

  protected AnnotatedBeanDefinitionReader createAnnotatedReader (GenericWebApplicationContext context) {
    AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader (context);
    reader.setEnvironment (getApplicationContext ().getEnvironment ());
    return reader;
  }

  private void mergeViews (GenericWebApplicationContext context) {
    for (String name : context.getBeanNamesForType (View.class))
      views.put (name, context.getBean (name, View.class));
  }
}
