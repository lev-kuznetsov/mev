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

import static com.google.common.collect.Iterables.concat;
import static com.google.inject.Key.get;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.reflections.util.ClasspathHelper.forPackage;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.reflections.scanners.AbstractScanner;
import org.reflections.util.ConfigurationBuilder;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;

/**
 * @author levk
 */
@Log4j
@ToString (exclude = "jaxrsProviderDefinitions")
public abstract class JaxrsModule extends AbstractModule {

  private Set<Key<?>> jaxrsProviderDefinitions = new HashSet<> ();

  protected <T> LinkedBindingBuilder<T> provide (Key<T> key) {
    jaxrsProviderDefinitions.add (key);
    return bind (key);
  }

  protected <T> AnnotatedBindingBuilder<T> provide (TypeLiteral<T> type) {
    jaxrsProviderDefinitions.add (get (type));
    return bind (type);
  }

  protected <T> AnnotatedBindingBuilder<T> provide (Class<T> type) {
    return provide (get (type).getTypeLiteral ());
  }

  protected final List<Object> providers (Injector injector) {
    List<Object> providers = new ArrayList<> ();
    for (Key<?> key : jaxrsProviderDefinitions)
      providers.add (injector.getInstance (key));
    return providers;
  }

  protected interface ScannerCallback {
    /**
     * @param clazz
     * @return scope binder if the class is applicable, null if not
     */
    ScopedBindingBuilder scan (Class<?> clazz);
  }

  protected Collection<ScannerCallback> callbacks () {
    return emptyList ();
  }

  protected ScopedBindingBuilder scan (String... basePackages) {
    final Iterable<ScannerCallback> callbacks = concat (asList (new ScannerCallback () {

      @Override
      public ScopedBindingBuilder scan (Class<?> clazz) {
        return (MessageBodyReader.class.isAssignableFrom (clazz)
                || MessageBodyWriter.class.isAssignableFrom (clazz)
                || ExceptionMapper.class.isAssignableFrom (clazz)) ? provide (clazz) : null;
      }
    }), callbacks ());
    final Collection<ScopedBindingBuilder> scopeBinders = new ArrayList<ScopedBindingBuilder> () {
      private static final long serialVersionUID = 1L;

      public boolean add (ScopedBindingBuilder element) {
        if (element != null)
          return super.add (element);
        else
          return false;
      }
    };

    ConfigurationBuilder configuration = new ConfigurationBuilder ();
    for (String basePackage : basePackages)
      configuration.addUrls (forPackage (basePackage));
    configuration.setScanners (new AbstractScanner () {

      @SuppressWarnings ("unchecked")
      @Override
      public void scan (Object cls) {
        try {
          Class<?> clazz = Class.forName (getMetadataAdapter ().getClassName (cls));
          for (ScannerCallback callback : callbacks)
            if (scopeBinders.add (callback.scan (clazz)) && log.isDebugEnabled ())
              log.debug ("Discovered JAX-RS entity " + clazz);
        } catch (ClassNotFoundException e) {}
      }
    });
    configuration.build ();

    return new ScopedBindingBuilder () {
      public void in (Scope scope) {
        for (ScopedBindingBuilder scopeBinder : scopeBinders)
          scopeBinder.in (scope);
      }

      public void in (Class<? extends Annotation> scopeAnnotation) {
        for (ScopedBindingBuilder scopeBinder : scopeBinders)
          scopeBinder.in (scopeAnnotation);
      }

      public void asEagerSingleton () {
        for (ScopedBindingBuilder scopeBinder : scopeBinders)
          scopeBinder.asEagerSingleton ();
      }
    };
  }
}
