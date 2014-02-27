package edu.dfci.cccb.mev.annotation.domain.probe.simple;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.SneakyThrows;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotation;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

public class SimpleProbeAnnotations implements ProbeAnnotations{
  
  URL rootFolder;
  ArrayList<ProbeAnnotation> probeList;
  String platformId; 
  
  @SneakyThrows
  public SimpleProbeAnnotations (URL rootFolder) throws URISyntaxException, IOException {
    
//    this.rootFolder=rootFolder;
//    String[] segments = rootFolder.getPath().split("/");
//    annotationType =segments[segments.length-2];
//    
//    Path path = Paths.get (rootFolder.toURI ());
//    try(DirectoryStream<Path> ds = Files.newDirectoryStream (path, "*.annot.out.tsv")){
//      for(Path file : ds){
//        
//      }
//    }

    throw new AnnotationException ("Method not implemented");
    
  }
  
  @SneakyThrows
  public SimpleProbeAnnotations (String platformId, URL rootFolder) throws URISyntaxException, IOException {
    
//    this.rootFolder=rootFolder;
//    String[] segments = rootFolder.getPath().split("/");
//    annotationType =segments[segments.length-2];
//    
//    Path path = Paths.get (rootFolder.toURI ());
//    try(DirectoryStream<Path> ds = Files.newDirectoryStream (path, "*.annot.out.tsv")){
//      for(Path file : ds){
//        
//      }
//    }

    throw new AnnotationException ("Method not implemented");
    
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String platformId () {
    return platformId;
  }

  

}
