package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.perf;

import java.io.IOException;

import javax.inject.Inject;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

@Component
public class NumerifyApp {

  @Inject Client client;
  @Inject BulkProcessorFactory bulkProcessorFactory;
  @Inject IndexAdminHelper adminHelper;
    
  public static void main (String[] args) throws IndexAdminException, IOException, IndexLoaderException {
    try(AnnotationConfigApplicationContext springContext = new AnnotationConfigApplicationContext ()){      
      springContext.register (ElasticSearchConfiguration.class);
      springContext.refresh ();
      
      NumerifyApp app = springContext.getBean (NumerifyApp.class);
      app.run(args);
    }
  }

  public void run(String[] args) throws IndexAdminException, IOException, IndexLoaderException{
//    adminHelper.numerifyField (adminHelper.dummyName (100, 10), "dummy_type", "field0", bulkProcessorFactory.create (10, 1));
    
    adminHelper.numerifyField (adminHelper.dummyName (150000, 10), "dummy_type", "field0", 
                               bulkProcessorFactory.create (1000, 8));
  }
}
