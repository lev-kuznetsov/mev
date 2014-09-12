package edu.dfci.cccb.mev.annotation.elasticsearch.index.admin.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j;
import net.spy.memcached.internal.BulkFuture;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test.AbstractTestWithElasticSearch;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.SimpleBulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

@Log4j
public class TestIndexAdminHelperImpl extends AbstractTestWithElasticSearch {

  String indexName="test_index";
  BulkProcessorFactory bulkFactory;
  
  @Before
  public void setup() {
    super.setup ();
    bulkFactory = new SimpleBulkProcessorFactory (client);
  };
  
  @Test
  public void testExistsFalse () {
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    assertFalse(helper.exists (indexName));
  }
  @Test
  public void testExistsTrue () {
    //create index
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    createIndexRequestBuilder.execute().actionGet();
    
    //test exists
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    assertTrue(helper.exists (indexName));
  }
  
  @Test
  public void testExistsByAliasTrue () {
    //create index
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    createIndexRequestBuilder.execute().actionGet();
    
    //create alias
    String alias = "test_alias";
    IndicesAliasesRequestBuilder builder = client.admin().indices ().prepareAliases ().addAlias (indexName, alias);
    IndicesAliasesResponse response = builder.execute ().actionGet ();
    assertTrue(response.isAcknowledged ());
        
    //test exists
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    assertTrue(helper.exists (alias));
  }


  @Test
  public void testDeleteIndex () {
    //create index
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    createIndexRequestBuilder.execute().actionGet();
    
    //make sure create worked
    final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(res.isExists());
    
    //delete index
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    helper.deleteIndex (indexName);
    
    //make sure deletion worked
    final IndicesExistsResponse resAfterDelete = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertFalse(resAfterDelete.isExists());
    
  }

  @Test
  public void testCreateIndexString () {
    //create index
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    helper.createIndex (indexName);
    
    //make sure creation worked
    final IndicesExistsResponse resAfterDelete = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(resAfterDelete.isExists());
  }

  @Test
  public void testCreateIndexWithMappingXBuilder() throws IOException {
    //create mapping json
    XContentBuilder mapping =  XContentFactory.jsonBuilder().startObject()
            .startObject ("_id")
            .field ("path", "id")
            .endObject ()
            .startObject("properties")
            .startObject("test_field")
            .field("type", "string")
            .field("index", "not_analyzed")
            .field("store", "yes")
            .endObject()
        .endObject().endObject();
    
    //create index
    String typeName="test_type";
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    CreateIndexResponse response = helper.createIndex (indexName, typeName, mapping);
   
    //make sure index creation worked
    final IndicesExistsResponse resExists = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(resExists.isExists());    
    //make sure mapping propagated through he cluster
    assertTrue(response.isAcknowledged ());
    
    //get the mapping
    ClusterState cs = client.admin().cluster().prepareState().setIndices (indexName).execute().actionGet().getState();
    IndexMetaData imd = cs.getMetaData().index(indexName);
    MappingMetaData mdd = imd.mapping(typeName);
    log.debug("***********mappingGetResponse:"+mdd.source ());
    assertEquals ("{\"test_type\":{\"_id\":{\"path\":\"id\"},\"properties\":{\"test_field\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"store\":true}}}}"
                  , mdd.source ().toString ());
  }

