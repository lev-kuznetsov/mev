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

package edu.dfci.cccb.mev.googleplus.domain.support.jackson;

import static com.fasterxml.jackson.databind.ser.BeanSerializerFactory.instance;
import static org.junit.Assert.assertEquals;
import lombok.SneakyThrows;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

public abstract class JsonSerializerTest {

  protected ObjectMapper mapper;
  protected JsonSerializer<?> serializer;

  private final Class<? extends JsonSerializer<?>> test;
  private final Class<?> target;
  private final Class<JsonSerializer<?>>[] more;

  @SuppressWarnings ("unchecked")
  @SneakyThrows
  protected JsonSerializerTest (Class<?> target, Class<? extends JsonSerializer<?>> test) {
    this.target = target;
    this.test = test;
    more = new Class[0];
  }

  @SafeVarargs
  @SneakyThrows
  protected JsonSerializerTest (Class<?> target,
                               Class<? extends JsonSerializer<?>> test,
                               Class<JsonSerializer<?>>... more) {
    this.target = target;
    this.test = test;
    this.more = more;
  }

  @Before
  public void setUpMapper () throws InstantiationException, IllegalAccessException {
    mapper = new ObjectMapper ();
    SimpleSerializers sers = new SimpleSerializers ();
    sers.addSerializer (serializer = test.newInstance ());
    for (Class<JsonSerializer<?>> ser : more)
      sers.addSerializer (ser.newInstance ());
    mapper.setSerializerFactory (instance.withAdditionalSerializers (sers));
  }

  @Test
  public void handledType () {
    assertEquals (serializer.handledType (), target);
  }
}
