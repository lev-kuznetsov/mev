package edu.dfci.cccb.mev.annotation.domain.probe.jooq;

import static org.jooq.impl.DSL.using;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.SelectQuery;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotation;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.Tables;
import edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.tables.records.MevProbeAnnotationsRecord;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import static edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.Tables.*;

@Log4j
public class JooqProbeAnnotations implements ProbeAnnotations {

  
  private final DSLContext context;
  
  @Inject 
  public JooqProbeAnnotations (@Named("probe-annotations-datasource") DataSource dataSource) throws SQLException {
    context = using (dataSource.getConnection ()); 
  }

  @Override
  public List<ProbeAnnotation> get (Dimension dimension) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public InputStream getAsStream (Dimension dimension) {
    
//    SelectQuery<Tables> query = context.selectQuery()
            
    InputStream input=null;
    ResultQuery<MevProbeAnnotationsRecord> query = context.selectFrom(Tables.MEV_PROBE_ANNOTATIONS)
            .where(Tables.MEV_PROBE_ANNOTATIONS.PROBESET_ID.in (dimension.keys ()));
     log.debug(query.toString ());
     String csv = query.fetch().formatCSV('\t');
     try {
      input = new ByteArrayInputStream (csv.getBytes ("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return input;
  }

}
