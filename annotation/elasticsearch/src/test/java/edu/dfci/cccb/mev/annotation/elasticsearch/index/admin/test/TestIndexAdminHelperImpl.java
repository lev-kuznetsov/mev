package edu.dfci.cccb.mev.annotation.elasticsearch.index.admin.test;

import static org.junit.Assert.*;

import java.io.IOException;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test.AbstractTestWithElasticSearch;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;

@Log4j
public class TestIndexAdminHelperImpl extends AbstractTestWithElasticSearch {

  String indexName="test_index";
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
  public void testCreateIndexWithMapping() throws IOException {
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
  public void testPutMapping () throws IOException {
    //create mapping json
    XContentBuilder mapping =  XContentFactory.jsonBuilder().startObject().startObject("properties")
            .startObject("test_field")
            .field("type", "string")
            .field("index", "not_analyzed")
            .field("store", "yes")
            .endObject()
        .endObject().endObject();
    
    //create index
    String typeName="test_type";
    final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
   
    //make sure index creation worked
    final IndicesExistsResponse resExists = client.admin().indices().prepareExists(indexName).execute().actionGet();
    assertTrue(resExists.isExists());    
    //make sure mapping propagated through he cluster
    assertTrue(response.isAcknowledged ());
    
    //add the mapping
    IndexAdminHelper helper = new IndexAdminHelperImpl (client);
    helper.putMapping (indexName, typeName, mapping);
    
    //get the mapping
    ClusterState cs = client.admin().cluster().prepareState().setIndices (indexName).execute().actionGet().getState();
    IndexMetaData imd = cs.getMetaData().index(indexName);
    MappingMetaData mdd = imd.mapping(typeName);
    log.debug("***********mappingGetResponse:"+mdd.source ());
    assertEquals ("{\"test_type\":{\"properties\":{\"test_field\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"store\":true}}}}"
                  , mdd.source ().toString ());
  }

}
