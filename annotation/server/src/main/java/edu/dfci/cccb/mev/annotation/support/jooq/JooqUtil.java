package edu.dfci.cccb.mev.annotation.support.jooq;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Row1;
import org.jooq.Table;

import static org.jooq.impl.DSL.*;
@Log4j
public class JooqUtil {

  public Table<Record> getTableBasedOnList(DSLContext create, List<String> list)
  {
    @SuppressWarnings ("unchecked")
    Row1<String>[] values = (Row1<String>[]) Array.newInstance (Row1.class, list.size ());
    
    Iterator<String> it = list.iterator ();
    int i=0;

    while(it.hasNext ())
      values[i++]=row(it.next ());
      
    Table<Record> table = create.select().from(
                                values(values)
                                ).asTable("tblDimension");
    if(log.isDebugEnabled ()) 
      log.debug ("table: " + table.toString ());
    return table;
  }
  
}
