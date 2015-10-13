package edu.dfci.cccb.mev.test.histogram.domain;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;

public class TestSimpleHistogramAnalysisBuilder {

//  Dataset dataset;  
//  @Before
//  public void setUp () throws Exception {
//    URL urlData = this.getClass ().getResource ("/mouse_test_data.tsv");
//    RawInput input = new UrlTsvInput (urlData);    
//    dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
//              .setValueStoreBuilder (new FlatFileValueStoreBuilder ())
//              .build (input);
//  }

  @Test
  public void test () {
    
  }

}
