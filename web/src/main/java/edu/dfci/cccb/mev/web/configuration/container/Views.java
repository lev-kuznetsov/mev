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
package edu.dfci.cccb.mev.web.configuration.container;

import static edu.dfci.cccb.mev.api.client.support.view.ViewBuilders.freemarker;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 * @author levk
 * 
 */
public class Views {

  @Bean
  public FreeMarkerView home () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/home.ftl").build ();
  }

  @Bean
  public FreeMarkerView api () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/api.ftl").build ();
  }

  // Elements

  @Bean (name = "elements/view1")
  public FreeMarkerView elementView1 () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/elements/view1.ftl").build ();
  }
  
  @Bean (name = "elements/menubar")
  public FreeMarkerView elementMenubar () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/elements/menubar.ftl").build ();
  }
  
  @Bean (name = "elements/heirarchicalbody")
  public FreeMarkerView elementHeirarchicalbody () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/elements/heirarchicalbody.ftl").build ();
  }
  
  @Bean (name = "elements/modal")
  public FreeMarkerView elementModal () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/elements/modal.ftl").build ();
  }
  
  @Bean (name = "elements/prevlimmashell")
  public FreeMarkerView elementPrevlimmashell () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/elements/prevlimmashell.ftl").build ();
  }
  
  @Bean (name = "elements/table")
  public FreeMarkerView elementTable () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/elements/prevlimmashell.ftl").build ();
  }
  
  // Partials

  @Bean (name = "partials/partial1")
  public FreeMarkerView partial1 () {
    return freemarker ().url ("/edu/dfci/cccb/mev/web/views/partials/partial1.ftl").build ();
  }
}
