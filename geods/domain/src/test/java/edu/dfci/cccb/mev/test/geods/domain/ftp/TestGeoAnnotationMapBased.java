package edu.dfci.cccb.mev.test.geods.domain.ftp;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.AnnotationInputStream;
import edu.dfci.cccb.mev.goeds.domain.dataset.ftp.GeoAnnotationMapBased;

public class TestGeoAnnotationMapBased {

  @Test
  public void testGetInputStream () throws IOException {
    Map<String, String> samples = new LinkedHashMap<String, String> ();
    samples.put ("a", "aa");
    samples.put ("b", "bb");
    samples.put ("c", "cc"); 
    AnnotationInputStream annotation = new GeoAnnotationMapBased (samples);
    
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(annotation.getInputStream()))){
      assertThat(reader.readLine(), is("ID\tTitle"));
      assertThat(reader.readLine(), is("a\taa"));
      assertThat(reader.readLine(), is("b\tbb"));
      assertThat(reader.readLine(), is("c\tcc"));
      assertNull (reader.readLine());
    }
    
    
  }

}
