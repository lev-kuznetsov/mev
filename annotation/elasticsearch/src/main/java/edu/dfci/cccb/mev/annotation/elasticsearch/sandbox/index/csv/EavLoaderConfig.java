package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent=true)
public class EavLoaderConfig extends CsvIndexLoaderConfig {

  @Getter private final String attributeField;
  @Getter private final String valueField;
  
  public EavLoaderConfig (String folder, String fileFilter, String indexName, String typeName
                          , String[] entityIdFields, String attributeField, String valueField ) {
    super (folder, fileFilter, indexName, typeName, entityIdFields);    
    this.attributeField=attributeField;
    this.valueField=valueField;
    
  }

  
  public boolean isValueField(String fieldName){
    return valueField ().equals (fieldName);
  }
  public boolean isAttributeField(String fieldName){
    return attributeField ().equals (fieldName);
  }
  
}
