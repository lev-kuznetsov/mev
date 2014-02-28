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

import static java.util.Arrays.asList;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

/**
 * @author levk
 */
@ToString
@Log4j
public class MevServicesModule extends CxfJaxrsServiceModule {

  private static final String SERVICE_URL = "/service/*";
  private static final String[] JAXRS_SCAN_PACKAGES = "edu.dfci.cccb.mev".split (":");

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.services.guice.JaxrsServerModule#configureServices
   * () */
  @Override
  protected void configureServices () {
    log.info ("Starting " + this + " under " + SERVICE_URL + " scanning " + asList (JAXRS_SCAN_PACKAGES));
    service ("/services/*");
    scan ("edu.dfci.cccb.mev");
  }
}
