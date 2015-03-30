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
package edu.dfci.cccb.mev.web.test.nmf;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.r.RDispatcher;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Rserve;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.nmf.domain.Nmf;
import edu.dfci.cccb.mev.nmf.domain.NmfBuilder;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = { RDispatcherConfiguration.class, StoreBuilderConf.class })
public class NmfTest {

  private @Inject RDispatcher r;
  private @Inject @Rserve Provider<InetSocketAddress> host;
  private @Inject @Rserve ObjectMapper mapper;

  @Test
  // @Ignore
  public void t () throws Exception {
    SimpleDatasetBuilder s = new SimpleDatasetBuilder ();
    s.setParserFactories (asList (new SuperCsvParserFactory ()));
    s.setValueStoreBuilder (new MapBackedValueStoreBuilder ());
    System.out.println (r);
    NmfBuilder b = new NmfBuilder ();
    b.setR (r);
    b.dataset (s.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\n" +
                                                  "g1\t.1\t.2\t.3\n" +
                                                  "g2\t.4\t.5\t.6")));
    System.out.println (b.build ());
  }

  @Test
  @Ignore
  public void tt () throws Exception {
    SimpleDatasetBuilder s = new SimpleDatasetBuilder ();
    s.setParserFactories (asList (new SuperCsvParserFactory ()));
    s.setValueStoreBuilder (new MapBackedValueStoreBuilder ());

    RConnection c = new RConnection ();
    c.assign ("ds", new REXPString (mapper.writeValueAsString (s.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\n" +
                                                                                                  "g1\t.1\t.2\t.3\n" +
                                                                                                  "g2\t.4\t.5\t.6")))));
    System.out.println (mapper.readValue (c.eval ("m <- NMF::nmf (jsonlite::fromJSON (ds), rank = 3);"
                                                  +
                                                  "w <- NMF::basis (m);"
                                                  +
                                                  "h <- NMF::coef (m);"
                                                  +
                                                  "jsonlite::toJSON (list (w = w, h = list (matrix = h)), auto_unbox = TRUE)")
                                           .asString (),
                                          Nmf.class));
  }
}

@Configuration
class StoreBuilderConf {
  @Bean
  @Scope ("prototype")
  public ValueStoreBuilder storeBuilder () throws IOException {
    return new FlatFileValueStoreBuilder ();
  }
}
