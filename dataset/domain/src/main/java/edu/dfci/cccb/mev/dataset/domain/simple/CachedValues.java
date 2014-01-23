/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.domain.simple;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.javatuples.Pair;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValues;

/**
 * @author levk
 * 
 */
public class CachedValues extends AbstractValues {

  private static final int CACHE_SIZE = 100000;
  private static final TimeUnit CACHE_AGE_UNIT = MINUTES;
  private static final long CACHE_AGE = 20;

  private final LoadingCache<Pair<String, String>, Double> cache;

  /**
   * 
   * @param values
   */
  public CachedValues (final Values values) {
    cache = newBuilder ().expireAfterWrite (CACHE_AGE, CACHE_AGE_UNIT)
                         .maximumSize (CACHE_SIZE)
                         .build (new CacheLoader<Pair<String, String>, Double> () {
                           /* (non-Javadoc)
                            * @see
                            * com.google.common.cache.CacheLoader#load(java
                            * .lang.Object) */
                           @Override
                           public Double load (Pair<String, String> key) throws Exception {
                             return values.get (key.getValue0 (), key.getValue1 ());
                           }
                         });
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Values#get(java.lang.String,
   * java.lang.String) */
  @Override
  public double get (String row, String column) throws InvalidCoordinateException {
    try {
      return cache.get (new Pair<String, String> (row, column));
    } catch (ExecutionException e) {
      if (e.getCause () instanceof InvalidCoordinateException)
        throw (InvalidCoordinateException) e.getCause ();
      else
        throw new InvalidCoordinateException (e); // shouldn't happen
    }
  }
}
