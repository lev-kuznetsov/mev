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

import static com.fasterxml.jackson.databind.ser.BeanSerializerFactory.instance;
import static com.google.inject.multibindings.Multibinder.newSetBinder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Set;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;

/**
 * Configures Jackson {@link ObjectMapper}
 * 
 * @author levk
 * @since CRYSTAL
 */
public class JacksonModule implements Module {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public final void configure (final Binder binder) {

    configure (new JacksonSerializerBinder () {
      private Multibinder<JsonSerializer<?>> serializers = newSetBinder (binder,
                                                                         new TypeLiteral<JsonSerializer<?>> () {});

      @Override
      public void withProvider (Key<? extends javax.inject.Provider<? extends JsonSerializer<?>>> providerKey) {
        serializers.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void withProvider (TypeLiteral<? extends javax.inject.Provider<? extends JsonSerializer<?>>> providerType) {
        serializers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void withProvider (Class<? extends javax.inject.Provider<? extends JsonSerializer<?>>> providerType) {
        serializers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void withProvider (Provider<? extends JsonSerializer<?>> provider) {
        serializers.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void withInstance (JsonSerializer<?> instance) {
        serializers.addBinding ().toInstance (instance);
      }

      @Override
      public <S extends JsonSerializer<?>> void withConstructor (Constructor<S> constructor,
                                                                 TypeLiteral<? extends S> type) {
        serializers.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends JsonSerializer<?>> void withConstructor (Constructor<S> constructor) {
        serializers.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void with (Key<? extends JsonSerializer<?>> targetKey) {
        serializers.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void with (TypeLiteral<? extends JsonSerializer<?>> implementation) {
        serializers.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void with (Class<? extends JsonSerializer<?>> implementation) {
        serializers.addBinding ().to (implementation).in (Singleton.class);
      }
    });

    configure (new JacksonIntrospectorBinder () {

      @Override
      public void use (Class<? extends AnnotationIntrospector> implementation) {
        binder.bind (AnnotationIntrospector.class).to (implementation).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends AnnotationIntrospector> implementation) {
        binder.bind (AnnotationIntrospector.class).to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends AnnotationIntrospector> targetKey) {
        binder.bind (AnnotationIntrospector.class).to (targetKey).in (Singleton.class);
      }

      @Override
      public void useInstance (AnnotationIntrospector instance) {
        binder.bind (AnnotationIntrospector.class).toInstance (instance);
      }

      @Override
      public void useProvider (Provider<? extends AnnotationIntrospector> provider) {
        binder.bind (AnnotationIntrospector.class).toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerType) {
        binder.bind (AnnotationIntrospector.class).toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerType) {
        binder.bind (AnnotationIntrospector.class).toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerKey) {
        binder.bind (AnnotationIntrospector.class).toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public <S extends AnnotationIntrospector> void useConstructor (Constructor<S> constructor) {
        binder.bind (AnnotationIntrospector.class).toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public <S extends AnnotationIntrospector> void useConstructor (Constructor<S> constructor,
                                                                     TypeLiteral<? extends S> type) {
        binder.bind (AnnotationIntrospector.class).toConstructor (constructor, type).in (Singleton.class);
      }
    });

    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        binder.bind (ObjectMapper.class).toInstance (new ObjectMapper () {
          private static final long serialVersionUID = 1L;

          @Inject
          public void withSerializers (Set<JsonSerializer<?>> serializers) {
            setSerializerFactory (instance.withAdditionalSerializers (new SimpleSerializers (new ArrayList<> (serializers))));
          }

          @Inject (optional = true)
          public void withIntrospector (AnnotationIntrospector introspector) {
            setAnnotationIntrospector (introspector);
          }
        });
      }
    });
  }

  /**
   * Configures serializers
   */
  public void configure (JacksonSerializerBinder binder) {}

  /**
   * Configures annotation introspector
   */
  public void configure (JacksonIntrospectorBinder binder) {}
}
