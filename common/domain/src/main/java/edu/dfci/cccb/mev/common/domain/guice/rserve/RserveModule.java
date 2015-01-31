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

package edu.dfci.cccb.mev.common.domain.guice.rserve;

import static ch.lambdaj.Lambda.convert;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.lang.Integer.valueOf;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import lombok.extern.log4j.Log4j;

import org.apache.commons.configuration.Configuration;

import ch.lambdaj.function.convert.Converter;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

import edu.dfci.cccb.mev.common.domain.guice.rserve.annotation.Rserve;
import edu.dfci.cccb.mev.common.domain.jobs.r.R;
import edu.dfci.cccb.mev.common.domain.jobs.r.RserveDoubleDeserializer;
import edu.dfci.cccb.mev.common.domain.jobs.r.RserveDoubleSerializer;

/**
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public class RserveModule implements Module {

  public static final String HOSTS = "rserve.host";

  public static final String CONCURRENT_JOBS = "rserve.maximum.concurrent.jobs";

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    newSetBinder (binder, new TypeLiteral<JsonSerializer<?>> () {}, Rserve.class).addBinding ()
                                                                                 .toInstance (new RserveDoubleSerializer ());

    newSetBinder (binder, new TypeLiteral<JsonDeserializer<?>> () {}, Rserve.class).addBinding ()
                                                                                   .toInstance (new RserveDoubleDeserializer ());

    binder.bind (InetSocketAddress.class)
          .annotatedWith (Rserve.class)
          .toProvider (new Provider<InetSocketAddress> () {
            private Iterator<InetSocketAddress> hosts;

            @Inject
            private void configure (final @Rserve Configuration configuration) {
              hosts = new Iterator<InetSocketAddress> () {
                private final InetSocketAddress[] hosts = convert (configuration.getList (HOSTS,
                                                                                          asList ("localhost:6311")),
                                                                   new Converter<String, InetSocketAddress> () {
                                                                     @Override
                                                                     public InetSocketAddress convert (String from) {
                                                                       String[] split = from.split (":");
                                                                       if (split.length > 2)
                                                                         throw new IllegalArgumentException ("Bad syntax for rserve host "
                                                                                                             + from);
                                                                       int port = split.length < 2 ? 6311
                                                                                                  : valueOf (split[1]);
                                                                       return new InetSocketAddress (split[0], port);
                                                                     }
                                                                   }).toArray (new InetSocketAddress[0]);
                private int index = 0;

                @Override
                public boolean hasNext () {
                  return true;
                }

                @Override
                public InetSocketAddress next () {
                  if (index >= hosts.length)
                    index = 0;
                  return hosts[index++];
                }

                @Override
                public void remove () {}
              };
            }

            @Override
            public InetSocketAddress get () {
              return hosts.next ();
            }
          });

    binder.bind (Executor.class).annotatedWith (Rserve.class).toProvider (new Provider<Executor> () {
      private @Inject @Rserve Configuration configuration;

      @Override
      public Executor get () {
        int max = configuration.getInt (CONCURRENT_JOBS,
                                        configuration.getList (HOSTS, asList ("localhost:6311"))
                                                     .size () + 1);
        final UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler () {

          @Override
          public void uncaughtException (Thread t, Throwable e) {
            log.error ("Uncaught exception in Rserve watcher thread", e);
          }
        };
        return new ThreadPoolExecutor (0, max,
                                       5, MINUTES,
                                       new LinkedBlockingQueue<Runnable> (),
                                       new ThreadFactory () {

                                         @Override
                                         public Thread newThread (Runnable r) {
                                           return new Thread (r) {
                                             {
                                               setUncaughtExceptionHandler (exceptionHandler);
                                             }
                                           };
                                         }
                                       });
      }
    }).in (Singleton.class);

    binder.bind (R.class).in (Singleton.class);
  }
}
