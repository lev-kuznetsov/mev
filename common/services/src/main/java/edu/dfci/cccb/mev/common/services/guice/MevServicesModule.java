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
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;

import javax.ws.rs.core.MediaType;

import edu.dfci.cccb.mev.common.services.guice.annotation.ContentNegotiationConfiguration;

/**
 * Enables the MeV RESTful services
 * 
 * @author levk
 * @since CRYSTAL
 */
public final class MevServicesModule extends JaxrsServiceModule {

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
  public void configure (JaxrsServiceBinder binder) {
    binder.service ("/services/*");
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.services.guice2.JaxrsServiceModule#configure(
   * edu.dfci
   * .cccb.mev.common.services.guice.annotation.ContentNegotiationConfiguration) */
  @Override
  public void configure (ContentNegotiationConfiguration configurer) {
    configurer.parameter (PARAMETER)
              .map ("json", APPLICATION_JSON_TYPE)
              .map ("xml", APPLICATION_XML_TYPE)
              .map ("tsv", TEXT_TSV_TYPE);
  }
}
