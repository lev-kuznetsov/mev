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

package edu.dfci.cccb.mev.common.domain2.jackson.guice;

import static com.google.inject.Key.get;
import static com.google.inject.internal.Annotations.isBindingAnnotation;
import static com.google.inject.multibindings.Multibinder.newSetBinder;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

import edu.dfci.cccb.mev.common.domain2.guice.SingletonModule;

/**
 * Defines global jackson modules
 * 
 * @author levk
 * @since CRYSTAL
 */
public class JacksonModule implements Module {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    binder.install (new SingletonModule () {
      @Override
      public void configure (Binder binder) {
        final SimpleModule global = new SimpleModule ("global") {
          private static final long serialVersionUID = 1L;

          private @Inject Injector injector;

          @Override
          public void setupModule (SetupContext context) {
            context.insertAnnotationIntrospector (new JaxbAnnotationIntrospector (context.getTypeFactory ()) {
              private static final long serialVersionUID = 1L;

              @Override
              public Object findInjectableValueId (AnnotatedMember m) {
                if (m.getAnnotation (javax.inject.Inject.class) != null || m.getAnnotation (Inject.class) != null) {
                  for (Annotation annotation : m.annotations ())
                    if (isBindingAnnotation (annotation.annotationType ()))
                      return get (m.getGenericType (), annotation);
                  return get (m.getGenericType ());
                } else
                  return null;
              }
            });

            ((ObjectMapper) context.getOwner ()).setInjectableValues (new InjectableValues () {

              @Override
              public Object findInjectableValue (Object valueId,
                                                 DeserializationContext ctxt,
                                                 BeanProperty forProperty,
                                                 Object beanInstance) {
                return injector.getInstance ((Key<?>) valueId);
              }
            });
          }
        };

        binder.requestInjection (global);

        newSetBinder (binder, com.fasterxml.jackson.databind.Module.class).addBinding ().toInstance (global);
      }
    });
  }
}
