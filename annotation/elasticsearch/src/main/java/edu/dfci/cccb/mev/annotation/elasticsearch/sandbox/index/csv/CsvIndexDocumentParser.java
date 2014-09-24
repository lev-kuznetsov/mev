package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import au.com.bytecode.opencsv.CSVReader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParser;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

@Log4j
public class CsvIndexDocumentParser implements IndexDocumentParser, Closeable, AutoCloseable {
  private final CsvIndexLoaderConfig config;
  private final CsvIndexDocumentHelper csvHelper;  
  private final CSVReader reader;
  
  
  public CsvIndexDocumentParser (Path file, CsvIndexLoaderConfig config) throws IOException {
    
    reader = new CSVReader(new InputStreamReader(Files.newInputStream (file)), 
                           config.separator (), config.quoteCharacter(), config.escapeCharacter());
    if (config.firstLineIsHeader()) {
      String[] headerLine;
      if ((headerLine = reader.readNext()) != null) {       
        config.csvFields(Arrays.asList(headerLine));
      }
    }    
    this.csvHelper = new CsvIndexDocumentHelper (config.csvFields (), config.entityIdFields ());
    this.config = config;
  }

  @Override
  public XContentBuilder getMapping () throws IOException {
    return this.csvHelper.createMapping (config.typeName ());
  }
  
  private class CsvIndexDocumentInterator implements Iterator<XContentBuilder>{

    private String[] nextLine;
    
    @Override
    @SneakyThrows({IOException.class})
    public boolean hasNext () {      
      while((nextLine = reader.readNext())!=null){
        log.debug("read line:" + Arrays.asList (nextLine));
        if(nextLine.length>0)
          break;
      }
      return nextLine!=null;
    }

    @Override
    @SneakyThrows({IndexLoaderException.class})
    public XContentBuilder next () {
      return csvHelper.process (nextLine);
    }

    @Override
    public void remove () {
      throw new UnsupportedOperationException(String.format("Remove operation not supported"));
    }    
  }
  
  @Override
  public Iterator<XContentBuilder> iterator () {
    return new CsvIndexDocumentInterator ();
  }

  @Override
  public void close () throws IOException {
    reader.close ();
  }

}
