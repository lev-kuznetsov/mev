package edu.dfci.cccb.mev.test.annotation.domain.probe.dal.jooq;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.jooq.JooqProbeAnnotations;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistenceConfiguration;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConfigTest.class})
public class TestJooqProbeAnnotations {

  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  @Test
  public void testGetAsStream () throws SQLException, IOException {
    List<String> keys = new ArrayList<String> (){
      {
        add("11986_at");
        add("12025_at");
        add("13029_at");
      }
    };
    Dimension dimension = new SimpleDimension(Type.ROW, keys, new ArrayListSelections (), null );
    
    JooqProbeAnnotations probeAnns = new JooqProbeAnnotations(dataSource);
    InputStream input = probeAnns.getAsStream (dimension);
    
    StringWriter writer = new StringWriter();
    IOUtils.copy(input, writer, "UTF-8");
    String theString = writer.toString();
    log.debug ("Probes: " + theString);
  }

}
