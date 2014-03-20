package edu.dfci.cccb.mev.test.annotation.domain.probe.dal.h2;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatforms;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConfigTest.class})
public class TestH2ProbeAnnotations {

  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  @Inject ProbeAnnotationPlatforms probeAnnotationPlatforms;
  
  @Before
  public void loadTestAnnotations() throws AnnotationException, URISyntaxException, IOException{
    ProbeAnnotationsLoader loader = new H2ProbeAnnotationsLoader (dataSource);    
    loader.loadAll (probeAnnotationPlatforms);
  }
  
  @Test 
  public void testGetAsStream () throws SQLException, IOException {
    
    List<String> keys = new ArrayList<String> (){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      {
        add("1007_s_at");
        add("1053_at");
        add("13029_at"); //not in the annotation file
      }
    };
    Dimension dimension = new SimpleDimension(Type.ROW, keys, new ArrayListSelections (), null );
    
    ProbeAnnotations probeAnns = new H2ProbeAnnotations("HT_HG-U133A.na33.top3.annot.out.tsv", dataSource);
    InputStream input = probeAnns.getAsStream (dimension);
    
    StringWriter writer = new StringWriter();
    IOUtils.copy(input, writer, "UTF-8");
    String theString = writer.toString();
    log.debug ("Probes: " + theString);
  }

}
