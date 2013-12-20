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
package edu.dfci.cccb.mev.web.configuration.resolvers;

import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.OrderComparator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * @author levk
 * 
 */
@Configuration
@Import ({
          ContentNegotiationConfiguration.class,
          MultipartUploadConfiguration.class,
          RestResolverConfiguration.class,
          ViewResolverConfiguration.class,
          WebjarResourceHandlerConfiguration.class })
@ToString
@Log4j
public class ResolverConfigurations {

  private @Inject RequestMappingHandlerAdapter adapter;
  private @Autowired (required = false) List<HandlerMethodArgumentResolver> methodArgumentResolvers;

  @PostConstruct
  private void prioritizeCustomArgumentMethodHandlers () {
    log.info ("Registering method argument resolvers " + methodArgumentResolvers);
    if (methodArgumentResolvers != null && methodArgumentResolvers.size () > 0) {
      sort (methodArgumentResolvers, new OrderComparator ());
      List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<> (adapter.getArgumentResolvers ());
      argumentResolvers.addAll (0, methodArgumentResolvers);
      adapter.setArgumentResolvers (argumentResolvers);
    }
  }
}
