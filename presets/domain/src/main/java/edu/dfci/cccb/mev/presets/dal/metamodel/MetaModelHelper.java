package edu.dfci.cccb.mev.presets.dal.metamodel;

import java.net.URL;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.eobjects.metamodel.BatchUpdateScript;
import org.eobjects.metamodel.DataContext;
import org.eobjects.metamodel.UpdateCallback;
import org.eobjects.metamodel.UpdateScript;
import org.eobjects.metamodel.UpdateableDataContext;
import org.eobjects.metamodel.convert.StringToDoubleConverter;
import org.eobjects.metamodel.create.ColumnCreationBuilder;
import org.eobjects.metamodel.create.CreateTableColumnBuilder;
import org.eobjects.metamodel.create.TableCreationBuilder;
import org.eobjects.metamodel.csv.CsvConfiguration;
import org.eobjects.metamodel.csv.CsvDataContext;
import org.eobjects.metamodel.data.DataSet;
import org.eobjects.metamodel.data.Row;
import org.eobjects.metamodel.drop.TableDropBuilder;
import org.eobjects.metamodel.insert.RowInsertionBuilder;
import org.eobjects.metamodel.query.Query;
import org.eobjects.metamodel.query.SelectItem;
import org.eobjects.metamodel.schema.ColumnType;
import org.eobjects.metamodel.schema.Schema;
import org.eobjects.metamodel.schema.Table;

import edu.dfci.cccb.mev.presets.util.timer.Timer;

@Log4j
public class MetaModelHelper {

  public MetaModelHelper () {}

  public CsvDataContext createCsvContext(URL url){
    CsvConfiguration csvConfiguration = new CsvConfiguration (1, "UTF-8", '\t', '\"', '\\');
    CsvDataContext csvDataContext = new CsvDataContext (url, csvConfiguration);
    return csvDataContext;
  }

  public Table getCsvTable(CsvDataContext csvDataContext){
    //get the one and only table (csv)
    Schema csvSchema = csvDataContext.getDefaultSchema();
    Table[] tables = csvSchema.getTables();
    assert tables.length == 1;
    return tables[0];    
  }

  public Table getTableByName(DataContext dataContext, String tableName){
    Table table = dataContext.getTableByQualifiedLabel(tableName);
    return table;
  }
  
  public boolean exists(DataContext dataContext, String tableName){
    return getTableByName (dataContext, dataContext.getDefaultSchema ().getName ()+"."+tableName)==null ? false : true; 
  }
  
  public boolean dropTable(UpdateableDataContext targetDataContext, String targetTableName){
    DropTableScript updateScript = new DropTableScript(targetDataContext, targetTableName);   
    targetDataContext.executeUpdate(updateScript);
    return true;
  }
  
  class DropTableScript implements UpdateScript {
    
    final UpdateableDataContext targetDataContext; 
    final String targetTableName;
    
    public DropTableScript(UpdateableDataContext targetDataContext, String targetTableName){
      this.targetDataContext=targetDataContext; 
      this.targetTableName=targetTableName;     
    }
    public void run(UpdateCallback callback) {                
        //drop table        
        TableDropBuilder tdb = callback.dropTable(targetDataContext.getDefaultSchema(), targetTableName);
        tdb.execute();
                
      }
  }
  
  public Table createTable(UpdateableDataContext targetDataContext, DataContext sourceDataContext, Table theSourceTable, String targetTableName, 
                           Map<String, ColumnTypeDescriptor> columnTypeMapper){
        
    CreateTableScript updateScript = new CreateTableScript(targetDataContext, targetTableName, sourceDataContext, theSourceTable, columnTypeMapper);    
   
//    targetDataContext.executeUpdate(updateScript);
    return updateScript.getTable();
  }
  
  public CreateTableScript createTableSQL(UpdateableDataContext targetDataContext, DataContext sourceDataContext, Table theSourceTable, String targetTableName, 
                           Map<String, ColumnTypeDescriptor> columnTypeMapper){
        
    CreateTableScript updateScript = new CreateTableScript(targetDataContext, targetTableName, sourceDataContext, theSourceTable, columnTypeMapper);    
   
    targetDataContext.executeUpdate(updateScript);
//    return updateScript.getTable();
    return updateScript;
  }
  
  public static class CreateTableScript implements UpdateScript {
    
    final UpdateableDataContext targetDataContext; 
    final DataContext sourceDataContext;
    final Table sourceTable; 
    final String targetTableName;    
    final Map<String, ColumnTypeDescriptor> columnTypeMapper;
    private Table table;
    private @Getter String sql;
    
    public CreateTableScript(UpdateableDataContext targetDataContext, String targetTableName, DataContext sourceDataContext, 
                             Table sourceTable, Map<String, ColumnTypeDescriptor> columnTypeMapper){
      this.targetDataContext=targetDataContext; 
      this.sourceDataContext=sourceDataContext;
      this.sourceTable=sourceTable; 
      this.targetTableName=targetTableName;      
      this.columnTypeMapper=columnTypeMapper;
    }
    
    
    public Table getTable(){
      return table;
    }
    public void run(UpdateCallback callback) {                
        //create schema
        TableCreationBuilder tcb = callback.createTable(targetDataContext.getDefaultSchema(), targetTableName);
        
        // iterate through columns        
        for (ColumnTypeDescriptor columnDescriptor : columnTypeMapper.values ()) {         
         
          //create the column def          
          ColumnCreationBuilder ccb = tcb.withColumn(columnDescriptor.getQuotedName ());
          columnDescriptor.apply (ccb);
        }
        this.sql=tcb.toSql ();
//        this.table = tcb.execute();        
      }
  }
  
