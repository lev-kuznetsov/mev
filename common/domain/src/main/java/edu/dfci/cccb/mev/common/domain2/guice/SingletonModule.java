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

package edu.dfci.cccb.mev.common.domain2.guice;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * @author levk
 * @since CRYSTAL
 */
public abstract class SingletonModule implements Module {

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode() */
  @Override
  public int hashCode () {
    return getClass ().hashCode ();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object) */
  @Override
  public boolean equals (Object obj) {
    return obj instanceof SingletonModule && getClass ().equals (obj.getClass ());
  }

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {}
}
