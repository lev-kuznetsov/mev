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

import static com.google.inject.Key.get;
import static com.google.inject.internal.UniqueAnnotations.create;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.util.Arrays.asList;
import static java.util.Collections.enumeration;
import static javax.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
import static org.apache.cxf.message.Message.ACCEPT_CONTENT_TYPE;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

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
import org.apache.cxf.message.Message;

import com.google.inject.Binder;
import com.google.inject.Binding;
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
import edu.dfci.cccb.mev.common.services.support.ResourceNotFoundException;

/**
 * JAX-RS service module
 * 
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public class ServiceModule extends JaxrsModule {

  protected static final Annotation MESSAGE_QUALIFIER = create ();
  protected static final Annotation PARAMETER_NAME_QUALIFIFER = create ();
  protected static final Annotation EXTENSIONS_QUALIFIER = create ();
  protected static final Annotation PARAMETER_VALUES_QUALIFIER = create ();
  protected static final Annotation RESOURCES_QUALIFIER = create ();

  protected static final Key<Message> MESSAGE = get (Message.class, MESSAGE_QUALIFIER);
  protected static final Key<String> PARAMETER_NAME = get (String.class, PARAMETER_NAME_QUALIFIFER);
  protected static final Key<Set<Entry<String, MediaType>>> EXTENSIONS =
                                                                         get (new TypeLiteral<Set<Entry<String, MediaType>>> () {},
                                                                              EXTENSIONS_QUALIFIER);
  protected static final Key<Set<Entry<String, MediaType>>> PARAMETER_VALUES =
                                                                               get (new TypeLiteral<Set<Entry<String, MediaType>>> () {},
                                                                                    PARAMETER_VALUES_QUALIFIER);
  protected static final Key<Set<Entry<String, Provider<?>>>> RESOURCES =
                                                                          get (new TypeLiteral<Set<Entry<String, Provider<?>>>> () {},
                                                                               RESOURCES_QUALIFIER);

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  @OverridingMethodsMustInvokeSuper
  public void configure (final Binder binder) {
    super.configure (binder);

    @Path ("/")
    @Accessors (fluent = true)
    class Root {
      private @Inject Provider<ResourceNotFoundException> notFound;
      private Map<String, Provider<?>> dispatch;

      @Inject
      private void configure (final Injector injector) {
        dispatch = new HashMap<String, Provider<?>> (new AbstractMap<String, Provider<?>> () {
          private final @Getter Set<Entry<String, Provider<?>>> entrySet = injector.getInstance (RESOURCES);
        }) {
          private static final long serialVersionUID = 1L;

          @Override
          public Provider<?> put (String key, Provider<?> value) {
            if (value == null)
              throw new NullPointerException ("Resource provider cannot be null");
            else if (super.put (key, value) != null)
              throw new IllegalArgumentException ("Multiple rest bindings to /" + key);
            else
              return null;
          }
        };
      }

      @Path ("{path}")
      public Object dispatch (@PathParam ("path") String path) throws ResourceNotFoundException {
        Provider<?> provider = dispatch.get (path);
        if (provider == null)
          throw notFound.get ();
        else
          return provider.get ();
      }

      @Override
      public String toString () {
        return "Root dispatch serving " + dispatch;
      }
    }

    configure (new ResourceBinder () {
      final Multibinder<Entry<String, Provider<?>>> resources =
                                                                newSetBinder (binder,
                                                                              new TypeLiteral<Entry<String, Provider<?>>> () {},
                                                                              RESOURCES_QUALIFIER);

      private Entry<String, Provider<?>> entry (final String dispatch, final Key<?> bind) {
        return new Entry<String, Provider<?>> () {
          private @Getter final String key = dispatch;
          private @Getter final Provider<?> value = binder.getProvider (bind);

          @Override
          public Provider<?> setValue (Provider<?> value) {
            throw new UnsupportedOperationException ();
          }
        };
      }

      @Override
      public <T> AnnotatedBindingBuilder<T> publish (String dispatch, Class<T> type) {
        resources.addBinding ().toInstance (entry (dispatch, get (type)));
        return binder.bind (type);
      }

      @Override
      public <T> AnnotatedBindingBuilder<T> publish (String dispatch, final TypeLiteral<T> type) {
        resources.addBinding ().toInstance (entry (dispatch, get (type)));
        return binder.bind (type);
      }

      @Override
      public <T> LinkedBindingBuilder<T> publish (String dispatch, Key<T> key) {
        resources.addBinding ().toInstance (entry (dispatch, key));
        return binder.bind (key);
      }
    });

    final Multibinder<Entry<String, MediaType>> parameterValues =
                                                                  newSetBinder (binder,
                                                                                new TypeLiteral<Entry<String, MediaType>> () {},
                                                                                PARAMETER_VALUES_QUALIFIER);
    final Multibinder<Entry<String, MediaType>> extensions =
                                                             newSetBinder (binder,
                                                                           new TypeLiteral<Entry<String, MediaType>> () {},
                                                                           EXTENSIONS_QUALIFIER);

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
        binder.bindConstant ().annotatedWith (PARAMETER_NAME_QUALIFIFER).to (name);
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
      private ThreadLocal<Message> message () {
        return new ThreadLocal<> ();
      }

      @Provides
      public Message message (ThreadLocal<Message> message) {
        return message.get ();
      }

      @Provides
      @RequestScoped
      public UriInfo uri (Message message) {
        return new UriInfoImpl (message);
      }

      @Provides
      public HttpHeaders header (Message message) {
        return new HttpHeadersImpl (message);
      }

      @Provides
      public Providers providers (Message message) {
        return new ProvidersImpl (message);
      }

      @Provides
      public SecurityContext securityContext (Message message) {
        return new SecurityContextImpl (message);
      }

      @Provides
      public Request request (Message message) {
        return new RequestImpl (message);
      }
    });

    configure (new ServiceBinder () {

      class InjectedCxfJaxrsServlet extends CXFNonSpringJaxrsServlet {
        private static final long serialVersionUID = 1L;

        private @Inject ThreadLocal<Message> message;
        private Set<Object> providers;
        private String parameter;
        private Map<String, MediaType> parameterValues;
        private Map<Object, Object> extensions;
        private Root root = new Root ();

        private <T> T getOptional (Injector injector, Key<T> key) {
          Binding<T> binding = injector.getExistingBinding (key);
          return binding == null ? null : binding.getProvider ().get ();
        }

        @Inject
        private void configure (final Injector injector) {
          injector.injectMembers (root);

          providers = getOptional (injector, Key.get (new TypeLiteral<Set<Object>> () {}, PROVIDERS));

          parameter = getOptional (injector, Key.get (String.class, PARAMETER_NAME_QUALIFIFER));

          extensions = new HashMap<Object, Object> (new AbstractMap<String, MediaType> () {
            private final Set<Entry<String, MediaType>> set = getOptional (injector,
                                                                           EXTENSIONS);

            @Override
            public Set<Entry<String, MediaType>> entrySet () {
              return set;
            }
          });

          parameterValues = new HashMap<> (new AbstractMap<String, MediaType> () {
            private final Set<Entry<String, MediaType>> set =
                                                              getOptional (injector,
                                                                           Key.get (new TypeLiteral<Set<Entry<String, MediaType>>> () {},
                                                                                    PARAMETER_VALUES_QUALIFIER));

            @Override
            public Set<Entry<String, MediaType>> entrySet () {
              return set;
            }
          });
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
        protected List<?> getProviders (ServletConfig servletConfig, String splitChar) throws ServletException {
          return new ArrayList<> (providers);
        }

        @Override
        protected Map<Class<?>, Map<String, List<String>>> getServiceClasses (ServletConfig servletConfig,
                                                                              boolean modelAvailable,
                                                                              String splitChar) throws ServletException {
          return Collections.<Class<?>, Map<String, List<String>>>singletonMap (Root.class, null);
        }

        @Override
        protected Map<Class<?>, ResourceProvider> getResourceProviders (ServletConfig servletConfig,
                                                                        Map<Class<?>, Map<String, List<String>>> resourceClasses) throws ServletException {
          return Collections.<Class<?>, ResourceProvider>singletonMap (Root.class, new ResourceProvider () {
            @Override
            public void releaseInstance (Message m, Object o) {}

            @Override
            public boolean isSingleton () {
              return true;
            }

            @Override
            public Class<?> getResourceClass () {
              return Root.class;
            }

            @Override
            public Object getInstance (Message m) {
              return root;
            }
          });
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
              message.set (exchange.getInMessage ());
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
            serveRegex (regex).with (new InjectedCxfJaxrsServlet ());
          }
        });
      }

      @Override
      public void service (final String url) {
        binder.install (new ServletModule () {
          @Override
          protected void configureServlets () {
            serve (url).with (new InjectedCxfJaxrsServlet ());
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
