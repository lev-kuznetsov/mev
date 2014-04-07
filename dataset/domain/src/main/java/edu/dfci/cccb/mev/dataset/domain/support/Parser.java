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

package edu.dfci.cccb.mev.dataset.domain.support;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.valueOf;

import java.io.IOException;
import java.io.InputStream;

import edu.dfci.cccb.mev.dataset.domain.contract.Values.Value;

/**
 * @author levk
 * @since BAYLIE
 */
public interface Parser <I> {

  public static final Resolver<String, String> STRING = new Resolver<String, String> () {
    @Override
    public String resolve (String input) throws IOException {
      return input;
    }
  };

  public static final Resolver<String, Double> DOUBLE = new Resolver<String, Double> () {
    @Override
    public Double resolve (String input) throws IOException {
      try {
        if ("Inf".equals (input))
          return POSITIVE_INFINITY;
        else if ("-Inf".equals (input))
          return NEGATIVE_INFINITY;
        else if ("NA".equals (input))
          return NaN;
        else if ("null".equals (input))
          return NaN;
        else
          return valueOf (input);
      } catch (RuntimeException e) {
        throw new IOException (e);
      }
    }
  };

  <K, V> void parse (InputStream input,
                     Consumer<Value<K, V>> valueListener,
                     Consumer<K>[] keyListeners,
                     Resolver<I, K> keyResolver,
                     Resolver<I, V> valueResolver) throws IOException;
}
