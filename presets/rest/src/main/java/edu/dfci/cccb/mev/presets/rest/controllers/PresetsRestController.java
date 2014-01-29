package edu.dfci.cccb.mev.presets.rest.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.TcgaPreset;
import edu.dfci.cccb.mev.presets.simple.TcgaPresetMetafile;

@RestController
@RequestMapping(value="/presets")
public class PresetsRestController {

  @Inject Presets presets;
  
  @RequestMapping(value="/tcga")
  public List<Preset> getTcgaPresets(){
    return presets.getAll ();
  }
  
  @RequestMapping(value="/tcga2")
  public List<TcgaPreset> getTcgaPresets2(){
    List<TcgaPreset> result = new ArrayList<TcgaPreset>();
    for(Preset preset : presets.getAll ()){
      result.add ((TcgaPreset)preset);
    }
    return result;
  }
  
  @RequestMapping(value="/tcga3")
  public List<TcgaPresetMetafile> getTcgaPresets3(){
    List<TcgaPresetMetafile> result = new ArrayList<TcgaPresetMetafile>();
    for(Preset preset : presets.getAll ()){
      result.add ((TcgaPresetMetafile)preset);
    }
    return result;
  }
}
