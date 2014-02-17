package edu.dfci.cccb.mev.annotation.server.configuration;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.ui.velocity.SpringResourceLoader;
import org.springframework.util.ResourceUtils;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.jooq.JooqProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.jooq.JooqProbeAnnotationsLoader;

@Log4j
@Configuration
@PropertySources({
@PropertySource(value={"classpath:loader/probe_annotations.loader-defaults.properties"}),
@PropertySource(value="classpath:loader/probe_annotations.loader-${spring_profiles_active}.properties", ignoreResourceNotFound=true)
})
@Import(ProbeAnnotationsPersistenceConfiguration.class)
public class ProbeAnnotationsLoaderConfiguration {

  @Inject Environment environment;
  public static final String MEV_PROBE_ANNOTATIONS_ROOT_FOLDER_URL="mev.annotations.probe.root.url";  
  public static final String MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER="mev.annotations.probe.affymetrix.folder";
  public static final String MEV_PROBE_ANNOTATIONS_AFFYMETRIX_SUFFIX="mev.annotations.probe.affymetrix.file.suffix";
  
  @Bean(name="probe-annotations-root")
  public URL probeAnnotationsRoot() throws IOException{
    String probeAnnotationsRoot = environment.getProperty (MEV_PROBE_ANNOTATIONS_ROOT_FOLDER_URL);
    log.info ("**** Probe Annotations Root Config ****");
    log.info (MEV_PROBE_ANNOTATIONS_ROOT_FOLDER_URL+" property is set to:" + probeAnnotationsRoot);            
    if(probeAnnotationsRoot.endsWith ("/")==false)
      probeAnnotationsRoot+="/";    
    
    URL probeAnnotationsRootURL = ResourceUtils.getURL (probeAnnotationsRoot);
    
    log.info ("URL.getFile():" + probeAnnotationsRootURL.getFile ());
    log.info ("URL.toString():" + probeAnnotationsRootURL.toString ());
    log.info ("URL.getProtocol ():" + probeAnnotationsRootURL.getProtocol ());
    
    probeAnnotationsRootURL.openStream ().close ();
    return probeAnnotationsRootURL;    
  }
  
  @Bean(name="affymatrix-probe-annotations-loader")
  public ProbeAnnotationsLoader loadProbeAnnotations(@Named(value="probe-annotations-root") URL root,
                                                     @Named("probe-annotations-datasource") DataSource dataSource) throws SQLException, IOException, URISyntaxException{
    
    //find annotation folder
    String annotationsFolder = environment.getProperty (MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER);
    log.info (MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER+" property is set to:" + annotationsFolder);
    
    if(annotationsFolder.equals ("")==false && annotationsFolder.endsWith ("/")==false)
      annotationsFolder+="/";    
    
    URL annotationFolderURL = new URL(root, annotationsFolder);
    log.info ("annotationFolderURL.toString():" + annotationFolderURL.toString ());
    
    String fileSuffix = environment.getProperty((MEV_PROBE_ANNOTATIONS_AFFYMETRIX_SUFFIX), "*.annot.out.tsv");    
    ProbeAnnotationsLoader loader = new JooqProbeAnnotationsLoader (dataSource);
    
    loader.init (annotationFolderURL, fileSuffix);
    return loader;
    
  }
  
  @Bean(name="affymatrix-probe-annotations")
  public ProbeAnnotations getProbeAnnotations(@Named("probe-annotations-datasource") DataSource dataSource) throws MalformedURLException, SQLException{
    return new JooqProbeAnnotations (dataSource);
  }  
}
