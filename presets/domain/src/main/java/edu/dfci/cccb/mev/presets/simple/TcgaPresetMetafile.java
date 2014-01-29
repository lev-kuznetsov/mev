package edu.dfci.cccb.mev.presets.simple;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.prototype.AbstractTcgaPreset;

@Accessors(fluent=true)
@EqualsAndHashCode
@ToString
@Named
public class TcgaPresetMetafile extends AbstractTcgaPreset {

  private @Setter  @Inject @Named ("tcgaPresetRoot") URL tcgaPresetRoot;
  private String filename;
  private String path;
  private @Getter String name;
  private @Getter String disease;
  private @Getter String diseaseName;
  private @Getter String platform;
  private @Getter String platformName;   
  private PresetDescriptor descriptor;
  
  public TcgaPresetMetafile(){}
  
  @Override
  public Preset init(Object[] values) throws PresetException{
      return init(
          (String)values[0],
          (String)values[1],    
          (String)values[2],
          (String)values[3],
          (String)values[4],
          (String)values[5]    
        );
  }
  
  public Preset init(String filename, String path, String disease, String diseaseName, String platform, String platformName) throws PresetException{
    this.filename=filename;
    this.path=path;
    this.name=filename;
    this.disease=disease;
    this.diseaseName=diseaseName;
    this.platform=platform;
    this.platformName=platformName;
    this.descriptor = new SimplePresetDescriptor (tcgaPresetRoot, getDataUrlSpec (), getColumnUrlSpec ()); 
    return this;
  }
   
  private String getDataUrlSpec() {
    return path+"/"+filename;
  }
  private String getColumnUrlSpec() {
    
    return disease+"/clinical/"+disease+".clinical_annotations.tsv";
  }

  @Override
  public PresetDescriptor getDescriptor () {
      return descriptor;
  }
  
  
}
  
