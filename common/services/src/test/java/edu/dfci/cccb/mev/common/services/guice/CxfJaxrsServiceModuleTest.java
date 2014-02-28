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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.RequestScoped;

/**
 * @author levk
 */
public class CxfJaxrsServiceModuleTest {

  @Test
  public void simple () throws Exception {
    try {
      assertEquals ("world", connect (serve (new CxfJaxrsServiceModule () {

        @Override
        protected void configureServices () {
          publish (HelloService.class).to (SimpleHelloServiceImpl.class).in (RequestScoped.class);
          service ("/*");
        }
      }), "/hello/hi"));
    } finally {
      stop ();
    }
  }

  @Test
  public void injectedAndScoped () throws Exception {
    try {
      int port = serve (new CxfJaxrsServiceModule () {

        @Override
        protected void configureServices () {
          bind (String.class).annotatedWith (named ("greeting")).toInstance ("Hello");
          publish (HelloService.class).to (InjectedScopedHelloServiceImpl.class);
          service ("/*");
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
      assertEquals ("(hello,5)", connect (serve (new CxfJaxrsServiceModule () {

        @Override
        protected void configureServices () {
          publish (PojoService.class).to (PojoServiceImpl.class);
          provide (PojoWriter.class);
          service ("/*");
        }
      }), "/pojo?word=hello&number=5"));
    } finally {
      stop ();
    }
  }

  @Test
  public void withProviderServedOffRoot () throws Exception {
    try {
      assertEquals ("(hello,5)", connect (serve (new CxfJaxrsServiceModule () {

        @Override
        protected void configureServices () {
          publish (PojoService.class).to (PojoServiceImpl.class);
          provide (PojoWriter.class);
          service ("/test/*");
        }
      }), "/test/pojo?word=hello&number=5"));
    } finally {
      stop ();
    }
  }

  @Test
  public void indirect () throws Exception {
    try {
      assertEquals ("(hello,5)", connect (serve (new Module () {

        @Override
        public void configure (Binder binder) {
          binder.install (new CxfJaxrsServiceModule () {

            @Override
            protected void configureServices () {
              publish (PojoService.class).to (PojoServiceImpl.class);
              provide (PojoWriter.class);
              service ("/*");
            }
          });
        }
      }), "/pojo?word=hello&number=5"));
    } finally {
      stop ();
    }
  }

  @Test
  public void scanningOffRoot () throws Exception {
    try {
      assertEquals ("(hello,5)", connect (serve (new CxfJaxrsServiceModule () {

        @Override
        protected void configureServices () {
          service ("/test/*");
          scan ("edu.dfci.cccb.mev");
        }
      }), "/test/pojo?word=hello&number=5"));
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
