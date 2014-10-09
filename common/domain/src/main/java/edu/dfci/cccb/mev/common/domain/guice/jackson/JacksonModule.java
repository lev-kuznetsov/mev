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

import static com.fasterxml.jackson.databind.AnnotationIntrospector.nopInstance;
import static com.fasterxml.jackson.databind.AnnotationIntrospector.pair;
import static com.google.inject.multibindings.Multibinder.newSetBinder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
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

    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        binder.bind (ObjectMapper.class).toProvider (new Provider<ObjectMapper> () {
          private @Inject (optional = true) Set<JsonSerializer<?>> serializers;
          private @Inject (optional = true) Set<AnnotationIntrospector> introspectors;
          private @Inject (optional = true) Set<com.fasterxml.jackson.databind.Module> modules;
          private @Inject (optional = true) InjectableValues injectables;
          private @Inject Injector injector;

          private AnnotationIntrospector chain (Iterator<AnnotationIntrospector> iterator) {
            return iterator.hasNext () ? pair (iterator.next (), chain (iterator)) : nopInstance ();
          }

          @Override
          public ObjectMapper get () {
            ObjectMapper mapper = new ObjectMapper ();

            if (serializers != null && !serializers.isEmpty ())
              mapper.setSerializerFactory (mapper.getSerializerFactory ()
                                                 .withAdditionalSerializers (new SimpleSerializers (new ArrayList<> (serializers))));

            if (introspectors != null && !introspectors.isEmpty ())
              mapper.setAnnotationIntrospector (chain (introspectors.iterator ()));

            if (modules != null && !modules.isEmpty ())
              mapper.registerModules (modules);

            if (injectables != null)
              mapper.setInjectableValues (injectables);

            mapper.setHandlerInstantiator (new HandlerInstantiator () {

              private <T> T instantiateAndInject (Class<?> clazz) {
                try {
                  @SuppressWarnings ("unchecked") T result = (T) clazz.newInstance ();
                  injector.injectMembers (result);
                  return result;
                } catch (InstantiationException | IllegalAccessException e) {
                  throw new RuntimeException (e);
                }
              }

              @Override
              public TypeResolverBuilder<?> typeResolverBuilderInstance (MapperConfig<?> config,
                                                                         Annotated annotated,
                                                                         Class<?> builderClass) {
                return instantiateAndInject (builderClass);
              }

              @Override
              public TypeIdResolver typeIdResolverInstance (MapperConfig<?> config,
                                                            Annotated annotated,
                                                            Class<?> resolverClass) {
                return instantiateAndInject (resolverClass);
              }

              @Override
              public JsonSerializer<?> serializerInstance (SerializationConfig config,
                                                           Annotated annotated,
                                                           Class<?> serClass) {
                return instantiateAndInject (serClass);
              }

              @Override
              public KeyDeserializer keyDeserializerInstance (DeserializationConfig config,
                                                              Annotated annotated,
                                                              Class<?> keyDeserClass) {
                return instantiateAndInject (keyDeserClass);
              }

              @Override
              public JsonDeserializer<?> deserializerInstance (DeserializationConfig config,
                                                               Annotated annotated,
                                                               Class<?> deserClass) {
                return instantiateAndInject (deserClass);
              }
            });
            return mapper;
          }
        })
              .in (Singleton.class);
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
}
