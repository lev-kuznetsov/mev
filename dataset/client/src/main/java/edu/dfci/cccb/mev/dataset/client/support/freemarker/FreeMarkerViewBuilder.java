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
package edu.dfci.cccb.mev.dataset.client.support.freemarker;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import edu.dfci.cccb.mev.dataset.client.support.prototype.AbstractTemplateViewBuilder;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;

/**
 * @author levk
 * 
 */
@Accessors (fluent = false, chain = true)
public class FreeMarkerViewBuilder extends AbstractTemplateViewBuilder<FreeMarkerViewBuilder> {

  private @Getter @Setter String encoding;
  private @Getter @Setter (onMethod = @_ (@Autowired (required = false))) Configuration configuration;

  public FreeMarkerView build () {
    FreeMarkerView view = new FreeMarkerView ();
    initialize (view);
    if (encoding != null)
      view.setEncoding (encoding);
    if (configuration != null)
      view.setConfiguration (configuration);
    view.addStaticAttribute ("enums", BeansWrapper.getDefaultInstance().getEnumModels());
    return view;
  }
}
