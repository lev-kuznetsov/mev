package edu.dfci.cccb.mev.annotation.domain.probe.prototype;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Named;
import javax.sql.DataSource;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.apache.commons.io.FilenameUtils;
import org.h2.tools.Csv;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

@Log4j
public abstract class AbstractH2Annotations extends AbstractProbeAnnotations {
  
  private final String PARAM_TABLE_NAME="[table_name]";
  private final String FULL_TABLE_NAME=getTableNamePrefix ()+PARAM_TABLE_NAME;
  private final static String PARAM_FILE_PATH="[file_path]";
  
  //private final static String TRUNCATE_TABLE_STATEMENT="TRUNCATE TABLE PUBLIC.\""+FULL_TABLE_NAME+"\"";
  private final String DROP_TABLE_STATEMENT="DROP TABLE IF EXISTS PUBLIC.\""+getFullTableName()+"\"";    
  private final String INSERT_STATEMENT 
  = "INSERT INTO PUBLIC.\""+getFullTableName()+"\" "+
    "SELECT * FROM CSVREAD('"+PARAM_FILE_PATH+"', null, 'charset=UTF-8 fieldSeparator='||chr(9))";  
  private final String CREATE_INDEX_STATEMENT
  ="CREATE UNIQUE INDEX IF NOT EXISTS \""+getFullTableName()+"_PK\" ON PUBLIC.\""+getFullTableName()+"\"("+getUniqueIdColumnName()+");";
  

  private final DataSource dataSource; 
  
  public AbstractH2Annotations (String platformId, @Named("probe-annotations-datasource") DataSource dataSource) {
    super (platformId);  
    this.dataSource=dataSource;
  }

  protected abstract String getSelectStatement();
  protected abstract String getTableNamePrefix();
  protected abstract String getCreateTableSql();
  protected abstract String getUniqueIdColumnName();
  
  protected String getFullTableName(){
    return FULL_TABLE_NAME;
  }
  @Override
  @SneakyThrows
  public InputStream getAsStream (Dimension dimension) {
    
    InputStream input=null;
    
    try(Connection connection = dataSource.getConnection ()){
      String selectSql = getSelectStatement ().replace (PARAM_TABLE_NAME,  this.platformId ());
      if(log.isDebugEnabled ()){
        log.debug ("Select Probe Annotations:" + selectSql);
        log.debug ("dimension.keys().toArray(): " + dimension.keys ().toArray ());
      }
      try(PreparedStatement prep = connection.prepareStatement(selectSql);){        
        
        prep.setObject (1, dimension.keys ().toArray ());        
        try(ResultSet rs = prep.executeQuery();){
          Csv csv = new Csv();
          csv.setFieldSeparatorWrite ("\t");          
          StringWriter writer = new StringWriter();
          csv.write (writer, rs);
          
          byte[] barray = writer.toString().getBytes("UTF-8");
          input = new ByteArrayInputStream(barray);
        }
      }
    }    
    return input;
  }

  @Override   
  public void loadUrlResource (URL url) throws AnnotationException {
      try(Connection connection = dataSource.getConnection ()){
        
        String tableName = FilenameUtils.getName (url.getPath ());        
        
        log.debug ("****Loading Annotations file: " + tableName);
        
        String dropTableSql = DROP_TABLE_STATEMENT.replace (PARAM_TABLE_NAME, tableName);
        String createTableSql = getCreateTableSql().replace (PARAM_TABLE_NAME, tableName);
        
        String insertTableSql = INSERT_STATEMENT.replace (PARAM_TABLE_NAME, tableName).replace (PARAM_FILE_PATH, url.toString ());
        
        String createIndexSql = CREATE_INDEX_STATEMENT.replace (PARAM_TABLE_NAME, tableName);
        if(log.isDebugEnabled ())
          log.debug ("****Creating table: " + createIndexSql);
        
        try(Statement statement = connection.createStatement ()){
          statement.execute (dropTableSql);
          statement.execute (createTableSql);
          statement.execute (insertTableSql);
          statement.execute (createIndexSql);
        }catch(SQLException e){
          connection.rollback ();
          throw new AnnotationException("Failed whle importing probe annotation url: " + url, e);
        }
        connection.commit ();
        
      } catch (SQLException e) {
        throw new AnnotationException ("Sql Error while loading resource:"+url, e);
      }
  }
}
