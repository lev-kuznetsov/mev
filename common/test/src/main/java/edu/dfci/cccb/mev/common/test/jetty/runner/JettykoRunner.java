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

package edu.dfci.cccb.mev.common.test.jetty.runner;

import static com.jayway.restassured.RestAssured.given;
import static java.util.EnumSet.allOf;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jukito.JukitoRunner;
import org.jukito.TestModule;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.jayway.restassured.specification.RequestSpecification;

import edu.dfci.cccb.mev.common.test.jetty.runner.annotation.PathSpec;
import edu.dfci.cccb.mev.common.test.jetty.runner.annotation.Port;

public class JettykoRunner extends JukitoRunner {

  public JettykoRunner (Class<?> klass) throws InitializationError,
                                       InvocationTargetException,
                                       InstantiationException,
                                       IllegalAccessException {
    super (klass);
  }

  public JettykoRunner (Class<?> klass, Injector injector) throws InitializationError,
                                                          InvocationTargetException,
                                                          InstantiationException,
                                                          IllegalAccessException {
    super (klass, injector);
  }

  private Server server;

  private static final class InjectorHolder {
    private volatile Injector injector;

    @Inject
    private synchronized void inject (Injector injector) {
      this.injector = injector;
      notifyAll ();
    }

    private synchronized Injector get () throws InterruptedException {
      if (injector == null)
        wait ();
      return injector;
    }
  }

  @Override
  protected Injector createInjector (final TestModule testModule) {
    try {
      server.stop ();
      server.join ();
    } catch (Exception e) {}

    try {
      // TODO: this is stupid, I can just create the injector serially here and
      // return it inside the context handler

      final InjectorHolder holder = new InjectorHolder ();

      server = new Server (0);
      server.setHandler (new ServletContextHandler () {
        {
          PathSpec pathSpec = getTestClass ().getJavaClass ().getAnnotation (PathSpec.class);
          addFilter (GuiceFilter.class, pathSpec == null ? "/*" : pathSpec.value (), allOf (DispatcherType.class));
          addServlet (DefaultServlet.class, "/");
          setSessionHandler (new SessionHandler ());

          ServletContextListener context = new GuiceServletContextListener () {

            @Override
            protected Injector getInjector () {
              return Guice.createInjector (testModule, new Module () {
                @Provides
                @Singleton
                @Port
                public int port () {
                  return ((ServerConnector) server.getConnectors ()[0]).getLocalPort ();
                }

                @Provides
                @Singleton
                public RequestSpecification request (@Port int port) {
                  return given ().port (port);
                }

                @Override
                public void configure (Binder binder) {
                  binder.newPrivateBinder ().bind (InjectorHolder.class).toInstance (holder);
                }
              });
            }
          };

          addEventListener (context);
        }
      });
      server.start ();
      return holder.get ();
    } catch (Exception e) {
      throw new RuntimeException (e);
    }
  }

  @Override
  protected Statement withAfterClasses (final Statement statement) {
    return new Statement () {

      @Override
      public void evaluate () throws Throwable {
        try {
          statement.evaluate ();
        } finally {
          try {
            server.stop ();
            server.join ();
          } catch (Exception e) {}
        }
      }
    };
  }
}
