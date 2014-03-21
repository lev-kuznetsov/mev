package edu.dfci.cccb.mev.annotation.domain.probe.h2;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.AbstractH2Annotations;

public class H2ProbeAnnotations extends AbstractH2Annotations {
  
  
  @Inject 
  public H2ProbeAnnotations (String platformId, @Named("probe-annotations-datasource") DataSource dataSource) throws SQLException {
    super(platformId, dataSource);
  }
  
  @Override
  protected String getSelectStatement () {
    return "select * from table(PROBESET_ID VARCHAR=?) t "
                    + "left outer join PUBLIC.\""+getFullTableName ()+"\" annotations on t.PROBESET_ID=annotations.PROBESET_ID";
  }

  @Override
  protected String getTableNamePrefix () {
    return "PROBE_ANNOT_";
  }

  @Override
  protected String getCreateTableSql () {
    return "CREATE CACHED TABLE IF NOT EXISTS PUBLIC.\""+getFullTableName()+"\"("+
                    "CHIP_VENDOR VARCHAR,"+
                    "CHIP_TYPE VARCHAR,"+
                    "CREATE_DATE VARCHAR,"+
                    "genome_species VARCHAR,"+
                    "genome_version VARCHAR,"+
                    "netaffx_annotation_netaffx_build VARCHAR,"+
                    "PROBESET_ID VARCHAR PRIMARY KEY,"+
                    "GENE_SYMBOL VARCHAR,"+
                    "GENE_DESC VARCHAR,"+
                    "CHR_LOCATION VARCHAR,"+
                    "STRAND VARCHAR,"+
                    "REFSEQ_ACCN VARCHAR)";
  }
  
  @Override
  protected String getUniqueIdColumnName () {
    return "PROBESET_ID";
  }

}
