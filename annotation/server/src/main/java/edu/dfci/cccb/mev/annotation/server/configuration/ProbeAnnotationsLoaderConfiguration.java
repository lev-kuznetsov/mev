package edu.dfci.cccb.mev.annotation.server.configuration;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.configuration.util.archaius.ArchaiusConfig;
import edu.dfci.cccb.mev.configuration.util.contract.Config;

@Log4j
@Configuration
//@PropertySources({
//@PropertySource(value={"classpath:loader/probe_annotations.loader.properties"}),
//@PropertySource(value="classpath:loader/probe_annotations.loader-${spring_profiles_active}.properties", ignoreResourceNotFound=true)
//})
@Import(ProbeAnnotationsPersistenceConfiguration.class)
public class ProbeAnnotationsLoaderConfiguration {
  Config environment = new ArchaiusConfig ("loader/probe_annotations.loader.properties");
//  @Inject Environment environment;
  public static final String MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER="mev.annotations.probe.affymetrix.folder";
  public static final String MEV_PROBE_ANNOTATIONS_AFFYMETRIX_SUFFIX="mev.annotations.probe.affymetrix.file.suffix";
  
  
  @Lazy @Bean(name="probe-annotations-filesystem-loader")
  public ProbeAnnotationsLoader getProbeAnnotationsFileSystemLoader(
                                                     @Named("probe-annotations-root") URL root,
                                                     @Named("probe-annotations-datasource") DataSource dataSource) throws SQLException, IOException, URISyntaxException, AnnotationException{
    //find annotation folder
    String annotationsFolder = environment.getProperty (MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER);
    log.info (MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER+" property is set to:" + annotationsFolder);
    
    if(annotationsFolder.equals ("")==false && annotationsFolder.endsWith ("/")==false)
      annotationsFolder+="/";    
    
    URL annotationFolderURL = new URL(root, annotationsFolder);
    log.info ("annotationFolderURL.toString():" + annotationFolderURL.toString ());
    
    //String fileSuffix = environment.getProperty((MEV_PROBE_ANNOTATIONS_AFFYMETRIX_SUFFIX), "*.annot.out.tsv");    
    ProbeAnnotationsLoader loader = new H2ProbeAnnotationsLoader (dataSource);
        
    return loader;
  }

  
  @Lazy @Bean(name="probe-annotations-platforms-loader") 
  public ProbeAnnotationsLoader getProbeAnnotationsLoader(@Named("probe-annotations-datasource") DataSource dataSource) throws SQLException, IOException, URISyntaxException, AnnotationException{    
    
    ProbeAnnotationsLoader loader = new H2ProbeAnnotationsLoader (dataSource);
            
    return loader;
  }
  
}
