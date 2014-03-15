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

package edu.dfci.cccb.mev.common.services.guice.jaxrs;

import static com.google.inject.Guice.createInjector;
import static java.util.EnumSet.allOf;
import static org.apache.log4j.lf5.util.StreamUtils.getBytes;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonIntrospectorBinder;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ContentNegotiationConfigurer;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.MessageWriterBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceModule;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceModuleTest.Pojo.Inner;

public class ServiceModuleTest {

  @Test
  public void empty () throws Exception {
    try {
      serve (new ServiceModule ());
    } finally {
      stop ();
    }
  }

  @Path ("/simple")
  public static final class Simple {

    @GET
    public String echo (@QueryParam ("hello") String hello) {
      return hello;
    }
  }

  @Test
  public void simpleEcho () throws Exception {
    try {
      assertThat (connect (serve (new ServiceModule () {
        public void configure (ResourceBinder binder) {
          binder.publish (Simple.class).in (Singleton.class);
        }

        public void configure (ServiceBinder binder) {
          binder.service ("/test/*");
        }
      }), "/test/simple?hello=world"), is ("world"));
    } finally {
      stop ();
    }
  }

  @XmlAccessorType (XmlAccessType.NONE)
  @XmlRootElement
  public static final class Pojo {
    public static class Inner {}

    private final @JsonProperty @XmlAttribute String word;
    private final @JsonProperty @XmlAttribute int number;
    private final @XmlAttribute Inner inner;

    public Pojo (String word, int number) {
      this.word = word;
      this.number = number;
      this.inner = new Inner ();
    }
  }

  @Path ("/pojo")
  public interface PojoService {
    @GET
    public Pojo pojo (@QueryParam ("word") String word, @QueryParam ("number") int number);
  }

  public static class PojoServiceImpl implements PojoService {
    public Pojo pojo (String word, int number) {
      return new Pojo (word, number);
    }
  }

  @Provider
  public static class JsonWriter implements MessageBodyWriter<Object> {
    private @Inject ObjectMapper mapper;

    public boolean isWriteable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return true;
    }

    public long getSize (Object t, Class<?> y, Type e, Annotation[] a, MediaType m) {
      return -1;
    }

