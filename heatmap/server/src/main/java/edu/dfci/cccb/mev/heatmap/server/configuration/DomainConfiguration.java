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
package edu.dfci.cccb.mev.heatmap.server.configuration;

import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.heatmap.domain.DataBuilder;
import edu.dfci.cccb.mev.heatmap.domain.DataParser;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapBuilder;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;
import edu.dfci.cccb.mev.heatmap.domain.concrete.ListWorkspace;
import edu.dfci.cccb.mev.heatmap.domain.concrete.supercsv.SuperCsvDataParser;

/**
 * @author levk
 * 
 */
@Configuration
public class DomainConfiguration {

  @Bean
  @Scope (value = SCOPE_SESSION, proxyMode = INTERFACES)
  public Workspace workspace () {
    return new ListWorkspace ();
  }

  @Bean
  public HeatmapBuilder heatmapBuilder () {
    return null; // FIXME: stub
  }

  @Bean
  public DataParser dataParser () {
    return new SuperCsvDataParser ();
  }

  @Bean
  public DataBuilder dataBuilder () {
    return null; // FIXME: stub
  }
}