  @RequiredArgsConstructor
  public static class ColumnTypeDescriptor{
    private @Getter final String name;
    private @Getter final ColumnType type;
    private @Getter final Integer size;
    private @Getter final boolean isPrimary;
    public String getQuotedName(){
      
//      if (name.indexOf(' ') == -1 && name.indexOf('-') == -1) 
        return "\""+name+"\"";
//      else
//        return name;
          
    }
    public ColumnCreationBuilder apply(ColumnCreationBuilder ccb){
      
      ccb.ofType (type);
            
      //primary key
      if(isPrimary)
        ccb.nullable(false).asPrimaryKey ();
      else
        ccb.nullable (true);
      
      //datatype
      if(size!=null)
        ccb.ofSize(size);
      
      return ccb;
    }
    
    public CreateTableColumnBuilder apply(CreateTableColumnBuilder ccb){
      
      ccb.ofType (type);
            
      //primary key
      if(isPrimary)
        ccb.nullable(false).asPrimaryKey ();
      else
        ccb.nullable (true);
      
      //datatype
      if(size!=null)
        ccb.ofSize(size);
      
      return ccb;
    }
  }  
  
  public class FoolProofStringToDoubleConverter extends StringToDoubleConverter{
    
  }
  

  public boolean insertTableData(UpdateableDataContext targetDataContext, Table targetTable, DataContext sourceDataContext, Table sourceTable){
    return insertTableData (targetDataContext, targetTable, sourceDataContext, sourceTable, 1);       
  }
  public boolean insertTableData(UpdateableDataContext targetDataContext, Table targetTable, DataContext sourceDataContext, Table sourceTable, int batchSize){
    InsertTableScript updateScript = new InsertTableScript(targetDataContext, targetTable, sourceDataContext, sourceTable, batchSize);   
    targetDataContext.executeUpdate(updateScript);
    return true;
  }
  
  class InsertTableScript implements UpdateScript {
    
    final UpdateableDataContext targetDataContext; 
    final DataContext sourceDataContext;
    final Table sourceTable; 
    final Table targetTable;
    final int batchSize;
    
    public InsertTableScript(UpdateableDataContext targetDataContext, Table targetTable, DataContext sourceDataContext, Table sourceTable, int batchSize){
      this.targetDataContext=targetDataContext; 
      this.sourceDataContext=sourceDataContext;
      this.sourceTable=sourceTable; 
      this.targetTable=targetTable;
      this.batchSize=batchSize;
    }
    
    public InsertTableScript(UpdateableDataContext targetDataContext, Table targetTable, DataContext sourceDataContext, Table sourceTable){
      this(targetDataContext, targetTable, sourceDataContext, sourceTable, 1);
    }
    
    public void run(UpdateCallback callback) {             
      Timer timer = Timer.start ("bulk import " + sourceTable.getName ());
      //import data
      Query sourceQuery = sourceDataContext.query().from(sourceTable).selectAll().toQuery();
      
      DataSet ds=null;      
      try{
        ds = sourceDataContext.executeQuery(sourceQuery);
        try{
          while (ds.next()) {
            InsertTableScriptBatch updateScript = new InsertTableScriptBatch(targetDataContext, targetTable, sourceDataContext, sourceTable, ds, batchSize);   
            targetDataContext.executeUpdate(updateScript);   
          }
        }catch(IllegalStateException e){
          //end of file
        }
        
      }finally{
        if(ds!=null) ds.close();
        timer.read ();
      }
    }
  }
  
class InsertTableScriptBatch implements BatchUpdateScript {
    
    final UpdateableDataContext targetDataContext; 
    final DataContext sourceDataContext;
    final Table sourceTable; 
    final Table targetTable;
    final DataSet ds;
    final int batchSize;
    long totalCount=0;
    public InsertTableScriptBatch(UpdateableDataContext targetDataContext, Table targetTable, DataContext sourceDataContext, Table sourceTable, DataSet ds, int batchSize){
      this.targetDataContext=targetDataContext; 
      this.sourceDataContext=sourceDataContext;
      this.sourceTable=sourceTable; 
      this.targetTable=targetTable;
      this.ds=ds;
      this.batchSize=batchSize;
    }
    
    public void run(UpdateCallback callback) {             
            int count=0;
            do {              
              Row row = ds.getRow();
              RowInsertionBuilder rib = callback.insertInto(this.targetTable);
              // iterate through columns
              int columnCount=0;
              for (SelectItem si : row.getSelectItems()) {
                  String columnName = si.getColumn().getName();
                  if(columnName.equals(""))
                    columnName="COLUMN"+columnCount++;
                  rib = rib.value(columnName, row.getValue(si));
              }
              rib.execute();
              
              if(++count == batchSize){
                totalCount+=batchSize;
                if(log.isDebugEnabled ())
                  log.debug ("Insert count: "+totalCount);
                break;
              }
            }while(ds.next ());
          
        
    }
  }
  
}