  @Test
  public void testCreateIndexWithMappingMAP() throws IOException {
    //create mapping json
    XContentBuilder mappingJson =  XContentFactory.jsonBuilder().startObject()
            .startObject ("_id")
            .field ("path", "id")
            .endObject ()
            .startObject("properties")
            .startObject("test_field")
            .field("type", "string")
            .field("index", "not_analyzed")
            .field("store", "yes")
            .endObject()
        .endObject().endObject();
    
    Map<String, Object> mapping =  new LinkedHashMap<String, Object> ();
    Map<String, Object> _id =  new LinkedHashMap<String, Object> ();
    _id.put ("path", "id");
    mapping.put ("_id", _id);
    Map<String, Object> properties =  new LinkedHashMap<String, Object> ();
    mapping.put("properties", properties);
    Map<String, Object> test_field =  new LinkedHashMap<String, Object> ();
    properties.put ("test_field", test_field);    
    test_field.put("type", "string");
    test_field.put("index", "not_analyzed");
    test_field.put("store", "yes");
    
    
    //create index
    String typeName="test_type";
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    CreateIndexResponse response = helper.createIndex (indexName, typeName, mapping);
   
    //make sure index creation worked
    final IndicesExistsResponse resExists = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(resExists.isExists());    
    //make sure mapping propagated through he cluster
    assertTrue(response.isAcknowledged ());
    
    //get the mapping
    ClusterState cs = client.admin().cluster().prepareState().setIndices (indexName).execute().actionGet().getState();
    IndexMetaData imd = cs.getMetaData().index(indexName);
    MappingMetaData mdd = imd.mapping(typeName);
    log.debug("***********mappingGetResponse:"+mdd.source ());
    assertEquals ("{\"test_type\":{\"_id\":{\"path\":\"id\"},\"properties\":{\"test_field\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"store\":true}}}}"
                  , mdd.source ().toString ());
  }

  
  @Test
  public void testPutMappingXContentBuilder () throws IOException {
    //create mapping json
    XContentBuilder mapping =  XContentFactory.jsonBuilder().startObject().startObject("properties")
            .startObject("test_field")
            .field("type", "string")
            .field("index", "not_analyzed")
            .field("store", "yes")
            .endObject()
        .endObject().endObject();
    
    //create index
    String documentType="test_type";
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
   
    //make sure index creation worked
    final IndicesExistsResponse resExists = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(resExists.isExists());    
    //make sure mapping propagated through he cluster
    assertTrue(response.isAcknowledged ());
    
    //add the mapping
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    helper.putMapping (indexName, documentType, mapping);
    
    //get the mapping
    ClusterState cs = client.admin().cluster().prepareState().setIndices (indexName).execute().actionGet().getState();
    IndexMetaData imd = cs.getMetaData().index(indexName);
    MappingMetaData mdd = imd.mapping(documentType);
    log.debug("***********mappingGetResponse:"+mdd.source ());
    assertEquals ("{\"test_type\":{\"properties\":{\"test_field\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"store\":true}}}}"
                  , mdd.source ().toString ());
  }

  @Test
  public void testPutMappingMap () throws IOException {
    //create mapping json
    Map<String, Object> mapping =  new LinkedHashMap<String, Object> (); 
    Map<String, Object> properties =  new LinkedHashMap<String, Object> ();
    mapping.put("properties", properties);
    Map<String, Object> test_field =  new LinkedHashMap<String, Object> ();
    properties.put ("test_field", test_field);    
    test_field.put("type", "string");
    test_field.put("index", "not_analyzed");
    test_field.put("store", "yes");

    //create index
    String documentType="test_type";
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
   
    //make sure index creation worked
    final IndicesExistsResponse resExists = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(resExists.isExists());    
    //make sure mapping propagated through he cluster
    assertTrue(response.isAcknowledged ());
    
    //add the mapping
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    helper.putMapping (indexName, documentType, mapping);
    
    //get the mapping
    ClusterState cs = client.admin().cluster().prepareState().setIndices (indexName).execute().actionGet().getState();
    IndexMetaData imd = cs.getMetaData().index(indexName);
    MappingMetaData mdd = imd.mapping(documentType);
    log.debug("***********mappingGetResponse:"+mdd.source ());
    assertEquals ("{\"test_type\":{\"properties\":{\"test_field\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"store\":true}}}}"
                  , mdd.source ().toString ());
  }
  
