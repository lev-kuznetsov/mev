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
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import lombok.Delegate;

import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.ServletModule;

import edu.dfci.cccb.mev.common.services.guice.annotation.ContentNegotiationConfiguration;
import edu.dfci.cccb.mev.common.services.guice.annotation.Publishes;

/**
 * Defines server side JAX-RS facilities
 * <p/>
 * Sublassing this module enables the use {@link Publishes} annotation on the
 * {@link Provides} annotated methods
 * 
 * @author levk
 * @since CRYSTAL
 */
public class JaxrsServiceModule extends AbstractJaxrsModule {

  /**
   * Binding name for JAX-RS resources
   */
  public static final String RESOURCES = "jaxrs.resources";
  /**
   * Binding name for path extensions used for content negotiation
   */
  public static final String CONTENT_EXTENSIONS = "jaxrs.content.negotiation.extensions";
  /**
   * Binding name of content negotiation query parameter name
   */
  public static final String CONTENT_PARAMETER = "jaxrs.content.negotiation.parameter";
  /**
   * Binding name of content negotiation query parameter value map
   */
  public static final String CONTENT_PARAMETER_VALUES = "jaxrs.content.negotiation.parameter.values";

  public interface JaxrsServiceBinder extends JaxrsBinder {
    /**
     * @param type to publish as a service
     * @return
     */
    <T> AnnotatedBindingBuilder<T> publishing (Class<T> type);

    /**
     * @param type to publish as a service
     * @return
     */
    <T> AnnotatedBindingBuilder<T> publishing (TypeLiteral<T> type);

    /**
     * @param key to publish as a service
     * @return
     */
    <T> LinkedBindingBuilder<T> publishing (Key<T> key);

    /**
     * @param url service root
     */
    void service (String url);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.services.guice2.AbstractJaxrsModule#configure
   * (edu.dfci.cccb.mev.common.services.guice2.AbstractJaxrsModule.JaxrsBinder) */
  @Override
  public final void configure (final JaxrsBinder binder) {

    configure (new JaxrsServiceBinder () {
      private final @Delegate JaxrsBinder delegate = binder;
      private final Multibinder<Class<?>> resources = newSetBinder (binder,
                                                                    new TypeLiteral<Class<?>> () {},
                                                                    named (RESOURCES));

      {
        registerServiceProviders (JaxrsServiceModule.this.getClass ());
      }

      @Override
      public <T> AnnotatedBindingBuilder<T> publishing (Class<T> type) {
        resources.addBinding ().toInstance (type);
        return bind (type);
      }

      @Override
      public <T> LinkedBindingBuilder<T> publishing (Key<T> key) {
        resources.addBinding ().toInstance (key.getTypeLiteral ().getRawType ());
        return bind (key);
      }

      @Override
      public <T> AnnotatedBindingBuilder<T> publishing (TypeLiteral<T> type) {
        resources.addBinding ().toInstance (type.getRawType ());
        return bind (type);
      }

      @Override
      public void service (final String url) {
        binder.install (new ServletModule () {
          @Override
          protected void configureServlets () {
            serve (url).with (CxfGuiceJaxrsServlet.class);
          }
        });
      }

      private void registerServiceProviders (Class<?> clazz) {
        if (clazz != null) {
          for (Method method : clazz.getMethods ())
            if (method.isAnnotationPresent (Publishes.class)
                && method.isAnnotationPresent (Provides.class))
              resources.addBinding ().toInstance (method.getReturnType ());
          registerServiceProviders (clazz.getSuperclass ());
          for (Class<?> interfase : clazz.getInterfaces ())
            registerServiceProviders (interfase);
        }
      }
    });

    configure (new ContentNegotiationConfiguration () {

      private final Multibinder<Entry<String, MediaType>> parameters;
      private final Multibinder<Entry<String, MediaType>> extensions;

      {
        parameters = newSetBinder (binder,
                                   new TypeLiteral<Entry<String, MediaType>> () {},
                                   named (CONTENT_PARAMETER_VALUES));
        extensions = newSetBinder (binder,
                                   new TypeLiteral<Entry<String, MediaType>> () {},
                                   named (CONTENT_EXTENSIONS));
      }

      class Mapper implements MappingConfiguration {
        private final Multibinder<Entry<String, MediaType>> mappings;

        Mapper (Multibinder<Entry<String, MediaType>> mappings) {
          this.mappings = mappings;
        }

        @Override
        public MappingConfiguration map (final String name, final MediaType content) {
          mappings.addBinding ().toInstance (new Entry<String, MediaType> () {
            private String key = name;
            private MediaType value = content;

            @Override
            public MediaType setValue (MediaType value) {
              MediaType previous = this.value;
              this.value = value;
              return previous;
            }

            @Override
            public MediaType getValue () {
              return value;
            }

            @Override
            public String getKey () {
              return key;
            }
          });
          return this;
        }

        @Override
        public MappingConfiguration map (Map<String, MediaType> map) {
          for (Entry<String, MediaType> entry : map.entrySet ())
            mappings.addBinding ().toInstance (entry);
          return this;
        }
      }

      @Override
      public MappingConfiguration parameter (String name) {
        binder.bindConstant ().annotatedWith (named (CONTENT_PARAMETER)).to (name);
        return new Mapper (parameters);
      }

      @Override
      public MappingConfiguration extension () {
        return new Mapper (extensions);
      }
    });
  }

  /**
   * Configure JAX-RS services
   * 
   * @param binder
   */
  public void configure (JaxrsServiceBinder binder) {}

  /**
   * Configure content negotiation
   * 
   * @param configurer
   */
  public void configure (ContentNegotiationConfiguration configurer) {}
}
