package edu.dfci.cccb.mev.annotation.server.configuration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ResourceUtils;

import edu.dfci.cccb.mev.configuration.util.archaius.ArchaiusConfig;
import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.io.utils.CCCPHelpers;

@Log4j
@Configuration
//@PropertySources({
//@PropertySource(value={"classpath:loader/probe_annotations.loader.properties"}),
//@PropertySource(value="classpath:loader/probe_annotations.loader-${spring_profiles_active}.properties", ignoreResourceNotFound=true)
//})
public class ProbeAnnotationsFilesConfiguration {
  public static final String MEV_PROBE_ANNOTATIONS_PLATFORM_METAFILE="mev.annotations.probe.root.metadata.file";
  public static final String MEV_PROBE_ANNOTATIONS_ROOT_FOLDER_URL="mev.annotations.probe.root.url";
  public static final String MEV_PROBE_ANNOTATIONS_RELOAD_FLAG="mev.annotations.probe.reload.flag";
  public static final String MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX="mev.annotations.probe.";  
//  @Inject Environment environment;    
  @Bean(name="probe-annotations-loader-config") 
  public Config getConfig(){    
    return new ArchaiusConfig ("loader/probe_annotations.loader.properties");
  }
  
  @Inject
  @Bean(name="probe-annotations-root")
  public URL probeAnnotationsRoot(@Named("probe-annotations-loader-config") Config environment) throws IOException{
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
  public URL probeAnnotationsPlatformsMetafile(@Named("probe-annotations-loader-config") Config environment) throws MalformedURLException, IOException{
    String probeAnnotationsPlatformsMetafile = environment.getProperty (MEV_PROBE_ANNOTATIONS_PLATFORM_METAFILE);
    log.info (MEV_PROBE_ANNOTATIONS_PLATFORM_METAFILE+" property is set to:" + probeAnnotationsPlatformsMetafile);
    URL probeAnnotationsMetafileURL = ResourceUtils.getURL (probeAnnotationsPlatformsMetafile);
    if(probeAnnotationsMetafileURL.getProtocol ().equals (""))
      probeAnnotationsMetafileURL=new URL(probeAnnotationsRoot (environment), probeAnnotationsPlatformsMetafile);
    log.info ("probe-annotatinos-platforms-metafile:" + probeAnnotationsMetafileURL.toString ());
    
    if(!CCCPHelpers.UrlUtils.checkExists(probeAnnotationsMetafileURL))
      throw new IOException ("Probe Annotations Platforms Metafile URL not found: " + probeAnnotationsMetafileURL.toString ());
       
    return probeAnnotationsMetafileURL;    
  }

  
}
