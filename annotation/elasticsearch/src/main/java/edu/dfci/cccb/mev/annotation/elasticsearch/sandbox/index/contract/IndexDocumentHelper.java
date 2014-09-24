package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;

public interface IndexDocumentHelper {
  XContentBuilder process(String[] data) throws IndexLoaderException;  
  XContentBuilder createMapping (String typeName) throws IndexLoaderException, IOException;
  int getFieldIndex (String fieldName);  
  int getIdIndex ();
  String getFieldName (int index);
}
