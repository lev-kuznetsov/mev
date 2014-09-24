package edu.dfci.cccb.mev.annotation.elasticsearch.index.eav.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavLoaderConfig;

public class TestEavLoaderConfig {

  EavTestBag bag = new EavTestBag ();
  EavLoaderConfig config;
  String[] entityIdFields;
  
  @Before  
  public void setup(){
    config=bag.getConfig ();
    entityIdFields=bag.getEntityFields ();
  }
  @Test
  public void testIsEntityIdField () {
    assertTrue(config.isEntityIdField ("case_id"));
    assertTrue(config.isEntityIdField ("visit_id"));
  }

  @Test
  public void testIsValueField () {
    assertTrue(config.isValueField ("value"));
  }

  @Test
  public void testIsAttributeField () {
    assertTrue(config.isAttributeField ("variable"));
  }

}

