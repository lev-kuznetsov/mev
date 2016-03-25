package edu.dfci.cccb.mev.dataset.domain.subset;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;

public class TestDataSubset {

  private SimpleDatasetBuilder builder;
  private Dataset parent;
  private Dataset subset;
  
  @Rule
  public ExpectedException thrown= ExpectedException.none();
  
  @Before
  public void initializeBuilder () throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException {
    builder = new SimpleDatasetBuilder ();
    builder.setParserFactories (asList (new SuperCsvParserFactory ()));
    builder.setValueStoreBuilder (new MapBackedValueStoreBuilder ());
    parent = builder.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\tsd\n" +
            "g1\t1.1\t1.2\t1.3\t1.4\n" +
            "g2\t2.1\t2.2\t2.3\t2.4\n" +
            "g3\t3.1\t3.2\t3.3\t3.4\n" + 
            "g4\t4.1\t4.2\t4.3\t4.4\n" 
            ));
    subset = parent.subset ("subset1", 
      new ArrayList <String>(){
        {add("sb"); add("sc");}
      }, 
      new ArrayList <String>(){
        {add("g2"); add("g3");}
      });
  }
  
  @Test
  public void testValues () throws InvalidCoordinateException {
    assertThat(subset.values ().get ("g2", "sb"), equalTo (2.2));
    assertThat(subset.values ().get ("g2", "sc"), equalTo (2.3));
    assertThat(subset.values ().get ("g3", "sb"), equalTo (3.2));
    assertThat(subset.values ().get ("g3", "sc"), equalTo (3.3));
    assertThat(subset.values ().skipJson (), equalTo(false));
    
    thrown.expect (InvalidCoordinateException.class);
    subset.values ().get ("g1", "sa");    
    thrown.expect (InvalidCoordinateException.class);
    subset.values ().get ("g1", "sd");
    thrown.expect (InvalidCoordinateException.class);
    subset.values ().get ("g2", "sa");
    thrown.expect (InvalidCoordinateException.class);
    subset.values ().get ("g2", "sd");    
    thrown.expect (InvalidCoordinateException.class);
    subset.values ().get ("g3", "sa");
    thrown.expect (InvalidCoordinateException.class);
    subset.values ().get ("g3", "sd");
    thrown.expect (InvalidCoordinateException.class);
    subset.values ().get ("g4", "sa");
    thrown.expect (InvalidCoordinateException.class);
    subset.values ().get ("g4", "sd");
        
  }

}
