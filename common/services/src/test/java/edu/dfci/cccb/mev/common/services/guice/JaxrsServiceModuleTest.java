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

import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.named;
import static java.util.EnumSet.allOf;
import static org.apache.log4j.lf5.util.StreamUtils.getBytes;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.RequestScoped;

import edu.dfci.cccb.mev.common.services.guice.annotation.ContentNegotiationConfiguration;
import edu.dfci.cccb.mev.common.services.guice.annotation.Handles;
import edu.dfci.cccb.mev.common.services.guice.annotation.Publishes;

public class JaxrsServiceModuleTest {

  @Test
  public void simple () throws Exception {
    try {
      assertEquals ("world", connect (serve (new JaxrsServiceModule () {
        public void configure (JaxrsServiceBinder binder) {
          binder.publishing (HelloService.class).to (SimpleHelloServiceImpl.class).in (RequestScoped.class);
          binder.service ("/*");
        }
      }), "/hello/hi"));
    } finally {
      stop ();
    }
  }

  @Test
  public void annotated () throws Exception {
    try {
      assertEquals ("world", connect (serve (new JaxrsServiceModule () {
        @Publishes
        @Provides
        public HelloService helloService () {
          return new SimpleHelloServiceImpl ();
        }

        public void configure (JaxrsServiceBinder binder) {
          binder.service ("/*");
        }
      }), "/hello/hi"));
    } finally {
      stop ();
    }
  }

  @Test
  public void injectedAndScoped () throws Exception {
    try {
      int port = serve (new JaxrsServiceModule () {
        public void configure (JaxrsServiceBinder binder) {
          binder.bind (String.class).annotatedWith (named ("greeting")).toInstance ("Hello");
          binder.publishing (HelloService.class).to (InjectedScopedHelloServiceImpl.class);
          binder.service ("/*");
        }
      });
      assertEquals ("Hello world!", connect (port, "/hello/world"));
      assertEquals ("Hello world2!", connect (port, "/hello/world2"));
      assertEquals ("Hello world again!", connect (port, "/hello/world"));
      assertEquals ("Hello world2 again!", connect (port, "/hello/world2"));
      assertEquals ("Hello world again!", connect (port, "/hello/world"));
    } finally {
      stop ();
    }
  }

  @Test
  public void withProvider () throws Exception {
    try {
      assertEquals ("(hello,5)", connect (serve (new JaxrsServiceModule () {
        public void configure (JaxrsServiceBinder binder) {
          binder.publishing (PojoService.class).to (PojoServiceImpl.class);
          binder.handling (PojoWriter.class);
          binder.service ("/*");
        }
      }), "/pojo?word=hello&number=5"));
    } finally {
      stop ();
    }
  }

  @Test
  public void withProviderServedOffRoot () throws Exception {
    try {
      assertEquals ("(hello,5)", connect (serve (new JaxrsServiceModule () {
        public void configure (JaxrsServiceBinder binder) {
          binder.publishing (PojoService.class).to (PojoServiceImpl.class);
          binder.handling (PojoWriter.class);
          binder.service ("/test/*");
        }
      }), "/test/pojo?word=hello&number=5"));
    } finally {
      stop ();
    }
  }

  @Test
  public void annotatedSeparateDefinitions () throws Exception {
    try {
      assertEquals ("(hello,5)", connect (serve (new JaxrsServiceModule () {
        public void configure (JaxrsServiceBinder binder) {
          binder.service ("/test/*");
        }
      }, new JaxrsServiceModule () {
        @Publishes
        @Provides
        public PojoService pojoService () {
          return new PojoServiceImpl ();
        }

        @Handles
        @Provides
        public PojoWriter pojoWriter () {
          return new PojoWriter ();
        }
      }), "/test/pojo?word=hello&number=5"));
    } finally {
      stop ();
    }
  }
  
  @Test
  public void annotatedWithExtensionNegotiation () throws Exception {
    try {
      assertEquals ("custom(hello,5)", connect (serve (new JaxrsServiceModule () {
        public void configure (JaxrsServiceBinder binder) {
          binder.service ("/test/*");
        }
      }, new JaxrsServiceModule () {
        @Publishes
        @Provides
        public PojoService pojoService () {
          return new PojoServiceImpl ();
        }

        @Handles
        @Provides
        public PojoWriter pojoWriter () {
          return new PojoWriter ();
        }
        
        @Handles
        @Provides
        public CustomContentPojoWriter customContentPojoWriter () {
          return new CustomContentPojoWriter ();
        }
        
        @Override
        public void configure (ContentNegotiationConfiguration configurer) {
          configurer.extension ().map ("xc", new MediaType ("application", "x-custom"));
        }
      }), "/test/pojo.xc?word=hello&number=5"));
    } finally {
      stop ();
    }
  }
  
  @Test
  public void annotatedWithParameterNegotiation () throws Exception {
    try {
      assertEquals ("custom(hello,5)", connect (serve (new JaxrsServiceModule () {
        public void configure (JaxrsServiceBinder binder) {
          binder.service ("/test/*");
        }
      }, new JaxrsServiceModule () {
        @Publishes
        @Provides
        public PojoService pojoService () {
          return new PojoServiceImpl ();
        }

        @Handles
        @Provides
        public PojoWriter pojoWriter () {
          return new PojoWriter ();
        }
        
        @Handles
        @Provides
        public CustomContentPojoWriter customContentPojoWriter () {
          return new CustomContentPojoWriter ();
        }
        
        @Override
        public void configure (ContentNegotiationConfiguration configurer) {
          configurer.parameter ("format").map ("xc", new MediaType ("application", "x-custom"));
        }
      }), "/test/pojo?word=hello&number=5&format=xc"));
    } finally {
      stop ();
    }
  }

  @Test
  public void indirect () throws Exception {
    try {
      assertEquals ("(hello,5)", connect (serve (new Module () {
        public void configure (Binder binder) {
          binder.install (new JaxrsServiceModule () {
            public void configure (JaxrsServiceBinder binder) {
              binder.publishing (PojoService.class).to (PojoServiceImpl.class);
              binder.handling (PojoWriter.class);
              binder.service ("/*");
            }
          });
        }
      }), "/pojo?word=hello&number=5"));
    } finally {
      stop ();
    }
  }

  private String connect (final int port, final String uri) throws IOException {
    return new String (getBytes (new URL ("http://localhost:" + port + uri).openStream ()));
  }

  private Server server = null;

  private int serve (final ServletContextListener context) throws Exception {
    server = new Server (0);
    server.setHandler (new ServletContextHandler (server, "/") {
      {
        addEventListener (context);
        addFilter (GuiceFilter.class, "/*", allOf (DispatcherType.class));
        addServlet (DefaultServlet.class, "/");
      }
    });
    server.start ();
    return ((ServerConnector) server.getConnectors ()[0]).getLocalPort ();
  }

  private int serve (final Module... modules) throws Exception {
    return serve (new GuiceServletContextListener () {

      @Override
      protected Injector getInjector () {
        return createInjector (modules);
      }
    });
  }

  private void stop () throws Exception {
    if (server != null) {
      server.stop ();
      server.join ();
    }
  }
}
