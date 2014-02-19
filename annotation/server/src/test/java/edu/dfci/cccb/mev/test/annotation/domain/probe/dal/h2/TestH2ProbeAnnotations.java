package edu.dfci.cccb.mev.test.annotation.domain.probe.dal.h2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistenceConfiguration;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;
import static org.hamcrest.core.Is.is;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConfigTest.class})
public class TestH2ProbeAnnotations {

  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  @Before
  public void loadTestAnnotations() throws AnnotationException, URISyntaxException, IOException{
    URL url = TestH2ProbeAnnotationsLoader.class.getResource ("/array_annotations/from_affymetrix/annotation_files/");
    assertNotNull (url);    
    ProbeAnnotationsLoader loader = new H2ProbeAnnotationsLoader (dataSource);    
    loader.loadAll (url, "*.annot.out.tsv");
  }
  
  @Test @Ignore
  public void testGetAsStream () throws SQLException, IOException {
    
    List<String> keys = new ArrayList<String> (){
      {
        add("1007_s_at");
        add("1053_at");
        add("13029_at");
      }
    };
    Dimension dimension = new SimpleDimension(Type.ROW, keys, new ArrayListSelections (), null );
    
    ProbeAnnotations probeAnns = new H2ProbeAnnotations(dataSource);
    InputStream input = probeAnns.getAsStream (dimension, "HT_HG-U133A.na33.top3.annot.out.tsv");
    
    StringWriter writer = new StringWriter();
    IOUtils.copy(input, writer, "UTF-8");
    String theString = writer.toString();
    log.debug ("Probes: " + theString);
  }

  @Test 
  public void testGetSources () throws SQLException, IOException, AnnotationException {

//    URL url = TestH2ProbeAnnotationsLoader.class.getResource ("/array_annotations/from_affymetrix/annotation_files/");
//    assertNotNull (url);    
//    ProbeAnnotationsLoader loader = new H2ProbeAnnotationsLoader (dataSource);    
//    loader.loadAll (url, "*.annot.out.tsv");

    List<String> keys = new ArrayList<String> (){
      {
        add("1007_s_at");
        add("1053_at");
        add("13029_at");
      }
    };
    Dimension dimension = new SimpleDimension(Type.ROW, keys, new ArrayListSelections (), null );    
    ProbeAnnotations probeAnns = new H2ProbeAnnotations(dataSource);
    List<String> sourceNames = probeAnns.getSources ();
    List<String> expectedSourceNames = new ArrayList<String>(){
      {
        add("HT_HG-U133A.na33.top3.annot.out.tsv");
      }
    };
    assertThat(sourceNames, is(expectedSourceNames));
    log.debug (sourceNames.toString ());
  }
}
