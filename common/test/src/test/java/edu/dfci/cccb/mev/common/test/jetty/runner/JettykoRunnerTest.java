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

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jukito.TestModule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.servlet.ServletModule;
import com.jayway.restassured.specification.RequestSpecification;

@RunWith (JettykoRunner.class)
public class JettykoRunnerTest {

  public static class JettykoRunnerTestModule extends TestModule {
    protected void configureTest () {
      install (new ServletModule () {
        protected void configureServlets () {
          serve ("/*").with (new HttpServlet () {
            private static final long serialVersionUID = 1L;

            protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                                                                                   IOException {
              try (PrintStream out = new PrintStream (resp.getOutputStream ())) {
                out.println ("RESPONSE");
              }
            }
          });
        }
      });
    }
  }

  @Test
  public void get (RequestSpecification given) throws Exception {
    assertThat (given.get ().body ().asString (), startsWith ("RESPONSE"));
  }
}
