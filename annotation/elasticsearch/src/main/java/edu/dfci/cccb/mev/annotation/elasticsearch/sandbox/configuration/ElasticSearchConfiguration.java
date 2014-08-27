package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;
import lombok.extern.log4j.Log4j;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

@Log4j
@Configuration
@ComponentScan(value="edu.dfci.cccb.mev.annotation.elasticsearch", includeFilters = @Filter (type = ANNOTATION, value = {Component.class }))
public class ElasticSearchConfiguration {

  
  
//  @Bean(name="es-client-node")
//  public Node getClientNode(){
//    log.info("Init ElasticSearch Node");
//    Node node = NodeBuilder.nodeBuilder ()
//            .local (true)
//            .clusterName ("elasticsearch")
//            .client (true).build ();
//    log.info ("***********Node: "+node);
//    return node;
//  }
  
//  @Inject
//  @Bean(name="es-client")
//  public Client getClient(Node node){
//    log.info("Get ElasticSearch Client");
//    return node.client ();
//  }

  @Bean(name="es-transport-client")
  public Client getTransportClient(){
    Client client = new TransportClient ().addTransportAddress (
                                                                new InetSocketTransportAddress ("anton-masha.dfci.harvard.edu", 9300));
    
    log.debug("****Init Transport Client: "+ client);
    
    return client;
  }
  
  
}
