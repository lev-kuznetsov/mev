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
package edu.dfci.cccb.mev.dataset.rest.assembly.tsv;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.multipart.MultipartFile;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;

/**
 * @author levk
 * 
 */
@ToString
public class MultipartUploadDatasetArgumentResolver extends RequestParamMethodArgumentResolver {

  private @Getter @Setter (onMethod = @_ (@Inject)) DatasetBuilder builder;

  /**
   * @param beanFactory
   * @param useDefaultResolution
   */
  public MultipartUploadDatasetArgumentResolver (ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
    super (beanFactory, useDefaultResolution);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.method.annotation.RequestParamMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    return parameter.hasParameterAnnotation (RequestParam.class)
           && parameter.getParameterAnnotation (RequestParam.class).value ().length () > 0
           && Dataset.class.isAssignableFrom (parameter.getParameterType ());
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.method.annotation.RequestParamMethodArgumentResolver
   * #resolveName(java.lang.String, org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  protected Object resolveName (String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
    Object value = super.resolveName (name, parameter, webRequest);
    if (value instanceof MultipartFile)
      return builder.build (new MultipartTsvInput ((MultipartFile) value));
    else
      throw new UnsupportedDatasetDefinitionTypeException ().of (value);
  }
}
