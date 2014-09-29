package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavLoaderConfig;

@Configuration
public class MacsImportConfiguration {
  
  
  @Bean
  public URL getDataRoot() throws MalformedURLException{
    return  Paths.get (System.getProperty ("user.home"), "/mev/data/macs/").toUri ().toURL ();
  }
    
  public String getFileFilterMask(){
    return "macs_data_dump.tsv";
  }
  
  @Bean
  public EavLoaderConfig getConfig() throws MalformedURLException{
    String[] entityIdField = {"case_id", "visit_id"};
    return new EavLoaderConfig (getDataRoot().getPath (), 
                                getFileFilterMask(), 
                                "macs_index", 
                                "macs_type", 
                                entityIdField, 
                                "variable", 
                                "num_value");
  }
}
