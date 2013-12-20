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
package edu.dfci.cccb.mev.web;

import static java.util.Collections.emptyList;
import static org.eclipse.jetty.util.resource.Resource.newResource;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.annotations.AbstractDiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.AnnotationDecorator;
import org.eclipse.jetty.annotations.AnnotationParser;
import org.eclipse.jetty.annotations.AnnotationParser.DiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.ClassNameResolver;
import org.eclipse.jetty.annotations.WebFilterAnnotationHandler;
import org.eclipse.jetty.annotations.WebListenerAnnotationHandler;
import org.eclipse.jetty.annotations.WebServletAnnotationHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author levk
 * 
 */
public class JavaConfigAnnotationConfiguration extends AnnotationConfiguration {

  @Override
  public void configure (WebAppContext context) throws Exception {
    boolean metadataComplete = context.getMetaData ().isMetaDataComplete ();
    context.addDecorator (new AnnotationDecorator (context));

    AnnotationParser parser = null;
    if (!metadataComplete)
      if (context.getServletContext ().getEffectiveMajorVersion () >= 3
          || context.isConfigurationDiscovered ()) {
        _discoverableAnnotationHandlers.add (new WebServletAnnotationHandler (context));
        _discoverableAnnotationHandlers.add (new WebFilterAnnotationHandler (context));
        _discoverableAnnotationHandlers.add (new WebListenerAnnotationHandler (context));
      }

    createServletContainerInitializerAnnotationHandlers (context, getNonExcludedInitializers (context));

    if (!_discoverableAnnotationHandlers.isEmpty ()
        || _classInheritanceHandler != null
        || !_containerInitializerAnnotationHandlers.isEmpty ()) {
      parser = createAnnotationParser ();

      parse (context, parser);

      for (DiscoverableAnnotationHandler h : _discoverableAnnotationHandlers)
        context.getMetaData ()
               .addDiscoveredAnnotations (((AbstractDiscoverableAnnotationHandler) h).getAnnotationList ());
    }
  }

  private void parse (final WebAppContext context, AnnotationParser parser) throws Exception {
    List<Resource> resources = getResources (getClass ().getClassLoader ());

    for (Resource resource : resources) {
      if (resource == null)
        return;

      parser.clearHandlers ();
      for (DiscoverableAnnotationHandler h : _discoverableAnnotationHandlers)
        if (h instanceof AbstractDiscoverableAnnotationHandler)
          ((AbstractDiscoverableAnnotationHandler) h).setResource (null);
      parser.registerHandlers (_discoverableAnnotationHandlers);
      parser.registerHandler (_classInheritanceHandler);
      parser.registerHandlers (_containerInitializerAnnotationHandlers);

      parser.parse (resource, new ClassNameResolver () {
        public boolean isExcluded (String name) {
          if (context.isSystemClass (name))
            return true;
          if (context.isServerClass (name))
            return false;
          return false;
        }

        public boolean shouldOverride (String name) {
          if (context.isParentLoaderPriority ())
            return false;
          return true;
        }
      });
    }
  }

  private List<Resource> getResources (ClassLoader aLoader) throws IOException {
    if (aLoader instanceof URLClassLoader) {
      List<Resource> result = new ArrayList<Resource> ();
      URL[] urls = ((URLClassLoader) aLoader).getURLs ();
      for (URL url : urls)
        result.add (newResource (url));

      return result;
    }
    return emptyList ();
  }
}
