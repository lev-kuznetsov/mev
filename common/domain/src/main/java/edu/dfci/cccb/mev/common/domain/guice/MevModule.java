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

import lombok.ToString;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.inject.Binder;
import com.google.inject.Module;

import edu.dfci.cccb.mev.common.domain.guice.c3p0.PooledDataSourceProvider;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonIntrospectorBinder;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.ExceptionBinder;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.JaxrsModule;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.MessageReaderBinder;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.MessageWriterBinder;
import edu.dfci.cccb.mev.common.domain.mappers.MevExceptionMapper;
import edu.dfci.cccb.mev.common.domain.messages.JacksonMessageReader;
import edu.dfci.cccb.mev.common.domain.messages.JacksonMessageWriter;

/**
 * MeV domain configuration module
 * 
 * @author levk
 * @since CRYSTAL
 */
@ToString
public class MevModule implements Module {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        // Persistence
        bindProperties (binder, load ("/META-INF/configuration/persistence.properties"));
        binder.bind (DataSource.class).toProvider (new PooledDataSourceProvider ());

        // JAX-RS
        binder.install (new JaxrsModule () {
          @Override
          public void configure (MessageReaderBinder binder) {
            binder.use (JacksonMessageReader.class);
          }

          @Override
          public void configure (MessageWriterBinder binder) {
            binder.use (JacksonMessageWriter.class);
          }

          @Override
          public void configure (ExceptionBinder binder) {
            binder.use (MevExceptionMapper.class);
          }
        });

        // Jackson
        binder.install (new JacksonModule () {
          @Override
          public void configure (JacksonIntrospectorBinder binder) {
            binder.useInstance (new AnnotationIntrospectorPair (new JacksonAnnotationIntrospector (),
                                                                new JaxbAnnotationIntrospector (defaultInstance ())));
          }
        });
      }
    });
    
    binder.install (new JaxrsModule () {
      @Override
      public void configure (ExceptionBinder binder) {
        MevModule.this.configure (binder);
      }
      
      @Override
      public void configure (MessageReaderBinder binder) {
        MevModule.this.configure (binder);
      }
      
      @Override
      public void configure (MessageWriterBinder binder) {
        MevModule.this.configure (binder);
      }
    });

    binder.install (new JacksonModule () {
      @Override
      public void configure (JacksonSerializerBinder binder) {
        MevModule.this.configure (binder);
      }
    });
  }

  /**
   * @see JacksonModule#configure(JacksonSerializerBinder)
   */
  public void configure (JacksonSerializerBinder binder) {}

  /**
   * @see JaxrsModule#configure(MessageReaderBinder)
   */
  public void configure (MessageReaderBinder binder) {}

  /**
   * @see JaxrsModule#configure(MessageWriterBinder)
   */
  public void configure (MessageWriterBinder binder) {}

  /**
   * @see JaxrsModule#configure(ExceptionBinder)
   */
  public void configure (ExceptionBinder binder) {}

  /**
   * @param resources names of classpath resources containing properties
   * @return loaded properties
   */
  public static Properties load (String... resources) {
    RuntimeException toThrow = null;
    Properties properties = new Properties ();
    for (String resource : resources)
      try {
        Configuration configuration = new PropertiesConfiguration (MevModule.class.getResource (resource));
        for (Iterator<String> keys = configuration.getKeys (); keys.hasNext ();) {
          String key = keys.next ();
          properties.setProperty (key, configuration.getString (key));
        }
      } catch (ConfigurationException e) {
        if (toThrow == null)
          toThrow = new RuntimeException (e);
        else
          toThrow.addSuppressed (e);
      }

    if (toThrow != null)
      throw toThrow;

    return properties;
  }
}
