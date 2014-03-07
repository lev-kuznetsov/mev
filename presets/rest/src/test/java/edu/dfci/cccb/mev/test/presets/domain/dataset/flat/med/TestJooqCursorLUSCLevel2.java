package edu.dfci.cccb.mev.test.presets.domain.dataset.flat.med;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
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
import edu.dfci.cccb.mev.test.presets.configuration.persistence.flat.TestJooqCursorLUSCLevel2Configuration;
import edu.dfci.cccb.mev.test.presets.configuration.persistence.flat.TestPresetsDatasetFlatTableConfig;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;

@Log4j
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = { TestJooqCursorLUSCLevel2Configuration.class })
public class TestJooqCursorLUSCLevel2 {

  @Inject @Named ("presets-jooq-context") DSLContext context;
  @Inject Environment environment;

  private URL rootUrl = null;
  private final String ID_FIELD_NAME = "COLUMN0";
  private String tsvFileName = "LUSC.HT_HG-U133A.Level_2.tsv";

  @PostConstruct
  public void init () throws MalformedURLException {
    this.rootUrl = new URL ("file://" + environment.getProperty ("user.home") + "/mev/data/tcga/tcga_data/");
  }

  @Test
  @Ignore
  public void testSimpleSelectAll () {
    Table<Record> table = tableByName (tsvFileName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME);

    long startTime = System.nanoTime ();
    int count = 0;
    ResultQuery<Record> query = context.selectFrom (table);
    // log.debug ("PresetValuesFlatTable sql:"+query.getSQL ());
    Cursor<Record> cursor = null;
    try {
      cursor = context.selectFrom (table).fetchLazy ();
      // Cursor has similar methods as Iterator<R>
      while (cursor.hasNext ()) {
        Record record = cursor.fetchOne ();
        for (Field<?> columnField : record.fields ()) {
          String value = (String) record.getValue (columnField);
          count++;
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close ();
      }
    }

    long endTime = System.nanoTime ();
    long duration = endTime - startTime;
    log.debug ("flat-count:" + count + ", duration: " + duration);
  }

  @Test
  @Ignore
  public void testSelect_AllFields () {
    log.debug ("... Running testSelect_AllFields...");

    List<String> rows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> columns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    int count = select2 (tsvFileName, rows, columns);
    log.debug ("flat-count:" + count);
  }

  @Test @Ignore
  public void testSelect_OneFieldAllRows () {
    log.debug ("... Running testSelect_OneFieldAllRows...");

    List<String> rows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> allColumns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    List<String> columns = new ArrayList<String> (1);
    columns.add (allColumns.get (0));

    int count = select2 (tsvFileName, rows, columns);
    log.debug ("flat-count:" + count);
  }

  @Test
  @Ignore
  public void testSelect_OneFieldOneRows () {
    log.debug ("... Running testSelect_OneFieldOneRows...");

    // List<String> allRows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> allColumns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    List<String> columns = new ArrayList<String> (1);
    columns.add (allColumns.get (3));
    List<String> rows = new ArrayList<String> (1);
    rows.add ("A_23_P100154");
    int count = select2 (tsvFileName, rows, columns);
    log.debug ("flat-count:" + count);
  }

  @Test  @Ignore
  public void testSelect_SomeFieldSomeRows () {
    log.debug ("... Running testSelect_SomeFieldSomeRows...");

    List<String> allRows = getRowKeys (tsvFileName, ID_FIELD_NAME);
    List<String> allColumns = getColumnKeys (tsvFileName, ID_FIELD_NAME);
    allColumns.remove (3);
    allRows.remove (4);
    int count = select2 (tsvFileName, allRows, allColumns);
    log.debug ("flat-count:" + count);
  }

  private List<Field<String>> createFieldList (List<String> columns) {
    List<Field<String>> selectFields = new ArrayList<Field<String>> (columns.size ());
    for (String columnName : columns)
      selectFields.add (fieldByName (String.class, columnName));

    return selectFields;
  }

  private ResultQuery<Record> queryAllRows (List<Field<String>> selectFields, Table<Record> table) {
    log.debug ("in queryAllRows");
    return context.select (selectFields)
                  .from (table)
                  .getQuery ();
  }

  private ResultQuery<Record> queryINRows (String tableName, List<Field<String>> selectFields, List<String> rows) {
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME);
    return context.select (selectFields)
                  .from (table).where (fieldRowId.in (rows))
                  .getQuery ();
  }

