package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;

public interface IndexDocumentParser extends Iterable<XContentBuilder>{
  public XContentBuilder getMapping() throws IOException;
  public IndexDocumentParserIterator iterator();
}
