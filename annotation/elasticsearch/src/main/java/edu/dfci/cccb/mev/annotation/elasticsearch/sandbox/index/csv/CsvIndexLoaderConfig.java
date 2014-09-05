package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderConfig;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;


@Accessors(fluent=true, chain=true)
@RequiredArgsConstructor
public class CsvIndexLoaderConfig implements IndexLoaderConfig {
  private final @Getter String folder;
  private final @Getter String fileFilter;
  private final @Getter String indexName;
  private final @Getter String typeName;
  private @Getter String charset="UTF-8";
  //TODO: allow String separators
  private @Getter char separator='\t';
  private @Getter char quoteCharacter='"';
  private @Getter char escapeCharacter='\\';
  private @Getter @Setter List<String> csvFields;
  private @Getter boolean firstLineIsHeader=true;
  private @Getter String idField="id";
  private @Getter @Setter int bulkSize = 100;
  private @Getter int concurrentRequests=1;
  
  public Path folderPath(){
    return Paths.get (folder);
  }
  public DirectoryStream<Path> getDirectoryStream() throws IndexLoaderException{
    try{
      DirectoryStream<Path> stream = Files.newDirectoryStream(this.folderPath(), this.fileFilter ());
      return stream;
    } catch (IOException e) {
      throw new IndexLoaderException (String.format("Cannot open files for path: %s, filter: %s", this.folderPath(), this.fileFilter ()));
    }
  }
  
}
