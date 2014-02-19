package edu.dfci.cccb.mev.test.annotation.server.support.jooq;

import static org.jooq.impl.DSL.using;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.support.jooq.JooqUtil;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;


@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConfigTest.class})
public class TestJooqUtil {
  
  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  
  public TestJooqUtil() throws SQLException{
    
  }
  
  @Test
  public void test () throws SQLException {
    DSLContext context = using (dataSource.getConnection ());
    List<String> list = new ArrayList <String>(){
      private static final long serialVersionUID = 1L;
      {
        add("probe1");
        add("probe2");
        add("probe3");
      }
    };
    Table<Record> table = new JooqUtil ().getTableBasedOnList (context, list);
        
    assertEquals ("c1\nprobe1\nprobe2\nprobe3\n", context.selectFrom (table).fetch ().formatCSV ('\t'));
    
    List<?> record = context.select().from (table).fetch ();
    log.debug ("record: " + record);
  }

}











































