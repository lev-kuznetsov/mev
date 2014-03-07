package edu.dfci.cccb.mev.test.presets.domain.dataset.flat.small;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.tableByName;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Select;
import org.jooq.Table;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Parser;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.simple.SimplePresetDescriptor;
import edu.dfci.cccb.mev.test.presets.configuration.persistence.flat.TestPresetsDatasetFlatTableConfig;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;
@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestJooqCursorLGGLevel2Configuration.class})
public class TestJooqCursorLGGLevel2 {


  
  @Inject @Named("presets-jooq-context") DSLContext context;
  @Inject Environment environment;
  
  private URL rootUrl=null;
  private final String ID_FIELD_NAME="COLUMN0";
  
  @PostConstruct
  public void init() throws MalformedURLException{
    this.rootUrl = new URL("file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/");
  }
  
  @Test @Ignore
  public void testSimpleSelectAll () {
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";
    Table<Record> table = tableByName (tsvFileName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME);

    long startTime = System.nanoTime();
    int count=0;
    ResultQuery<Record> query =context.selectFrom (table);            
  //  log.debug ("PresetValuesFlatTable sql:"+query.getSQL ());      
    Cursor<Record> cursor = null;
    try {
      cursor = context.selectFrom(table).fetchLazy();
      // Cursor has similar methods as Iterator<R>
      while (cursor.hasNext()) {
          Record record = cursor.fetchOne();
          for(Field<?> columnField: record.fields()){
            String value = (String)record.getValue (columnField);
            count++;
          }
      }
    }finally {
      if (cursor != null) {cursor.close();}
    }
    
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    log.debug ("flat-count:"+count+", duration: "+duration);
  }
  
  @Test @Ignore
  public void testSelect_AllFields () {
    log.debug("... Running testSelect_AllFields...");
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";

    List<String> rows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> columns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    int count = select2(tsvFileName, rows, columns);    
    log.debug ("flat-count:"+count);    
  }

  @Test @Ignore
  public void testSelect_AllFieldsShuffleKeys () {
    log.debug("... testSelect_AllFieldsShuffleKeys...");
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";


    List<String> rows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    Collections.shuffle(rows, new Random(System.nanoTime ()));
    List<String> columns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    Collections.shuffle(columns, new Random(System.nanoTime ()));
    int count = select2(tsvFileName, rows, columns);    
    log.debug ("flat-count:"+count);    
  }
  
  @Test @Ignore
  public void testSelect_OneFieldAllRows () {
    log.debug("... Running testSelect_OneFieldAllRows...");
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";

    List<String> rows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> allColumns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    List<String> columns = new ArrayList<String>(1);
    columns.add (allColumns.get (0));
    
    int count = select2(tsvFileName, rows, columns);    
    log.debug ("flat-count:"+count);    
  }

  @Test @Ignore
  public void testSelect_OneFieldOneRows () {
    log.debug("... Running testSelect_OneFieldOneRows...");
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";

    List<String> allRows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> allColumns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    List<String> columns = new ArrayList<String>(1);
    columns.add (allColumns.get (3));
    List<String> rows = new ArrayList<String>(1);
    rows.add(allRows.get (allRows.size ()-1));
    int count = select2(tsvFileName, rows, columns);    
    log.debug ("flat-count:"+count);    
  }

  @Test @Ignore
  public void testSelect_OneFieldOneRows_DoubleLoop () {
    log.debug("... testSelect_OneFieldOneRows_DoubleLoop...");
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";

    List<String> allRows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> allColumns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    int count=0;
    Timer timer = Timer.start ("double-for-loop");
    for(String curRow : allRows){
      List<String> rows = new ArrayList<String>(1);
      rows.add(allRows.get (allRows.size ()-1));
      for(String curColumn : allColumns){
        List<String> columns = new ArrayList<String>(1);
        columns.add (allColumns.get (3));
        
        count = select2(tsvFileName, rows, columns,false);            
      }
    }
    timer.read ();
    log.debug ("flat-count:"+count);
  }
  
  @Test 
  public void testSelect_SomeFieldSomeRows () {
    log.debug("... Running testSelect_SomeFieldSomeRows...");
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";

    List<String> allRows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> allColumns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    allColumns.remove (3);
    allRows.remove (4);
    int count = select2(tsvFileName, allRows, allColumns);    
    log.debug ("flat-count:"+count);    
  }

  private List<Field<String>> createFieldList(List<String> columns){
    List<Field<String>> selectFields = new ArrayList<Field<String>>(columns.size ());
    for(String columnName : columns)
      selectFields.add (fieldByName(String.class, columnName));

    return selectFields;
  }
  
  private ResultQuery<Record> queryAllRows(List<Field<String>> selectFields, Table<Record> table){
    return context.select(selectFields)
            .from(table)
            .getQuery ();    
  }
  