  @Test
  public void testGetMapping() throws IOException{
    //create mapping json
    XContentBuilder mapping =  XContentFactory.jsonBuilder().startObject().startObject("properties")
            .startObject("test_field")
            .field("type", "string")
            .field("index", "not_analyzed")
            .field("store", "yes")
            .endObject()
        .endObject().endObject();
    
    //create index
    String documentType="test_type";
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
   
    //make sure index creation worked
    final IndicesExistsResponse resExists = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(resExists.isExists());    
    //make sure mapping propagated through he cluster
    assertTrue(response.isAcknowledged ());
    
    //add the mapping
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    helper.putMapping (indexName, documentType, mapping);

    Map<String, Object> mappingResponse = helper.getMapping (indexName, documentType);
////    assertEquals ("{\"test_type\":{\"properties\":{\"test_field\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"store\":true}}}}"
////                  , mmd.source ().toString ());
//    
    Map<String, Object> fieldMap = (Map<String, Object>) ((Map<String, Object>) mappingResponse.get ("properties")).get ("test_field");
    assertEquals ("string", fieldMap.get ("type"));
    assertEquals ("not_analyzed", fieldMap.get ("index"));
    assertEquals (true, fieldMap.get ("store"));
    
  }
  
  @Test
  public void testGetMappingMetaData() throws IOException{
    //create mapping json
    XContentBuilder mapping =  XContentFactory.jsonBuilder().startObject().startObject("properties")
            .startObject("test_field")
              .field("type", "string")
              .field("index", "not_analyzed")
              .field("store", "yes")
              .startObject ("fields")
                .startObject ("num")
                  .field ("type", "long")
                  .field ("ignore_malformed", "true")
                .endObject ()
              .endObject ()             
            .endObject()
        .endObject().endObject();
    
    //create index
    String documentType="test_type";
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
   
    //make sure index creation worked
    final IndicesExistsResponse resExists = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(resExists.isExists());    
    //make sure mapping propagated through he cluster
    assertTrue(response.isAcknowledged ());
    
    //add the mapping
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    helper.putMapping (indexName, documentType, mapping);

    MappingMetaData mmd = helper.getMappingMetaData (indexName, documentType);
    log.debug ("MappingMetaData:"+mmd.source ());   
    assertNotNull (mmd);
     
  }

  @Test
  public void testGetMappingMetaDataDynamic() throws IOException{
    
    //create index
    String documentType="test_type";
//    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
//    CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
   
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    String internalIndexName = helper.createIndex (indexName);
    
    //make sure index creation worked
    final IndicesExistsResponse resExists = client.admin().indices().prepareExists(internalIndexName).execute().actionGet();
    assertTrue(resExists.isExists());    
    //make sure mapping propagated through he cluster
//    assertTrue(response.isAcknowledged ());
   
    String fieldName = "test_field";
    int numOfDocs = 11;
    Map<String, Integer> validIds = new HashMap <String, Integer> (numOfDocs); 
    for(Integer i=0;i<numOfDocs;i++){
      IndexResponse indexResponse = client.prepareIndex (indexName, documentType, i.toString ())
              .setSource (String.format ("{id:%s, %s:\"%s\"}", i, fieldName, i*10))
      .execute ().actionGet ();
    
      assertTrue(indexResponse.isCreated ());
      validIds.put (i.toString (), i);
    } 
        
    MappingMetaData mmd = helper.getMappingMetaData (internalIndexName, documentType);
    log.debug ("**** MappingMetaData:"+mmd.source ());   
    assertNotNull (mmd);
     
  }

  
  @Test
  public void testNumerifyFieldMapping() throws IOException, IndexLoaderException{
    //create mapping
    testGetMapping();
        
    String documentType="test_type";
    
    //add numeric field to the mapping
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    Map<String, Object> mapping = helper.numerifyFieldMapping (indexName, documentType, "test_field") ;
    
////    assertEquals ("{\"test_type\":{\"properties\":{\"test_field\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"store\":true}}}}"
////                  , mmd.source ().toString ());
//    
    log.debug(String.format("**** NUMERIFIED MAPPING: %s", mapping));
    Map<String, Object> fieldMap = (Map<String, Object>) ((Map<String, Object>) mapping.get ("properties")).get ("test_field");
    assertEquals ("string", fieldMap.get ("type"));
    assertEquals ("not_analyzed", fieldMap.get ("index"));
    assertEquals (true, fieldMap.get ("store"));
    Map<String, Map> subFields = (Map<String, Map>) fieldMap.get("fields");
    Map<String, Map> subField = (Map<String, Map>) subFields.get("num");
    assertEquals ("long", subField.get ("type"));
    
  }
    
