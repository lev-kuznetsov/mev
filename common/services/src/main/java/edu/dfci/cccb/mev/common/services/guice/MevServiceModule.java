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

import static edu.dfci.cccb.mev.common.services.servlet.ServicesFilter.SERVICE_ROOT_URL;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;

import javax.ws.rs.core.MediaType;

import lombok.Delegate;
import edu.dfci.cccb.mev.common.services.guice.annotation.ContentNegotiationConfiguration;
import edu.dfci.cccb.mev.common.services.guice.annotation.ContentNegotiationConfiguration.MappingConfiguration;

/**
 * Enables the MeV RESTful services
 * 
 * @author levk
 * @since CRYSTAL
 */
public class MevServiceModule extends JaxrsServiceModule {

  /**
   * Content type query parameter name
   */
  public static final String PARAMETER = "format";
  /**
   * Tab separated value media type
   */
  public static final MediaType TEXT_TSV_TYPE = new MediaType ("text", "tab-separated-values");

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.services.guice2.JaxrsServiceModule#configure(
   * edu.dfci
   * .cccb.mev.common.services.guice2.JaxrsServiceModule.JaxrsServiceBinder) */
  @Override
  public final void configure (final JaxrsServiceBinder binder) {
    configure (new PublishingJaxrsBinder () {
      private final @Delegate JaxrsServiceBinder delegate = binder;
    });

    binder.service (SERVICE_ROOT_URL);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.services.guice2.JaxrsServiceModule#configure(
   * edu.dfci
   * .cccb.mev.common.services.guice.annotation.ContentNegotiationConfiguration) */
  @Override
  public final void configure (ContentNegotiationConfiguration configurer) {
    configure (configurer.parameter (PARAMETER)
                         .map ("json", APPLICATION_JSON_TYPE)
                         .map ("xml", APPLICATION_XML_TYPE)
                         .map ("tsv", TEXT_TSV_TYPE));
  }

  /**
   * Configure MeV REST resources
   * 
   * @param binder
   */
  public void configure (PublishingJaxrsBinder binder) {}

  /**
   * Configure content negotiation
   * 
   * @param configurer
   */
  public void configure (MappingConfiguration configurer) {}
}
