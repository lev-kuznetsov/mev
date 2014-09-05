package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

import java.io.IOException;
import java.nio.file.Path;

public interface IndexLoader {
  
  public void process() throws IndexLoaderException;
  void processFile (Path entry) throws IOException;
  
}
