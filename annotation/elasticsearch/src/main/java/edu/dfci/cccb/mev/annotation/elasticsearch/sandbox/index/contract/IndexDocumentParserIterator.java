package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

import java.util.Iterator;

import org.elasticsearch.common.xcontent.XContentBuilder;

public interface IndexDocumentParserIterator extends Iterator<XContentBuilder> {
  public String getId();
}
