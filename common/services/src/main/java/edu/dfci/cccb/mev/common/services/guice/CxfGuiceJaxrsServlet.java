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
import static com.google.inject.name.Names.named;
import static edu.dfci.cccb.mev.common.services.guice.AbstractJaxrsModule.PROVIDERS;
import static edu.dfci.cccb.mev.common.services.guice.JaxrsServiceModule.CONTENT_EXTENSIONS;
import static edu.dfci.cccb.mev.common.services.guice.JaxrsServiceModule.CONTENT_PARAMETER;
import static edu.dfci.cccb.mev.common.services.guice.JaxrsServiceModule.CONTENT_PARAMETER_VALUES;
import static edu.dfci.cccb.mev.common.services.guice.JaxrsServiceModule.RESOURCES;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.enumeration;
import static javax.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.message.Message;

import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 * @author levk
 */
@Singleton
public class CxfGuiceJaxrsServlet extends CXFNonSpringJaxrsServlet {
  private static final long serialVersionUID = 1L;

  private Injector injector;
  private @Inject @Named (PROVIDERS) Set<Class<?>> providers;
  private @Inject @Named (RESOURCES) Set<Class<?>> resources;

  private String parameter = null;
  private Map<String, MediaType> extensions = emptyMap ();
  private Map<String, MediaType> parameters = emptyMap ();

  @Inject
  private void setupNegotiation (final Injector injector) {
    this.injector = injector;

    try {
      this.parameter = injector.getInstance (get (new TypeLiteral<String> () {}, named (CONTENT_PARAMETER)));
    } catch (ConfigurationException e) {
      // Not negotiating based on query parameter
    }

    if (this.parameter != null)
      this.parameters = new HashMap<> (new AbstractMap<String, MediaType> () {
        private final Set<Entry<String, MediaType>> set;

        {
          set = injector.getInstance (Key.get (new TypeLiteral<Set<Entry<String, MediaType>>> () {},
                                               named (CONTENT_PARAMETER_VALUES)));
        }

        @Override
        public Set<Entry<String, MediaType>> entrySet () {
          return set;
        }
      });

    try {
      this.extensions = new AbstractMap<String, MediaType> () {
        private final Set<Entry<String, MediaType>> set;

        {
          set = injector.getInstance (Key.get (new TypeLiteral<Set<Entry<String, MediaType>>> () {},
                                               named (CONTENT_EXTENSIONS)));
        }

        @Override
        public Set<Entry<String, MediaType>> entrySet () {
          return set;
        }
      };
    } catch (ConfigurationException e) {
      // Not negotiating based on extension
    }
  }

  @Override
  protected void invoke (HttpServletRequest request, HttpServletResponse response) throws ServletException {
    if (parameter != null) {
      String key = request.getParameter (parameter);
      if (key != null) {
        final MediaType value = parameters.get (key);
        if (value != null)
          request = new HttpServletRequestWrapper (request) {

            private static final String ACCEPT_HEADER = "Accept";

            @Override
            public String getHeader (String name) {
              return ACCEPT_HEADER.equalsIgnoreCase (name) ? value.toString () : super.getHeader (name);
            }

            @Override
            public Enumeration<String> getHeaderNames () {
              final Enumeration<String> wrapped = super.getHeaderNames ();
              return new Enumeration<String> () {
                private boolean listedAcceptHeader = false;

                @Override
                public boolean hasMoreElements () {
                  return wrapped.hasMoreElements () || !listedAcceptHeader;
                }

                @Override
                public String nextElement () {
                  if (wrapped.hasMoreElements ()) {
                    String result = wrapped.nextElement ();
                    if (ACCEPT_HEADER.equalsIgnoreCase (result))
                      listedAcceptHeader = true;
                    return result;
                  } else if (!listedAcceptHeader) {
                    listedAcceptHeader = true;
                    return ACCEPT_HEADER;
                  } else
                    throw new NoSuchElementException ();
                }
              };
            }

            @Override
            public Enumeration<String> getHeaders (String name) {
              return ACCEPT_HEADER.equalsIgnoreCase (name)
                                                          ? enumeration (asList (value.toString ()))
                                                          : super.getHeaders (name);
            }
          };
        else {
          response.setStatus (SC_UNSUPPORTED_MEDIA_TYPE);
          return;
        }
      }
    }
    super.invoke (request, response);
  }

  @Override
  protected void setExtensions (JAXRSServerFactoryBean bean, ServletConfig servletConfig) {
    bean.setExtensionMappings (new HashMap<Object, Object> () {
      private static final long serialVersionUID = 1L;

      {
        for (Entry<String, MediaType> entry : extensions.entrySet ())
          put (entry.getKey (), entry.getValue ().toString ());
      }
    });
  }

  @Override
  protected List<?> getProviders (ServletConfig servletConfig, String splitChar) throws ServletException {
    List<Object> result = new ArrayList<> ();
    for (Class<?> key : providers)
      result.add (injector.getInstance (key));
    return result;
  }

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
        return resources;
      }
    };
  }
}
