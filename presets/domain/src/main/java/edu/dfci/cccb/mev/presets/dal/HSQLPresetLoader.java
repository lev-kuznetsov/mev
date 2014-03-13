package edu.dfci.cccb.mev.presets.dal;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sound.sampled.TargetDataLine;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eobjects.metamodel.DataContext;
import org.eobjects.metamodel.UpdateCallback;
import org.eobjects.metamodel.UpdateScript;
import org.eobjects.metamodel.UpdateableDataContext;
import org.eobjects.metamodel.convert.Converters;
import org.eobjects.metamodel.create.ColumnCreationBuilder;
import org.eobjects.metamodel.create.CreateTable;
import org.eobjects.metamodel.create.CreateTableColumnBuilder;
import org.eobjects.metamodel.create.TableCreationBuilder;
import org.eobjects.metamodel.csv.CsvDataContext;
import org.eobjects.metamodel.jdbc.JdbcDataContext;
import org.eobjects.metamodel.query.Query;
import org.eobjects.metamodel.schema.Column;
import org.eobjects.metamodel.schema.ColumnType;
import org.eobjects.metamodel.schema.Table;

import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.dal.metamodel.FoolProofStringToDoubleConverter;
import edu.dfci.cccb.mev.presets.dal.metamodel.MetaModelHelper;
import edu.dfci.cccb.mev.presets.dal.metamodel.MetaModelHelper.ColumnTypeDescriptor;
import edu.dfci.cccb.mev.presets.dal.metamodel.MetaModelHelper.CreateTableScript;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetValuesLoader;

@Log4j
public class HSQLPresetLoader extends AbstractPresetValuesLoader {

  final private String INDEX_COL_NAME = "COLUMN0";
  final private boolean reloadFlag;
  final private int batchSize;
  
  private @Getter @Setter @Inject @Named("presets-datasource") DataSource dataSource;
  private MetaModelHelper metaModelHelper;
  private UpdateableDataContext dbDataContext;
  
  public HSQLPresetLoader (DataSource dataSource) {
    this(dataSource, false, 1);
  }
  public HSQLPresetLoader(DataSource dataSource, boolean reloadFlag, int batchSize){
    this.dataSource=dataSource;
    this.metaModelHelper=new MetaModelHelper ();
    this.dbDataContext = new JdbcDataContext(dataSource);
    this.reloadFlag=reloadFlag;
    this.batchSize=batchSize;
  }
  
  private String formatFilePath(String filePath){
     return filePath.replaceFirst ("[/]{2,}", "/");
  }
  private void executeStatement(Statement statement, String sql) throws PresetException{    
      try {
        if(log.isDebugEnabled ())
          log.debug (sql);
        statement.execute (sql);
      } catch (SQLException e) {
        throw new PresetException("Error while executing sq: " + sql, e);
      }    
  }
  
