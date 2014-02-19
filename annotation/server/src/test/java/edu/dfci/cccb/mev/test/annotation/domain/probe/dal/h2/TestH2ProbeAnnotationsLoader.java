package edu.dfci.cccb.mev.test.annotation.domain.probe.dal.h2;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConfigTest.class})
public class TestH2ProbeAnnotationsLoader {
  
  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  @Test @Ignore
  public void testLoadUrlResourceLocal () throws SQLException, IOException, AnnotationException {
    URL url = new URL ("file://"+System.getProperty ("user.home")+"/mev/data/array_annotations/from_affymetrix/annotation_files/HG-U133A.na33.annot.out.tsv");
    assertNotNull (url);
    
    ProbeAnnotationsLoader loader = new H2ProbeAnnotationsLoader (dataSource);
    loader.loadUrlResource (url);
  }
    
  @Test 
  public void testLoadUrlResource () throws SQLException, IOException, AnnotationException {
    URL url = TestH2ProbeAnnotationsLoader.class.getResource ("/array_annotations/from_affymetrix/annotation_files/HT_HG-U133A.na33.top3.annot.out.tsv");
    assertNotNull (url);
    
    ProbeAnnotationsLoader loader = new H2ProbeAnnotationsLoader (dataSource);
    
    loader.loadUrlResource (url);
  }
  
  @Test 
  public void testInit_Force() throws SQLException, IOException, URISyntaxException, AnnotationException{
    
    //this folder has a file in it which tells the loader to reload the database: 'reload.flag'
    URL url = TestH2ProbeAnnotationsLoader.class.getResource ("/array_annotations/from_affymetrix_force/annotation_files/");
    assertNotNull (url);
    
    ProbeAnnotationsLoader loader = new H2ProbeAnnotationsLoader (dataSource);
    URL fileUrl = new URL(url, "HT_HG-U133A.na33.top3.annot.out.tsv");
    assertNotNull (fileUrl);    
    
    assert(loader.loadAll (url, "*.annot.out.tsv")>0);    
  }
  
  
}






