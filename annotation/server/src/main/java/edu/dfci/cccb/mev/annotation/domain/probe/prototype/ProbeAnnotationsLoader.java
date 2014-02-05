package edu.dfci.cccb.mev.annotation.domain.probe.prototype;

import static org.jooq.impl.DSL.using;
import static edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.Tables.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.Tables;
import edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.tables.MevProbeAnnotations;

public class ProbeAnnotationsLoader {

  private final DSLContext context;
  
  @Inject
  public ProbeAnnotationsLoader ( @Named("probe-annotations-datasource") DataSource dataSource) throws SQLException {
    context = using (dataSource.getConnection ());      
  }
  
  public void loadUrlResource(URL url) throws IOException{
    context.loadInto(MEV_PROBE_ANNOTATIONS)
    .onDuplicateKeyError ()
    .loadCSV(url.openStream ())
    .fields(MEV_PROBE_ANNOTATIONS.fields ())
    .separator ('\t')
    .ignoreRows (1)    
    .execute();
  }

}
