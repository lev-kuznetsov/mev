package edu.dfci.cccb.mev.annotation.domain.probe.h2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Named;
import javax.sql.DataSource;

import lombok.SneakyThrows;

import org.apache.commons.io.FilenameUtils;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationSource;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationSources;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;

public class H2ProbeAnnotationsLoader implements ProbeAnnotationsLoader {

  private Path rootPath;  
  private final DataSource dataSource;
  
  public final static String TABLE_NAME_PREFIX="PROBE_ANNOT_";  
  private final static String PARAM_TABLE_NAME="[table_name]";
  private final static String FULL_TABLE_NAME=TABLE_NAME_PREFIX+PARAM_TABLE_NAME;
  private final static String PARAM_FILE_PATH="[file_path]";
  
//  private final static String TRUNCATE_TABLE_STATEMENT="TRUNCATE TABLE PUBLIC.\""+FULL_TABLE_NAME+"\"";
  private final static String DROP_TABLE_STATEMENT="DROP TABLE IF EXISTS PUBLIC.\""+FULL_TABLE_NAME+"\"";
  private final static String CREATE_TABLE_STATEMENT
  ="CREATE CACHED TABLE IF NOT EXISTS PUBLIC.\""+FULL_TABLE_NAME+"\"("+
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
  private final static String INSERT_STATEMENT 
  = "INSERT INTO PUBLIC.\""+FULL_TABLE_NAME+"\" "+
    "SELECT * FROM CSVREAD('"+PARAM_FILE_PATH+"', null, 'charset=UTF-8 fieldSeparator='||chr(9))";  
  private final static String CREATE_INDEX_STATEMENT
  ="CREATE UNIQUE INDEX IF NOT EXISTS \""+FULL_TABLE_NAME+"_PK\" ON PUBLIC.\""+FULL_TABLE_NAME+"\"(PROBESET_ID);";
  
  
  public H2ProbeAnnotationsLoader (@Named("probe-annotations-datasource") DataSource dataSource){
    this.dataSource=dataSource;    
  }
  
  @Override
  public int loadAll (URL rootFolder, String suffix) throws AnnotationException {
    int count=0;
    
    try{
      rootPath = Paths.get(rootFolder.toURI ());
      if(rootPath==null)
        throw new IOException ("Root Folder "+rootFolder.toURI ()+" not found");
      
      try(DirectoryStream<Path> ds = Files.newDirectoryStream (rootPath, suffix)){
        for(Path path : ds){
          loadUrlResource (path.toUri ().toURL ());
          count++;
        }
      }
    }catch(URISyntaxException | IOException e){
      throw new AnnotationException(e);
    }
    return count;
  }
  
  @Override
  public int loadAll (ProbeAnnotationSources probeAnnotationSources) throws AnnotationException {
    int count=0;
    for(ProbeAnnotationSource probeAnnotationSource : probeAnnotationSources.getAll ()){
        loadUrlResource (probeAnnotationSource.url ());
        count++;
    }
    return count;
  }


  @Override
  @SneakyThrows  
  public void loadUrlResource (URL url) throws AnnotationException {
      try(Connection connection = dataSource.getConnection ()){
        
        
        String tableName = FilenameUtils.getName (url.getPath ());
        String dropTableSql = DROP_TABLE_STATEMENT.replace (PARAM_TABLE_NAME, tableName);
        String createTableSql = CREATE_TABLE_STATEMENT.replace (PARAM_TABLE_NAME, tableName);
        
        Path filePath = Paths.get (url.toURI());
        String insertTableSql = INSERT_STATEMENT.replace (PARAM_TABLE_NAME, tableName).replace (PARAM_FILE_PATH, filePath.toAbsolutePath ().toString ());
        
        String createIndexSql = CREATE_INDEX_STATEMENT.replace (PARAM_TABLE_NAME, tableName);
        
        try(Statement statement = connection.createStatement ()){
          statement.execute (dropTableSql);
          statement.execute (createTableSql);
          statement.execute (insertTableSql);
          statement.execute (createIndexSql);
        }catch(SQLException e){
          connection.rollback ();
          throw e;
        }
        connection.commit ();
      }
  }
}
