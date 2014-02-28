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

package edu.dfci.cccb.mev.common.services.guice;

import static com.google.inject.Key.get;
import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;

import lombok.ToString;

import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.message.Message;

import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.servlet.ServletModule;

/**
 * @author levk
 */
@ToString (exclude = "url")
public abstract class CxfJaxrsServiceModule extends JaxrsModule {

  private String url;
  private Set<Key<?>> jaxrsServiceDefinitions = new HashSet<> ();

  protected final void service (String url) {
    this.url = url;
  }

  protected <T> LinkedBindingBuilder<T> publish (Key<T> key) {
    jaxrsServiceDefinitions.add (key);
    return bind (key);
  }

  protected <T> AnnotatedBindingBuilder<T> publish (TypeLiteral<T> type) {
    jaxrsServiceDefinitions.add (get (type));
    return bind (type);
  }

  protected <T> AnnotatedBindingBuilder<T> publish (Class<T> type) {
    jaxrsServiceDefinitions.add (get (type));
    return bind (type);
  }

  /* (non-Javadoc)
   * @see com.google.inject.AbstractModule#configure() */
  @Override
  protected final void configure () {
    configureServices ();

    install (new ServletModule () {

      protected void configureServlets () {
        serve (url).with (new CXFNonSpringJaxrsServlet () {
          private static final long serialVersionUID = 1L;

          private @Inject Injector injector;

          @Override
          protected List<?> getProviders (ServletConfig servletConfig, String splitChar) throws ServletException {
            return providers (injector);
          }

          /* (non-Javadoc)
           * @see org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet#
           * getResourceProviders(javax.servlet.ServletConfig, java.util.Map) */
          @Override
          protected Map<Class<?>, ResourceProvider> getResourceProviders (ServletConfig servletConfig,
                                                                          Map<Class<?>, Map<String, List<String>>> resourceClasses) throws ServletException {
            return new AbstractMap<Class<?>, ResourceProvider> () {
              private final Map<Key<?>, Binding<?>> map = injector.getAllBindings ();

              abstract class GuiceResourceProviderAdapter implements ResourceProvider {
                public boolean isSingleton () {
                  return false;
                }

                public void releaseInstance (Message m, Object o) {}
              }

              @Override
              public ResourceProvider get (final Object key) {
                try {
                  return key instanceof Class<?> ? new GuiceResourceProviderAdapter () {
                    private final Class<?> type = (Class<?>) key;
                    private final Provider<?> provider = injector.getProvider (type);

                    @Override
                    public Class<?> getResourceClass () {
                      return type;
                    }

                    @Override
                    public Object getInstance (Message m) {
                      return provider.get ();
                    }
                  } : null;
                } catch (ConfigurationException e) {
                  return null;
                }
              }

              @Override
              public Set<Entry<Class<?>, ResourceProvider>> entrySet () {
                return new AbstractSet<Entry<Class<?>, ResourceProvider>> () {
                  private final Set<Entry<Key<?>, Binding<?>>> set = map.entrySet ();

                  @Override
                  public Iterator<Entry<Class<?>, ResourceProvider>> iterator () {
                    return new Iterator<Entry<Class<?>, ResourceProvider>> () {
                      private final Iterator<Entry<Key<?>, Binding<?>>> iterator = set.iterator ();

                      @Override
                      public boolean hasNext () {
                        return iterator.hasNext ();
                      }

                      @Override
                      public Entry<Class<?>, ResourceProvider> next () {
                        return new Entry<Class<?>, ResourceProvider> () {
                          private final Entry<Key<?>, Binding<?>> entry = iterator.next ();

                          @Override
                          public ResourceProvider setValue (ResourceProvider value) {
                            throw new UnsupportedOperationException ();
                          }

                          @Override
                          public ResourceProvider getValue () {
                            return new GuiceResourceProviderAdapter () {
                              private final Binding<?> value = entry.getValue ();

                              @Override
                              public Class<?> getResourceClass () {
                                return value.getKey ().getTypeLiteral ().getRawType ();
                              }

                              @Override
                              public Object getInstance (Message m) {
                                return value.getProvider ().get ();
                              }
                            };
                          }

                          @Override
                          public Class<?> getKey () {
                            return entry.getKey ().getTypeLiteral ().getRawType ();
                          }
                        };
                      }

                      @Override
                      public void remove () {
                        iterator.remove ();
                      }
                    };
                  }

                  @Override
                  public int size () {
                    return set.size ();
                  }
                };
              }
            };
          }

          @Override
          protected Map<Class<?>, Map<String, List<String>>> getServiceClasses (ServletConfig servletConfig,
                                                                                boolean modelAvailable,
                                                                                String splitChar) throws ServletException {
            return new AbstractMap<Class<?>, Map<String, List<String>>> () {

              @Override
              public Set<Entry<Class<?>, Map<String, List<String>>>> entrySet () {
                throw new UnsupportedOperationException ();
              }

              @Override
              public Set<Class<?>> keySet () {
                Set<Class<?>> serviceClasses = new HashSet<> ();
                for (Key<?> key : jaxrsServiceDefinitions)
                  serviceClasses.add (key.getTypeLiteral ().getRawType ());
                return serviceClasses;
              }
            };
          }
        });
      }
    });
  }

  protected abstract void configureServices ();

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.common.services.guice.JaxrsModule#callbacks() */
  @Override
  protected Collection<ScannerCallback> callbacks () {
    return asList ((ScannerCallback) new ScannerCallback () {

      private boolean hasInheritedAnnotation (Set<Annotation> checked,
                                              Collection<Class<? extends Annotation>> types,
                                              Annotation... annotations) {
        for (Annotation annotation : annotations) {
          if (checked.contains (annotation))
            continue;
          else
            checked.add (annotation);
          for (Class<? extends Annotation> type : types)
            if (annotation.annotationType ().isAnnotationPresent (type)
                || annotation.annotationType ().isAssignableFrom (type))
              return true;
          return hasInheritedAnnotation (checked, types, annotation.annotationType ().getAnnotations ());
        }
        return false;
      }

      private boolean hasInheritedAnnotation (Collection<Class<? extends Annotation>> types, Method... methods) {
        for (Method method : methods)
          return hasInheritedAnnotation (new HashSet<Annotation> (), types, method.getAnnotations ());
        return false;
      }

      @Override
      public ScopedBindingBuilder scan (Class<?> clazz) {
        if (clazz.isAnnotationPresent (Path.class)
            || hasInheritedAnnotation (asList (Path.class, HttpMethod.class), clazz.getMethods ()))
          return publish (clazz);
        return null;
      }
    });
  }
}
