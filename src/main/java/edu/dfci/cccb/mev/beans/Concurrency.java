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
package edu.dfci.cccb.mev.beans;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Executor context
 * 
 * @author levk
 */
@Stateless
public class Concurrency {

  /**
   * Managed thread factory
   */
  static @Produces @Resource ManagedThreadFactory factory;

  /**
   * @return executor
   */
  @Produces
  @Singleton
  static ExecutorService executor (ManagedThreadFactory factory) {
    return new ThreadPoolExecutor (0, getRuntime ().availableProcessors (), 1, MINUTES, new LinkedBlockingQueue <> (),
                                   factory) {
      /*
       * (non-Javadoc)
       * 
       * @see
       * java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread,
       * java.lang.Runnable)
       */
      @Override
      protected void beforeExecute (Thread t, Runnable r) {
        super.beforeExecute (t, r);
        try {
          UserTransaction ut = (UserTransaction) new InitialContext ().lookup ("java:comp/UserTransaction");
          ut.begin ();
        } catch (SystemException | NamingException | NotSupportedException e) {
          throw new RuntimeException (e);
        }
      }

      /*
       * (non-Javadoc)
       * 
       * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.
       * Runnable, java.lang.Throwable)
       */
      @Override
      protected void afterExecute (Runnable r, Throwable t) {
        super.afterExecute (r, t);
        try {
          UserTransaction ut = (UserTransaction) new InitialContext ().lookup ("java:comp/UserTransaction");
          ut.commit ();
        } catch (SystemException | NamingException | IllegalStateException | SecurityException | HeuristicMixedException
            | HeuristicRollbackException | RollbackException e) {
          throw new RuntimeException (e);
        }
      }

      /*
       * (non-Javadoc)
       * 
       * @see java.util.concurrent.ThreadPoolExecutor#toString()
       */
      @Override
      public String toString () {
        return "tx executor";
      }
    };
  }
}
