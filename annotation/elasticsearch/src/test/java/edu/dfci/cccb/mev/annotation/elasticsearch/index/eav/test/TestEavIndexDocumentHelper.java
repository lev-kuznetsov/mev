package edu.dfci.cccb.mev.annotation.elasticsearch.index.eav.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.tools.ant.filters.StringInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavIndexDocumentHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavLoaderConfig;

public class TestEavIndexDocumentHelper {

  EavTestBag bag = new EavTestBag ();
  EavIndexDocumentHelper eavHelper;  
  
  @Before
  public void setup() throws IOException, URISyntaxException{    
    eavHelper = bag.getHelper ();
  }
  
  @Test
  public void testReadNext () throws IOException {
    //entity 1
    Map<String, String> entity = eavHelper.readNext ();
    assertEquals("1000", entity.get ("case_id"));
    assertEquals("0", entity.get ("visit_id"));
    assertEquals("1984-85 cohort censored", entity.get ("MACSCODE"));    
    assertEquals("other than 2001 recruit or returning censored", entity.get ("STATUS02"));
    assertEquals("1000!!!!0", eavHelper.getId());
    
    //entity 2
    entity = eavHelper.readNext ();
    assertEquals("1001", entity.get ("case_id"));
    assertEquals("0", entity.get ("visit_id"));
    assertEquals("1984-85 cohort censored", entity.get ("MACSCODE"));    
    assertEquals("other than 2001 recruit or returning censored", entity.get ("STATUS02"));
    assertEquals("1001!!!!0", eavHelper.getId());
    
    //entity 3
    entity = eavHelper.readNext ();
    assertEquals("1002", entity.get ("case_id"));
    assertEquals("0", entity.get ("visit_id"));
    assertEquals("1984-85 cohort not censored", entity.get ("MACSCODE"));    
    assertEquals("other than 2001 recruit or returning censored", entity.get ("STATUS02"));
    assertEquals("1002!!!!0", eavHelper.getId());
    
    //entity 4
    entity = eavHelper.readNext ();
    assertEquals("1003", entity.get ("case_id"));
    assertEquals("0", entity.get ("visit_id"));
    assertEquals("1984-85 cohort not censored", entity.get ("MACSCODE"));    
    assertEquals("other than 2001 recruit or returning censored", entity.get ("STATUS02"));
    assertEquals("1003!!!!0", eavHelper.getId());
    
    //entity 5
    entity = eavHelper.readNext ();
    assertEquals("1004", entity.get ("case_id"));
    assertEquals("0", entity.get ("visit_id"));
    assertEquals("1984-85 cohort not censored", entity.get ("MACSCODE"));        
    assertEquals("1004!!!!0", eavHelper.getId());
    
    //empty
    entity = eavHelper.readNext ();
    assertTrue(entity.size ()==0);
    entity = eavHelper.readNext ();
    assertTrue(entity.size ()==0);
    
  }

  @After
  public void after() throws IOException{
    eavHelper.close();
  }
}
