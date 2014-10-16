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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Setter;
import lombok.SneakyThrows;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.RequestScoped;

import edu.dfci.cccb.mev.common.domain.guice.jaxrs.ExceptionBinder;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.MessageReaderBinder;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.MessageWriterBinder;
import edu.dfci.cccb.mev.common.test.jetty.Jetty9;

public class ServiceModuleTest {

  private Jetty9 serve (final Module... modules) throws Exception {
    return new Jetty9 (new ServletContextHandler () {
      {
        ServletContextListener context = new GuiceServletContextListener () {

          @Override
          protected Injector getInjector () {
            return createInjector (modules);
          }
        };

        addEventListener (context);
        addFilter (GuiceFilter.class, "/*", allOf (DispatcherType.class));
        addServlet (DefaultServlet.class, "/");
      }
    });
  }

  @Test
  public void empty () throws Exception {
    try (Jetty9 j = serve ()) {}
  }

  @Path ("/simple")
  public static final class Echo {

    @GET
    public String echo (@QueryParam ("hello") String hello) {
      return hello;
    }
  }

  public static class TestServiceModule extends ServiceModule {
    public void configure (ServiceBinder binder) {
      binder.service ("/test/*");
    }
  }

  @Test
  public void publishClass () throws Exception {
    try (Jetty9 j = serve (new TestServiceModule () {
      public void configure (ResourceBinder binder) {
        binder.publish ("simple", Echo.class).in (Singleton.class);
      }
    })) {
      assertThat (j.get ("/test/simple?hello=world"), startsWith ("world"));
    }
  }

