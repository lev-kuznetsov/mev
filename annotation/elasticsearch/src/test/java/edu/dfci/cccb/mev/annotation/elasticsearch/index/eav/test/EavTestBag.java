package edu.dfci.cccb.mev.annotation.elasticsearch.index.eav.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import au.com.bytecode.opencsv.CSVReader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavIndexDocumentHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavLoaderConfig;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavParser;

public class EavTestBag {

  public EavLoaderConfig getConfig(){
    return new EavLoaderConfig ("path/to/folder", 
                                                 "*.tsv", 
                                                 "test_index", 
                                                 "test_type", 
                                                 getEntityFields (), 
                                                 "variable", 
                                                 "value");
  }
  
  public URL getFileUrl(){
    return EavTestBag.class.getResource ("/macs/data/macs_data_dumpTop10.tsv");
  }
  
  public Path getFilePath() throws URISyntaxException{
    return Paths.get (getFileUrl ().toURI ());
  }
  
  
  public InputStream getInputStream() throws IOException, URISyntaxException{
    return Files.newInputStream (getFilePath ());
  }

  public String[] getEntityFields(){
    String[] entityIdFields = {"case_id", "visit_id"};
    return entityIdFields;
  }
  
  public CSVReader getReader() throws IOException, URISyntaxException{
     EavLoaderConfig config = getConfig ();
     return new CSVReader (new InputStreamReader(getInputStream ())
       , config.separator (), config.quoteCharacter(), config.escapeCharacter());
  }
  
  public EavIndexDocumentHelper getHelper() throws IOException, URISyntaxException{
    return new EavIndexDocumentHelper (getConfig (), getReader ());
  }
  
  public EavParser getParser() throws IOException, URISyntaxException{
    return new EavParser (getFilePath (), getConfig ());
  }
}
