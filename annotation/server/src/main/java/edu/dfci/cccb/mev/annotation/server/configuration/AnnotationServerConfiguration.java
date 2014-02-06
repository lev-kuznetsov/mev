package edu.dfci.cccb.mev.annotation.server.configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.refine.ProjectManager;
import com.google.refine.SessionWorkspaceDir;
import com.google.refine.io.FileProjectManager;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.jooq.JooqProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.jooq.JooqProbeAnnotationsLoader;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;

@Configuration
@ComponentScan (value = "edu.dfci.cccb.mev.annotation",
                includeFilters = @Filter (type = ANNOTATION, value = {
                                                                      Controller.class,
                                                                      ControllerAdvice.class,
                                                                      RestController.class }))
@ToString
@PropertySource("classpath:probe_annotations.properties")
public class AnnotationServerConfiguration extends WebMvcConfigurerAdapter {

  private @Inject Workspace workspace;
  private @Inject FileProjectManager sessionProjectManager;
  private @Inject DatasetBuilder builder;
  private @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  private @Inject org.springframework.core.env.Environment environment;
  
  public static final String MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX="mev.annotations.probe.";
  public static final String MEV_PROBE_ANNOTATIONS_ROOT_FOLDER="mev.annotations.probe.root";
  public static final String MEV_PROBE_ANNOTATIONS_MODIFIED_THRESHOLD_MILLISECONDS="mev.annotations.probe.modified.threashold.milliseconds";
  public static final String MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER="mev.annotations.probe.affymetrix.folder";
  public static final String MEV_PROBE_ANNOTATIONS_AFFYMETRIX_SUFFIX="mev.annotations.probe.affymetrix.file.suffix";
  
  @Bean(name="probe-annotations-root")
  public URL probeAnnotationsRoot() throws IOException{
    String probeAnnotationsRoot = environment.getProperty (MEV_PROBE_ANNOTATIONS_ROOT_FOLDER, "classpath:/array_annotations");
    if(probeAnnotationsRoot==null)
      return null;
    if(probeAnnotationsRoot.endsWith ("/")==false){
      probeAnnotationsRoot+="/";
    }
    URL probeAnnotationsRootURL = new URL("file:"+probeAnnotationsRoot);
    
    try{
      probeAnnotationsRootURL.openStream ().close ();
      return probeAnnotationsRootURL;
    }catch(IOException e){
      return (new ClassPathResource ("/array_annotations/")).getURL();
    }   
  }
  
  @Bean(name="affymatrix-probe-annotations-loader")
  public ProbeAnnotationsLoader loadProbeAnnotations(@Named(value="probe-annotations-root") URL root,
                                                     @Named("probe-annotations-datasource") DataSource dataSource) throws SQLException, IOException, URISyntaxException{
    
    //find annotation folder
    String annotationsFolder = environment.getProperty (MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER);
    if(annotationsFolder==null)
      return null;    
    
    if(annotationsFolder.endsWith ("/")==false)
      annotationsFolder+="/";    
    URL annotationFolderURL = new URL(root, annotationsFolder);
    
    String fileSuffix = environment.getProperty((MEV_PROBE_ANNOTATIONS_AFFYMETRIX_SUFFIX), "*.annot.out.tsv");    
    ProbeAnnotationsLoader loader = new JooqProbeAnnotationsLoader (dataSource);
    
    
    
    loader.init (annotationFolderURL, fileSuffix);
    return loader;
    
  }
  
  @Bean(name="affymatrix-probe-annotations")
  public ProbeAnnotations getProbeAnnotations(@Named("probe-annotations-datasource") DataSource dataSource) throws MalformedURLException, SQLException{
    
    return new JooqProbeAnnotations (dataSource);
  }
  
  
  @Bean
  @Scope (value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public FileProjectManager sessionProjectManager () throws IOException, SQLException {
    FileProjectManager projectManager = new FileProjectManager ();
    projectManager.setWorkspaceDir (new SessionWorkspaceDir ());
    projectManager.setWorkspace (workspace);
    projectManager.setDatasetBuilder (builder);
    projectManager.setProbeAnnotations (getProbeAnnotations (dataSource));
    return projectManager;
  }


  @PostConstruct
  public void setProjectmanagerSingleton () {
    ProjectManager.setSingleton (sessionProjectManager);
  }
  
  

}
