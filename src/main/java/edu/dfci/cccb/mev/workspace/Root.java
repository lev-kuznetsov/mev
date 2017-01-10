/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Dana-Farber Cancer Institute
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package edu.dfci.cccb.mev.workspace;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

/**
 * Root resource
 * 
 * @author levk
 */
@ApplicationPath ("/p")
@Path ("/")
@Singleton
public class Root extends Application {

  /**
   * Database
   */
  private @Inject EntityManager db;

  /**
   * @return workspace
   */
  @Path ("")
  @Transactional
  public Workspace workspace (@CookieParam ("workspace") @DefaultValue ("0") long id,
                              @Context HttpServletResponse response) {
    return ofNullable (db.find (Workspace.class, id)).orElseGet ( () -> {
      Workspace w = new Workspace ();
      db.persist (w);
      Cookie c = new Cookie ("workspace", valueOf (w.id));
      c.setMaxAge ((int) SECONDS.convert (3, HOURS));
      response.addCookie (new Cookie ("workspace", valueOf (w.id)));
      return w;
    });
  }
}
