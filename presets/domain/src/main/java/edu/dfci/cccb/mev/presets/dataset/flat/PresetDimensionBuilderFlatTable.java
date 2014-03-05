package edu.dfci.cccb.mev.presets.dataset.flat;

import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.tableByName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.ResultQuery;
import org.jooq.Table;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.util.timer.Timer;

@Log4j
public class PresetDimensionBuilderFlatTable implements PresetDimensionBuilder{

  private String ID_FIELD_NAME="COLUMN0";
  private @Inject @Named("presets-jooq-context") DSLContext context;
  private Map<String, List<String>> rowCasche;
  public PresetDimensionBuilderFlatTable (DSLContext context) {
    this.context=context;            
    this.rowCasche=new HashMap<String, List<String>> ();
  }

  @Override  
  public Dimension build (Type type, PresetDescriptor descriptor) {
    if(type==Dimension.Type.COLUMN)
      return new SimpleDimension (type, getColumnKeys (descriptor.name (), ID_FIELD_NAME), 
                                  new ArrayListSelections (), null);
    else
      return new SimpleDimension (type, getRowKeys (descriptor.name (), ID_FIELD_NAME), 
                                  new ArrayListSelections (), null);    
  }
  
  @Override
  public Dimension buildColumns (PresetDescriptor descriptor) {    
    return build(Dimension.Type.COLUMN, descriptor);
  }

  @Override
  public Dimension buildRows (PresetDescriptor descriptor) {    
    return build(Dimension.Type.ROW, descriptor);
  }
  
  @Synchronized
  private List<String> getRowKeys(String tableName, String fieldRowIdName){
    if(rowCasche.containsKey (tableName)){
      return rowCasche.get (tableName);
    }else{
      
      Table<Record> table = tableByName (tableName);
      Field<String> fieldRowId = fieldByName (String.class, fieldRowIdName);
      //get rows
      Timer timer = Timer.start ("get-row-keys");
      ResultQuery<Record1<String>> queryRowKeys =context.select(fieldRowId).from(table).getQuery ();
      String[] arows = queryRowKeys.fetchArray (fieldRowId);
  //    List<String> rows = Arrays.asList (arows);
      List<String> rows = new ArrayList<String>(arows.length);
      Collections.addAll (rows, arows);
      
      if(log.isDebugEnabled ()){
        timer.read();
        log.debug ("rows.size(): " + rows.size ());
      }
      rowCasche.put (tableName, rows);
      return rows;
    }
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
    
    if(log.isDebugEnabled ()){
      timer.read();
      log.debug ("columns.size(): " + columns.size ());
      log.debug ("columns: " + columns);
    }
    return columns;
  }

  @Override
  public Dimension build (Type type, PresetDescriptor descriptor, Selection selection) {
    if(log.isDebugEnabled ()){
      log.debug ("selection.size(): " + selection.keys ().size ());
    }
    return new SimpleDimension (type, selection.keys (), new ArrayListSelections (), null);
  }



}
