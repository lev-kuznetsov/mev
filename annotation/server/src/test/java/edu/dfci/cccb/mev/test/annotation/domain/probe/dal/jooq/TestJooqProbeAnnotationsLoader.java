package edu.dfci.cccb.mev.test.annotation.domain.probe.dal.jooq;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.domain.probe.jooq.JooqProbeAnnotationsLoader;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConfigTest.class})
public class TestJooqProbeAnnotationsLoader {
  
  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  @Test
  public void testLoadUrlResource () throws SQLException, IOException, AnnotationException {
    URL url = ProbeAnnotationsPersistanceConfigTest.class.getResource ("array_annotations/from_affymetrix/annotation_files/HT_HG-U133A.na33.top3.annot.out.tsv");
    assertNotNull (url);
    
    ProbeAnnotationsLoader loader = new JooqProbeAnnotationsLoader (dataSource);
    
    loader.loadUrlResource (url);
  }
  
  @Test
  public void testInit_Force() throws SQLException, IOException, URISyntaxException, AnnotationException{
    
    //this folder has a file in it which tells the loader to reload the database: 'reload.flag'
    URL url = ProbeAnnotationsPersistanceConfigTest.class.getResource ("array_annotations/from_affymetrix_force/annotation_files/");
    assertNotNull (url);
    
    ProbeAnnotationsLoader loader = new JooqProbeAnnotationsLoader (dataSource);
    URL fileUrl = new URL(url, "HT_HG-U133A.na33.top3.annot.out.tsv");
    log.debug ("************FILE-URL:"+fileUrl);
    assertNotNull (fileUrl);    
    int count = loader.loadAll (url, "*.annot.out.tsv");
    log.debug ("************RELOAD-COUNT:"+count);
    assertTrue (count>0);    
  }
  
  @Test
  public void testInit_NoReload() throws SQLException, IOException, URISyntaxException, AnnotationException{
    
    URL url = ProbeAnnotationsPersistanceConfigTest.class.getResource ("array_annotations/from_affymetrix/annotation_files/");
    assertNotNull (url);
    
    ProbeAnnotationsLoader loader = new JooqProbeAnnotationsLoader (dataSource);
    URL fileUrl = new URL (url, "HT_HG-U133A.na33.top3.annot.out.tsv");
    assertNotNull (fileUrl);    
    int count = loader.loadAll (url, "*.annot.out.tsv");
    log.debug ("reload count:"+count);
    assert(count==0);    
  }
}