    public void writeTo (Object t, Class<?> y, Type e, Annotation[] a,
                         MediaType m, MultivaluedMap<String, Object> h,
                         OutputStream o) throws IOException, WebApplicationException {
      mapper.writeValue (o, t);
    }
  }

  @Test
  public void jacksonPojo () throws Exception {
    try {
      assertThat (connect (serve (new ServiceModule () {
        public void configure (ResourceBinder binder) {
          binder.publish (PojoService.class).to (PojoServiceImpl.class).in (Singleton.class);
        }

        public void configure (MessageWriterBinder binder) {
          binder.use (JsonWriter.class);
        }

        public void configure (ServiceBinder binder) {
          binder.service ("/test/*");
        }
      }, new JacksonModule () {
        public void configure (JacksonIntrospectorBinder binder) {
          binder.useInstance (new JacksonAnnotationIntrospector ());
        }
      }), "/test/pojo?word=world&number=5"),
                  is ("{\"word\":\"world\",\"number\":5}"));
    } finally {
      stop ();
    }
  }

  @Provider
  @Produces (MediaType.APPLICATION_JSON)
  public static class NegotiatedJsonWriter extends JsonWriter {}

  public static class InnerSerializer extends JsonSerializer<Inner> {
    public void serialize (Inner value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                        JsonProcessingException {
      jgen.writeStartObject ();
      jgen.writeStringField ("iNNer", "InnER");
      jgen.writeEndObject ();
    }

    public Class<Inner> handledType () {
      return Inner.class;
    }
  }

  @Provider
  @Produces (MediaType.TEXT_PLAIN)
  public static class NegotiatedTextWriter implements MessageBodyWriter<Object> {
    public boolean isWriteable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return true;
    }

    public long getSize (Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return -1;
    }

    public void writeTo (Object t, Class<?> y, Type e, Annotation[] a,
                         MediaType m, MultivaluedMap<String, Object> h,
                         OutputStream o) throws IOException, WebApplicationException {
      o.write (t.getClass ().getSimpleName ().getBytes ());
    }
  }

  @Test
  public void contentNegotiation () throws Exception {
    try {
      int port = serve (new ServiceModule () {
        public void configure (ResourceBinder binder) {
          binder.publish (PojoService.class).to (PojoServiceImpl.class).in (Singleton.class);
        }

        public void configure (MessageWriterBinder binder) {
          binder.use (NegotiatedJsonWriter.class);
          binder.use (NegotiatedTextWriter.class);
        }

        public void configure (ContentNegotiationConfigurer content) {
          content.parameter ("format")
                 .map ("json", MediaType.APPLICATION_JSON_TYPE)
                 .map ("text", MediaType.TEXT_PLAIN_TYPE);
        }

        public void configure (ServiceBinder binder) {
          binder.service ("/test/*");
        }
      }, new JacksonModule () {
        public void configure (JacksonSerializerBinder binder) {
          binder.with (InnerSerializer.class);
        }

        public void configure (JacksonIntrospectorBinder binder) {
          binder.useInstance (new JaxbAnnotationIntrospector (TypeFactory.defaultInstance ()));
        }
      });
      assertThat (connect (port, "/test/pojo?word=world&number=5&format=json"),
                  is ("{\"word\":\"world\",\"number\":5,\"inner\":{\"iNNer\":\"InnER\"}}"));
      assertThat (connect (port, "/test/pojo?word=world&number=5&format=text"),
                  is ("Pojo"));
    } finally {
      stop ();
    }
  }

  @Path ("/top")
  public static class Top {
    private @Inject Set<Sub> subs;

    @Path ("/{name}")
    public Sub sub (@PathParam ("name") String name) {
      for (Sub sub : subs)
        if (sub.name ().word.equals (name))
          return sub;
      return null;
    }

    @GET
    public String list () {
      return subs.toString ();
    }
  }

  public static class Sub {
    private String name;

    public Sub (String name) {
      this.name = name;
    }

    @Path ("/name")
    @GET
    public Blob name () {
      return new Blob (name);
    }

    @Path ("/enam")
    @GET
    public Blob enam () {
      return new Blob (new StringBuffer (name).reverse ().toString ());
    }

    @GET
    public String toString () {
      return "Blob(" + name + ")";
    }
  }

  @XmlRootElement
  @XmlAccessorType (XmlAccessType.NONE)
  public static class Blob {
    private @XmlAttribute String word;

    public Blob (String word) {
      this.word = word;
    }
  }

  @Test
  public void subResources () throws Exception {
    try {
      assertThat (connect (serve (new ServiceModule () {
        public void configure (ResourceBinder binder) {
          binder.publish (Top.class).in (Singleton.class);
        }

        public void configure (MessageWriterBinder binder) {
          binder.use (NegotiatedJsonWriter.class);
        }

        public void configure (ContentNegotiationConfigurer content) {
          content.parameter ("format")
                 .map ("json", MediaType.APPLICATION_JSON_TYPE);
        }

        public void configure (ServiceBinder binder) {
          binder.service ("/test/*");
        }
      }, new JacksonModule () {
        public void configure (JacksonIntrospectorBinder binder) {
          binder.useInstance (new JaxbAnnotationIntrospector (TypeFactory.defaultInstance ()));
        }
      }, new Module () {
        public void configure (Binder binder) {
          Multibinder<Sub> subs = Multibinder.newSetBinder (binder, Sub.class);
          subs.addBinding ().toInstance (new Sub ("hello"));
          subs.addBinding ().toInstance (new Sub ("cruel"));
          subs.addBinding ().toInstance (new Sub ("world"));
        }
      }), "/test/top/hello/enam?format=json"),
                  is ("{\"word\":\"olleh\"}"));
    } finally {
      stop ();
    }
  }

  private String connect (final int port, final String uri) throws IOException {
    return new String (getBytes (new URL ("http://localhost:" + port + uri).openStream ()));
  }

  private Server server = null;

  private int serve (final ServletContextListener context) throws Exception {
    server = new Server (0);
    server.setHandler (new ServletContextHandler (server, "/") {
      {
        addEventListener (context);
        addFilter (GuiceFilter.class, "/*", allOf (DispatcherType.class));
        addServlet (DefaultServlet.class, "/");
      }
    });
    server.start ();
    return ((ServerConnector) server.getConnectors ()[0]).getLocalPort ();
  }

  private int serve (final Module... modules) throws Exception {
    return serve (new GuiceServletContextListener () {

      @Override
      protected Injector getInjector () {
        return createInjector (modules);
      }
    });
  }

  private void stop () throws Exception {
    if (server != null) {
      server.stop ();
      server.join ();
    }
  }
}