  private ResultQuery<Record> queryINRows(String tableName, List<Field<String>> selectFields, List<String> rows){
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME); 
    return context.select(selectFields)
            .from(table).where (fieldRowId.in (rows))
            .getQuery ();    
  }
  
  
  private ResultQuery<Record> queryRowByID(String tableName, List<Field<String>> selectFields, String row){
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME); 
    return context.select(selectFields)
            .from(table).where (fieldRowId.eq (row))
            .getQuery ();    
  }  
  

  /**
   * @param tableName
   * @param rows
   * @param columns
   * @return
   */
  private int select2(String tableName, List<String> rows, List<String> columns, boolean bTimer){
    Timer timer = Timer.start ("flat-dataset");
    
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME);
    List<Field<String>> selectFields = createFieldList (columns);
    
    int totalCellCount=0;
    ResultQuery<Record> query;    
    List<String> rowKeys = getRowKeys (tableName, ID_FIELD_NAME);    
    if(rowKeys.size ()==rows.size ())
      query = queryAllRows (selectFields, table);
    else{
        final int FETCH_LIMIT=1;
        
        List<String> batchRows = new ArrayList<String>(FETCH_LIMIT);
        for(String row : rows){
          batchRows.add(row);
          if(batchRows.size () % FETCH_LIMIT == 0){            
            if(FETCH_LIMIT==1)
              query = queryRowByID (tableName, selectFields, row);
            else
              query = queryINRows (tableName, selectFields, rows);
            totalCellCount += read(query);
            batchRows = new ArrayList<String>(FETCH_LIMIT); //reset
          }
        }
        if(batchRows.size ()>0){          
          query = queryINRows (tableName, selectFields, rows);
          totalCellCount += read(query);
        }
    }
    
    if(bTimer)
      timer.read ();
    return totalCellCount;
  }
  
  private int select2(String tableName, List<String> rows, List<String> columns){
    return select2(tableName, rows, columns, true);
  }
  
  private int select(String tableName, List<String> rows, List<String> columns){
    Timer timer = Timer.start ("flat-dataset");
    
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME);
    List<Field<String>> selectFields = createFieldList (columns);
    
    ResultQuery<Record> query;    
    List<String> rowKeys = getRowKeys (tableName, ID_FIELD_NAME);    
    if(rowKeys.size ()==rows.size ())
      query = queryAllRows (selectFields, table);
    else{
      query = queryINRows (tableName, selectFields, rows);
    }
    int count = read(query);
    timer.read ();
    return count;
  }
  
  private int read(ResultQuery<Record> query){
    Cursor<Record> cursor = null;
    int count=0;
    try {
      cursor = query.fetchLazy();
      // Cursor has similar methods as Iterator<R>
      while (cursor.hasNext()) {
          Record record = cursor.fetchOne();
          for(Field<?> columnField: record.fields()){
            String sValue = (String)record.getValue (columnField);
            double value=NaN;
            if ("Inf".equalsIgnoreCase (sValue))
              value = POSITIVE_INFINITY;
            else if ("-Inf".equalsIgnoreCase (sValue))
              value = NEGATIVE_INFINITY;
            else if ("NA".equalsIgnoreCase (sValue))
              value = NaN;
            else if ("null".equalsIgnoreCase (sValue))
              value = NaN;
            else if (sValue==null)
              value = NaN;
            else
              value = Double.parseDouble (sValue);            
            count++;
          }
      }
    }finally {
      if (cursor != null) {cursor.close();}
    }
    return count;
  }
    
  private List<String> getRowKeys(String tableName, String fieldRowIdName){
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, fieldRowIdName);
    //get rows
    Timer timer = Timer.start ("get-row-keys");
    ResultQuery<Record1<String>> queryRowKeys =context.select(fieldRowId).from(table).getQuery ();
    String[] arows = queryRowKeys.fetchArray (fieldRowId);
//    List<String> rows = Arrays.asList (arows);
    List<String> rows = new ArrayList<String>(arows.length);
    Collections.addAll (rows, arows);
    timer.read();
    log.debug ("rows.size(): " + rows.size ());
    return rows;
  }

  private List<String> getColumnKeys(String tableName, String fieldRowIdName){
    //get columns
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, fieldRowIdName);
    Timer timer = Timer.start ("get-column-keys");
    ResultQuery<Record> queryColumnKeys =context.selectFrom(table).limit (1) .getQuery ();
    Record recordColumnKeys = queryColumnKeys.fetchOne ();
    List<String> columns = new ArrayList<String>();
    for(Field<?> field : recordColumnKeys.fields())
      if(!field.getName ().equals (ID_FIELD_NAME))
        columns.add (field.getName ());    
    timer.read();
    log.debug ("columns.size(): " + columns.size ());
    log.debug ("columns: " + columns);
    return columns;
  }
  
  
  private static class Timer{
    private static DecimalFormat formatter = new DecimalFormat("0.######E0");    
    String name;
    long startTime=0;
    long lastPolledTime=0;    
    public Timer(){this("timer");};
    public Timer(String name){
      this.name=name;
      this.startTime=System.nanoTime();
    }
    
    public long start(){
      return this.startTime=System.nanoTime();
    }
    public long poll(){
      this.lastPolledTime = System.nanoTime();
      return this.lastPolledTime - startTime;      
    }
    public long stop(){
      long endTime = System.nanoTime();
      return endTime - startTime;      
    }
    public long read(){
      long duration = stop();
      log.debug ("Timer::"+name+":"+formatter.format(duration));
      return duration;
    }
    public static Timer start(String name){
      return new Timer(name);      
    }
  }
}
