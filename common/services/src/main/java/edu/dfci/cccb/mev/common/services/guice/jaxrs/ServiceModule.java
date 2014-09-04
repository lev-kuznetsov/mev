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
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;
import static java.util.Arrays.asList;
import static java.util.Collections.enumeration;
import static javax.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.inject.Named;
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
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;

import lombok.extern.log4j.Log4j;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.impl.ProvidersImpl;
import org.apache.cxf.jaxrs.impl.RequestImpl;
import org.apache.cxf.jaxrs.impl.SecurityContextImpl;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import ch.lambdaj.function.convert.Converter;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;

/**
 * JAX-RS service module
 * 
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public class ServiceModule implements Module {

  private static final String PROVIDERS = "jaxrs.providers";
  private static final String RESOURCES = "jaxrs.resources";
  private static final String PARAMETER_NAME = "jaxrs.cn.query.param";
  private static final String EXTENSIONS = "jaxrs.cn.extensions";
  private static final String PARAMETER_VALUES = "jaxrs.cn.query.param.values";

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public final void configure (final Binder binder) {
    final Multibinder<Object> providers = newSetBinder (binder,
                                                        new TypeLiteral<Object> () {},
                                                        named (PROVIDERS));

    configure (new ExceptionBinder () {

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> providerKey) {
        providers.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Provider<? extends ExceptionMapper<?>> provider) {
        providers.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useInstance (ExceptionMapper<?> instance) {
        providers.addBinding ().toInstance (instance);
      }

      @Override
      public <S extends ExceptionMapper<?>> void useConstructor (Constructor<S> constructor,
                                                                 TypeLiteral<? extends S> type) {
        providers.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends ExceptionMapper<?>> void useConstructor (Constructor<S> constructor) {
        providers.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends ExceptionMapper<?>> targetKey) {
        providers.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends ExceptionMapper<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Class<? extends ExceptionMapper<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }
    });

    configure (new MessageReaderBinder () {

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends MessageBodyReader<?>>> providerKey) {
        providers.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends MessageBodyReader<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends MessageBodyReader<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Provider<? extends MessageBodyReader<?>> provider) {
        providers.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useInstance (MessageBodyReader<?> instance) {
        providers.addBinding ().toInstance (instance);
      }

      @Override
      public <S extends MessageBodyReader<?>> void useConstructor (Constructor<S> constructor,
                                                                   TypeLiteral<? extends S> type) {
        providers.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends MessageBodyReader<?>> void useConstructor (Constructor<S> constructor) {
        providers.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends MessageBodyReader<?>> targetKey) {
        providers.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends MessageBodyReader<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Class<? extends MessageBodyReader<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }
    });

    configure (new MessageWriterBinder () {

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends MessageBodyWriter<?>>> providerKey) {
        providers.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends MessageBodyWriter<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends MessageBodyWriter<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Provider<? extends MessageBodyWriter<?>> provider) {
        providers.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useInstance (MessageBodyWriter<?> instance) {
        providers.addBinding ().toInstance (instance);
      }

      @Override
      public <S extends MessageBodyWriter<?>> void useConstructor (Constructor<S> constructor,
                                                                   TypeLiteral<? extends S> type) {
        providers.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends MessageBodyWriter<?>> void useConstructor (Constructor<S> constructor) {
        providers.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends MessageBodyWriter<?>> targetKey) {
        providers.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends MessageBodyWriter<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Class<? extends MessageBodyWriter<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }
    });

    final Multibinder<Class<?>> resources = newSetBinder (binder, new TypeLiteral<Class<?>> () {}, named (RESOURCES));

    configure (new ResourceBinder () {

      @Override
      public <T> AnnotatedBindingBuilder<T> publish (Class<T> type) {
        resources.addBinding ().toInstance (type);
        return binder.bind (type);
      }

      @Override
      public <T> AnnotatedBindingBuilder<T> publish (TypeLiteral<T> type) {
        resources.addBinding ().toInstance (type.getRawType ());
        return binder.bind (type);
      }

      @Override
      public <T> LinkedBindingBuilder<T> publish (Key<T> key) {
        resources.addBinding ().toInstance (key.getTypeLiteral ().getRawType ());
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

    final ThreadLocal<Message> message = new ThreadLocal<Message> () {

      @Override
      protected Message initialValue () {
        throw new IllegalStateException ("Attempted to bind JAXRS message outside of request context");
      }
    };

    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        binder.bind (UriInfo.class).toProvider (new Provider<UriInfo> () {
          @Override
          public UriInfo get () {
            return new UriInfoImpl (message.get ());
          }
        }).in (RequestScoped.class);

        binder.bind (HttpHeaders.class).toProvider (new Provider<HttpHeaders> () {
          @Override
          public HttpHeaders get () {
            return new HttpHeadersImpl (message.get ());
          }
        }).in (RequestScoped.class);

        binder.bind (Providers.class).toProvider (new Provider<Providers> () {
          @Override
          public Providers get () {
            return new ProvidersImpl (message.get ());
          }
        }).in (RequestScoped.class);

        binder.bind (SecurityContext.class).toProvider (new Provider<SecurityContext> () {
          @Override
          public SecurityContext get () {
            return new SecurityContextImpl (message.get ());
          }
        }).in (RequestScoped.class);

        binder.bind (Request.class).toProvider (new Provider<Request> () {
          @Override
          public Request get () {
            return new RequestImpl (message.get ());
          }
        }).in (RequestScoped.class);
      }
    });

    configure (new ServiceBinder () {

      class InjectedCxfJaxrsServlet extends CXFNonSpringJaxrsServlet {
        private static final long serialVersionUID = 1L;

        private @Inject Injector injector;
        private @Inject @Named (PROVIDERS) Set<Object> providers;
        private @Inject @Named (RESOURCES) Set<Class<?>> resources;
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
          for (Class<?> r : resources) {
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
        protected void invoke (HttpServletRequest request, HttpServletResponse response) throws ServletException {
          if (parameter != null) {
            String key = request.getParameter (parameter);
            if (key != null) {
              final MediaType value = parameterValues.get (key);
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

          if (log.isDebugEnabled ())
            log.debug ("Serving "
                       + request.getMethod () + " request to " + request.getRequestURI () + " with parameters "
                       + request.getParameterMap ());

          super.invoke (request, response);
        }

        @Override
        protected void setExtensions (JAXRSServerFactoryBean bean, ServletConfig servletConfig) {
          bean.setExtensionMappings (extensions);
        }

        @Override
        protected void setAllInterceptors (JAXRSServerFactoryBean bean, ServletConfig servletConfig, String splitChar) throws ServletException {
          List<Interceptor<? extends Message>> interceptors = new ArrayList<> ();
          interceptors.add (new AbstractPhaseInterceptor<Message> (Phase.PRE_INVOKE) {
            @Override
            public void handleMessage (Message m) throws Fault {
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
              return resources;
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
                public int size () {
                  return set.size ();
                }

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
                          return get (getKey ());
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
   * Binds {@link ExceptionMapper} providers
   */
  public void configure (ExceptionBinder binder) {}

  /**
   * Binds {@link MessageBodyReader} providers
   */
  public void configure (MessageReaderBinder binder) {}

  /**
   * Binds {@link MessageBodyWriter} providers
   */
  public void configure (MessageWriterBinder binder) {}

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
