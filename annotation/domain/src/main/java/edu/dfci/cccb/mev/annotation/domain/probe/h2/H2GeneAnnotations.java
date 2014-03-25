package edu.dfci.cccb.mev.annotation.domain.probe.h2;

import javax.inject.Named;
import javax.sql.DataSource;

import edu.dfci.cccb.mev.annotation.domain.probe.prototype.AbstractH2Annotations;

public class H2GeneAnnotations extends AbstractH2Annotations {

  public H2GeneAnnotations (String platformId,  @Named("probe-annotations-datasource") DataSource dataSource) {
    super (platformId, dataSource);
  }

  @Override
  protected String getSelectStatement () {
    return "select * from table(GENE_SYMBOL VARCHAR=?) t "
                    + "left outer join PUBLIC.\""+getFullTableName ()+"\" annotations on t.GENE_SYMBOL=annotations.Symbol";
  }

  @Override
  protected String getTableNamePrefix () {
    return "GENE_ANNOT_";
  }

  @Override
  protected String getCreateTableSql () {
    return "CREATE CACHED TABLE IF NOT EXISTS PUBLIC.\""+getFullTableName()+"\"("+
                    "Symbol VARCHAR,"+
                    "Name VARCHAR,"+
                    "GO_biological_process VARCHAR,"+
                    "GO_cellular_component VARCHAR,"+
                    "GO_molecular_function VARCHAR,"+
                    ")";
  }

  @Override
  protected String getUniqueIdColumnName () {
    return "Symbol";
  }
}
