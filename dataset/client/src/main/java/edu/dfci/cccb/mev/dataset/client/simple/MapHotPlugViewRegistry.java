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

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;

import edu.dfci.cccb.mev.dataset.client.contract.AnnotatedClassViewRegistrar;
import edu.dfci.cccb.mev.dataset.client.contract.XmlBeanResourceViewRegistrar;

/**
 * @author levk
 * 
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode (callSuper = true)
@Log4j
public class MapHotPlugViewRegistry extends WebApplicationObjectSupport implements AnnotatedClassViewRegistrar, XmlBeanResourceViewRegistrar {

  private final Map<String, View> views;

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.client.contract.XmlBeanResourceViewRegistrar
   * #register(org.springframework.core.io.Resource,
   * org.springframework.core.io.Resource[]) */
  @Override
  public XmlBeanResourceViewRegistrar register (final Resource first, final Resource... rest) {
    // Inverted for() try{} due to JDK-7178324:
    // http://bugs.sun.com/view_bug.do?bug_id=7178324
    try (GenericWebApplicationContext context = createContext ()) {
      for (Resource resource : new ArrayList<Resource> () {
        private static final long serialVersionUID = 1L;

        {
          add (first);
          addAll (asList (rest));
        }
      }) {
        XmlBeanDefinitionReader reader = createXmlReader (context);

        reader.loadBeanDefinitions (resource);

        context.refresh ();

        mergeViews (context);
      }
    }
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.client.contract.AnnotatedClassViewRegistrar#
   * register(java.lang.Class, java.lang.Class<?>[]) */
  @Override
  public AnnotatedClassViewRegistrar register (Class<?> first, Class<?>... rest) {
    try (GenericWebApplicationContext context = createContext ()) {
      AnnotatedBeanDefinitionReader reader = createAnnotatedReader (context);

      reader.register (first);
      reader.register (rest);

      context.refresh ();

      mergeViews (context);
    }
    return this;
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
    for (String name : context.getBeanNamesForType (View.class)) {
      View view = context.getBean (name, View.class);
      log.debug ("Merging view " + view + " of type " + view.getClass ().getName () + " with name " + name);
      views.put (name, view);
    }
  }
}
