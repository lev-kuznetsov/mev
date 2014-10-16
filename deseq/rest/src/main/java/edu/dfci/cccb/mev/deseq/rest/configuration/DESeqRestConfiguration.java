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
package edu.dfci.cccb.mev.deseq.rest.configuration;

import static java.util.Arrays.asList;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;

import com.fasterxml.jackson.databind.JsonSerializer;

import edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter;
import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeq;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeqBuilder;
import edu.dfci.cccb.mev.deseq.domain.simple.StatelessScriptEngineFileBackedDESeqBuilder;
import edu.dfci.cccb.mev.deseq.rest.assembly.json.EntryJsonSerializer;
import edu.dfci.cccb.mev.deseq.rest.assembly.json.DESeqJsonSerializer;
import edu.dfci.cccb.mev.deseq.rest.assembly.tsv.DESeqTsvMessageConverter;

/**
 * @author levk
 * 
 */
@ToString
@Configuration
@ComponentScan ("edu.dfci.cccb.mev.deseq.rest.controllers")
public class DESeqRestConfiguration extends MevRestConfigurerAdapter {

  @Bean
  @Scope (SCOPE_REQUEST)
  public DESeqBuilder deseqBuilder () {
    return new StatelessScriptEngineFileBackedDESeqBuilder ();
  }

  @Bean (name = "R")
  public ScriptEngine r () {
    return new ScriptEngineManager ().getEngineByName ("CliR");
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<DESeq> deseqPathVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (DESeq.class);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addJsonSerializers(java.util.List) */
  @Override
  public void addJsonSerializers (List<JsonSerializer<?>> serializers) {
    serializers.addAll (asList (new EntryJsonSerializer (), new DESeqJsonSerializer ()));
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addHttpMessageConverters(java.util.List) */
  @Override
  public void addHttpMessageConverters (List<HttpMessageConverter<?>> converters) {
    converters.add (new DESeqTsvMessageConverter ());
  }
}
