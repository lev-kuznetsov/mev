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
package edu.dfci.cccb.mev.analysis.r;

import static edu.dfci.cccb.mev.analysis.State.COMPLETED;
import static edu.dfci.cccb.mev.analysis.State.FAILED;
import static edu.dfci.cccb.mev.analysis.State.RUNNING;
import static edu.dfci.cccb.mev.analysis.State.STARTING;
import static edu.dfci.cccb.mev.tools.concurrent.CheckedCompletableFuture.callAsync;
import static edu.dfci.cccb.mev.tools.reflection.Classes.base;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.fasterxml.jackson.databind.JavaType;

import edu.dfci.cccb.mev.analysis.Analysis;
import edu.dfci.cccb.mev.analysis.Define;
import edu.dfci.cccb.mev.analysis.Error;
import edu.dfci.cccb.mev.analysis.Resolve;
import edu.dfci.cccb.mev.analysis.State;
import edu.dfci.cccb.mev.tools.jackson.rserve.RserveMapper;

/**
 * Rserve analysis runner
 * 
 * @author levk
 */
public class Rserve {

  /**
   * Executor
   */
  private @Inject ExecutorService executor;
  /**
   * Object mapper
   */
  private @Inject RserveMapper mapper;
  /**
   * Rserve
   */
  private @Inject Provider <RConnection> r;
  /**
   * Persistence
   */
  private @Inject EntityManager db;

  /**
   * Launches the analysis if it is in {@link State#STARTING} state
   * 
   * @param analysis
   */
  @PrePersist
  @PreUpdate
  protected void callback (Analysis analysis) {
    if (analysis.state () == STARTING) {
      analysis.transition (RUNNING);
      RConnection r = this.r.get ();
      callAsync (of ((Stream <Callable <Void>>) base (analysis.getClass ()).flatMap (c -> of (of (c.getDeclaredFields ()).filter (f -> {
        return f.isAnnotationPresent (Define.class);
      }).map (f -> (Callable <Void>) () -> {
        Define d = f.getAnnotation (Define.class);
        String n = "".equals (d.value ()) ? f.getName () : d.value ();
        if (!f.isAccessible ()) f.setAccessible (true);
        r.assign (n, mapper.writerFor (mapper.constructType (f.getGenericType ())).mapValue (f.get (analysis)));
        return null;
      }), of (c.getDeclaredMethods ()).filter (m -> {
        return m.isAnnotationPresent (Define.class);
      }).map (m -> (Callable <Void>) () -> {
        Define d = m.getAnnotation (Define.class);
        String n = "".equals (d.value ()) ? m.getName () : d.value ();
        if (!m.isAccessible ()) m.setAccessible (true);
        JavaType t = mapper.constructType (m.getGenericReturnType ());
        r.assign (n, mapper.writerFor (t).mapValue (m.invoke (analysis)));
        return null;
      })).flatMap (x -> x)), (Stream <Callable <Void>>) base (analysis.getClass ()).map (c -> (Callable <Void>) () -> {
        Rscript z = c.getAnnotation (Rscript.class);
        R l = c.getAnnotation (R.class);
        StringBuffer s = new StringBuffer ();
        if (z != null) {
          InputStream i = c.getResourceAsStream (z.value ());
          if (i == null) throw new IOException ("Unable to find script " + z.value ());
          s.append (new BufferedReader (new InputStreamReader (i)).lines ().parallel ().filter (g -> {
            return !g.startsWith ("#") && !"".equals (g.trim ());
          }).collect (joining ("\n")));
        }
        if (l != null) s.append (l.value ());
        if (!"".equals (s.toString ().trim ())) {
          REXP v = r.eval ("tryCatch({{" + s.toString () + "};NULL},warning=function(e)NULL,error=function(e)e)");
          if (v.isNull ()) return null;
          else if (v.inherits ("error"))
            throw new REngineException (r, ((REXPString) v.asList ().get ("message")).asString ());
          else throw new RuntimeException ("Unrecognized return object from Rserve " + v.toDebugString ());
        } else return null;
      }), (Stream <Callable <Void>>) base (analysis.getClass ()).flatMap (c -> of (of (c.getDeclaredFields ()).filter (f -> {
        return f.isAnnotationPresent (Resolve.class);
      }).map (f -> (Callable <Void>) () -> {
        Resolve v = f.getAnnotation (Resolve.class);
        String n = "".equals (v.value ()) ? f.getName () : v.value ();
        if (!f.isAccessible ()) f.setAccessible (true);
        try {
          JavaType t = mapper.constructType (f.getGenericType ());
          f.set (analysis, mapper.readerFor (t).mapExpression (r.get (n, null, true)));
        } catch (REngineException e) {
          if (v.required ()) throw new RserveException (r, "Unable to resolve " + n, e);
        }
        return null;
      }), of (c.getDeclaredMethods ()).filter (m -> {
        return m.isAnnotationPresent (Resolve.class);
      }).map (m -> (Callable <Void>) () -> {
        Resolve v = m.getAnnotation (Resolve.class);
        String n = "".equals (v.value ()) ? m.getName () : v.value ();
        if (!m.isAccessible ()) m.setAccessible (true);
        try {
          JavaType t = mapper.constructType (m.getGenericParameterTypes ()[0]);
          m.invoke (analysis, mapper.readerFor (t).mapExpression (r.get (n, null, true)));
        } catch (REngineException e) {
          if (v.required ()) throw new RserveException (r, "Unable to resolve " + n, e);
        }
        return null;
      })).flatMap (x -> x))).flatMap (x -> x).reduce ( () -> null, (f, s) -> () -> {
        f.call ();
        return s.call ();
      }), executor).thenRun ( () -> analysis.transition (COMPLETED)).exceptionally (e -> {
        analysis.transition (FAILED);
        try {
          base (analysis.getClass ()).flatMap (c -> {
            return of (of (c.getDeclaredFields ()).filter (f -> {
              return f.isAnnotationPresent (Error.class);
            }).map (f -> (Callable <Void>) () -> {
              if (!f.isAccessible ()) f.setAccessible (true);
              if (f.getType ().equals (String.class)) f.set (analysis, e.getMessage ());
              else if (f.getType ().isAssignableFrom (e.getClass ())) f.set (analysis, e);
              else throw new IllegalArgumentException ("Unable to inject error into field " + f, e);
              return null;
            }), of (c.getDeclaredMethods ()).filter (m -> {
              return m.isAnnotationPresent (Error.class);
            }).map (m -> (Callable <Void>) () -> {
              if (!m.isAccessible ()) m.setAccessible (true);
              if (m.getParameterTypes ()[0].equals (String.class)) m.invoke (analysis, e.getMessage ());
              else if (m.getParameterTypes ()[0].isAssignableFrom (e.getClass ())) m.invoke (analysis, e);
              else throw new IllegalArgumentException ("Unable to inject error into method " + m, e);
              return null;
            })).flatMap (x -> x);
          }).reduce ( () -> null, (f, s) -> () -> {
            f.call ();
            return s.call ();
          }).call ();
        } catch (Exception x) {
          RuntimeException y = new RuntimeException ("Unable to inject error", x);
          y.addSuppressed (e);
          throw y;
        }
        return null;
      }).whenComplete ( (x, e) -> {
        r.close ();
        db.merge (analysis);
        if (e instanceof RuntimeException) throw (RuntimeException) e;
        else if (e instanceof java.lang.Error) throw (java.lang.Error) e;
      });
    }
  }
}
