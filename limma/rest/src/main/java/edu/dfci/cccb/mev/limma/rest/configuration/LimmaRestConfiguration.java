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
package edu.dfci.cccb.mev.limma.rest.configuration;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.limma.domain.contract.LimmaBuilder;
import edu.dfci.cccb.mev.limma.domain.simple.StatelessScriptEngineFileBackedLimmaBuilder;
import edu.dfci.cccb.mev.limma.rest.assembly.json.EntryJsonSerializer;
import edu.dfci.cccb.mev.limma.rest.assembly.json.LimmaJsonSerializer;
import edu.dfci.cccb.mev.limma.rest.assembly.tsv.LimmaTsvMessageConverter;

/**
 * @author levk
 * 
 */
@ToString
@Configuration
@ComponentScan ("edu.dfci.cccb.mev.limma.rest.controllers")
public class LimmaRestConfiguration {

  @Bean
  @Scope (SCOPE_REQUEST)
  public LimmaBuilder limmaBuilder () {
    return new StatelessScriptEngineFileBackedLimmaBuilder ();
  }

  @Bean (name = "R")
  public ScriptEngine r () {
    return new ScriptEngineManager ().getEngineByName ("CliR");
  }

  @Bean
  public LimmaTsvMessageConverter limmaTsvMessageConverter () {
    return new LimmaTsvMessageConverter ();
  }

  @Bean
  public EntryJsonSerializer entryJsonSerializer () {
    return new EntryJsonSerializer ();
  }

  @Bean
  public LimmaJsonSerializer limmaJsonSerializer () {
    return new LimmaJsonSerializer ();
  }
}
