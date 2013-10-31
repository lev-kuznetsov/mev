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
package edu.dfci.cccb.mev.web.domain;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author levk
 * 
 */
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class RestApiDescriptor {

  @RequiredArgsConstructor
  public static class ApiParameter {
    private @Getter final String parameter;
    private @Getter final Class<?> type;
  }

  private @Getter final RequestMethod[] methods;
  private @Getter final String[] urls;
  private @Getter final ApiParameter[] parameters;
  private @Getter final Method[] handlers;

  public interface DescriptorBuilder {
    public interface DescriptorUrlBuilder {
      public interface DescriptorRequestBuilder {
        public interface DescriptorHandlerBuilder {
          RestApiDescriptor handlers (Method... handlers);
        }
        
        DescriptorHandlerBuilder parameters (ApiParameter... request);
      }

      DescriptorRequestBuilder urls (String... urls);
    }

    DescriptorUrlBuilder methods (RequestMethod... methods);
  }

  public static DescriptorBuilder descriptor () {
    return new DescriptorBuilder () {
      /* (non-Javadoc)
       * @see
       * edu.dfci.cccb.mev.web.domain.RestApiDescriptor.DescriptorBuilder#methods
       * (java.lang.String[]) */
      @Override
      public DescriptorUrlBuilder methods (final RequestMethod... methods) {
        return new DescriptorUrlBuilder () {

          @Override
          public DescriptorRequestBuilder urls (final String... urls) {
            return new DescriptorRequestBuilder () {

              @Override
              public DescriptorHandlerBuilder parameters (final ApiParameter... parameters) {
                return new DescriptorHandlerBuilder () {

                  @Override
                  public RestApiDescriptor handlers (Method... handlers) {
                    return new RestApiDescriptor (methods, urls, parameters, handlers);
                  }
                };
              }
            };
          }
        };
      }
    };
  }

  public static Collection<RestApiDescriptor> descriptors (RequestMappingHandlerMapping mappings) {
    Collection<RestApiDescriptor> descriptors = new HashSet<> ();

    for (Entry<RequestMappingInfo, HandlerMethod> mapping : mappings.getHandlerMethods ().entrySet ()) {
      RequestMappingInfo info = mapping.getKey ();
      HandlerMethod method = mapping.getValue ();
      if (method.getMethodAnnotation (ResponseBody.class) != null
          || method.getBeanType ().getAnnotation (RestController.class) != null) {
        Set<ApiParameter> parameters = new HashSet<> ();
        for (MethodParameter parameter : method.getMethodParameters ())
          if (parameter.hasParameterAnnotation (RequestParam.class))
            parameters.add (new ApiParameter (parameter.getParameterAnnotation (RequestParam.class).value (),
                                              parameter.getParameterType ()));
        descriptors.add (descriptor ().methods (info.getMethodsCondition ()
                                                    .getMethods ()
                                                    .toArray (new RequestMethod[0]))
                                      .urls (info.getPatternsCondition ()
                                                 .getPatterns ()
                                                 .toArray (new String[0]))
                                      .parameters (parameters.toArray (new ApiParameter[0]))
                                      .handlers (method.getMethod ()));
      }
    }

    return descriptors;
  }
}
