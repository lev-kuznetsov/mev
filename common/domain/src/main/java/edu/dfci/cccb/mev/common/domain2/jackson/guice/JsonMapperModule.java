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

import java.util.Set;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import edu.dfci.cccb.mev.common.domain2.annotation.Json;
import edu.dfci.cccb.mev.common.domain2.guice.SingletonModule;

/**
 * Configures a json mapper
 * 
 * @author levk
 * @since CRYSTAL
 */
public class JsonMapperModule implements Module {

  /**
   * Provides {@link ObjectMapper} registering all global and json modules
   * 
   * @param global
   * @param json
   * @return
   */
  @Provides
  @Json
  @Singleton
  public ObjectMapper mapper (Set<com.fasterxml.jackson.databind.Module> global,
                              @Json Set<com.fasterxml.jackson.databind.Module> json) {
    return new ObjectMapper ().registerModules (global).registerModules (json);
  }

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    binder.install (new SingletonModule () {
      @Override
      public void configure (Binder binder) {
        binder.install (new JacksonModule ());

        newSetBinder (binder,
                      com.fasterxml.jackson.databind.Module.class,
                      Json.class).addBinding ()
                                 .toInstance (new SimpleModule ("json") {
                                   private static final long serialVersionUID = 1L;

                                   @Override
                                   public void setupModule (SetupContext context) {
                                     context.insertAnnotationIntrospector (new JacksonAnnotationIntrospector ());
                                   }
                                 });
      }
    });
  }
}
