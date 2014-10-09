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

package edu.dfci.cccb.mev.common.domain.guice.jackson;

import java.lang.reflect.Constructor;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

/**
 * @author levk
 */
public interface JacksonProviderSetBinder <T> {
  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void with (Class<? extends T> implementation);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void with (TypeLiteral<? extends T> implementation);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void with (Key<? extends T> targetKey);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   * 
   * @see com.google.inject.Injector#injectMembers
   */
  void withInstance (T instance);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   * 
   * @see com.google.inject.Injector#injectMembers
   */
  void withProvider (Provider<? extends T> provider);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void withProvider (Class<? extends javax.inject.Provider<? extends T>> providerType);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void withProvider (TypeLiteral<? extends javax.inject.Provider<? extends T>> providerType);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void withProvider (Key<? extends javax.inject.Provider<? extends T>> providerKey);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  <S extends T> void withConstructor (Constructor<S> constructor);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  <S extends T> void withConstructor (Constructor<S> constructor, TypeLiteral<? extends S> type);
}
