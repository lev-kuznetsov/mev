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
package edu.dfci.cccb.mev.dataset.client.support.velocity;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.view.velocity.VelocityView;

import edu.dfci.cccb.mev.dataset.client.support.prototype.AbstractTemplateViewBuilder;

/**
 * @author levk
 * 
 */
@Accessors (fluent = false, chain = true)
public class VelocityViewBuilder extends AbstractTemplateViewBuilder<VelocityViewBuilder> {

  private @Getter @Setter boolean cacheTemplate = false;
  private @Getter @Setter String dateToolAttribute;
  private @Getter @Setter String encoding;
  private @Getter @Setter String numberToolAttribute;
  private @Getter @Setter Map<String, Class<?>> toolAttributes = new HashMap<> ();
  private @Getter @Setter VelocityEngine velocityEngine;

  public VelocityViewBuilder addToolAttributes (Map<String, Class<?>> toolAttributes) {
    this.toolAttributes.putAll (toolAttributes);
    return this;
  }

  public VelocityView build () {
    VelocityView view = new VelocityView ();
    super.initialize (view);
    view.setCacheTemplate (cacheTemplate);
    if (dateToolAttribute != null)
      view.setDateToolAttribute (dateToolAttribute);
    if (encoding != null)
      view.setEncoding (encoding);
    if (numberToolAttribute != null)
      view.setNumberToolAttribute (numberToolAttribute);
    if (toolAttributes != null)
      view.setToolAttributes (toolAttributes);
    if (velocityEngine != null)
      view.setVelocityEngine (velocityEngine);
    return view;
  }
}
