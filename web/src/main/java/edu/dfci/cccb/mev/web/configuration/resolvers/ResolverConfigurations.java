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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
public class ResolverConfigurations {

  private @Inject RequestMappingHandlerAdapter adapter;
  private @Inject Collection<HandlerMethodArgumentResolver> methodArgumentResolvers;

  @PostConstruct
  private void prioritizeCustomArgumentMethodHandlers () {
    List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<> (adapter.getArgumentResolvers ());
    argumentResolvers.addAll (0, new HashSet<> (methodArgumentResolvers));
    adapter.setArgumentResolvers (argumentResolvers);
  }
}
