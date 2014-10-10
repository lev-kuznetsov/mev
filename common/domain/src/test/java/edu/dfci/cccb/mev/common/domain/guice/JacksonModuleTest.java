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

import static edu.dfci.cccb.mev.common.domain.guice.jackson.annotation.Handling.Factory.APPLICATION_JSON;
import static com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance;
import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.named;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.SneakyThrows;
import lombok.ToString;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonIntrospectorBinder;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;

public class JacksonModuleTest {
  
  private final Key<ObjectMapper> json = Key.get (ObjectMapper.class, APPLICATION_JSON);

  @Test
  public void empty () {
    createInjector (new JacksonModule ());
  }

  @Test
  public void doubleEmpty () {
    createInjector (new JacksonModule (), new JacksonModule ());
  }

  @XmlRootElement
  @XmlAccessorType (XmlAccessType.NONE)
  public static final class JaxbAnnotated {
    private final @XmlAttribute String foo;
    private final @SuppressWarnings ("unused") String ignored = null;

    public JaxbAnnotated (String foo) {
      this.foo = foo;
    }
  }

  @ToString
  public static final class NumberSerializer extends JsonSerializer<Number> {
    public void serialize (Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                         JsonProcessingException {
      jgen.writeStartObject ();
      jgen.writeStringField ("type", value.getClass ().getSimpleName ());
      jgen.writeStringField ("value", value.toString ());
      jgen.writeEndObject ();
    }

    public Class<Number> handledType () {
      return Number.class;
    }
  }

  public static final class NumberSerializerProvider implements Provider<NumberSerializer> {
    public NumberSerializer get () {
      return new NumberSerializer ();
    }
  }

  @Test
  public void withInstance () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.withInstance (new NumberSerializer ());
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withClass () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.with (NumberSerializer.class);
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withTypeLiteral () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.with (new TypeLiteral<NumberSerializer> () {});
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withKey () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.with (Key.get (NumberSerializer.class));
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withProviderClass () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.withProvider (NumberSerializerProvider.class);
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withProviderTypeLiteral () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.withProvider (new TypeLiteral<NumberSerializerProvider> () {});
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withProviderKey () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.withProvider (Key.get (NumberSerializerProvider.class));
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withProviderInstance () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.withProvider (new NumberSerializerProvider ());
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withConstructor () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      @SneakyThrows
      public void configure (JacksonSerializerBinder binder) {
        binder.withConstructor (NumberSerializer.class.getConstructor ());
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @Test
  public void withConstructorTypeLiteral () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      @SneakyThrows
      public void configure (JacksonSerializerBinder binder) {
        binder.withConstructor (NumberSerializer.class.getConstructor (), new TypeLiteral<NumberSerializer> () {});
      }
    }).getInstance (json).writeValueAsString (new Integer (8)),
                is ("{\"type\":\"Integer\",\"value\":\"8\"}"));
  }

  @ToString
  public static final class TypedJaxbAnnotationIntrospector extends JaxbAnnotationIntrospector {
    private static final long serialVersionUID = 1L;

    public TypedJaxbAnnotationIntrospector () {
      super (defaultInstance ());
    }
  }

  @ToString
  public static final class TypedJaxbAnnotationIntrospectorProvider implements Provider<JaxbAnnotationIntrospector> {
    public JaxbAnnotationIntrospector get () {
      return new TypedJaxbAnnotationIntrospector ();
    }
  }

  @Test
  public void useInstance () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonIntrospectorBinder binder) {
        binder.withInstance (new TypedJaxbAnnotationIntrospector ());
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useClass () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonIntrospectorBinder binder) {
        binder.with (TypedJaxbAnnotationIntrospector.class);
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useTypeLiteral () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonIntrospectorBinder binder) {
        binder.with (new TypeLiteral<TypedJaxbAnnotationIntrospector> () {});
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useKey () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonIntrospectorBinder binder) {
        binder.with (Key.get (TypedJaxbAnnotationIntrospector.class));
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useProviderClass () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonIntrospectorBinder binder) {
        binder.withProvider (TypedJaxbAnnotationIntrospectorProvider.class);
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useProviderTypeLiteral () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonIntrospectorBinder binder) {
        binder.withProvider (new TypeLiteral<TypedJaxbAnnotationIntrospectorProvider> () {});
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useProviderKey () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonIntrospectorBinder binder) {
        binder.withProvider (Key.get (TypedJaxbAnnotationIntrospectorProvider.class));
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useProviderInstance () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonIntrospectorBinder binder) {
        binder.withProvider (new TypedJaxbAnnotationIntrospectorProvider ());
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useConstructor () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      @SneakyThrows
      public void configure (JacksonIntrospectorBinder binder) {
        binder.withConstructor (TypedJaxbAnnotationIntrospector.class.getConstructor ());
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @Test
  public void useConstructorTypeLiteral () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      @SneakyThrows
      public void configure (JacksonIntrospectorBinder binder) {
        binder.withConstructor (TypedJaxbAnnotationIntrospector.class.getConstructor (),
                               new TypeLiteral<TypedJaxbAnnotationIntrospector> () {});
      }
    }).getInstance (json).writeValueAsString (new JaxbAnnotated ("bar")),
                is ("{\"foo\":\"bar\"}"));
  }

  @XmlRootElement
  @XmlAccessorType (XmlAccessType.NONE)
  public static final class Bean {
    private @Inject @Named ("one") int one;
    private @Inject @Named ("two") int two;
    private @XmlElement int three;

    private void verify () throws Exception {
      assertThat (one, is (1));
      assertThat (two, is (2));
      assertThat (three, is (3));
    }
  }

  @Test
  public void injectedValues () throws Exception {
    createInjector (new MevModule (), new Module () {
      public void configure (Binder binder) {
        binder.bindConstant ().annotatedWith (named ("one")).to (1);
        binder.bindConstant ().annotatedWith (named ("two")).to (2);
      }
    }).getInstance (json).readValue ("{\"three\":3}", Bean.class).verify ();
  }
}
