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

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.util.Set;

import javax.inject.Singleton;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import edu.dfci.cccb.mev.common.domain2.annotation.Json;
import edu.dfci.cccb.mev.common.domain2.annotation.Rserve;
import edu.dfci.cccb.mev.common.domain2.guice.SingletonModule;

/**
 * Configures a json mapper responsible for rserve transport
 * 
 * @author levk
 * @since CRYSTAL
 */
public class RserveMapperModule implements Module {

  /**
   * Provides {@link ObjectMapper} registering all global, json, and rserve
   * modules
   * 
   * @param json
   * @param rserve
   * @return
   */
  @Provides
  @Rserve
  @Singleton
  public ObjectMapper mapper (@Json ObjectMapper json, @Rserve Set<com.fasterxml.jackson.databind.Module> rserve) {
    return json.copy ().registerModules (rserve);
  }

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    binder.install (new SingletonModule () {
      @Override
      public void configure (Binder binder) {
        binder.install (new JsonMapperModule ());

        newSetBinder (binder,
                      com.fasterxml.jackson.databind.Module.class,
                      Rserve.class).addBinding ()
                                   .toInstance (new SimpleModule ("rserve") {
                                     private static final long serialVersionUID = 1L;

                                     @Override
                                     public void setupModule (SetupContext context) {
                                       context.addSerializers (new SimpleSerializers () {
                                         private static final long serialVersionUID = 1L;

                                         {
                                           addSerializer (Double.class, new JsonSerializer<Double> () {

                                             @Override
                                             public void serialize (Double value,
                                                                    JsonGenerator gen,
                                                                    SerializerProvider serializers) throws IOException,
                                                                                                   JsonProcessingException {
                                               if (value == null)
                                                 gen.writeString ("NA");
                                               else if (value.isInfinite () && value > 0)
                                                 gen.writeString ("Inf");
                                               else if (value.isInfinite () && value < 0)
                                                 gen.writeString ("-Inf");
                                               else
                                                 gen.writeNumber (value.doubleValue ());
                                             }
                                           });

                                           addSerializer (Integer.class, new JsonSerializer<Integer> () {

                                             @Override
                                             public void serialize (Integer value,
                                                                    JsonGenerator gen,
                                                                    SerializerProvider serializers) throws IOException,
                                                                                                   JsonProcessingException {
                                               if (value == null)
                                                 gen.writeString ("NA");
                                               else
                                                 gen.writeNumber (value);
                                             }
                                           });

                                           addSerializer (Boolean.class, new JsonSerializer<Boolean> () {

                                             @Override
                                             public void serialize (Boolean value,
                                                                    JsonGenerator gen,
                                                                    SerializerProvider serializers) throws IOException,
                                                                                                   JsonProcessingException {
                                               if (value == null)
                                                 gen.writeString ("NA");
                                               else
                                                 gen.writeBoolean (value);
                                             }
                                           });

                                           addDeserializer (Double.class, new JsonDeserializer<Double> () {
                                             @Override
                                             public Double deserialize (JsonParser p, DeserializationContext ctxt) throws IOException,
                                                                                                                  JsonProcessingException {
                                               String value = p.readValueAs (String.class);
                                               if ("NA".equals (value))
                                                 return null;
                                               else if ("Inf".equals (value))
                                                 return POSITIVE_INFINITY;
                                               else if ("-Inf".equals (value))
                                                 return NEGATIVE_INFINITY;
                                               else
                                                 return parseDouble (value);
                                             }
                                           });

                                           addDeserializer (Integer.class, new JsonDeserializer<Integer> () {

                                             @Override
                                             public Integer deserialize (JsonParser p, DeserializationContext ctxt) throws IOException,
                                                                                                                   JsonProcessingException {
                                               String value = p.readValueAs (String.class);
                                               return "NA".equals (value) ? null : parseInt (value);
                                             }
                                           });

                                           addDeserializer (Boolean.class, new JsonDeserializer<Boolean> () {

                                             @Override
                                             public Boolean deserialize (JsonParser p, DeserializationContext ctxt) throws IOException,
                                                                                                                   JsonProcessingException {
                                               String value = p.readValueAs (String.class);
                                               return "NA".equals (value) ? null : "TRUE".equals (value);
                                             }
                                           });
                                         }
                                       });
                                     }
                                   });
      }
    });
  }
}
