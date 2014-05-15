package edu.dfci.cccb.mev.presets.dataset.fs;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.Analyses;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValues;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDataset;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

@Log4j
public class PresetDatasetBuilderFlatFile extends AbstractDatasetBuilder implements PresetDatasetBuilder {

  private @Inject PresetDimensionBuilder dimensionBuilder;

  public PresetDatasetBuilderFlatFile(PresetDimensionBuilder dimensionBuilder){
    this.dimensionBuilder=dimensionBuilder;
  }
  private Dimension buildRows(PresetDescriptor descriptor, Selection selection) throws PresetException{
    Dimension rows = dimensionBuilder.build (Dimension.Type.ROW, descriptor, selection);    
    if(log.isDebugEnabled ())
      log.debug ("rows="+rows);
    return rows;
  }
  
  private Dimension buildColumns(PresetDescriptor descriptor, Selection selection) throws PresetException{
    Dimension columns = dimensionBuilder.build (Dimension.Type.COLUMN, descriptor, selection);       
    if(log.isDebugEnabled ())
      log.debug ("columns="+columns);
    return columns;
  }
  
  @Override
  public Dataset build (PresetDescriptor descriptor,
                        String datasetName,
                        Selection columnSelection,
                        Selection rowSelection) throws PresetException {
    try{
      if(log.isDebugEnabled ())
        log.debug ("Creating preset dataset from FLAT FILE:"+descriptor.name ());
      
      Dimension rows = buildRows(descriptor, rowSelection);
      Dimension columns = buildColumns (descriptor, columnSelection);
      PresetDescriptorFlatFileAdaptor flatFileDescriptor =  new PresetDescriptorFlatFileAdaptor (descriptor);
      File dataFile = new File(flatFileDescriptor.binaryUrl ().toURI ());
      
      Map<String, Integer> rowKeys = flatFileDescriptor.getRowKeys ();
      Map<String, Integer> columnKeys = flatFileDescriptor.getColumnKeys ();
      
      log.debug("rowKeys:"+rowKeys);
      log.debug("columnKeys:"+columnKeys);
      
      
      Values values = new FlatFileValues (dataFile, 
                                                rowKeys, 
                                                columnKeys ,
                                                rowKeys.size (),
                                                columnKeys.size ());
      if(log.isDebugEnabled ()){
        String firstRow = (String)rowKeys.keySet ().toArray ()[0];
        String firstColumn = (String)columnKeys.keySet ().toArray ()[0];
        log.debug("TOP:LEFT: "+firstRow+":"+firstColumn);
        log.debug("TOP:LEFT:value "+values.get (firstRow, firstColumn));
      }
            
      return aggregate (datasetName, values, super.analyses (), columns, rows);
      
    }catch(IOException | DatasetBuilderException | InvalidDatasetNameException | URISyntaxException | InvalidCoordinateException e ){
      throw new PresetException ("Error while building dataset name="+datasetName
                                 +",descritpor"+descriptor
                                 +",columnsSelection"+columnSelection+",rowSelection"+rowSelection, e);
    }  
    
  }

  @Override
  public Dataset build (Preset preset, 
                        String datasetName, 
                        Selection columnSelection, 
                        Selection rowSelection) throws PresetException {    
   return build(preset.descriptor (), datasetName, columnSelection, rowSelection);
  }
  
  @Override
  protected Dataset aggregate (String name, Values values, Analyses analyses, Dimension... dimensions) throws DatasetBuilderException,
                                                                                                      InvalidDatasetNameException {
    return new SimpleDataset (name, values, analyses, dimensions);
  }
}
