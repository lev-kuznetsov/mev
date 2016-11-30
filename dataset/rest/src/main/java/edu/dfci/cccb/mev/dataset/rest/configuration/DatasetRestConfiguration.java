/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.rest.configuration;

import static edu.dfci.cccb.mev.dataset.rest.assembly.tsv.prototype.AbstractTsvHttpMessageConverter.TSV_EXTENSION;
import static edu.dfci.cccb.mev.dataset.rest.assembly.tsv.prototype.AbstractTsvHttpMessageConverter.TSV_MEDIA_TYPE;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver.ANALYSIS_MAPPING_NAME;
import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Arrays.asList;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.context.annotation.ScopedProxyMode.NO;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import edu.dfci.cccb.mev.dataset.rest.assembly.binary.FlatFileValuesBinaryMessageConverter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter;
import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListWorkspace;
import edu.dfci.cccb.mev.dataset.rest.assembly.binary.FlatFileValuesBinary32FloatMessageConverter;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.DimensionTypeJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.SimpleDatasetJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.SimpleDatasetValuesJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.SimpleDimensionJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.SimpleSelectionJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.tsv.DatasetTsvMessageConverter;
import edu.dfci.cccb.mev.dataset.rest.assembly.tsv.SelectionsTsvMessageConverter;
import edu.dfci.cccb.mev.dataset.rest.google.SecurityContext;
import edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.dataset.rest.resolvers.DimensionPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.dataset.rest.resolvers.SelectionPathVariableMethodArgumentResolver;

/**
 * @author levk
 * 
 */
@Configuration
@ComponentScan (basePackages = "edu.dfci.cccb.mev.dataset.rest.controllers")
@ToString
@Log4j
@Import ({ DatasetDomainBuildersConfiguration.class })
public class DatasetRestConfiguration extends MevRestConfigurerAdapter {

  // Domain conversational objects

  @Bean
  @Scope (value = SCOPE_SESSION, proxyMode = INTERFACES)
  public Workspace workspace () {
      Workspace workspace = new ArrayListWorkspace ();
      log.info ("Supplying  workspace " + System.identityHashCode(workspace));
      return workspace;
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = NO)
  public Dataset dataset (final NativeWebRequest request, final DatasetPathVariableMethodArgumentResolver resolver) throws Exception {
    return nonCloseableProxy (resolver.resolveObject (request));
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = NO)
  public Dimension dimension (NativeWebRequest request, DimensionPathVariableMethodArgumentResolver resolver) throws Exception {
    return nonCloseableProxy (resolver.resolveObject (request));
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = NO)
  public Selection selection (NativeWebRequest request, SelectionPathVariableMethodArgumentResolver resolver) throws Exception {
    return nonCloseableProxy (resolver.resolveObject (request));
  }

  // FIXME: this is a hack until type of analysis goes into the request mapping,
  // regarding issue #442
  @SuppressWarnings ("unchecked")
  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = NO)
  public Analysis analysis (NativeWebRequest request, Dataset dataset) throws Exception {
    return nonCloseableProxy (dataset.analyses ()
                                     .get (((Map<String, String>) request.getAttribute (URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                                                                                        RequestAttributes.SCOPE_REQUEST)).get (ANALYSIS_MAPPING_NAME)));
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.prototype.MevRestConfigurerAdapter#
   * addJsonSerializers(java.util.List) */
  @Override
  public void addJsonSerializers (List<JsonSerializer<?>> serializers) {
    serializers.addAll (asList (new DimensionTypeJsonSerializer (),
                                new SimpleDatasetJsonSerializer (),
                                new SimpleDatasetValuesJsonSerializer(),
                                new SimpleDimensionJsonSerializer (),
                                // This serializer returns selections as an
                                // array.
                                // Instead we want it wrapped object for
                                // angular's $resource service to work
                                // The annotated Selections class is taking care
                                // of that now.
                                // new SimpleSelectionsJsonSerializer (),
                                new SimpleSelectionJsonSerializer ()));
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.prototype.MevRestConfigurerAdapter#
   * addHttpMessageConverters(java.util.List) */
  @Override
  public void addHttpMessageConverters (List<HttpMessageConverter<?>> converters) {
    converters.addAll (asList (new DatasetTsvMessageConverter (),
                               new SelectionsTsvMessageConverter (),
                               new FlatFileValuesBinary32FloatMessageConverter (),
                               new FlatFileValuesBinaryMessageConverter()
            ));
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addPreferredArgumentResolvers(java.util.List) */
  @Override
  public void addPreferredArgumentResolvers (List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.addAll (asList (new SelectionPathVariableMethodArgumentResolver (),
                              datasetPathVariableMethodArgumentResolver (),
                              dimensionPathVariableMethodArgumentResolver ()));
  }

  @Bean
  public DatasetPathVariableMethodArgumentResolver datasetPathVariableMethodArgumentResolver () {
    return new DatasetPathVariableMethodArgumentResolver ();
  }

  @Bean
  public DimensionPathVariableMethodArgumentResolver dimensionPathVariableMethodArgumentResolver () {
    return new DimensionPathVariableMethodArgumentResolver ();
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #configureContentNegotiation
   * (org.springframework.web.servlet.config.annotation
   * .ContentNegotiationConfigurer) */
  @Override
  public void configureContentNegotiation (ContentNegotiationConfigurer configurer) {

    configurer.mediaType (TSV_EXTENSION, TSV_MEDIA_TYPE)
              .mediaType ("binary", APPLICATION_OCTET_STREAM)
              .mediaType ("binary64", MediaType.valueOf("application/x.dfci.cccb.mev.dataset.binary64"))
              .mediaType ("text", TEXT_PLAIN);
  }

  @SuppressWarnings ("unchecked")
  private static <T> T nonCloseableProxy (final T of) {
    if (of == null)
      return null;

    if (!(AutoCloseable.class.isAssignableFrom (of.getClass ()) || Closeable.class.isAssignableFrom (of.getClass ())))
      return of;

    return (T) newProxyInstance (of.getClass ().getClassLoader (), new HashSet<Class<?>> () {
      private static final long serialVersionUID = 1L;

      {
        addImplementedInterfaces (of.getClass ());
        remove (AutoCloseable.class);
        remove (Closeable.class);
        if (log.isDebugEnabled ())
          log.debug ("Returning proxy of " + of + " implementing " + this);
      }

      private void addImplementedInterfaces (Class<?> clazz) {
        if (clazz != null) {
          addAll (asList (clazz.getInterfaces ()));
          addImplementedInterfaces (clazz.getSuperclass ());
          for (Class<?> implemented : clazz.getInterfaces ())
            addImplementedInterfaces (implemented);
        }
      }
    }.toArray (new Class<?>[0]), new InvocationHandler () {

      @Override
      public Object invoke (Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke (of, args);
      }
    });
  }
}
