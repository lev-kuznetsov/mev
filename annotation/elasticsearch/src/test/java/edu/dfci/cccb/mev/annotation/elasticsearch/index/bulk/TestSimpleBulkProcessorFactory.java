package edu.dfci.cccb.mev.annotation.elasticsearch.index.bulk;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test.AbstractTestWithElasticSearch;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.SimpleBulkProcessorFactory;


public class TestSimpleBulkProcessorFactory  extends AbstractTestWithElasticSearch{

  private final BulkProcessorFactory bulkFactory=new SimpleBulkProcessorFactory (client);
  
  @Test
  public void testCalculateBulkRows () {
    int bulkRows = bulkFactory.calculateBulkRows (1);
    assertEquals (1250000, bulkRows);
    
    bulkRows = bulkFactory.calculateBulkRows (60);
    //1250000/60 = 20833
    assertEquals (20833, bulkRows);
    
    bulkRows = bulkFactory.calculateBulkRows (3000);
    //1250000/3000 = 416
    assertEquals (416, bulkRows);
    
    bulkRows = bulkFactory.calculateBulkRows (6000);
    //1250000/6000 = 208
    assertEquals (208, bulkRows);

    bulkRows = bulkFactory.calculateBulkRows (500);
    //1250000/500 = 2500
    assertEquals (2500, bulkRows);

  }

}
