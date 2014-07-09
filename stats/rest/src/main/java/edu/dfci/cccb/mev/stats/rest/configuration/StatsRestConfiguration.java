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
package edu.dfci.cccb.mev.stats.rest.configuration;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter;
import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.stats.domain.cli.CliRFisherBuilder;
import edu.dfci.cccb.mev.stats.domain.cli.CliRWilcoxonBuilder;
import edu.dfci.cccb.mev.stats.domain.contract.Fisher;
import edu.dfci.cccb.mev.stats.domain.contract.FisherBuilder;
import edu.dfci.cccb.mev.stats.domain.contract.Wilcoxon;
import edu.dfci.cccb.mev.stats.domain.contract.WilcoxonBuilder;

/**
 * @author levk
 * 
 */
@ToString
@Configuration
@ComponentScan ("edu.dfci.cccb.mev.stats.rest.controllers")
public class StatsRestConfiguration extends MevRestConfigurerAdapter {

  @Bean (name = "R")
  public ScriptEngine r () {
    return new ScriptEngineManager ().getEngineByName ("CliR");
  }

  @Bean
  @Scope (SCOPE_REQUEST)
  public FisherBuilder fisherBuilder () {
    return new CliRFisherBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<Fisher> fisherAnalysisPathVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (Fisher.class);
  }

  @Bean
  @Scope (SCOPE_REQUEST)
  public WilcoxonBuilder wilcoxonBuilder () {
    return new CliRWilcoxonBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<Wilcoxon> wilcoxonAnalysisPathVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (Wilcoxon.class);
  }
}
