package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.SneakyThrows;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import au.com.bytecode.opencsv.CSVReader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder_NoIndex;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParser;

public class EavParser implements IndexDocumentParser, Closeable, AutoCloseable {

  private final CsvIndexLoaderConfig config;
  private final EavIndexDocumentHelper eavHelper;
    
  public EavParser (Path file, EavLoaderConfig config) throws IOException {
    eavHelper = new EavIndexDocumentHelper (config, 
                  new CSVReader(new InputStreamReader(Files.newInputStream (file)), 
                     config.separator (), config.quoteCharacter(), config.escapeCharacter()));
    this.config = config;
  }

  @Override
  public Iterator<XContentBuilder> iterator () {
    
    return new EavIterator ();
  }

  @Override
  public void close () throws IOException {
    eavHelper.close (); 
  }

  @Override
  public XContentBuilder getMapping () throws IOException {
    DynamicTemplateBuilder dynamicTemplateBuilder = new DynamicTemplateBuilder_NoIndex ();
    XContentBuilder mapping = XContentFactory.jsonBuilder ();
    mapping.startObject ()
    .field("dynamic_templates", dynamicTemplateBuilder.build ())
    .endObject();
    return mapping;
  }
  
  public class EavIterator implements Iterator<XContentBuilder>{
    
    private Map<String, String> nextEntity;
    @Getter private String key;
    
    private String readKey(Map<String, String> entity){
      return entity.get("uniqueKey");
    }
    private void setNextEntity(Map<String, String> entity){
      this.nextEntity=entity;      
    }
    
    @SneakyThrows(IOException.class)
    public EavIterator() {
      nextEntity=eavHelper.readNext ();
    }
        
    @Override
    public boolean hasNext () {
      return nextEntity.size ()>0;
    }

    @Override
    @SneakyThrows({IOException.class})
    public XContentBuilder next () {
        
              
        Map<String, String> curEntity = nextEntity;
        nextEntity = eavHelper.readNext ();
        
        XContentBuilder jsonEntity = XContentFactory.jsonBuilder ().startObject ();
        for(String field : curEntity.keySet ()){
          jsonEntity.field (field, curEntity.get (field));
        }        
        return jsonEntity.endObject ();
    }

    
    @Override
    public void remove () {
      throw new UnsupportedOperationException(String.format("Remove operation not supported"));
    }      
    
    
  }

}
