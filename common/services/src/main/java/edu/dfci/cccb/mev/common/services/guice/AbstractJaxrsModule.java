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

package edu.dfci.cccb.mev.common.services.guice;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;

import java.lang.reflect.Method;

import lombok.Delegate;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

import edu.dfci.cccb.mev.common.services.guice.annotation.Handles;

/**
 * Defines common JAX-RS facilities
 * <p/>
 * Subclassing this module enables the use of {@link Handles} annotation on the
 * {@link Provides} annotated methods
 * 
 * @author levk
 * @since CRYSTAL
 */
public abstract class AbstractJaxrsModule implements Module {

  /**
   * Binding name for JAX-RS providers
   */
  public static final String PROVIDERS = "jaxrs.providers";

  public static interface JaxrsBinder extends Binder {
    /**
     * @param type to bind as a JAX-RS provider
     * @return
     */
    <T> AnnotatedBindingBuilder<T> handling (Class<T> type);

    /**
     * @param type to bind as a JAX-RS provider
     * @return
     */
    <T> AnnotatedBindingBuilder<T> handling (TypeLiteral<T> type);

    /**
     * @param key to bind as a JAX-RS provider
     * @return
     */
    <T> LinkedBindingBuilder<T> handling (Key<T> key);
  }

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public final void configure (final Binder binder) {
    configure (new JaxrsBinder () {
      private final @Delegate Binder delegate = binder;
      private final Multibinder<Class<?>> providers = newSetBinder (binder,
                                                                    new TypeLiteral<Class<?>> () {},
                                                                    named (PROVIDERS));

      {
        registerHandlerProviders (AbstractJaxrsModule.this.getClass ());
      }

      @Override
      public <T> LinkedBindingBuilder<T> handling (Key<T> key) {
        providers.addBinding ().toInstance (key.getTypeLiteral ().getRawType ());
        return bind (key);
      }

      @Override
      public <T> AnnotatedBindingBuilder<T> handling (TypeLiteral<T> type) {
        providers.addBinding ().toInstance (type.getRawType ());
        return bind (type);
      }

      @Override
      public <T> AnnotatedBindingBuilder<T> handling (Class<T> type) {
        providers.addBinding ().toInstance (type);
        return bind (type);
      }

      private void registerHandlerProviders (Class<?> clazz) {
        if (clazz != null) {
          for (Method method : clazz.getMethods ())
            if (method.isAnnotationPresent (Handles.class)
                && method.isAnnotationPresent (Provides.class))
              providers.addBinding ().toInstance (method.getReturnType ());
          registerHandlerProviders (clazz.getSuperclass ());
          for (Class<?> interfase : clazz.getInterfaces ())
            registerHandlerProviders (interfase);
        }
      }
    });
  }

  /**
   * @param binder configures JAX-RS facilities
   */
  public abstract void configure (JaxrsBinder binder);
}
