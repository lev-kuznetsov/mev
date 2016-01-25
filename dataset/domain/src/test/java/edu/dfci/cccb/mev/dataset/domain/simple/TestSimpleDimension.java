package edu.dfci.cccb.mev.dataset.domain.simple;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnnotation;

public class TestSimpleDimension {

  
  @Test
  public void testSubset () {
    List<String> keys = new ArrayList<String> ();
    keys.add("a");
    keys.add("b");
    keys.add("c");
    keys.add("d");
      
    Dimension parent = new SimpleDimension (Type.COLUMN, keys, new ArrayListSelections (), new AbstractAnnotation() {});
    Dimension subset = parent.subset (new ArrayList<String>(){
                                              private static final long serialVersionUID = 1L;
                                              {
                                                add("b");
                                                add("c");
                                      }});
    assertThat (subset.keys ().size (), is(2));
    assertThat (subset.keys ().get (0), is("b"));
    assertThat (subset.keys ().get (1), is("c"));
    
  }

}
