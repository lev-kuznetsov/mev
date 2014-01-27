package edu.dfci.cccb.mev.presets.tcga;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.simple.SimplePresetDescriptor;
import edu.dfci.cccb.mev.presets.tcga.contract.TcgaPreset;
import edu.dfci.cccb.mev.presets.tcga.prototype.AbstractTcgaPreset;

@Accessors(fluent=true)
public class TcgaPresetMetafile extends AbstractTcgaPreset {

  private @Inject @Named ("tcgaPresetRoot") URL tcgaPresetRoot;
  private final String filename;
  private final String path;
  private final @Getter String name;
  private final @Getter String disease;
  private final @Getter String diseaseName;
  private final @Getter String platform;
  private final @Getter String platformName;   
  private final PresetDescriptor descriptor;
  
  public TcgaPresetMetafile(String filename, String path, String disease, String diseaseName, String platform, String platformName) throws MalformedURLException{
    this.filename=filename;
    this.path=path;
    this.name=filename;
    this.disease=disease;
    this.diseaseName=diseaseName;
    this.platform=platform;
    this.platformName=platformName;
    this.descriptor = new SimplePresetDescriptor (getDataUrl (), getColumnUrl ()); 
  }
  
  private URL getDataUrl() throws MalformedURLException{
    return new URL("file:"+tcgaPresetRoot+"/"+path+"/"+filename);
  }
  private URL getColumnUrl() throws MalformedURLException{
    
    return new URL("file:"+tcgaPresetRoot+"/"+disease+"/clinical/"+disease+".clinical_annotations.tsv");
  }

  @Override
  public PresetDescriptor getDescriptor () {
      return descriptor;
  }
  
  
}
  
