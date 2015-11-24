package edu.dfci.cccb.mev.presets.dataset.flat;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.tableByName;

import java.util.Map;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.Table;

import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValues;
import edu.dfci.cccb.mev.presets.contract.PresetValues;
@Log4j
public class PresetValuesFlatTable extends AbstractValues implements PresetValues {

  private final DSLContext context;
  private final Table<Record> table;
  private final Field<String> rowIdField = fieldByName (String.class, "COLUMN0");
    
  private Map<String, Object> lastMap;
  private Record lastRecord;
  private String lastRowKey=null; 
  public PresetValuesFlatTable (DSLContext context, String tableName) {
    this.context=context;
    table = tableByName (tableName); 
    
  }

  @Synchronized
  private void setLastRecord(Record record){    
    this.lastMap=record.intoMap ();
    this.lastRecord=record;
    this.lastRowKey=(String)this.lastMap.get ("COLUMN0");
  }
  @Synchronized
  private boolean isSameRecord(String row){
    if(this.lastRowKey!=null && this.lastRowKey.equals (row))
      return true;
    return false;
  }
  
  
  @Override
  public double get (String row, String column) throws InvalidCoordinateException {
    try {
      Field<String> columnField = fieldByName (String.class, column.toUpperCase ());
      ResultQuery<Record> query=null;
      if(!isSameRecord (row)){        
        try{
          query =context.selectFrom (table)
                  .where (this.rowIdField.eq (row));
          log.debug ("PresetValuesFlatTable sql:"+query.getSQL ());      
          Record record = query.fetchOne ();
          
  //        Record record=null;
  //        Cursor<Record> cursor = null;
  //        try {
  //          cursor = context.selectFrom (table).fetchLazy ();
  //          // Cursor has similar methods as Iterator<R>
  //          if(cursor.hasNext ()) {
  //            record = cursor.fetchOne ();              
  //          }
  //        } finally {
  //          if (cursor != null) {
  //            cursor.close ();
  //          }
  //        }
          setLastRecord (record);
        }finally{
          if(query!=null) 
            query.close();            
        }
      } 
//      String value = (String)this.lastMap.get(column);
      String value = (String)this.lastRecord.getValue (columnField);
      
      if ("Inf".equalsIgnoreCase (value))
        return POSITIVE_INFINITY;
      else if ("-Inf".equalsIgnoreCase (value))
        return NEGATIVE_INFINITY;
      else if ("NA".equalsIgnoreCase (value))
        return NaN;
      else if ("null".equalsIgnoreCase (value))
        return NaN;
      else if (value==null)
        return NaN;
      else
        return Double.parseDouble (value);
      
    } catch (RuntimeException e) {
      throw new RuntimeException (" Failed to fetch row " + row + ", column " + column + " from " + table, e);
    }
  }


}
