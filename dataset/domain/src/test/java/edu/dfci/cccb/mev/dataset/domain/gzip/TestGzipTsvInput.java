package edu.dfci.cccb.mev.dataset.domain.gzip;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Parser;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;

public class TestGzipTsvInput {

  @Test
  public void testInput () throws IOException, DatasetBuilderException, InvalidDimensionTypeException {
    RawInput gzip = new GzipTsvInput (this.getClass ().getResource ("geo.gds.soft.txt.gz"));
    Parser p = new SuperCsvParserFactory ().parse (gzip.input ());
    while(p.next ()) {
//      log.debug (p.value () + ":" + p.projection (ROW)+ ":" + p.projection (COLUMN));
    }
    List<String> expectedRows = new ArrayList<String>(){
      private static final long serialVersionUID = 1L;
      {      
        add("1007_s_at");
        add("1053_at");
        add("117_at");
      }
    };
    assertFalse(p.next ());
    assertThat(p.rowKeys (), is(expectedRows));
    
    List<String> expectedColumns = Arrays.asList ("GSM564960\tGSM564961\tGSM564952\tGSM564958\tGSM564953\tGSM564959\tGSM564956\tGSM564957\tGSM564950\tGSM564954\tGSM564951\tGSM564955".split ("\t"));
    assertThat(p.columnKeys (), is(expectedColumns));
    
  }

}
