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

import static java.lang.String.valueOf;

import java.io.IOException;
import java.io.OutputStream;

import edu.dfci.cccb.mev.dataset.domain.contract.Values;

/**
 * @author levk
 */
public interface Compiler <K, V> {

  public static final Emitter<String> STRING = new Emitter<String> () {

    @Override
    public void emit (String entity, OutputStream out) throws IOException {
      out.write (entity.getBytes ());
    }
  };

  public static final Emitter<Double> DOUBLE = new Emitter<Double> () {

    @Override
    public void emit (Double entity, OutputStream out) throws IOException {
      String value;
      if (entity == null || entity.isNaN ())
        value = "NA";
      else if (entity.isInfinite () && entity > 0)
        value = "Inf";
      else if (entity.isInfinite () && entity < 0)
        value = "-Inf";
      else
        value = valueOf (entity);
      out.write (value.getBytes ());
    }
  };

  void compile (OutputStream out, Emitter<K> keyEmitter, Emitter<V> valueEmitter, Values<K, V> values) throws IOException;
}