  private ResultQuery<Record> queryRowByID (String tableName, List<Field<String>> selectFields, String row) {
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME);
    return context.select (selectFields)
                  .from (table).where (fieldRowId.eq (row))
                  .getQuery ();
  }

  /**
   * @param tableName
   * @param rows
   * @param columns
   * @return
   */
  private int select2 (String tableName, List<String> rows, List<String> columns) {
    Timer timer = Timer.start ("flat-dataset");

    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME);
    List<Field<String>> selectFields = createFieldList (columns);

    int totalCellCount = 0;
    ResultQuery<Record> query;
    List<String> rowKeys = getRowKeys (tableName, ID_FIELD_NAME);
    if(rowKeys.size ()==rows.size ()){
    // TODO:hardcoded data size for test only
//    if (90797 == rows.size ()){
      query = queryAllRows (selectFields, table);
      totalCellCount = read (query);
    }else {
      final int FETCH_LIMIT = 1;

      List<String> batchRows = new ArrayList<String> (FETCH_LIMIT);
      for (String row : rows) {
        batchRows.add (row);
        if (batchRows.size () % FETCH_LIMIT == 0) {
          if (FETCH_LIMIT == 1)
            query = queryRowByID (tableName, selectFields, row);
          else
            query = queryINRows (tableName, selectFields, rows);
          totalCellCount += read (query);
          batchRows = new ArrayList<String> (FETCH_LIMIT); // reset
        }
      }
      if (batchRows.size () > 0) {
        query = queryINRows (tableName, selectFields, rows);
        totalCellCount += read (query);
      }
    }
    timer.read ();
    return totalCellCount;
  }

  private int select (String tableName, List<String> rows, List<String> columns) {
    Timer timer = Timer.start ("flat-dataset");

    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, ID_FIELD_NAME);
    List<Field<String>> selectFields = createFieldList (columns);

    ResultQuery<Record> query;
    List<String> rowKeys = getRowKeys (tableName, ID_FIELD_NAME);
    if (rowKeys.size () == rows.size ())
      query = queryAllRows (selectFields, table);
    else {
      query = queryINRows (tableName, selectFields, rows);
    }
    int count = read (query);
    timer.read ();
    return count;
  }

  private int read (ResultQuery<Record> query) {
    Cursor<Record> cursor = null;
    int count = 0;
    try {
      cursor = query.fetchLazy ();
      // Cursor has similar methods as Iterator<R>
      while (cursor.hasNext ()) {
        Record record = cursor.fetchOne ();
        for (Field<?> columnField : record.fields ()) {
          String sValue = (String) record.getValue (columnField);
          double value = NaN;
          if ("Inf".equalsIgnoreCase (sValue))
            value = POSITIVE_INFINITY;
          else if ("-Inf".equalsIgnoreCase (sValue))
            value = NEGATIVE_INFINITY;
          else if ("NA".equalsIgnoreCase (sValue))
            value = NaN;
          else if ("null".equalsIgnoreCase (sValue))
            value = NaN;
          else if (sValue == null)
            value = NaN;
          else
            value = Double.parseDouble (sValue);
          count++;
        }
      }
    } finally {
      if (cursor != null) {
        cursor.close ();
      }
    }
    return count;
  }

  private List<String> fetchList (ResultQuery<Record1<String>> query, String columnField) {
    Cursor<Record1<String>> cursor = null;
    List<String> result = new ArrayList<String> (1000);
    try {
      cursor = query.fetchLazy ();
      // Cursor has similar methods as Iterator<R>
      while (cursor.hasNext ()) {
        Record record = cursor.fetchOne ();
        String sValue = (String) record.getValue (columnField);
        result.add (sValue);
      }
    } finally {
      if (cursor != null) {
        cursor.close ();
      }
    }
    return result;
  }

  private List<String> rowKeys = null;

  @Synchronized
  private List<String> getRowKeys (String tableName, String fieldRowIdName) {
    Timer timer = Timer.start ("get-row-keys");
    if (this.rowKeys == null) {
      Table<Record> table = tableByName (tableName);
      Field<String> fieldRowId = fieldByName (String.class, fieldRowIdName);
      // get rows
      Timer timer2 = Timer.start ("fetch-array");
      ResultQuery<Record1<String>> queryRowKeys = context.select (fieldRowId).from (table).getQuery ();
      String[] arows = queryRowKeys.fetchArray (fieldRowId);
      timer2.read ();
      // List<String> rows = Arrays.asList (arows);
      timer2 = Timer.start ("copy-array-to-list");
      // this.rowKeys = new ArrayList<String>(arows.length);
      // Collections.addAll (this.rowKeys, arows);
      // this.rowKeys = Arrays.asList (arows);
      this.rowKeys = fetchList (queryRowKeys, ID_FIELD_NAME);
      timer2.read ();

      log.debug ("rows.size(): " + this.rowKeys.size ());
    }
    timer.read ();
    return this.rowKeys;
  }

  private List<String> getColumnKeys (String tableName, String fieldRowIdName) {
    // get columns
    Table<Record> table = tableByName (tableName);
    Field<String> fieldRowId = fieldByName (String.class, fieldRowIdName);
    Timer timer = Timer.start ("get-column-keys");
    ResultQuery<Record> queryColumnKeys = context.selectFrom (table).limit (1).getQuery ();
    Record recordColumnKeys = queryColumnKeys.fetchOne ();
    List<String> columns = new ArrayList<String> ();
    for (Field<?> field : recordColumnKeys.fields ())
      if (!field.getName ().equals (ID_FIELD_NAME))
        columns.add (field.getName ());
    timer.read ();
    log.debug ("columns.size(): " + columns.size ());
    log.debug ("columns: " + columns);
    return columns;
  }

  private static class Timer {
    private static DecimalFormat formatter = new DecimalFormat ("0.######E0");
    String name;
    long startTime = 0;
    long lastPolledTime = 0;

    public Timer () {
      this ("timer");
    };

    public Timer (String name) {
      this.name = name;
      this.startTime = System.nanoTime ();
    }

    public long start () {
      return this.startTime = System.nanoTime ();
    }

    public long poll () {
      this.lastPolledTime = System.nanoTime ();
      return this.lastPolledTime - startTime;
    }

    public long stop () {
      long endTime = System.nanoTime ();
      return endTime - startTime;
    }

    public long read () {
      long duration = stop ();
      log.debug ("Timer::" + name + ":" + formatter.format (duration));
      return duration;
    }

    public static Timer start (String name) {
      return new Timer (name);
    }
  }
}
