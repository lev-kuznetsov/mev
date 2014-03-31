package edu.dfci.cccb.mev.presets.dataset.flat;

import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.tableByName;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.Table;

public class PresetValuesQueryHelper {
  public static String ID_FIELD_NAME="COLUMN0";
  
  private final @Inject @Named("presets-jooq-context") DSLContext context;
  

  public PresetValuesQueryHelper(DSLContext context){
    this.context = context;
    
  }

  public ResultQuery<Record> queryAllRows(List<Field<String>> selectFields, Table<Record> table){
    return context.select(selectFields)
            .from(table)
            .getQuery ();    
  }
  
  public ResultQuery<Record> queryINRows(List<Field<String>> selectFields, List<String> rows, Table<Record> table){    
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME); 
    return context.select(selectFields)
            .from(table).where (fieldRowId.in (rows))
            .getQuery ();    
  }
  
  
  public ResultQuery<Record> queryRowByID(String tableName, List<Field<String>> selectFields, String row){
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME); 
    return context.select(selectFields)
            .from(table).where (fieldRowId.eq (row))
            .getQuery ();    
  }  

  public ResultQuery<Record> queryValue(String row, String column, Table<Record> table, Field<String> fieldRowId){    
    return context.selectFrom (table)
            .where (fieldRowId.eq (row));
  }
  
}
