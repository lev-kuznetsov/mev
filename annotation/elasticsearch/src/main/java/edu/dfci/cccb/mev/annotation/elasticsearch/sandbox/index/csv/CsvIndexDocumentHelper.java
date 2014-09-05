package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import au.com.bytecode.opencsv.CSVReader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

@RequiredArgsConstructor
public class CsvIndexDocumentHelper implements IndexDocumentHelper {

  private final List<String> fields;
  private final String idField;
  
  @Override
  public XContentBuilder process (String[] data) throws IndexLoaderException {
    try{
      XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();    
      jsonBuilder.startObject();    
      int position = 0;
      for (String fieldName : fields) {          
          jsonBuilder.field(fieldName, data[position]);          
          position++;
      }    
      jsonBuilder.endObject();
      return jsonBuilder;
    }catch(IOException e){
      throw new IndexLoaderException (String.format("Unable to parse data %s", data), e);
    }
  }
  
  @Override
  public XContentBuilder createMapping(String typeName) throws IOException{
    
      XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();    
      
      jsonBuilder.startObject()
      .startObject ("_id")
      .field ("path", idField)
      .endObject ();
      
      jsonBuilder.startObject ("properties");
      for (String fieldName : fields) {          
          jsonBuilder.startObject (fieldName)
          .field("type", "string")
          .field("index", "not_analyzed")
          .field("ignore_malformed", true)
          .endObject ();          
      }    
      
      jsonBuilder.endObject();
      jsonBuilder.endObject ();      
      return jsonBuilder;    
  }
  
}
