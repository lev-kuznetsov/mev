package edu.dfci.cccb.mev.presets.dataset.fs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.dal.TsvReader;
import edu.dfci.cccb.mev.presets.dal.TsvReaderMetaModel;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetDimensionBuilder;

@Log4j
public class PresetDimensionBuilderFlatFile extends AbstractPresetDimensionBuilder implements PresetDimensionBuilder {

  
  private final Map<String, List<String>> rowCasche;
  
  public PresetDimensionBuilderFlatFile(){
    rowCasche = new HashMap<String, List<String>> ();
  }
  
  @Synchronized
  private List<String> getRowKeys(PresetDescriptorFlatFileAdaptor descriptor) throws MalformedURLException{
    if(rowCasche.containsKey (descriptor.name ()))
      return rowCasche.get (descriptor.name ());
    else{
      List<String> keys = getKeys (descriptor.rowListUrl());
      rowCasche.put(descriptor.name (), keys);      
      return keys;
    }
  }
  private List<String> getColumnKeys(PresetDescriptorFlatFileAdaptor descriptor) throws MalformedURLException{
    return getKeys (descriptor.columnListUrl());
  }
  private List<String> getKeys(URL url){
    TsvReader reader = new TsvReaderMetaModel ();
    reader.init (url);
    String[] keys = reader.getColumnNames ();
    List<String> keyList = Arrays.asList (keys);
    return keyList;
  }
  
  @Override
  public Dimension build (Type type, PresetDescriptor descriptor) throws PresetException{
    PresetDescriptorFlatFileAdaptor fsDescriptor = new PresetDescriptorFlatFileAdaptor (descriptor);
    try{
      if(type==Type.COLUMN){
        return new SimpleDimension (type, getColumnKeys(fsDescriptor), new ArrayListSelections (), null);
              
      }else{
        return new SimpleDimension (type, getRowKeys (fsDescriptor), new ArrayListSelections (), null);      
      }
    }catch(MalformedURLException e){
      throw new PresetException ("Error while creating dimension of type "+type+" for descriptor "+descriptor, e);
    }
    
  }

  public Dimension build (Type type, PresetDescriptor descriptor, Selection selection) throws PresetException {    
    if(selection!=null){
      if(log.isDebugEnabled ()){
        log.debug ("selection.size(): " + selection.keys ().size ());
      }
      return new SimpleDimension (type, selection.keys (), new ArrayListSelections (), null);
    }else{
      return build (type, descriptor);
    }
  }
  
}
