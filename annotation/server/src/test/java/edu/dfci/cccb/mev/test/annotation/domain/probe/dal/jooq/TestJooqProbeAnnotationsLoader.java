package edu.dfci.cccb.mev.test.annotation.domain.probe.dal.jooq;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.prototype.JooqProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.JooqProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistanceConriguration;
import edu.dfci.cccb.mev.annotation.support.FileChecker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConriguration.class})
public class TestJooqProbeAnnotationsLoader {
  
  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  @Test
  public void testLoadUrlResource () throws SQLException, IOException {
    URL url = TestJooqProbeAnnotationsLoader.class.getResource ("/array_annotations/AG.na33.annot.out.tsv");
    assertNotNull (url);
    
    JooqProbeAnnotationsLoader loader = new JooqProbeAnnotationsLoader (dataSource);
    
    loader.loadUrlResource (url);
  }

  @Test
  public void testInit() throws SQLException, IOException, URISyntaxException{
    
    URL url = TestJooqProbeAnnotationsLoader.class.getResource ("/array_annotations/");
    assertNotNull (url);
    
    
    JooqProbeAnnotationsLoader loader = new JooqProbeAnnotationsLoader (dataSource);
    URL fileUrl = TestJooqProbeAnnotationsLoader.class.getResource ("/array_annotations/changedFile.annot.out.tsv");
    assertNotNull (fileUrl);
    FileChecker.touch (Paths.get (fileUrl.toURI ()));
    
    loader.init (url, "*.annot.out.tsv", 2000L);    
  }
}
