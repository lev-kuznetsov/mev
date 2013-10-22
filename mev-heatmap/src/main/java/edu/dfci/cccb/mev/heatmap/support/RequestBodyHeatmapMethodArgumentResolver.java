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
package edu.dfci.cccb.mev.heatmap.support;

import java.io.IOException;
import java.io.InputStream;

import lombok.extern.log4j.Log4j;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import us.levk.util.runtime.support.Annotations;
import us.levk.util.runtime.support.Methods;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap.Builder;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap.Builder.Content;

/**
 * @author levk
 * 
 */
@Log4j
public class RequestBodyHeatmapMethodArgumentResolver extends RequestParamMethodArgumentResolver {

  private Builder builder;

  /**
   * @param beanFactory
   * @param useDefaultResolution
   */
  public RequestBodyHeatmapMethodArgumentResolver (Builder builder) {
    super (null, false);
    this.builder = builder;
  }

  /* (non-Javadoc)
   * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#
   * supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    if (log.isDebugEnabled ())
      log.debug ("Method parameter " + parameter.getParameterIndex ()
                 + " for " + Methods.brief (parameter.getMethod ()) + " is "
                 + (Annotations.validate (parameter.getParameterAnnotation (RequestParam.class), "value")
                    && Heatmap.class.equals (parameter.getParameterType ()) ? "" : "not ") + "supported");
    return Annotations.validate (parameter.getParameterAnnotation (RequestParam.class), "value")
           && Heatmap.class.equals (parameter.getParameterType ());
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.method.annotation.RequestParamMethodArgumentResolver
   * #resolveName(java.lang.String, org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  protected Object resolveName (final String name, MethodParameter parameter, final NativeWebRequest request) throws Exception {
    log.debug ("Resolving named " + name + " to heatmap");
    return builder.build (new Content () {
      
      final MultipartFile file = request.getNativeRequest (MultipartRequest.class).getFile (name);

      @Override
      public InputStream data () throws IOException {
        return file.getInputStream ();
      }

      @Override
      public String name () {
        return file.getOriginalFilename ();
      }

      @Override
      public String type () {
        return file.getContentType ();
      }

      @Override
      public long size () {
        return file.getSize ();
      }
    });
  }
}
