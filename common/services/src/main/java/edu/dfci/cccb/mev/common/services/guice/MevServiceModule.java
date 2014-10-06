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

package edu.dfci.cccb.mev.common.services.guice;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import com.google.inject.Binder;

import edu.dfci.cccb.mev.common.domain.guice.MevModule;
import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ContentNegotiationConfigurer;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ContentNegotiationMapper;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceModule;

/**
 * Enables the MeV RESTful services
 * 
 * @author levk
 * @since CRYSTAL
 */
public class MevServiceModule extends MevModule {
  public static final String SERVICE_URI = "/services/*";

  private static final String PARAMETER = "format";

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    super.configure (binder);
    binder.install (new ServiceModule () {
      @Override
      public void configure (ContentNegotiationConfigurer content) {
        MevServiceModule.this.configure (content.parameter (PARAMETER));
      }

      @Override
      public void configure (ResourceBinder binder) {
        MevServiceModule.this.configure (binder);
      }
    });

    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        binder.install (new ServiceModule () {
          @Override
          public void configure (ContentNegotiationConfigurer content) {
            content.parameter (PARAMETER)
                   .map ("json", APPLICATION_JSON_TYPE);
          }

          @Override
          public void configure (ServiceBinder binder) {
            binder.service (SERVICE_URI);
          }
        });
      }
    });
  }

  /**
   * @see ServiceModule#configure(ResourceBinder)
   */
  public void configure (ResourceBinder binder) {}

  /**
   * @see ServiceModule#configure(ContentNegotiationConfigurer)
   */
  public void configure (ContentNegotiationMapper content) {}
}
