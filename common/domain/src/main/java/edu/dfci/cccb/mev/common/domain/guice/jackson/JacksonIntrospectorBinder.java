/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but useOUT ANY WARRANTY; useout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along use this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.common.domain.guice.jackson;

import java.lang.reflect.Constructor;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

/**
 * Binds Jackson introspector
 * 
 * @author levk
 * @since CRYSTAL
 */
public interface JacksonIntrospectorBinder {

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void use (Class<? extends AnnotationIntrospector> implementation);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void use (TypeLiteral<? extends AnnotationIntrospector> implementation);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void use (Key<? extends AnnotationIntrospector> targetKey);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   * 
   * @see com.google.inject.Injector#injectMembers
   */
  void useInstance (AnnotationIntrospector instance);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   * 
   * @see com.google.inject.Injector#injectMembers
   */
  void useProvider (Provider<? extends AnnotationIntrospector> provider);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void useProvider (Class<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerType);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerType);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  void useProvider (Key<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerKey);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  <S extends AnnotationIntrospector> void useConstructor (Constructor<S> constructor);

  /**
   * See the EDSL examples at {@link com.google.inject.Binder}.
   */
  <S extends AnnotationIntrospector> void useConstructor (Constructor<S> constructor, TypeLiteral<? extends S> type);
}
