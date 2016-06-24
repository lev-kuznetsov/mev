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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author levk
 * 
 */
@Configuration
public class MultipartUploadConfiguration {

  private @Inject Environment environment;

  @Bean
  public MultipartResolver multipartResolver () {
    final long DEFAULT_MAX_UPLOAD_SIZE = 1024L * 1024L * 200; // 200Mb

    //TODO: find a better way to bypass Spring controllers for OpenRefine
    CommonsMultipartResolver resolver = new CommonsMultipartResolver (){
      @Override
      public boolean isMultipart(HttpServletRequest request){        
        if(request.getServletPath().startsWith ("/annotations")){
          if(request.getServletPath().endsWith("/row/import") || request.getServletPath().endsWith("/column/import"))
            //annotation import is handled by AnnotationConroller
            return super.isMultipart (request);
          else
            //Let OpenRefine handle parsing of the multipart request
            return false;
        }else{
          return super.isMultipart (request);
        }
        
      }
    };
//    StandardServletMultipartResolver resolver = new StandardServletMultipartResolver ();
    resolver.setMaxUploadSize (environment.getProperty ("multipart.upload.max.size",
                                                        Long.class,
                                                        DEFAULT_MAX_UPLOAD_SIZE));
    resolver.setResolveLazily (true);
    return resolver;
  }
}
