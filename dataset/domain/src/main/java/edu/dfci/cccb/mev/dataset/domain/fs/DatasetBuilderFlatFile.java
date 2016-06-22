package edu.dfci.cccb.mev.dataset.domain.fs;

import edu.dfci.cccb.mev.dataset.domain.contract.*;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnnotation;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDataset;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.io.implementation.TemporaryFolder;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j
public class DatasetBuilderFlatFile extends AbstractDatasetBuilder{

  private Map<String, Integer> createKeyMap(List<String> keys){
    LinkedHashMap<String, Integer> keyMap = new LinkedHashMap<String, Integer>(keys.size());
    Integer i=0;
    for(String key : keys)
      keyMap.put(key, i++);
    return keyMap;
  }

  public Dataset build (RawInput binary,
                        String datasetName,
                        List<String> columnList,
                        List<String> rowList,
                        List<Selection> columnSelections,
                        List<Selection> rowSelections) throws DatasetException {
    try{
      File dataFile = new File(new TemporaryFolder("-binary"), datasetName);
      Files.copy(binary.input (), dataFile.toPath());

      if(log.isDebugEnabled ())
        log.debug ("Creating dataset from FLAT FILE:"+dataFile.getName());

      Map<String, Integer> rowKeys = createKeyMap(rowList);
      Map<String, Integer> columnKeys = createKeyMap(columnList);

      Dimension rows = new SimpleDimension(Dimension.Type.ROW, rowList, new ArrayListSelections(), new AbstractAnnotation() {});
      Dimension columns = new SimpleDimension(Dimension.Type.COLUMN, columnList, new ArrayListSelections(), new AbstractAnnotation() {});
      for(Selection selection : rowSelections)
        rows.selections().put(selection);
      for(Selection selection : columnSelections)
        columns.selections().put(selection);

      Values values = new FlatFileValues (dataFile,
              rowKeys,
              columnKeys ,
              rowKeys.size (),
              columnKeys.size ());

      return aggregate (datasetName, values, super.analyses (), columns, rows);

    }catch(IOException | DatasetBuilderException | InvalidDatasetNameException e){
      throw new DatasetException ("Error while building dataset name="+datasetName
              +",input"+ binary
              +",columns"+ columnList +",rowSelection"+ rowList, e);
    }

  }

  @Override
  protected Dataset aggregate (String name, Values values, Analyses analyses, Dimension... dimensions) throws DatasetBuilderException,
                                                                                                      InvalidDatasetNameException {
    return new SimpleDataset (name, values, analyses, dimensions);
  }
}
