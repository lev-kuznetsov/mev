package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;

public interface IndexAdminHelper {
  public boolean exists(String indexName);
  public void deleteIndex(String indexName);
  public void createIndex(String indexName);
  public CreateIndexResponse createIndex (String indexName, String documentType, XContentBuilder mappingBuilder);  
  public PutMappingResponse putMapping (String indexName, String documentType, XContentBuilder mappingBuilder);
  Map<String, Object> getMapping (String indexName, String documentType) throws IOException;
  MappingMetaData getMappingMetaData (String indexName, String documentType) throws IOException;
  Map<String, Object> numerifyFieldMapping (String indexName, String documentType, String fieldName) throws IOException,
                                                                                                    IndexLoaderException;
  PutMappingResponse putMapping (String indexName, String documentType, Map<String, Object> mappingBuilder);
  String getIndexNameForAlias (String alias) throws IndexAdminException;  
  IndicesAliasesResponse putIndexAlias (String indexName, String alias);
  List<AliasMetaData> getIndexAliases (String indexName);  
}
