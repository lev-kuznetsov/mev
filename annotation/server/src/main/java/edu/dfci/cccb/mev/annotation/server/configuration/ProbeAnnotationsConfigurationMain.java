package edu.dfci.cccb.mev.annotation.server.configuration;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.context.annotation.ScopedProxyMode.NO;

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
import org.springframework.context.annotation.Scope;
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
@Import({ProbeAnnotationsPersistenceConfiguration.class, ProbeAnnotationsLoaderConfiguration.class})
public class ProbeAnnotationsConfigurationMain {
  public static final String MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX="mev.annotations.probe.";  
  

  @Inject Environment environment;
  public static final String MEV_PROBE_ANNOTATIONS_PLATFORM_METAFILE="mev.annotations.probe.root.metadata.file";
  public static final String MEV_PROBE_ANNOTATIONS_ROOT_FOLDER_URL="mev.annotations.probe.root.url";
  public static final String MEV_PROBE_ANNOTATIONS_RELOAD_FLAG="mev.annotations.probe.reload.flag";
  
  @Bean(name="probe-annotations-root")
  public URL probeAnnotationsRoot() throws IOException{
    String probeAnnotationsRoot = environment.getProperty (MEV_PROBE_ANNOTATIONS_ROOT_FOLDER_URL);
    log.info ("**** Probe Annotations Root Config ****");
    log.info (MEV_PROBE_ANNOTATIONS_ROOT_FOLDER_URL+" property is set to:" + probeAnnotationsRoot);            
    if(probeAnnotationsRoot.endsWith ("/")==false)
      probeAnnotationsRoot+="/";    
    
    URL probeAnnotationsRootURL = ResourceUtils.getURL (probeAnnotationsRoot);
    log.info ("URL.toString():" + probeAnnotationsRootURL.toString ());
    
    if(!CCCPHelpers.UrlUtils.checkExists(probeAnnotationsRootURL))
      throw new IOException ("Probe Annotations Root URL not found: " + probeAnnotationsRootURL.toString ());

    return probeAnnotationsRootURL;    
  }

  
  @Bean(name="probe-annotatinos-platforms-metafile") @Profile("!test")
  public URL probeAnnotationsPlatformsMetafile() throws MalformedURLException, IOException{
    String probeAnnotationsPlatformsMetafile = environment.getProperty (MEV_PROBE_ANNOTATIONS_PLATFORM_METAFILE);
    URL probeAnnotationsMetafileURL = ResourceUtils.getURL (probeAnnotationsPlatformsMetafile);
    if(probeAnnotationsMetafileURL.getProtocol ().equals (""))
      probeAnnotationsMetafileURL=new URL(probeAnnotationsRoot (), probeAnnotationsPlatformsMetafile);
    log.info ("probe-annotatinos-platforms-metafile:" + probeAnnotationsMetafileURL.toString ());
    
    if(!CCCPHelpers.UrlUtils.checkExists(probeAnnotationsMetafileURL))
      throw new IOException ("Probe Annotations Platforms Metafile URL not found: " + probeAnnotationsMetafileURL.toString ());

    return probeAnnotationsMetafileURL;    
  }

  @Bean 
  public ProbeAnnotationsFactory probeAnnotationsFactory(){
    return new DBProbeAnnotationsFactory ();
  }
   
  @Bean
  //@Scope(value=SCOPE_PROTOTYPE, proxyMode=NO)
  public ProbeAnnotationPlatformFactory platformFactory(){
    return new ProbeAnnotationPlatformFactory() {
      @Override @SneakyThrows
      public ProbeAnnotationPlatform create (Object[] values) {
        return new MetafileProbeAnnotationPlatform (values, probeAnnotationsRoot(), probeAnnotationsFactory ()); 
      }
    };
  }
  
  @Bean @Inject
  public ProbeAnnotationPlatforms probeAnnotationPlatforms(@Named("probe-annotations-platforms-loader")ProbeAnnotationsLoader loader) throws MalformedURLException, IOException, AnnotationException{
    ProbeAnnotationPlatforms probeAnnotationPlatforms = new MetafileProbeAnnotationPlatforms(probeAnnotationsPlatformsMetafile(), platformFactory ());
    
    String sReload = environment.getProperty (MEV_PROBE_ANNOTATIONS_RELOAD_FLAG, "false");
    if(Boolean.parseBoolean (sReload)){
      loader.loadAll (probeAnnotationPlatforms);
    }
    
    return probeAnnotationPlatforms;
  }
  
}
