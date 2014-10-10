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

import static com.fasterxml.jackson.databind.AnnotationIntrospector.pair;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static edu.dfci.cccb.mev.common.domain.guice.jackson.annotation.Handling.Factory.APPLICATION_JSON;
import static edu.dfci.cccb.mev.common.domain.guice.jackson.annotation.Handling.Factory.TEXT_TSV;
import static java.util.Collections.sort;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
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
  @OverridingMethodsMustInvokeSuper
  @Override
  public void configure (final Binder binder) {

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
      private Multibinder<AnnotationIntrospector> introspectors = newSetBinder (binder, AnnotationIntrospector.class);

      @Override
      public void with (Class<? extends AnnotationIntrospector> implementation) {
        introspectors.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void with (TypeLiteral<? extends AnnotationIntrospector> implementation) {
        introspectors.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void with (Key<? extends AnnotationIntrospector> targetKey) {
        introspectors.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void withInstance (AnnotationIntrospector instance) {
        introspectors.addBinding ().toInstance (instance);
      }

      @Override
      public void withProvider (Provider<? extends AnnotationIntrospector> provider) {
        introspectors.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void withProvider (Class<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerType) {
        introspectors.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void withProvider (TypeLiteral<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerType) {
        introspectors.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void withProvider (Key<? extends javax.inject.Provider<? extends AnnotationIntrospector>> providerKey) {
        introspectors.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public <S extends AnnotationIntrospector> void withConstructor (Constructor<S> constructor) {
        introspectors.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public <S extends AnnotationIntrospector> void withConstructor (Constructor<S> constructor,
                                                                      TypeLiteral<? extends S> type) {
        introspectors.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }
    });

    configure (new JacksonModuleBinder () {
      private Multibinder<com.fasterxml.jackson.databind.Module> modules =
                                                                           newSetBinder (binder,
                                                                                         com.fasterxml.jackson.databind.Module.class);

      @Override
      public void withProvider (Key<? extends javax.inject.Provider<? extends com.fasterxml.jackson.databind.Module>> providerKey) {
        modules.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void withProvider (TypeLiteral<? extends javax.inject.Provider<? extends com.fasterxml.jackson.databind.Module>> providerType) {
        modules.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void withProvider (Class<? extends javax.inject.Provider<? extends com.fasterxml.jackson.databind.Module>> providerType) {
        modules.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void withProvider (Provider<? extends com.fasterxml.jackson.databind.Module> provider) {
        modules.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void withInstance (com.fasterxml.jackson.databind.Module instance) {
        modules.addBinding ().toInstance (instance);
      }

      @Override
      public <S extends com.fasterxml.jackson.databind.Module> void withConstructor (Constructor<S> constructor,
                                                                                     TypeLiteral<? extends S> type) {
        modules.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends com.fasterxml.jackson.databind.Module> void withConstructor (Constructor<S> constructor) {
        modules.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void with (Key<? extends com.fasterxml.jackson.databind.Module> targetKey) {
        modules.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void with (TypeLiteral<? extends com.fasterxml.jackson.databind.Module> implementation) {
        modules.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void with (Class<? extends com.fasterxml.jackson.databind.Module> implementation) {
        modules.addBinding ().to (implementation).in (Singleton.class);
      }
    });

    configure (new JacksonInjectableValuesBinder () {

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends InjectableValues>> providerKey) {
        binder.bind (InjectableValues.class).toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends InjectableValues>> providerType) {
        binder.bind (InjectableValues.class).toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends InjectableValues>> providerType) {
        binder.bind (InjectableValues.class).toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Provider<? extends InjectableValues> provider) {
        binder.bind (InjectableValues.class).toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useInstance (InjectableValues instance) {
        binder.bind (InjectableValues.class).toInstance (instance);
      }

      @Override
      public <S extends InjectableValues> void useConstructor (Constructor<S> constructor, TypeLiteral<? extends S> type) {
        binder.bind (InjectableValues.class).toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends InjectableValues> void useConstructor (Constructor<S> constructor) {
        binder.bind (InjectableValues.class).toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends InjectableValues> targetKey) {
        binder.bind (InjectableValues.class).to (targetKey).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends InjectableValues> implementation) {
        binder.bind (InjectableValues.class).to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Class<? extends InjectableValues> implementation) {
        binder.bind (InjectableValues.class).to (implementation).in (Singleton.class);
      }
    });

    configure (new JacksonHandlerInstantiatorBinder () {

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends HandlerInstantiator>> providerKey) {
        binder.bind (HandlerInstantiator.class).toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends HandlerInstantiator>> providerType) {
        binder.bind (HandlerInstantiator.class).toProvider (providerType);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends HandlerInstantiator>> providerType) {
        binder.bind (HandlerInstantiator.class).toProvider (providerType);
      }

      @Override
      public void useProvider (Provider<? extends HandlerInstantiator> provider) {
        binder.bind (HandlerInstantiator.class).toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useInstance (HandlerInstantiator instance) {
        binder.bind (HandlerInstantiator.class).toInstance (instance);
      }

      @Override
      public <S extends HandlerInstantiator> void useConstructor (Constructor<S> constructor,
                                                                  TypeLiteral<? extends S> type) {
        binder.bind (HandlerInstantiator.class).toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends HandlerInstantiator> void useConstructor (Constructor<S> constructor) {
        binder.bind (HandlerInstantiator.class).toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends HandlerInstantiator> targetKey) {
        binder.bind (HandlerInstantiator.class).to (targetKey).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends HandlerInstantiator> implementation) {
        binder.bind (HandlerInstantiator.class).to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Class<? extends HandlerInstantiator> implementation) {
        binder.bind (HandlerInstantiator.class).to (implementation).in (Singleton.class);
      }
    });

    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        abstract class ObjectMapperProviderAdapter implements Provider<ObjectMapper> {
          private @Inject (optional = true) Set<JsonSerializer<?>> serializers;
          private @Inject (optional = true) Set<AnnotationIntrospector> introspectors;
          private @Inject (optional = true) Comparator<AnnotationIntrospector> introspectorComparator;
          private @Inject (optional = true) Set<com.fasterxml.jackson.databind.Module> modules;
          private @Inject (optional = true) InjectableValues injectables;
          private @Inject (optional = true) HandlerInstantiator instantiator;

          protected void configureSerializers (ObjectMapper mapper) {
            if (serializers != null && !serializers.isEmpty ())
              mapper.setSerializerFactory (mapper.getSerializerFactory ()
                                                 .withAdditionalSerializers (new SimpleSerializers (new ArrayList<> (serializers))));
          }

          protected void configureIntrospectors (ObjectMapper mapper) {
            if (introspectors != null && !introspectors.isEmpty ()) {
              ArrayList<AnnotationIntrospector> introspectors = new ArrayList<> (this.introspectors);
              if (introspectorComparator != null)
                sort (introspectors, introspectorComparator);

              AnnotationIntrospector aggregate = null;
              for (AnnotationIntrospector current : introspectors)
                aggregate = aggregate == null ? current : pair (aggregate, current);

              mapper.setAnnotationIntrospector (aggregate);
            }
          }

          protected void configureModules (ObjectMapper mapper) {
            if (modules != null && !modules.isEmpty ())
              mapper.registerModules (modules);
          }

          protected void configureInjectables (ObjectMapper mapper) {
            if (injectables != null)
              mapper.setInjectableValues (injectables);
          }

          protected void configureHandlerInstantiator (ObjectMapper mapper) {
            mapper.setHandlerInstantiator (instantiator);
          }
        }

        binder.bind (ObjectMapper.class)
              .annotatedWith (APPLICATION_JSON)
              .toProvider (new ObjectMapperProviderAdapter () {
                @Override
                public ObjectMapper get () {
                  ObjectMapper mapper = new ObjectMapper ();

                  configureSerializers (mapper);
                  configureIntrospectors (mapper);
                  configureModules (mapper);
                  configureInjectables (mapper);
                  configureHandlerInstantiator (mapper);

                  return mapper;
                }
              })
              .in (Singleton.class);

        binder.bind (ObjectMapper.class)
              .annotatedWith (TEXT_TSV)
              .toProvider (new ObjectMapperProviderAdapter () {
                @Override
                public ObjectMapper get () {
                  ObjectMapper mapper = new CsvMapper ();

                  configureIntrospectors (mapper);
                  configureModules (mapper);
                  configureInjectables (mapper);
                  configureHandlerInstantiator (mapper);

                  return mapper;
                }
              }).in (Singleton.class);
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

  /**
   * Configures Jackson modules
   */
  public void configure (JacksonModuleBinder binder) {}

  /**
   * Configures injectables values
   */
  public void configure (JacksonInjectableValuesBinder binder) {}

  /**
   * Configures handler instantiator
   */
  public void configure (JacksonHandlerInstantiatorBinder binder) {}
}
