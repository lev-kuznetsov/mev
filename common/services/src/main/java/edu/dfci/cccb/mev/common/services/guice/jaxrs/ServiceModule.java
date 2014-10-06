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

package edu.dfci.cccb.mev.common.services.guice.jaxrs;

import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.join;
import static com.google.inject.Key.get;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.asList;
import static java.util.Collections.enumeration;
import static javax.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
import static org.apache.cxf.message.Message.ACCEPT_CONTENT_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxrs.JAXRSInvoker;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.impl.ProvidersImpl;
import org.apache.cxf.jaxrs.impl.RequestImpl;
import org.apache.cxf.jaxrs.impl.SecurityContextImpl;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import ch.lambdaj.function.convert.Converter;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.JaxrsModule;

/**
 * JAX-RS service module
 * 
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public class ServiceModule extends JaxrsModule {

  private static final String PARAMETER_NAME = "jaxrs.cn.query.param";
  private static final String EXTENSIONS = "jaxrs.cn.extensions";
  private static final String PARAMETER_VALUES = "jaxrs.cn.query.param.values";

  @Retention (RUNTIME)
  @Qualifier
  private @interface Message {}

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (final Binder binder) {
    super.configure (binder);

    @RequiredArgsConstructor
    class KeyHolder {
      final Key<?> key;
    }

    final Multibinder<KeyHolder> resources = newSetBinder (binder, KeyHolder.class);

    configure (new ResourceBinder () {

      @Override
      public <T> AnnotatedBindingBuilder<T> publish (Class<T> type) {
        resources.addBinding ().toInstance (new KeyHolder (get (type)));
        return binder.bind (type);
      }

      @Override
      public <T> AnnotatedBindingBuilder<T> publish (final TypeLiteral<T> type) {
        resources.addBinding ().toInstance (new KeyHolder (get (type)));
        return binder.bind (type);
      }

      @Override
      public <T> LinkedBindingBuilder<T> publish (Key<T> key) {
        resources.addBinding ().toInstance (new KeyHolder (key));
        return binder.bind (key);
      }
    });

    final Multibinder<Entry<String, MediaType>> parameterValues =
                                                                  newSetBinder (binder,
                                                                                new TypeLiteral<Entry<String, MediaType>> () {},
                                                                                named (PARAMETER_VALUES));
    final Multibinder<Entry<String, MediaType>> extensions =
                                                             newSetBinder (binder,
                                                                           new TypeLiteral<Entry<String, MediaType>> () {},
                                                                           named (EXTENSIONS));

    configure (new ContentNegotiationConfigurer () {

      class ContentMapperBinder implements ContentNegotiationMapper {
        private final Multibinder<Entry<String, MediaType>> binder;

        ContentMapperBinder (Multibinder<Entry<String, MediaType>> binder) {
          this.binder = binder;
        }

        @Override
        public ContentNegotiationMapper map (Map<String, MediaType> map) {
          for (Entry<String, MediaType> entry : map.entrySet ())
            map (entry.getKey (), entry.getValue ());
          return this;
        }

        @Override
        public ContentNegotiationMapper map (final String key, final MediaType type) {
          binder.addBinding ().toInstance (new Entry<String, MediaType> () {

            @Override
            public MediaType setValue (MediaType value) {
              throw new UnsupportedOperationException ();
            }

            @Override
            public MediaType getValue () {
              return type;
            }

            @Override
            public String getKey () {
              return key;
            }
          });
          return this;
        }
      }

      @Override
      public ContentNegotiationMapper parameter (String name) {
        binder.bindConstant ().annotatedWith (named (PARAMETER_NAME)).to (name);
        return new ContentMapperBinder (parameterValues);
      }

      @Override
      public ContentNegotiationMapper extension () {
        return new ContentMapperBinder (extensions);
      }
    });

    binder.install (new SingletonModule () {
      @Provides
      @Singleton
      @Message
      private ThreadLocal<org.apache.cxf.message.Message> message () {
        return new ThreadLocal<> ();
      }

      @Provides
      @RequestScoped
      public UriInfo uri (@Message ThreadLocal<org.apache.cxf.message.Message> message) {
        return new UriInfoImpl (message.get ());
      }

      @Provides
      @RequestScoped
      public HttpHeaders header (@Message ThreadLocal<org.apache.cxf.message.Message> message) {
        return new HttpHeadersImpl (message.get ());
      }

      @Provides
      @RequestScoped
      public Providers providers (@Message ThreadLocal<org.apache.cxf.message.Message> message) {
        return new ProvidersImpl (message.get ());
      }

      @Provides
      @RequestScoped
      public SecurityContext securityContext (@Message ThreadLocal<org.apache.cxf.message.Message> message) {
        return new SecurityContextImpl (message.get ());
      }

      @Provides
      @RequestScoped
      public Request request (@Message ThreadLocal<org.apache.cxf.message.Message> message) {
        return new RequestImpl (message.get ());
      }

      @Provides
      @RequestScoped
      public Locale locale (HttpServletRequest request) {
        return request.getLocale ();
      }
    });

    configure (new ServiceBinder () {

      class InjectedCxfJaxrsServlet extends CXFNonSpringJaxrsServlet {
        private static final long serialVersionUID = 1L;

        private @Inject @Message ThreadLocal<org.apache.cxf.message.Message> message;
        private @Inject Injector injector;
        private @Inject @Named (PROVIDERS) Set<Object> providers;
        private @Inject Set<KeyHolder> resources;
        private @Inject (optional = true) @Named (PARAMETER_NAME) String parameter;
        private Map<String, MediaType> parameterValues;
        private Map<Object, Object> extensions;

        private final String serviceUrl;

        public InjectedCxfJaxrsServlet (String serviceUrl) {
          this.serviceUrl = serviceUrl;
        }

        @Inject
        public void setParameterValues (final @Named (PARAMETER_VALUES) Set<Entry<String, MediaType>> vals) {
          parameterValues = new HashMap<> (new AbstractMap<String, MediaType> () {
            @Override
            public Set<Entry<String, MediaType>> entrySet () {
              return vals;
            }
          });
        }

        @SuppressWarnings ({ "unchecked", "rawtypes" })
        @Inject
        public void setExtensions (final @Named (EXTENSIONS) Set<Entry<String, MediaType>> vals) {
          extensions = new HashMap<> (new AbstractMap () {
            @Override
            public Set<Entry<String, MediaType>> entrySet () {
              return vals;
            }
          });
        }

        private String dump (Object o) {
          if (o == null)
            return "null";
          else if (o instanceof Object[])
            return Arrays.toString ((Object[]) o);
          else
            return o.toString ();
        }

        private String param (Class<?> t, Annotation... as) {
          StringBuilder s = new StringBuilder ();
          for (Annotation a : as) {
            s.append ("@")
             .append (a.annotationType ().getSimpleName ());
            try {
              s.append ("(" + dump (a.annotationType ().getMethod ("value").invoke (a)) + ") ");
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
              s.append (" ");
            }
          }
          return s.append (t.getSimpleName ()).toString ().trim ();
        }

        private void dumpEndpoints (PrintStream o, String uri, Class<?> r) {
          for (Method m : r.getMethods ()) {
            List<HttpMethod> requestMethods = new ArrayList<> ();
            Path path = m.getAnnotation (Path.class);
            for (Annotation a : m.getAnnotations ()) {
              HttpMethod request = a.annotationType ().getAnnotation (HttpMethod.class);
              if (request != null)
                requestMethods.add (request);
            }

            final String format = "%-35s %6s %-10s %30s %-25s %15s\n";
            if (!requestMethods.isEmpty ()) { // endpoint
              o.printf (format,
                        (path != null ? uri + path.value () : uri).replaceAll ("/[/]+", "/").trim (),
                        requestMethods.get (0).value ().trim (),
                        m.getReturnType ().getSimpleName (),
                        r.getSimpleName () + "." + m.getName (),
                        m.getParameterTypes ().length > 0
                                                         ? param (m.getParameterTypes ()[0],
                                                                  m.getParameterAnnotations ()[0])
                                                         : "",
                        m.getExceptionTypes ().length > 0 ? m.getExceptionTypes ()[0].getSimpleName () : "");
              for (int i = 1;; i++) {
                String req = (requestMethods.size () > i ? requestMethods.get (i).value () : "").trim ();
                String par = (m.getParameterTypes ().length > i ? param (m.getParameterTypes ()[i],
                                                                         m.getParameterAnnotations ()[i])
                                                               : "").trim ();
                String exc = (m.getExceptionTypes ().length > i ? m.getExceptionTypes ()[i].getSimpleName ()
                                                               : "").trim ();
                String line = String.format (format,
                                             "", req, "", "", par, exc);
                if (line.trim ().equals (""))
                  break;
                else
                  o.print (line);
              }
            } else if (path != null) // subresource
              dumpEndpoints (o, uri + "/" + path.value () + "/", m.getReturnType ());
          }
        }

        @Override
        public void init (ServletConfig servletConfig) throws ServletException {
          super.init (servletConfig);

          ByteArrayOutputStream buffer = new ByteArrayOutputStream ();
          PrintStream info = new PrintStream (buffer);
          info.println ("JAX-RS");
          for (KeyHolder k : resources) {
            Class<?> r = k.key.getTypeLiteral ().getRawType ();
            Path p = r.getAnnotation (Path.class);
            if (p != null)
              dumpEndpoints (info, serviceUrl + "/" + p.value (), r);
          }

          info.print ("Using ");
          info.print (join (convert (providers, new Converter<Object, String> () {
            @Override
            public String convert (Object from) {
              Class<?> clazz = from.getClass ();
              StringBuilder s = new StringBuilder (param (clazz, clazz.getAnnotations ()));
              List<String> interfaces = new ArrayList<> ();
              for (Class<?> c = clazz; c != null; c = c.getSuperclass ())
                for (Class<?> i : c.getInterfaces ())
                  interfaces.add (i.getSimpleName ());
              if (!interfaces.isEmpty ()) {
                s.append (" implements ").append (join (interfaces));
              }
              return s.toString ();
            }
          }), "\n      "));
          log.info (buffer.toString ());
        }

        @Override
        protected void invoke (HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException {
          if (parameter != null) {
            String key = httpRequest.getParameter (parameter);
            if (key != null) {
              final MediaType value = parameterValues.get (key);
              if (value != null)
                httpRequest = new HttpServletRequestWrapper (httpRequest) {

                  @Override
                  public String getHeader (String name) {
                    return ACCEPT_CONTENT_TYPE.equalsIgnoreCase (name) ? value.toString () : super.getHeader (name);
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
                          if (ACCEPT_CONTENT_TYPE.equalsIgnoreCase (result))
                            listedAcceptHeader = true;
                          return result;
                        } else if (!listedAcceptHeader) {
                          listedAcceptHeader = true;
                          return ACCEPT_CONTENT_TYPE;
                        } else
                          throw new NoSuchElementException ();
                      }
                    };
                  }

                  @Override
                  public Enumeration<String> getHeaders (String name) {
                    return ACCEPT_CONTENT_TYPE.equalsIgnoreCase (name)
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

          if (log.isDebugEnabled ())
            log.debug ("Serving "
                       + httpRequest.getMethod () + " request to " + httpRequest.getRequestURI () + " with parameters "
                       + httpRequest.getParameterMap ());

          super.invoke (httpRequest, response);
        }

        @Override
        protected void setExtensions (JAXRSServerFactoryBean bean, ServletConfig servletConfig) {
          bean.setExtensionMappings (extensions);
        }

        @Override
        protected void setAllInterceptors (JAXRSServerFactoryBean bean, ServletConfig servletConfig, String splitChar) throws ServletException {
          List<Interceptor<? extends org.apache.cxf.message.Message>> interceptors = new ArrayList<> ();
          interceptors.add (new AbstractPhaseInterceptor<org.apache.cxf.message.Message> (Phase.RECEIVE) {
            @Override
            public void handleMessage (org.apache.cxf.message.Message m) throws Fault {
              message.set (m);
            }
          });
          bean.setInInterceptors (interceptors);
        }

        @Override
        protected List<?> getProviders (ServletConfig servletConfig, String splitChar) throws ServletException {
          return new ArrayList<> (providers);
        }

        @Override
        protected Map<Class<?>, Map<String, List<String>>> getServiceClasses (ServletConfig servletConfig,
                                                                              boolean modelAvailable,
                                                                              String splitChar) throws ServletException {
          return new AbstractMap<Class<?>, Map<String, List<String>>> () {
            @Override
            public Set<Class<?>> keySet () {
              return new HashSet<Class<?>> (convert (resources, new Converter<KeyHolder, Class<?>> () {
                @Override
                public Class<?> convert (KeyHolder from) {
                  return from.key.getTypeLiteral ().getRawType ();
                }
              }));
            }

            @Override
            public Set<Entry<Class<?>, Map<String, List<String>>>> entrySet () {
              throw new UnsupportedOperationException ();
            }
          };
        }

        @Override
        protected Map<Class<?>, ResourceProvider> getResourceProviders (ServletConfig servletConfig,
                                                                        Map<Class<?>, Map<String, List<String>>> resourceClasses) throws ServletException {
          return new AbstractMap<Class<?>, ResourceProvider> () {
            private final Map<Key<?>, Provider<?>> map;

            {
              map = new HashMap<> ();
              for (KeyHolder key : resources)
                map.put (key.key, injector.getProvider (key.key));
            }

            @Override
            public Set<Entry<Class<?>, ResourceProvider>> entrySet () {
              return new AbstractSet<Entry<Class<?>, ResourceProvider>> () {
                private final Set<Entry<Key<?>, Provider<?>>> set = map.entrySet ();

                @Override
                public int size () {
                  return set.size ();
                }

                @Override
                public Iterator<Entry<Class<?>, ResourceProvider>> iterator () {
                  return new Iterator<Entry<Class<?>, ResourceProvider>> () {
                    private final Iterator<Entry<Key<?>, Provider<?>>> iterator = set.iterator ();

                    @Override
                    public boolean hasNext () {
                      return iterator.hasNext ();
                    }

                    @Override
                    public Entry<Class<?>, ResourceProvider> next () {
                      return new Entry<Class<?>, ResourceProvider> () {
                        private final Entry<Key<?>, Provider<?>> entry = iterator.next ();

                        @Override
                        public ResourceProvider setValue (ResourceProvider value) {
                          throw new UnsupportedOperationException ();
                        }

                        @Override
                        public ResourceProvider getValue () {
                          return new ResourceProvider () {

                            @Override
                            public Object getInstance (org.apache.cxf.message.Message m) {
                              return entry.getValue ().get ();
                            }

                            @Override
                            public Class<?> getResourceClass () {
                              return entry.getKey ().getTypeLiteral ().getRawType ();
                            }

                            @Override
                            public boolean isSingleton () {
                              return false;
                            }

                            @Override
                            public void releaseInstance (org.apache.cxf.message.Message m, Object o) {}
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
                      throw new UnsupportedOperationException ();
                    }
                  };
                }
              };
            }
          };
        }

        @Override
        protected boolean getStaticSubResolutionValue (ServletConfig servletConfig) {
          return false;
        }

        @Override
        protected void setInvoker (JAXRSServerFactoryBean bean, ServletConfig servletConfig) throws ServletException {
          bean.setInvoker (new JAXRSInvoker () {
            @Override
            protected Object performInvocation (Exchange exchange, Object serviceObject, Method m, Object[] paramArray) throws Exception {
              m.setAccessible (true);
              return super.performInvocation (exchange, serviceObject, m, paramArray);
            }
          });
        }
      }

      @Override
      public void serviceRegex (final String regex) {
        binder.install (new ServletModule () {
          @Override
          protected void configureServlets () {
            serveRegex (regex).with (new InjectedCxfJaxrsServlet ("<" + regex + ">"));
          }
        });
      }

      @Override
      public void service (final String url) {
        binder.install (new ServletModule () {
          @Override
          protected void configureServlets () {
            serve (url).with (new InjectedCxfJaxrsServlet (url.replaceAll ("\\*+", "")));
          }
        });
      }
    });
  }

  /**
   * Binds resources
   */
  public void configure (ResourceBinder binder) {}

  /**
   * Configures content negotiation
   */
  public void configure (ContentNegotiationConfigurer content) {}

  /**
   * Configures service root
   */
  public void configure (ServiceBinder binder) {}
}
