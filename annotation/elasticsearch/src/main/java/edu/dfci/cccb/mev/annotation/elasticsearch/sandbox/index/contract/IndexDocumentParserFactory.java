package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

import java.io.IOException;
import java.nio.file.Path;

public interface IndexDocumentParserFactory {
  public IndexDocumentParser create(Path file, IndexLoaderConfig config) throws IOException;
}