  @Override @SneakyThrows(SQLException.class)
  public void load (URL url) throws PresetException{
    
    CsvDataContext csvDataContext = metaModelHelper.createCsvContext (url);
    Table csvTable = metaModelHelper.getCsvTable (csvDataContext);    
    Column indexColumn = csvTable.getColumn (0);
    boolean tableDroppedFlag=false;
    String tableName = getTableName(url);
    if(metaModelHelper.exists (dbDataContext, tableName)){
      if(this.reloadFlag){
        log.info ("Dropping table "+tableName);
        metaModelHelper.dropTable (dbDataContext, tableName);
        tableDroppedFlag=true;
      }else{
        log.info ("Table "+tableName+" already exists, skipping import");
        return;
      }
    }
    String textTableName = tableName+".text";
    if(metaModelHelper.exists (dbDataContext, textTableName)){
      log.info ("Dropping table "+textTableName);
      metaModelHelper.dropTable (dbDataContext, textTableName);
      tableDroppedFlag=true;    
    }
        
//    create schema    
//    Table targetTable = metaModelHelper.createTable (dbDataContext, csvDataContext, csvTable, tableName, getColumnTypeMapperVarchar (csvDataContext, csvTable));    
//    String filePath = url.getPath ().replace (url.getProtocol (), "").replaceAll ("//", "res:");
    String filePath = formatFilePath(url.getPath ());
    
    try(Connection conn = dataSource.getConnection ()){
      try(Statement statement = conn.createStatement ()){
      
        statement.execute ("CHECKPOINT");
        statement.execute (("SET FILES LOG FALSE"));
        
        MetaModelHelper.CreateTableScript creatScript = metaModelHelper.createTableSQL (dbDataContext, csvDataContext, csvTable, "\""+textTableName+"\"", getColumnTypeMapperVarchar (csvDataContext, csvTable));      
        String createTextTable = creatScript.getSql ().replace ("CREATE TABLE", "CREATE TEXT TABLE");      
        executeStatement (statement, createTextTable);        
        
        creatScript = metaModelHelper.createTableSQL (dbDataContext, csvDataContext, csvTable, "\""+tableName+"\"", getColumnTypeMapperVarchar (csvDataContext, csvTable));              
        String createTable = creatScript.getSql ().replace ("CREATE TABLE", "CREATE CACHED TABLE"); 
        executeStatement (statement, createTable);        
                
        String setTextTableONSql = "SET TABLE PUBLIC.\""+textTableName+"\" SOURCE \""+filePath+";fs=\\t;ignore_first=true;cache_rows=1000;cache_size=100\"";
        executeStatement (statement, setTextTableONSql);
        
        String insertSql = "INSERT INTO \""+tableName+"\" (SELECT * FROM \""+textTableName+"\")";
        executeStatement (statement, insertSql);
        
        String setTextTableOFFSql = "SET TABLE \""+textTableName+"\" SOURCE OFF"; 
        executeStatement (statement, setTextTableOFFSql);
        
        String dropTextTable = "DROP TABLE \""+textTableName+"\""; 
        executeStatement (statement, dropTextTable);        

        String createIndex = "CREATE UNIQUE INDEX \""+tableName+"\" ON \""+tableName+"\"(COLUMN0)";
        executeStatement (statement, createIndex);
                
//        metaModelHelper.insertTableData (dbDataContext, targetTable, csvDataContext, csvTable);
        
        statement.execute ("SET FILES LOG TRUE");
        statement.execute ("CHECKPOINT DEFRAG");
        
      }catch(Exception e){
        if(conn!=null){
          if(!conn.isClosed ()){
              Statement statement = conn.createStatement ();
              statement.execute ("CHECKPOINT DEFRAG");
              conn.close ();
          }          
          throw new PresetException ("Error while SHUTDOWN IMMEDIATELE", e);
        }
      }
    }
    
  }
  
  @Override
  protected String getTableNamePrefix () {
    return "PRESET-";
  }
  
  protected Map<String, ColumnTypeDescriptor> getColumnTypeMapperDouble(DataContext sourceDataContext, Table sourceTable){
    LinkedHashMap<String, ColumnTypeDescriptor> mapper = new LinkedHashMap<String, ColumnTypeDescriptor>(sourceTable.getColumnCount ());
    mapper.put (INDEX_COL_NAME, new MetaModelHelper.ColumnTypeDescriptor (INDEX_COL_NAME, ColumnType.VARCHAR, 255, true));
    for(int i=1; i<sourceTable.getColumnCount (); i++){
      Column column = sourceTable.getColumns ()[i];
      mapper.put (column.getName (), new MetaModelHelper.ColumnTypeDescriptor (column.getName (), ColumnType.DOUBLE, null, false));
      Converters.addTypeConverter(sourceDataContext, column, new FoolProofStringToDoubleConverter());
    }
    return mapper;            
  }
  
  protected Map<String, ColumnTypeDescriptor> getColumnTypeMapperVarchar(DataContext sourceDataContext, Table sourceTable){
    LinkedHashMap<String, ColumnTypeDescriptor> mapper = new LinkedHashMap<String, ColumnTypeDescriptor>(sourceTable.getColumnCount ());
    mapper.put (INDEX_COL_NAME, new MetaModelHelper.ColumnTypeDescriptor (INDEX_COL_NAME, ColumnType.VARCHAR, 255, true));
    for(int i=1; i<sourceTable.getColumnCount (); i++){
      Column column = sourceTable.getColumns ()[i];
      mapper.put (column.getName (), new MetaModelHelper.ColumnTypeDescriptor (column.getName (), ColumnType.VARCHAR, 255, false));
      Converters.addTypeConverter(sourceDataContext, column, new FoolProofStringToDoubleConverter());
    }
    return mapper;            
  }
}
