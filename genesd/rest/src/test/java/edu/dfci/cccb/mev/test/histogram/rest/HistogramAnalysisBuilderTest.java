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
package edu.dfci.cccb.mev.test.histogram.rest;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.genesd.rest.configuration.GeneSDAnalysisConfiguration;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = {RDispatcherConfiguration.class, GeneSDAnalysisConfiguration.class})
public class HistogramAnalysisBuilderTest {
  
//  Dataset dataset;  
//  @Inject @Named("histogram.analysis.builder") HistogramAnalysisBuilder builder; 
//  
//  /* Sample json result
//  {
//    "breaks":[0,5,10,15,20,25,30,35,40],
//    "counts":[30,30,30,33,31,31,30,19],
//    "density":[0.0256,0.0256,0.0256,0.0282,0.0265,0.0265,0.0256,0.0162],
//    "mids":[2.5,7.5,12.5,17.5,22.5,27.5,32.5,37.5]
//  } 
//  */
//  Integer abreaks[] = {0,5,10,15,20,25,30,35,40};
//  List<Integer> breaks = Arrays.asList (abreaks);
//  Integer acounts[] = {30,30,30,33,31,31,30,19};
//  List<Integer> counts= Arrays.asList (acounts);
//  Double adensity[] = {0.0256,0.0256,0.0256,0.0282,0.0265,0.0265,0.0256,0.0162};
//  List<Double> density= Arrays.asList (adensity);
//  Double amids[] = {2.5,7.5,12.5,17.5,22.5,27.5,32.5,37.5};
//  List<Double> mids= Arrays.asList (amids);
//  
//  @Before
//  public void setUp () throws Exception {
//    URL urlData = this.getClass ().getResource ("/mouse_test_data.tsv");
//    RawInput input = new UrlTsvInput (urlData);    
//    dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
//              .setValueStoreBuilder (new FlatFileValueStoreBuilder ())
//              .build (input);
//  }
//
  @Test @Ignore
  // This test will only work if there's an R backend active
  public void simple () throws DatasetException {
//    HistogramAnalysis analysis = builder.name("histogram_test").build();
//    assertThat (analysis.result ().breaks (), is(breaks));
//    assertThat (analysis.result ().counts (), is(counts));
//    assertThat (analysis.result ().density(), is(density));
//    assertThat (analysis.result ().mids (), is(mids));
  }
}
