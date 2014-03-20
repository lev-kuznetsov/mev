package edu.dfci.cccb.mev.presets.tools.loader;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetDomainBuildersConfiguration;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.util.timer.Timer;

@Log4j
@Component
public class PresetsLoaderApp {

  @Inject @Named("mev-presets-loader") PresetValuesLoader loader;
  @Inject Presets presets;
  
  public static void main (String[] args) throws PresetException {
    try(final AnnotationConfigApplicationContext springContext = new AnnotationConfigApplicationContext ()){
      springContext.register(DatasetDomainBuildersConfiguration.class);
      springContext.register (PresetsLoaderConfiguration.class);
      springContext.refresh ();
      final PresetsLoaderApp app = springContext.getBean (PresetsLoaderApp.class);
      app.run (args);
    }    
  }
  private boolean checkInclude(String[] includeFilters, Preset preset){
    for(String filter : includeFilters){
      if(preset.descriptor ().dataUrl ().toString ().contains (filter))
        return true;       
    }
    return false;
  }
  public void run(String[] args){
    String[] includeFilters = new String[0];
    if(args.length>0)
      includeFilters=args[0].split (",");
    
    Timer timerTotal = Timer.start("toal import time");
    for(Preset preset : presets.getAll ()){
      if(includeFilters.length>0 && checkInclude (includeFilters, preset)==false)
        continue;      
      try {
        log.info (preset.descriptor ().dataUrl ());      
        Timer timer = Timer.start ("import "+preset.descriptor ().dataUrl ());
        loader.load (preset);
        timer.read();
      } catch (PresetException e) {
        log.error ("Error while loading preset "+preset.descriptor ().dataUrl ());
        e.printStackTrace();
      }            
    }
    
    timerTotal.read ();
    log.info ("exit");
  }

}

