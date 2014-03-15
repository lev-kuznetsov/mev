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

package edu.dfci.cccb.mev.common.services.guice.jaxrs;

import java.lang.reflect.Constructor;

import javax.ws.rs.ext.ExceptionMapper;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

/**
 * Binds {@link ExceptionMapper} providers
 * 
 * @author levk
 * @since CRYSTAL
 */
public interface ExceptionBinder {

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void use (Class<? extends ExceptionMapper<?>> implementation);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void use (TypeLiteral<? extends ExceptionMapper<?>> implementation);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void use (Key<? extends ExceptionMapper<?>> targetKey);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   * 
   * @see com.google.inject.Injector#injectMembers
   */
  void useInstance (ExceptionMapper<?> instance);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   * 
   * @see com.google.inject.Injector#injectMembers
   */
  void useProvider (Provider<? extends ExceptionMapper<?>> user);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void useProvider (Class<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> userType);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> userType);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void useProvider (Key<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> userKey);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  <S extends ExceptionMapper<?>> void useConstructor (Constructor<S> constructor);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  <S extends ExceptionMapper<?>> void useConstructor (Constructor<S> constructor,
                                                      TypeLiteral<? extends S> type);
}
