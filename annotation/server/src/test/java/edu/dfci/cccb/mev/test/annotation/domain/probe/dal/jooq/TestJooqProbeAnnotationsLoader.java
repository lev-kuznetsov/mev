package edu.dfci.cccb.mev.test.annotation.domain.probe.dal.jooq;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.prototype.JooqProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistanceConriguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConriguration.class})
public class TestJooqProbeAnnotationsLoader {
  
  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  @Test
  public void testLoadUrlResource () throws SQLException, IOException {
    URL url = TestJooqProbeAnnotationsLoader.class.getResource ("/array_annotations/AG.na33.annot.out.tsv");
    assertNotNull (url);
    
    ProbeAnnotationsLoader loader = new ProbeAnnotationsLoader (dataSource);
    
    loader.loadUrlResource (url);
  }

}
