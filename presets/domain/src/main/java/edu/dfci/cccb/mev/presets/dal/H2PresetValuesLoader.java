package edu.dfci.cccb.mev.presets.dal;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

public class H2PresetValuesLoader implements PresetValuesLoader {

   
  @Getter @Setter @Inject @Named("presets-datasource") private DataSource dataSource;
  
  public H2PresetValuesLoader(DataSource dataSource){
    this.dataSource=dataSource;
  }
  
  @Override
  public void loadAll (Presets presets) throws PresetException {
    

  }

  @Override
  public void load (Preset preset) throws PresetException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void load (URL url) throws PresetException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void load (PresetDescriptor descriptor) throws PresetException {
    // TODO Auto-generated method stub
    
  }

}
