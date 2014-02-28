/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.common.test.jetty;

import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static org.eclipse.jetty.util.resource.Resource.newResource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lombok.SneakyThrows;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.AnnotationDecorator;
import org.eclipse.jetty.annotations.AnnotationParser.Handler;
import org.eclipse.jetty.annotations.ClassNameResolver;
import org.eclipse.jetty.annotations.WebFilterAnnotationHandler;
import org.eclipse.jetty.annotations.WebListenerAnnotationHandler;
import org.eclipse.jetty.annotations.WebServletAnnotationHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Servlet 3+ no-xml annotation configuration parser
 * 
 * @author levk
 */
public class Servlet3Jetty9AnnotationConfiguration extends AnnotationConfiguration {

  /* (non-Javadoc)
   * @see
   * org.eclipse.jetty.annotations.AnnotationConfiguration#configure(org.eclipse
   * .jetty.webapp.WebAppContext) */
  @Override
  public void configure (final WebAppContext context) throws Exception {
    context.addDecorator (new AnnotationDecorator (context));

    if (!context.getMetaData ().isMetaDataComplete ()
        && (context.getServletContext ().getEffectiveMajorVersion () >= 3 || context.isConfigurationDiscovered ()))
      _discoverableAnnotationHandlers.addAll (asList (new WebServletAnnotationHandler (context),
                                                      new WebFilterAnnotationHandler (context),
                                                      new WebListenerAnnotationHandler (context)));

    createServletContainerInitializerAnnotationHandlers (context, getNonExcludedInitializers (context));

    if (!_discoverableAnnotationHandlers.isEmpty ()
        || _classInheritanceHandler != null || !_containerInitializerAnnotationHandlers.isEmpty ()) {

      Set<Handler> handlers = new HashSet<Handler> () {
        private static final long serialVersionUID = 1L;

        {
          addAll (_discoverableAnnotationHandlers);
          addAll (_containerInitializerAnnotationHandlers);
          if (_classInheritanceHandler != null)
            add (_classInheritanceHandler);
        }
      };
      for (Resource resource : new Iterable<Resource> () {

        public Iterator<Resource> iterator () {
          final Iterator<String> classpaths = asList (getProperty ("java.class.path").split (":")).iterator ();
          return new Iterator<Resource> () {
            public void remove () {
              throw new UnsupportedOperationException ();
            }

            @SneakyThrows (IOException.class)
            public Resource next () {
              return newResource (classpaths.next ());
            }

            public boolean hasNext () {
              return classpaths.hasNext ();
            }
          };
        }
      })
        if (resource == null)
          break;
        else
          createAnnotationParser ().parse (handlers, resource, new ClassNameResolver () {
            public boolean isExcluded (String name) {
              return context.isSystemClass (name);
            }

            public boolean shouldOverride (String name) {
              return !context.isParentLoaderPriority ();
            }
          });
    }
  }
}
