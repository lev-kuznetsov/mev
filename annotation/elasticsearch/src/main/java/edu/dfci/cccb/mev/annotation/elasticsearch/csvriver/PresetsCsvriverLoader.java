package edu.dfci.cccb.mev.annotation.elasticsearch.csvriver;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import org.elasticsearch.client.Client;

public class PresetsCsvriverLoader {
  Client client;
  PresetsCsvriverLoader(Client client){
    this.client=client;
  }
  
  public void load(URL data){
    
  }
  public void load(Path data){
    
  }
  public void load(File data){
        
  }
  
  
}
