package edu.dfci.cccb.mev.dataset.domain.gzip;

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;
import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.log4j.Log4j;

import org.apache.commons.io.input.ReaderInputStream;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Parser;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParser;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;

@Log4j
public class TestGzipTsvInput {

  @Test
  public void testInput () throws IOException, DatasetBuilderException, InvalidDimensionTypeException {
    String url ="ftp://ftp.ncbi.nlm.nih.gov/geo/datasets/GDS4nnn/GDS4092/soft/GDS4092.soft.gz";
//    String url = "ftp://ftp.ncbi.nih.gov/pub/geo/DATA/SOFT/GDS/GDS4092.soft.gz";
    RawInput gzip = new GzipTsvInput (new URL(url));
    Parser p = new SuperCsvParserFactory ().parse (gzip.input ());
    while(p.next ()) {
      log.debug (p.value () + ":" + p.projection (ROW)+ ":" + p.projection (COLUMN));
    }
    
  }

}
