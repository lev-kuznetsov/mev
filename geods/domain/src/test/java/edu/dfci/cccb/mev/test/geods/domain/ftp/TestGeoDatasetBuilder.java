package edu.dfci.cccb.mev.test.geods.domain.ftp;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.ParserFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.gzip.GzipTsvInput;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.goeds.domain.contract.GeoSource;
import edu.dfci.cccb.mev.goeds.domain.dataset.ftp.GeoDatasetBuilderFTP;
import edu.dfci.cccb.mev.goeds.domain.dataset.ftp.GeoSourceFTP;

public class TestGeoDatasetBuilder {

  @Test
  public void testBuildRawInput () throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException, IOException {
    Map<String, String> samples = new HashMap<String, String> ();
    samples.put ("a", "aa");
    samples.put ("b", "bb");
    samples.put ("c", "cc");
    URL root = this.getClass ().getResource ("root/");
    GeoSource geoSource = new GeoSourceFTP ("4092", "570", samples, root);
    RawInput gdsInput = new GzipTsvInput(geoSource.getDatasetUrl ());
    GeoDatasetBuilderFTP builder = new GeoDatasetBuilderFTP ();
    builder.setValueStoreBuilder (new FlatFileValueStoreBuilder());
    builder.setParserFactories (new ArrayList<ParserFactory> (){
      private static final long serialVersionUID = 1L;
      {
          add(new SuperCsvParserFactory ());          
      }
    });
    Dataset dataset = builder.setGeoSource (geoSource).build (gdsInput);
    assertNotNull ((dataset.dimension (Type.ROW).annotation ()));
    
    
  }

}