  @Test
  public void publishTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new TestServiceModule () {
      public void configure (ResourceBinder binder) {
        binder.publish ("simple", new TypeLiteral<Echo> () {}).in (Singleton.class);
      }
    })) {
      assertThat (j.get ("/test/simple?hello=world"), startsWith ("world"));
    }
  }

  @Test
  public void publishKey () throws Exception {
    try (Jetty9 j = serve (new TestServiceModule () {
      public void configure (ResourceBinder binder) {
        binder.publish ("simple", Key.get (Echo.class)).in (Singleton.class);
      }
    })) {
      assertThat (j.get ("/test/simple?hello=world"), startsWith ("world"));
    }
  }

  @XmlAccessorType (XmlAccessType.NONE)
  @XmlRootElement
  public static final class Pojo {
    @XmlRootElement
    @XmlAccessorType (XmlAccessType.NONE)
    public static class Inner {}

    private @Setter @JsonProperty @XmlAttribute String word;
    private @Setter @JsonProperty @XmlAttribute int number;
    private @Setter @XmlAttribute Inner inner;

    public Pojo (String word, int number) {
      this.word = word;
      this.number = number;
      this.inner = new Inner ();
    }

    public Pojo () {}
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
    private ObjectMapper mapper;

    @Inject
    public JsonWriter (ObjectMapper mapper) {
      this.mapper = mapper;
    }

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

  public static class JsonWriterProvider implements com.google.inject.Provider<JsonWriter> {
    private @Inject ObjectMapper mapper;

    public JsonWriter get () {
      return new JsonWriter (mapper);
    }
  }

  public static class PojoPublishingTestServiceModule extends TestServiceModule {
    public void configure (ResourceBinder binder) {
      binder.publish ("pojo", PojoService.class).to (PojoServiceImpl.class);
    }
  }

  @Test
  public void writerClass () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      public void configure (MessageWriterBinder binder) {
        binder.use (JsonWriter.class);
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      public void configure (MessageWriterBinder binder) {
        binder.use (new TypeLiteral<JsonWriter> () {});
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerKey () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      public void configure (MessageWriterBinder binder) {
        binder.use (Key.get (JsonWriter.class));
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerProviderClass () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      public void configure (MessageWriterBinder binder) {
        binder.useProvider (JsonWriterProvider.class);
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerProviderTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      public void configure (MessageWriterBinder binder) {
        binder.useProvider (new TypeLiteral<JsonWriterProvider> () {});
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerProviderKey () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      public void configure (MessageWriterBinder binder) {
        binder.useProvider (Key.get (JsonWriterProvider.class));
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerProviderInstance () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      public void configure (MessageWriterBinder binder) {
        binder.useProvider (new JsonWriterProvider ());
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerInstance () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      public void configure (MessageWriterBinder binder) {
        binder.useInstance (new JsonWriter (new ObjectMapper ()));
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerConstructor () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      @SneakyThrows
      public void configure (MessageWriterBinder binder) {
        binder.useConstructor (JsonWriter.class.getConstructor (ObjectMapper.class));
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  @Test
  public void writerContstructorTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new PojoPublishingTestServiceModule () {
      @SneakyThrows
      public void configure (MessageWriterBinder binder) {
        binder.useConstructor (JsonWriter.class.getConstructor (ObjectMapper.class), new TypeLiteral<JsonWriter> () {});
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5"), startsWith ("{\"word\":\"world\",\"number\":5}"));
    }
  }

  public static class RestException extends Exception {
    private static final long serialVersionUID = 1L;
  }

  @Path ("/fail")
  public static class ExceptionService {
    @GET
    public void fail () throws RestException {
      throw new RestException ();
    }
  }

  @Provider
  public static class RestExceptionMapper implements ExceptionMapper<RestException> {
    public Response toResponse (RestException exception) {
      Response result = mock (Response.class);
      when (result.getMetadata ()).thenReturn (new MultivaluedHashMap<String, Object> ());
      when (result.getStatus ()).thenReturn (567);
      return result;
    }
  }

  public static class RestExceptionMapperProvider implements com.google.inject.Provider<RestExceptionMapper> {
    public RestExceptionMapper get () {
      return new RestExceptionMapper ();
    }
  }

  public static class ExceptionTestServiceModule extends TestServiceModule {
    public void configure (ResourceBinder binder) {
      binder.publish ("fail", ExceptionService.class);
    }
  }

  @Test
  public void exceptionClass () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      public void configure (ExceptionBinder binder) {
        binder.use (RestExceptionMapper.class);
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      public void configure (ExceptionBinder binder) {
        binder.use (new TypeLiteral<RestExceptionMapper> () {});
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionKey () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      public void configure (ExceptionBinder binder) {
        binder.use (Key.get (RestExceptionMapper.class));
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionInstance () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      public void configure (ExceptionBinder binder) {
        binder.useInstance (new RestExceptionMapper ());
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionProviderClass () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      public void configure (ExceptionBinder binder) {
        binder.useProvider (RestExceptionMapperProvider.class);
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionProviderTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      public void configure (ExceptionBinder binder) {
        binder.useProvider (new TypeLiteral<RestExceptionMapperProvider> () {});
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionProviderKey () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      public void configure (ExceptionBinder binder) {
        binder.useProvider (Key.get (RestExceptionMapperProvider.class));
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionProviderInstance () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      public void configure (ExceptionBinder binder) {
        binder.useProvider (new RestExceptionMapperProvider ());
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionConstructor () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      @SneakyThrows
      public void configure (ExceptionBinder binder) {
        binder.useConstructor (RestExceptionMapper.class.getConstructor ());
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Test
  public void exceptionConstructorTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new ExceptionTestServiceModule () {
      @SneakyThrows
      public void configure (ExceptionBinder binder) {
        binder.useConstructor (RestExceptionMapper.class.getConstructor (), new TypeLiteral<RestExceptionMapper> () {});
      }
    })) {
      assertThat (j.rc ("/test/fail"), is (567));
    }
  }

  @Provider
  @Produces (MediaType.APPLICATION_JSON)
  public static class AnnotatedJsonWriter extends JsonWriter {
    @Inject
    public AnnotatedJsonWriter (ObjectMapper mapper) {
      super (mapper);
    }
  }

  @Provider
  @Produces ("application/x-custom")
  public static class CustomWriter implements MessageBodyWriter<Pojo> {
    public boolean isWriteable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return Pojo.class.isAssignableFrom (type);
    }

    public long getSize (Pojo t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return -1;
    }

    public void writeTo (Pojo t,
                         Class<?> type,
                         Type genericType,
                         Annotation[] annotations,
                         MediaType mediaType,
                         MultivaluedMap<String, Object> httpHeaders,
                         OutputStream entityStream) throws IOException, WebApplicationException {
      try (PrintStream out = new PrintStream (entityStream)) {
        out.println ("Pojo.number=" + t.number);
      }
    }
  }

  public static class ContentNegotiatedPojoPublishingTestServiceModule extends PojoPublishingTestServiceModule {
    public void configure (MessageWriterBinder binder) {
      binder.useInstance (new AnnotatedJsonWriter (new ObjectMapper () {
        private static final long serialVersionUID = 1L;

        {
          setAnnotationIntrospector (new JaxbAnnotationIntrospector (TypeFactory.defaultInstance ()));
        }
      }));
      binder.useInstance (new CustomWriter ());
    }
  }

  @Test
  public void negotiatorParameterSingles () throws Exception {
    try (Jetty9 j = serve (new ContentNegotiatedPojoPublishingTestServiceModule () {
      public void configure (ContentNegotiationConfigurer content) {
        content.parameter ("format")
               .map ("json", MediaType.APPLICATION_JSON_TYPE)
               .map ("custom", new MediaType ("application", "x-custom"));
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5&format=json"),
                  startsWith ("{\"word\":\"world\",\"number\":5,\"inner\":{}}"));
      assertThat (j.get ("/test/pojo?word=world&number=5&format=custom"),
                  startsWith ("Pojo.number=5"));
    }
  }

  @Test
  public void negotiatorParameterMap () throws Exception {
    try (Jetty9 j = serve (new ContentNegotiatedPojoPublishingTestServiceModule () {
      public void configure (ContentNegotiationConfigurer content) {
        content.parameter ("format")
               .map (new HashMap<String, MediaType> () {
                 static final long serialVersionUID = 1L;

                 {
                   put ("json", MediaType.APPLICATION_JSON_TYPE);
                   put ("custom", new MediaType ("application", "x-custom"));
                 }
               });
      }
    })) {
      assertThat (j.get ("/test/pojo?word=world&number=5&format=json"),
                  startsWith ("{\"word\":\"world\",\"number\":5,\"inner\":{}}"));
      assertThat (j.get ("/test/pojo?word=world&number=5&format=custom"),
                  startsWith ("Pojo.number=5"));
    }
  }

  @Test
  public void negotiatorExtensionsMap () throws Exception {
    try (Jetty9 j = serve (new ContentNegotiatedPojoPublishingTestServiceModule () {
      public void configure (ContentNegotiationConfigurer content) {
        content.extension ()
               .map (new HashMap<String, MediaType> () {
                 static final long serialVersionUID = 1L;

                 {
                   put ("json", MediaType.APPLICATION_JSON_TYPE);
                   put ("custom", new MediaType ("application", "x-custom"));
                 }
               });
      }
    })) {
      assertThat (j.get ("/test/pojo.json?word=world&number=5"),
                  startsWith ("{\"word\":\"world\",\"number\":5,\"inner\":{}}"));
      assertThat (j.get ("/test/pojo.custom?word=world&number=5"),
                  startsWith ("Pojo.number=5"));
    }
  }

  @Path ("/pojo2")
  public static class Pojo2Service {
    @POST
    @Path ("/{name}")
    public int post (@PathParam ("name") String name, Pojo pojo) {
      return pojo.number;
    }
  }

  @Provider
  // @Consumes (MediaType.APPLICATION_JSON)
  public static class PojoReader implements MessageBodyReader<Pojo> {
    private ObjectMapper mapper;

    @Inject
    public PojoReader (ObjectMapper mapper) {
      this.mapper = mapper;
    }

    public boolean isReadable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return Pojo.class.isAssignableFrom (type);
    }

    public Pojo readFrom (Class<Pojo> type,
                          Type genericType,
                          Annotation[] annotations,
                          MediaType mediaType,
                          MultivaluedMap<String, String> httpHeaders,
                          InputStream entityStream) throws IOException, WebApplicationException {
      return mapper.readValue (entityStream, Pojo.class);
    }
  }

  public static class PojoReaderProvider implements com.google.inject.Provider<PojoReader> {
    private ObjectMapper mapper;

    @Inject
    public PojoReaderProvider (ObjectMapper mapper) {
      this.mapper = mapper;
    }

    public PojoReader get () {
      return new PojoReader (mapper);
    }
  }

  public static class Pojo2PublishingTestServiceModule extends TestServiceModule {
    public void configure (ResourceBinder binder) {
      binder.publish ("pojo2", Pojo2Service.class);
    }
  }

  @Test
  public void readerClass () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.use (PojoReader.class);
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.use (new TypeLiteral<PojoReader> () {});
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerKey () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.use (Key.get (PojoReader.class));
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerInstance () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.useInstance (new PojoReader (new ObjectMapper ()));
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerProviderClass () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.useProvider (PojoReaderProvider.class);
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerProviderTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.useProvider (new TypeLiteral<PojoReaderProvider> () {});
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerProviderKey () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.useProvider (Key.get (PojoReaderProvider.class));
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerProviderInstance () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.useProvider (new PojoReaderProvider (new ObjectMapper ()));
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerConstructor () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      @SneakyThrows
      public void configure (MessageReaderBinder binder) {
        binder.useConstructor (PojoReader.class.getConstructor (ObjectMapper.class));
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Test
  public void readerConstructorTypeLiteral () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      @SneakyThrows
      public void configure (MessageReaderBinder binder) {
        binder.useConstructor (PojoReader.class.getConstructor (ObjectMapper.class), new TypeLiteral<PojoReader> () {});
      }
    })) {
      assertThat (j.post ("/test/pojo2/hello",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("5"));
    }
  }

  @Path ("/inject")
  @SuppressWarnings ("unused")
  public static class InjectedTestService {
    private @Inject javax.inject.Provider<UriInfo> i;
    private @Inject Providers p;
    private @Inject Request r;
    private @Inject HttpHeaders h;
    private @Inject SecurityContext s;
    private @Inject @Named ("param") javax.inject.Provider<String> param;

    @GET
    @Path ("/{param}")
    public String get (@PathParam ("param") String param) {
      return i.get ().getPathParameters ()
              .getFirst ("param").equals (this.param.get ()) ? param
                                                            : (this.param.get () + " != " + i.get ()
                                                                                             .getPathParameters ()
                                                                                             .getFirst ("param"));
    }
  }

  public static class InjectedTestServiceModule extends TestServiceModule {
    public void configure (ResourceBinder binder) {
      binder.publish ("inject", InjectedTestService.class).in (Singleton.class);
    }
  }

  @Test
  public void injectables () throws Exception {
    try (Jetty9 j = serve (new InjectedTestServiceModule (), new Module () {

      @Provides
      @Named ("param")
      @RequestScoped
      public String param (UriInfo i) {
        return i.getPathParameters ().getFirst ("param");
      }

      @Override
      public void configure (Binder binder) {}
    })) {
      assertThat (j.get ("/test/inject/hello"), is ("hello"));
    }
  }

  @Path ("/top")
  @Singleton
  public static class TopService {
    public interface Sub {
      public String get ();

      public void put (String value);
    }

    private HashMap<String, Sub> subs;

    {
      subs = new HashMap<> ();
      subs.put ("hello", new Sub () {
        private String value = "world";

        @GET
        @Path ("/value")
        public String get () {
          return value;
        }

        @POST
        @Path ("/value")
        public void put (@QueryParam ("value") String value) {
          this.value = value;
        }
      });
    }

    @Path ("/{name}")
    public Sub sub (@PathParam ("name") String name) {
      return subs.get (name);
    }
  }

  @Test
  public void dynamicResolution () throws Exception {
    try (Jetty9 j = serve (new TestServiceModule () {
      public void configure (ResourceBinder binder) {
        binder.publish ("top", TopService.class);
      }
    })) {
      assertThat (j.get ("/test/top/hello/value"), is ("world"));
      j.post ("/test/top/hello/value?value=werld", new ByteArrayInputStream (new byte[0]));
      assertThat (j.get ("/test/top/hello/value"), is ("werld"));
    }
  }

  @Provider
  // @Consumes (MediaType.APPLICATION_JSON)
  public static class ContextInjectingPojoReader implements MessageBodyReader<Pojo> {
    private @Inject @Named ("name") javax.inject.Provider<String> name;

    public boolean isReadable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return Pojo.class.isAssignableFrom (type);
    }

    public Pojo readFrom (Class<Pojo> type,
                          Type genericType,
                          Annotation[] annotations,
                          MediaType mediaType,
                          MultivaluedMap<String, String> httpHeaders,
                          InputStream entityStream) throws IOException, WebApplicationException {
      return new Pojo (name.get (), name.get ().length ());
    }
  }

  @Test
  public void readerWithContextInjection () throws Exception {
    try (Jetty9 j = serve (new Pojo2PublishingTestServiceModule () {
      public void configure (MessageReaderBinder binder) {
        binder.use (ContextInjectingPojoReader.class);
      }
    }, new Module () {
      @Provides
      @RequestScoped
      @Named ("name")
      public String name (UriInfo i) {
        return i.getPathParameters ().getFirst ("name");
      }

      @Override
      public void configure (Binder binder) {}
    })) {
      assertThat (j.post ("/test/pojo2/hellolong",
                          new ByteArrayInputStream ("{\"word\":\"world\",\"number\":5}".getBytes ())),
                  startsWith ("9"));
    }
  }
}
