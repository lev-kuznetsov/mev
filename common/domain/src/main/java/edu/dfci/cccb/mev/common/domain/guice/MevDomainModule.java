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

package edu.dfci.cccb.mev.common.domain.guice;

import static com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance;
import static com.google.inject.name.Names.bindProperties;

import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.inject.Binder;
import com.google.inject.Module;

import edu.dfci.cccb.mev.common.domain.c3p0.PooledDataSourceProvider;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonIntrospectorBinder;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;
import edu.dfci.cccb.mev.common.domain.guice.utilities.SingletonModule;

/**
 * MeV domain configuration module
 * 
 * @author levk
 * @since CRYSTAL
 */
public class MevDomainModule implements Module {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public final void configure (Binder binder) {
    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {

        // Persistence
        bindProperties (binder, load ("/database.properties"));
        binder.bind (DataSource.class).toProvider (new PooledDataSourceProvider ());

        // Jackson
        binder.install (new JacksonModule () {
          @Override
          public void configure (JacksonIntrospectorBinder binder) {
            binder.useInstance (new JaxbAnnotationIntrospector (defaultInstance ()));
          }
        });
      }
    });

    binder.install (new JacksonModule () {
      @Override
      public void configure (JacksonSerializerBinder binder) {
        MevDomainModule.this.configure (binder);
      }
    });
  }

  /**
   * @see JacksonModule#configure(JacksonSerializerBinder)
   */
  public void configure (JacksonSerializerBinder binder) {}

  /**
   * @param resources names of classpath resources containing properties
   * @return loaded properties
   */
  public static Properties load (String... resources) {
    Properties properties = new Properties ();
    for (String resource : resources)
      try {
        Configuration configuration = new PropertiesConfiguration (MevDomainModule.class.getResource (resource));
        for (Iterator<String> keys = configuration.getKeys (); keys.hasNext ();) {
          String key = keys.next ();
          properties.setProperty (key, configuration.getString (key));
        }
      } catch (ConfigurationException e) {
        throw new RuntimeException (e);
      }

    return properties;
  }
}
