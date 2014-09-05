package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;

public interface IndexAdminHelper {
  public boolean exists(String indexName);
  public void deleteIndex(String indexName);
  public void createIndex(String indexName);
  public CreateIndexResponse createIndex (String indexName, String documentType, XContentBuilder mappingBuilder);  
  public PutMappingResponse putMapping (String indexName, String documentType, XContentBuilder mappingBuilder);  
}
