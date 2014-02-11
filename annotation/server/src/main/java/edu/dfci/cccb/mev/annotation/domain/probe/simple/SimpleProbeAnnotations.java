package edu.dfci.cccb.mev.annotation.domain.probe.simple;

import static java.util.UUID.randomUUID;
import static org.jooq.impl.DSL.tableByName;
import static org.jooq.impl.DSL.using;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.hssf.record.TableRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotation;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.Tables;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

public class SimpleProbeAnnotations implements ProbeAnnotations{

  String annotationType;
  URL rootFolder;
  ArrayList<ProbeAnnotation> probeList;
  
  public SimpleProbeAnnotations (URL rootFolder) throws URISyntaxException, IOException {
    
    this.rootFolder=rootFolder;
    String[] segments = rootFolder.getPath().split("/");
    annotationType =segments[segments.length-2];
    
    Path path = Paths.get (rootFolder.toURI ());
    try(DirectoryStream<Path> ds = Files.newDirectoryStream (path, "*.annot.out.tsv")){
      for(Path file : ds){
        
      }
    }
        
  }

  public SimpleProbeAnnotations(URL rootFolder, DataSource dataSource){

  }
  
  @Override
  public List<ProbeAnnotation> get (Dimension dimension) {
 // Use ";" as the separator character
    return null;
  }

  @Override
  public InputStream getAsStream (Dimension dimension) {
     
    return null;
  }
  
  

}
