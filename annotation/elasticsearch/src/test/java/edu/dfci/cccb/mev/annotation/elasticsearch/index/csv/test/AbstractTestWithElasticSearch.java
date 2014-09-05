package edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.After;
import org.junit.Before;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class AbstractTestWithElasticSearch {

  protected Node node;
  protected Client client;
  
  @Before
  public void setup () {
      Settings settings = ImmutableSettings.settingsBuilder()
              .put("node.http.enabled", true)
              .put("path.logs","target/elasticsearch/logs")
              .put("path.data","target/elasticsearch/data")
              .put("gateway.type", "none")
              .put("index.store.type", "memory")
              .put("index.number_of_shards", 1)
              .put("index.number_of_replicas", 1).build();
  
      node = NodeBuilder.nodeBuilder().local(true).settings(settings).node();
      client = node.client();
  }

  @After
  public void tearDown () {
    client.close ();
    if(node!=null)
      node.close ();
  }

}