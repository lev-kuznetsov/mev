package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import lombok.Delegate;
import lombok.extern.log4j.Log4j;
@Log4j
public class EavIndexDocumentHelper implements Closeable, AutoCloseable {

  private final CSVReader reader;
  @Delegate private final CsvIndexDocumentHelper csvHelper;
  private final EavLoaderConfig config;
  
  private String[] data=null;
  private String[] prevData=null;
  private String prevEntityId=null;
  private String curEntityId=null;
  
  
  public EavIndexDocumentHelper (EavLoaderConfig config, CSVReader reader) throws IOException {
    this.reader=reader;
    if (config.firstLineIsHeader()) {
      String[] headerLine;
      if ((headerLine = reader.readNext()) != null) {       
        config.csvFields(Arrays.asList(headerLine));
      }
    }    
    this.config = config;
    csvHelper = new CsvIndexDocumentHelper (config.csvFields (), config.entityIdFields ());
    //read in the first line from the file
    data=reader.readNext ();
    curEntityId=getEntityId (data);
  }
  
  private String getEntityId(String data[]){    
    StringBuilder sb=new StringBuilder ();
    
    for(String keyField : config.entityIdFields ()){
        try{
          sb.append ("!!!!").append (data[csvHelper.getFieldIndex (keyField)]);
        }catch(ArrayIndexOutOfBoundsException e){
          throw new ArrayIndexOutOfBoundsException (String.format("Error reading field %s index %s data: %s"
                                                                  , keyField, csvHelper.getFieldIndex (keyField), Arrays.toString(data)));
        }
      }    
      return sb.toString ().substring (4);    
  }
  
  private boolean isSameEntity() throws IOException{
    //remember prev line to check for new entity
    prevData=data;
    prevEntityId=curEntityId;
    
    //read in next line
    data=reader.readNext ();    
    if(data==null)
      return false;
    curEntityId=getEntityId (data);
    
    try{
      return curEntityId.equals (prevEntityId);
    }catch (ArrayIndexOutOfBoundsException e){
      log.error (e);
      //if a bad record is found, move on to the next one
      return true;
    }
    
  }
    
  private void processLine(Map<String, String> entity, String[] data) throws IOException{
    int position=0;
    String value="";
    String attribute="";
    try{
      for(String curFieldValue : data){
        String fieldName = csvHelper.getFieldName (position);
        if(config.isValueField (fieldName)){
          value=curFieldValue;
        }else if(config.isAttributeField (fieldName)){
          attribute=curFieldValue;
        }else if(config.isEntityIdField (fieldName)){
          entity.put (fieldName, curFieldValue);
        }      
        position++;
      }
    }catch(ArrayIndexOutOfBoundsException e){
      log.error (e);
    }
    
    if("".equals (value) || "".equals (attribute))
      log.error(new IOException (String.format("Invalid EAV record - attribute '%s' or value '%s' is empty in entity %s from data: %s", attribute, value, entity, Arrays.toString (data))));
    else
      entity.put (attribute, value);    
//    entity.put ("uniqueKey", getEntityId (data));
  }
  
  public Map<String, String> readNext() throws IOException{
    Map<String, String> entity = new LinkedHashMap<String, String> ();    
    //the first line is read on creation, so data should not be null
    while(data!=null){      
      //append feilds to the entity
      processLine(entity, data);         
      if(isSameEntity()==false){        
        break;
      }
    }
    return entity;
  }

  public String getId(){
    return prevEntityId;    
  }
  @Override
  public void close () throws IOException {
    reader.close();
  }
  
  
}
