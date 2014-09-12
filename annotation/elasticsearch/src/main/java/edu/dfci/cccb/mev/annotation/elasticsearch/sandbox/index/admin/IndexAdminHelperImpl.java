package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import scala.annotation.meta.field;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.SimpleBulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeCopier;

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
  public String createIndex (String indexName) {
    //TODO: check if index with this name already exists
    
    //generate index name: indexName+UUID
    final String internalIndexName = indexName + UUID.randomUUID ();
    //add alias: indexName
    final Alias alias = new Alias (indexName);    
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(internalIndexName);
    createIndexRequestBuilder.addAlias (alias) .execute().actionGet();
    return internalIndexName;
  }
    
  @Override
  public CreateIndexResponse createIndex (String indexName, String documentType, XContentBuilder mappingBuilder) {
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse response = createIndexRequestBuilder.addMapping(documentType, mappingBuilder).execute().actionGet();
    return response;
  }  
  
  @Override
  public CreateIndexResponse createIndex (String indexName, String documentType, Map<String, Object> mapping) {
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse response = createIndexRequestBuilder.addMapping(documentType, mapping).execute().actionGet();
    return response;
  }  
  
  
  @Override
  public PutMappingResponse putMapping (String indexName, String documentType, XContentBuilder mappingBuilder) {
    final PutMappingRequestBuilder putMappingRequestBuilder 
    = client.admin().indices().preparePutMapping (indexName).setType (documentType).setSource (mappingBuilder);    
    PutMappingResponse response = putMappingRequestBuilder.execute().actionGet();
    return response;
  }
  
  @Override
  public PutMappingResponse putMapping (String indexName, String documentType, Map<String, Object> mappingBuilder) {
    final PutMappingRequestBuilder putMappingRequestBuilder 
    = client.admin().indices().preparePutMapping (indexName).setType (documentType).setSource (mappingBuilder);    
    PutMappingResponse response = putMappingRequestBuilder.execute().actionGet();
    return response;
  }
  
  @Override
  public MappingMetaData getMappingMetaData(String indexName, String documentType) throws IOException{
    //find the mapping
    ClusterState cs = client.admin().cluster().prepareState().setIndices (indexName).execute().actionGet().getState();
    MappingMetaData mdd = cs.getMetaData().index(indexName).mapping (documentType);
    return mdd;
  }
  
  
  @Override
  public Map<String, Object> getMapping(String indexName, String documentType) throws IOException{
    //mind the mapping
    ClusterState cs = client.admin().cluster().prepareState().setIndices (indexName).execute().actionGet().getState();
    MappingMetaData mdd = cs.getMetaData().index(indexName).mapping (documentType);
    return mdd.getSourceAsMap ();
  }
  
  @Override
  public Map<String, Object> numerifyFieldMapping(String indexName, String documentType, String fieldName) throws IOException, IndexLoaderException{
    //TODO: implement
    //1.get the mapping
    MappingMetaData mappingMetaData = getMappingMetaData (indexName, documentType);
    Map<String, Object> mapping = mappingMetaData.getSourceAsMap ();
    
    Map<String, Map> properties = (Map<String, Map>) mapping.get ("properties");
    Map<String, Object> fieldMapping = properties.get(fieldName);
    if(fieldMapping==null){
      throw new IndexLoaderException (String.format ("Field %s does not exist in %s/%s metadata: %s", 
                                                     fieldName, indexName, documentType, mappingMetaData.source ()));
    }
    
        
    //2.check if "num" sub-field exists
    //see if any subfields are defined
    Map<String, Map> subFields = (Map<String, Map>) fieldMapping.get ("fields");
    if(subFields==null){      
      subFields = new LinkedHashMap<String, Map> ();
      fieldMapping.put ("fields", subFields);      
    }
    //2a: yes - return the mapping
    if(!subFields.containsKey (fieldName)){      
      //3 augment mapping with "num" field
      Map<String, String> numericSubfield = new LinkedHashMap<String, String> ();
      numericSubfield.put("type", "long");
      subFields.put ("num", numericSubfield);
    }
    
    //4. return mapping
    return mapping;
  }

  @Override 
  public Map<String, Object> numerifyField(String publicIndexName, String documentType, String fieldName, BulkProcessor bulkProcessor) throws IndexAdminException, IOException, IndexLoaderException{
    //find out the internalIndexName
    String curInternalName = getIndexNameForAlias (publicIndexName);   
    
    //get the numerified mapping    
    Map<String, Object> newMapping = numerifyFieldMapping (curInternalName, documentType, fieldName);
        
    //create new index with new mapping (generate index name to reflect new version)
    String newInternalName = publicIndexName+"_"+UUID.randomUUID ();
    CreateIndexResponse createIndexResponse = createIndex (newInternalName, documentType, newMapping);
    if(!createIndexResponse.isAcknowledged())
      throw new IndexAdminException (String.format("Unable to numerifyField %s. Error creating index %s with type %s and mapping %s", 
                                                   fieldName, newInternalName, documentType, newMapping));
      
    //copy data from internalIndexName
    BulkProcessorFactory bulkFactory = new SimpleBulkProcessorFactory (client);
    TypeCopier copier = new TypeCopierImpl (client, bulkProcessor);
    copier.process (publicIndexName, documentType, newInternalName);
    
    //point alias to new internal index
    redirectIndexAlias (publicIndexName, curInternalName, newInternalName);
    
    return newMapping;
  }
  
  @Override
  public IndicesAliasesResponse putIndexAlias(String indexName, String alias){
    IndicesAliasesRequestBuilder builder = client.admin().indices ().prepareAliases ().addAlias (indexName, alias);
    IndicesAliasesResponse response = builder.execute ().actionGet ();
    return response;
  }
  
  @Override
  public IndicesAliasesResponse removeIndexAlias(String indexName, String alias){
    IndicesAliasesRequestBuilder builder = client.admin().indices ().prepareAliases ().removeAlias (indexName, alias);
    IndicesAliasesResponse response = builder.execute ().actionGet ();
    return response;
  }
  
  @Override
  public IndicesAliasesResponse redirectIndexAlias(String alias, String oldIndexName, String newIndexName){
    IndicesAliasesRequestBuilder builder = client.admin().indices ().prepareAliases ()
            .removeAlias (oldIndexName, alias)
            .addAlias (newIndexName, alias);    
    IndicesAliasesResponse response = builder.execute ().actionGet ();
    return response;
  }
  
  
  @Override
  public List<AliasMetaData> getIndexAliases(String indexName){
    GetAliasesRequestBuilder aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ().setIndices (indexName);
    GetAliasesResponse getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();
    return getAliasesResponse.getAliases ().get (indexName);
  }
  
  
  @Override
  public String getIndexNameForAlias(String alias) throws IndexAdminException{
    GetAliasesRequestBuilder aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ().setIndices (alias);
    GetAliasesResponse getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();
    String[] aliases = getAliasesResponse.getAliases ().keys ().toArray (String.class);    
    if(aliases.length==1)
      return aliases[0];
    else
      throw new IndexAdminException (String.format ("Could not find index for alias %s, query returned: %s", alias, getAliasesResponse.getAliases ()));
  }

  
  
}
