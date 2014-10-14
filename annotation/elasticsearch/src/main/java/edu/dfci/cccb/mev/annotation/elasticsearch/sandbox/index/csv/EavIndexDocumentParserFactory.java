package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.IOException;
import java.nio.file.Path;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParser;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParserFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderConfig;

public class EavIndexDocumentParserFactory implements IndexDocumentParserFactory{

  public IndexDocumentParser create(Path file, IndexLoaderConfig config) throws IOException{
    return new EavParser (file, (EavLoaderConfig) config);
  }
}
