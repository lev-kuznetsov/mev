package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;

public interface IndexAdminHelper {
  public boolean exists(String indexName);
  public void deleteIndex(String indexName);
  public String createIndex(String indexName);
  public CreateIndexResponse createIndex (String indexName, String documentType, XContentBuilder mappingBuilder);  
  public PutMappingResponse putMapping (String indexName, String documentType, XContentBuilder mappingBuilder);
  Map<String, Object> getMapping (String indexName, String documentType) throws IOException, IndexAdminException;
  MappingMetaData getMappingMetaData (String indexName, String documentType) throws IOException;
  PutMappingResponse putMapping (String indexName, String documentType, Map<String, Object> mappingBuilder);
  String getIndexNameForAlias (String alias) throws IndexAdminException;  
  IndicesAliasesResponse putIndexAlias (String indexName, String alias);
  List<AliasMetaData> getIndexAliases (String indexName);  
  CreateIndexResponse createIndex (String indexName, String documentType, Map<String, Object> mapping);
  IndicesAliasesResponse removeIndexAlias (String indexName, String alias);
  IndicesAliasesResponse redirectIndexAlias (String indexName, String oldAlias, String newAlias);
  Map<String, Object> numerifyField (String publicIndexName,
                                     String documentType,
                                     String fieldName,
                                     BulkProcessor bulkProcessor) throws IndexAdminException,
                                                                 IOException,
                                                                 IndexLoaderException;
  Map<String, Object> numerifyFieldMapping (String indexName, String documentType, String fieldName) throws IOException,
                                                                                                    IndexLoaderException;
  String dummyName (long numOfDocs, long numOfFields);
  Map<String, Object> numerifyField (String publicIndexName,
                                     String documentType,
                                     String fieldName,
                                     BulkProcessor bulkProcessor,
                                     int pageSize) throws IndexAdminException, IOException, IndexLoaderException;
  CreateIndexResponse createInternalIndex (String indexName, String documentType, Map<String, Object> mapping);
  CreateIndexResponse createInternalIndex (String indexName, String documentType, XContentBuilder mapping);
  Map<String, Object> numerifyField2 (String publicIndexName,
                                      String documentType,
                                      String fieldName,
                                      BulkProcessor bulkProcessor,
                                      int pageSize) throws IndexAdminException, IOException, IndexLoaderException;  
}
