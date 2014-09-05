package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;

@RequiredArgsConstructor
public class IndexAdminHelperImpl implements IndexAdminHelper {

  final private Client client;

  @Override
  public boolean exists (String indexName) {
    final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
    return res.isExists();
  }

  @Override
  public void deleteIndex (String indexName) {
    final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(indexName);
    delIdx.execute().actionGet();    
  }

  @Override
  public void createIndex (String indexName) {
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    createIndexRequestBuilder.execute().actionGet();
  }
    
  @Override
  public CreateIndexResponse createIndex (String indexName, String documentType, XContentBuilder mappingBuilder) {
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse response = createIndexRequestBuilder.addMapping(documentType, mappingBuilder).execute().actionGet();
    return response;
  }  
  
  @Override
  public PutMappingResponse putMapping (String indexName, String documentType, XContentBuilder mappingBuilder) {
    final PutMappingRequestBuilder putMappingRequestBuilder 
    = client.admin().indices().preparePutMapping (indexName).setType (documentType).setSource (mappingBuilder);    
    PutMappingResponse response = putMappingRequestBuilder.execute().actionGet();
    return response;
  }
  
}
