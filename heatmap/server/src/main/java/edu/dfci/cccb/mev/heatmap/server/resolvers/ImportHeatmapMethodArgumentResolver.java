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
package edu.dfci.cccb.mev.heatmap.server.resolvers;

import static edu.dfci.cccb.mev.heatmap.server.resolvers.MethodParameters.brief;
import static org.springframework.util.StringUtils.isEmpty;

import java.io.IOException;
import java.io.InputStream;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import edu.dfci.cccb.mev.heatmap.domain.Content;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapBuilder;
import edu.dfci.cccb.mev.heatmap.domain.ImportException;

/**
 * @author levk
 * 
 */
@Log4j
@ToString
@EqualsAndHashCode (callSuper = false)
public class ImportHeatmapMethodArgumentResolver extends RequestParamMethodArgumentResolver {

  private final HeatmapBuilder builder;

  /**
   * @param beanFactory
   * @param useDefaultResolution
   */
  public ImportHeatmapMethodArgumentResolver (ConfigurableBeanFactory beanFactory,
                                              boolean useDefaultResolution,
                                              HeatmapBuilder builder) {
    super (beanFactory, useDefaultResolution);
    this.builder = builder;
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.method.annotation.RequestParamMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    RequestParam annotation = parameter.getParameterAnnotation (RequestParam.class);
    boolean supported = annotation == null ? false
                                          : (!isEmpty (annotation.value ()) && parameter.getParameterType ()
                                                                                        .equals (Heatmap.class));
    if (log.isDebugEnabled ())
      log.debug ("Method parameter " + (supported ? "" : "not ") + "supported on parameter " + brief (parameter));
    return supported;
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.method.annotation.RequestParamMethodArgumentResolver
   * #resolveName(java.lang.String, org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  protected Object resolveName (final String name, MethodParameter parameter, final NativeWebRequest request) throws Exception {
    final MultipartFile imported = request.getNativeRequest (MultipartRequest.class).getFile (name);
    if (imported == null)
      throw new MissingServletRequestParameterException (parameter.getParameterAnnotation (RequestParam.class).value (),
                                                         Heatmap.class.getName ());
    return builder.build (new Content () {

      @Override
      public String name () {
        return imported.getOriginalFilename ();
      }

      @Override
      public InputStream input () throws ImportException {
        try {
          return imported.getInputStream ();
        } catch (IOException e) {
          throw new ImportException (e);
        }
      }

      @Override
      public String contentType () {
        return imported.getContentType ();
      }
    });
  }
}
