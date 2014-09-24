package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import au.com.bytecode.opencsv.CSVReader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

public class CsvIndexDocumentHelper implements IndexDocumentHelper {

  private final LinkedHashMap<String, Integer> fields;
  private final List<String> fieldNames;
  private final String[] idFields;
  
  public CsvIndexDocumentHelper(List<String> fields, String[] idFields){
    this.fields = new LinkedHashMap <String, Integer>(fields.size ());
    int fieldIndex=0;
    fieldNames=fields;
    for(String field : this.fieldNames){
      this.fields.put(field, fieldIndex);
      fieldIndex++;
    }
    this.idFields=idFields;
  }
  
  @Override
  public XContentBuilder process (String[] data) throws IndexLoaderException {
    try{
      XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();    
      jsonBuilder.startObject();    
      int position = 0;
      for (String fieldName : fields.keySet ()) {          
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
    
      XContentBuilder jsonBuilder = XContentFactory.jsonBuilder().startObject();    
      
      if(idFields.length==1){        
        jsonBuilder.startObject ("_id")
        .field ("path", idFields[0])
        .endObject ();
      }
      
      jsonBuilder.startObject ("properties");
      for (String fieldName : fields.keySet ()) {          
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
  
  @Override
  @SneakyThrows({NullPointerException.class})
  public int getFieldIndex(String fieldName){
    try{
        return fields.get (fieldName);
    }catch(NullPointerException e){      
      throw new NullPointerException(String.format("Field %s not found in %s", fieldName, fields));
    }
  }
    
  @Override
  public String getFieldName(int index){
    return fieldNames.get(index);
  }
  
}