  @Test
  public void testGetIndexAliases(){
    
    //Create index
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
    
    //Put the alias
    String alias = "test_alias";
    IndicesAliasesRequestBuilder builder = client.admin().indices ().prepareAliases ().addAlias (indexName, alias);
    IndicesAliasesResponse response = builder.execute ().actionGet ();
    assertTrue(response.isAcknowledged ());
    
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);    
    List<AliasMetaData> aliases = helper.getIndexAliases (indexName);
    
    if(log.isDebugEnabled ())
      for(AliasMetaData aliasMetaData : aliases)
        log.debug(String.format("***** Internal Index Name: %s", aliasMetaData.alias ()));
    
    assertEquals (1, aliases.size ());
    assertEquals (alias, aliases.get (0).alias ());
  }
  
  @Test
  public void testGetIndexNameForAlias() throws IndexAdminException{
    
    //Create index
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
    
    //Put the alias
    String alias = "test_alias";
    IndicesAliasesRequestBuilder builder = client.admin().indices ().prepareAliases ().addAlias (indexName, alias);
    IndicesAliasesResponse response = builder.execute ().actionGet ();
    assertTrue(response.isAcknowledged ());
    
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);    
    String theIndexName = helper.getIndexNameForAlias(alias);
    log.debug(String.format("**** The index name: %s", theIndexName));
    assertEquals (indexName, theIndexName);
  }
  
  @Test
  public void testGetIndexNameForAliasNONE() throws IndexAdminException{
    
    //Create index
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
    
    //Use actual index name as the alias
    String alias = indexName;
    
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);    
    String theIndexName = helper.getIndexNameForAlias(alias);
    log.debug(String.format("**** The index name: %s", alias));
    assertEquals (indexName, theIndexName);
  }
  
  
  @Test
  public void testPutIndexAlias(){
    
    //Create index
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
    
    //Put the alias
    String alias = "test_alias";
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    IndicesAliasesResponse response = helper.putIndexAlias (indexName, alias);
    assertTrue(response.isAcknowledged ());
    
    GetAliasesRequestBuilder aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    GetAliasesResponse getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    List<AliasMetaData> aliases = getAliasesResponse.getAliases ().get (indexName);    
    
    if(log.isDebugEnabled ())
      for(AliasMetaData aliasMetaData : aliases)
        log.debug(String.format("***** Internal Index Name: %s", aliasMetaData.alias ()));
    
    assertEquals (1, aliases.size ());
    assertEquals (alias, aliases.get (0).alias ());
  }
  
  @Test
  public void testRemvoeIndexAlias(){
    
    //Create index
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
    
    //Put the alias
    String alias = "test_alias";
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    IndicesAliasesResponse response = helper.putIndexAlias (indexName, alias);
    assertTrue(response.isAcknowledged ());
    
    GetAliasesRequestBuilder aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    GetAliasesResponse getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    List<AliasMetaData> aliases = getAliasesResponse.getAliases ().get (indexName);    
    
    if(log.isDebugEnabled ())
      for(AliasMetaData aliasMetaData : aliases)
        log.debug(String.format("***** Internal Index Name: %s", aliasMetaData.alias ()));
    
    assertEquals (1, aliases.size ());
    assertEquals (alias, aliases.get (0).alias ());
    
    //Put second alias
    String alias2 = "test_alias2";    
    response = helper.putIndexAlias (indexName, alias2);
    assertTrue(response.isAcknowledged ());
    
    aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    aliases = getAliasesResponse.getAliases ().get (indexName);    
    
    if(log.isDebugEnabled ())
      for(AliasMetaData aliasMetaData : aliases)
        log.debug(String.format("***** Internal Index Name: %s", aliasMetaData.alias ()));
    
    assertEquals (2, aliases.size ());
    assertEquals (alias, aliases.get (0).alias ());
    assertEquals (alias2, aliases.get (1).alias ());
        
    //Remove the alias1        
    response = helper.removeIndexAlias (indexName, alias);        
    //get aliases
    aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    aliases = getAliasesResponse.getAliases ().get (indexName);        
    if(log.isDebugEnabled ())
      for(AliasMetaData aliasMetaData : aliases)
        log.debug(String.format("***** Internal Index Name: %s", aliasMetaData.alias ()));    
    assertEquals (1, aliases.size ());
    assertEquals (alias2, aliases.get (0).alias ());
    
    //Remove the alias2        
    response = helper.removeIndexAlias (indexName, alias2);        
    //get aliases
    aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    aliases = getAliasesResponse.getAliases ().get (indexName);    
    
    assertNull(aliases);
    
  }
  
  @Test
  public void testRedirectIndexAlias(){
    
    //Create index1
    CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
    
    //Put the alias
    String alias = "test_alias";
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    IndicesAliasesResponse response = helper.putIndexAlias (indexName, alias);
    assertTrue(response.isAcknowledged ());
    
    GetAliasesRequestBuilder aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    GetAliasesResponse getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    List<AliasMetaData> aliases = getAliasesResponse.getAliases ().get (indexName);    
    
    if(log.isDebugEnabled ())
      for(AliasMetaData aliasMetaData : aliases)
        log.debug(String.format("***** Internal Index Name: %s", aliasMetaData.alias ()));
    
    assertEquals (1, aliases.size ());
    assertEquals (alias, aliases.get (0).alias ());
            
    //Create index2
    String indexName2 = "test_index2";
    createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName2);
    createIndexResponse = createIndexRequestBuilder.execute().actionGet();

    aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    aliases = getAliasesResponse.getAliases ().get (indexName2);    
    
    assertNull (aliases);
    
    //Redirect the alias            
    response = helper.redirectIndexAlias (alias, indexName, indexName2);        
    //make sure index2 has the alias
    aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    aliases = getAliasesResponse.getAliases ().get (indexName2);        
    if(log.isDebugEnabled ())
      for(AliasMetaData aliasMetaData : aliases)
        log.debug(String.format("***** Internal Index Name: %s", aliasMetaData.alias ()));    
    assertEquals (1, aliases.size ());
    assertEquals (alias, aliases.get (0).alias ());
    //make sure index1 does not
    aliasesRequestBuilder = client.admin ().indices ().prepareGetAliases ();
    getAliasesResponse = aliasesRequestBuilder.execute ().actionGet ();    
    aliases = getAliasesResponse.getAliases ().get (indexName);        
    assertNull (aliases);    
  }
  
  @Test 
  public void testNumerifyField() throws IOException, IndexAdminException, IndexLoaderException{
    
    //Create index 
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    helper.createIndex (indexName);
    
    //populate index
    String documentType="test_type";    
    String fieldName = "test_field";
    int numOfDocs = 11;
    Map<String, Integer> validIds = new HashMap <String, Integer> (numOfDocs); 
    for(Integer i=0;i<numOfDocs;i++){
      IndexResponse indexResponse = client.prepareIndex (indexName, documentType, i.toString ())
              .setSource (String.format ("{id:%s, %s:\"%s\"}", i, fieldName, i*10))
      .execute ().actionGet ();
    
      assertTrue(indexResponse.isCreated ());
      validIds.put (i.toString (), i);
    } 
    
    //numerify field
    Map<String, Object> mapping = helper.numerifyField (indexName, documentType, fieldName, bulkFactory.create (2, 1));    
        
    //test new mapping
    log.debug(String.format("**** NUMERIFIED MAPPING: %s", mapping));
    Map<String, Object> fieldMap = (Map<String, Object>) ((Map<String, Object>) mapping.get ("properties")).get (fieldName);
    assertEquals ("string", fieldMap.get ("type"));        
    Map<String, Map> subFields = (Map<String, Map>) fieldMap.get("fields");
    Map<String, Map> subField = (Map<String, Map>) subFields.get("num");
    assertEquals ("long", subField.get ("type"));
        
  }
  
}
