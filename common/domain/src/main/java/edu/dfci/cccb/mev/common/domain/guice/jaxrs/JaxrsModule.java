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

package edu.dfci.cccb.mev.common.domain.guice.jaxrs;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;

import java.lang.reflect.Constructor;

import javax.inject.Singleton;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

/**
 * @author levk
 * @since CRYSTAL
 */
public class JaxrsModule implements Module {

  protected static final String PROVIDERS = "jaxrs.providers";

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    final Multibinder<Object> providers = newSetBinder (binder,
                                                        new TypeLiteral<Object> () {},
                                                        named (PROVIDERS)).permitDuplicates ();

    configure (new ExceptionBinder () {

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> providerKey) {
        providers.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends ExceptionMapper<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Provider<? extends ExceptionMapper<?>> provider) {
        providers.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useInstance (ExceptionMapper<?> instance) {
        providers.addBinding ().toInstance (instance);
      }

      @Override
      public <S extends ExceptionMapper<?>> void useConstructor (Constructor<S> constructor,
                                                                 TypeLiteral<? extends S> type) {
        providers.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends ExceptionMapper<?>> void useConstructor (Constructor<S> constructor) {
        providers.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends ExceptionMapper<?>> targetKey) {
        providers.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends ExceptionMapper<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Class<? extends ExceptionMapper<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }
    });

    configure (new MessageReaderBinder () {

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends MessageBodyReader<?>>> providerKey) {
        providers.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends MessageBodyReader<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends MessageBodyReader<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Provider<? extends MessageBodyReader<?>> provider) {
        providers.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useInstance (MessageBodyReader<?> instance) {
        providers.addBinding ().toInstance (instance);
      }

      @Override
      public <S extends MessageBodyReader<?>> void useConstructor (Constructor<S> constructor,
                                                                   TypeLiteral<? extends S> type) {
        providers.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends MessageBodyReader<?>> void useConstructor (Constructor<S> constructor) {
        providers.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends MessageBodyReader<?>> targetKey) {
        providers.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends MessageBodyReader<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Class<? extends MessageBodyReader<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }
    });

    configure (new MessageWriterBinder () {

      @Override
      public void useProvider (Key<? extends javax.inject.Provider<? extends MessageBodyWriter<?>>> providerKey) {
        providers.addBinding ().toProvider (providerKey).in (Singleton.class);
      }

      @Override
      public void useProvider (TypeLiteral<? extends javax.inject.Provider<? extends MessageBodyWriter<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Class<? extends javax.inject.Provider<? extends MessageBodyWriter<?>>> providerType) {
        providers.addBinding ().toProvider (providerType).in (Singleton.class);
      }

      @Override
      public void useProvider (Provider<? extends MessageBodyWriter<?>> provider) {
        providers.addBinding ().toProvider (provider).in (Singleton.class);
      }

      @Override
      public void useInstance (MessageBodyWriter<?> instance) {
        providers.addBinding ().toInstance (instance);
      }

      @Override
      public <S extends MessageBodyWriter<?>> void useConstructor (Constructor<S> constructor,
                                                                   TypeLiteral<? extends S> type) {
        providers.addBinding ().toConstructor (constructor, type).in (Singleton.class);
      }

      @Override
      public <S extends MessageBodyWriter<?>> void useConstructor (Constructor<S> constructor) {
        providers.addBinding ().toConstructor (constructor).in (Singleton.class);
      }

      @Override
      public void use (Key<? extends MessageBodyWriter<?>> targetKey) {
        providers.addBinding ().to (targetKey).in (Singleton.class);
      }

      @Override
      public void use (TypeLiteral<? extends MessageBodyWriter<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }

      @Override
      public void use (Class<? extends MessageBodyWriter<?>> implementation) {
        providers.addBinding ().to (implementation).in (Singleton.class);
      }
    });
  }

  /**
   * Binds {@link ExceptionMapper} providers
   */
  public void configure (ExceptionBinder binder) {}

  /**
   * Binds {@link MessageBodyReader} providers
   */
  public void configure (MessageReaderBinder binder) {}

  /**
   * Binds {@link MessageBodyWriter} providers
   */
  public void configure (MessageWriterBinder binder) {}
}
