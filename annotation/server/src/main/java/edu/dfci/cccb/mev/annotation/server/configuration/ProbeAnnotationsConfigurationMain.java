package edu.dfci.cccb.mev.annotation.server.configuration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatform;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatformFactory;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatforms;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsFactory;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.domain.probe.factory.DBProbeAnnotationsFactory;
import edu.dfci.cccb.mev.annotation.domain.probe.metafile.MetafileProbeAnnotationPlatform;
import edu.dfci.cccb.mev.annotation.domain.probe.metafile.MetafileProbeAnnotationPlatforms;
import edu.dfci.cccb.mev.io.utils.CCCPHelpers;

@Log4j
@Configuration
@Import({ProbeAnnotationsFilesConfiguration.class, ProbeAnnotationsPersistenceConfiguration.class, ProbeAnnotationsLoaderConfiguration.class})
public class ProbeAnnotationsConfigurationMain {
  
  public static final String MEV_PROBE_ANNOTATIONS_RELOAD_FLAG="mev.annotations.probe.reload.flag";
  @Inject Environment environment;
  
  
  @Bean 
  //@Scope(value=SCOPE_PROTOTYPE, proxyMode=NO)
  @Inject
  public ProbeAnnotationPlatformFactory platformFactory(@Named("probe-annotations-root") final URL probeAnnotationsRoot){
    return new ProbeAnnotationPlatformFactory() {
      @Override @SneakyThrows
      public ProbeAnnotationPlatform create (Object[] values) {
        return new MetafileProbeAnnotationPlatform (values, probeAnnotationsRoot, probeAnnotationsFactory ()); 
      }
    };
  }
  
  @Bean @Inject
  public ProbeAnnotationPlatforms probeAnnotationPlatforms(@Named("probe-annotations-root") final URL probeAnnotationsRoot,
                                                           @Named("probe-annotatinos-platforms-metafile") URL probeAnnotationsPlatformsMetafile,
                                                           @Named("probe-annotations-platforms-loader")ProbeAnnotationsLoader loader) throws MalformedURLException, IOException, AnnotationException{
    
    ProbeAnnotationPlatforms probeAnnotationPlatforms = new MetafileProbeAnnotationPlatforms(platformFactory (probeAnnotationsRoot));    
    probeAnnotationPlatforms.loadFromFile (probeAnnotationsPlatformsMetafile);
    
    String sReload = environment.getProperty (MEV_PROBE_ANNOTATIONS_RELOAD_FLAG, "false");
    if(Boolean.parseBoolean (sReload)){
      loader.loadAll (probeAnnotationPlatforms);
    }
    
    return probeAnnotationPlatforms;
  }
  
  @Bean 
  public ProbeAnnotationsFactory probeAnnotationsFactory(){
    return new DBProbeAnnotationsFactory ();
  } 
   
}
