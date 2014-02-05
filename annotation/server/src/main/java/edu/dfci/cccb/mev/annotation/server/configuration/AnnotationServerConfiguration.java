package edu.dfci.cccb.mev.annotation.server.configuration;

import static java.io.File.separator;
import static java.lang.System.getProperty;
import static org.springframework.context.annotation.FilterType.ANNOTATION;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.ToString;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.eobjects.metamodel.util.ClasspathResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
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
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.SimpleProbeAnnotations;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;

@Configuration
@ComponentScan (value = "edu.dfci.cccb.mev.annotation",
                includeFilters = @Filter (type = ANNOTATION, value = {
                                                                      Controller.class,
                                                                      ControllerAdvice.class,
                                                                      RestController.class }))
@ToString
public class AnnotationServerConfiguration extends WebMvcConfigurerAdapter {

  private @Inject Workspace workspace;
  private @Inject FileProjectManager sessionProjectManager;
  private @Inject DatasetBuilder builder;
  private @Inject org.springframework.core.env.Environment environment;
  
  public static final String MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX="mev.annotations.probe.";
  public static final String MEV_PROBE_ANNOTATIONS_ROOT_FOLDER=MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"root";
  public static final String MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER=MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"affymetrix.folder";
  
  @Bean
  @Scope (value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public FileProjectManager sessionProjectManager () throws IOException {
    FileProjectManager projectManager = new FileProjectManager ();
    projectManager.setWorkspaceDir (new SessionWorkspaceDir ());
    projectManager.setWorkspace (workspace);
    projectManager.setDatasetBuilder (builder);
    return projectManager;
  }


  @PostConstruct
  public void setProjectmanagerSingleton () {
    ProjectManager.setSingleton (sessionProjectManager);
  }
  
  @Bean(name="probe-annotations")
  public URL probeAnnotationsRoot() throws IOException{
    String probeAnnotationsRoot = environment.getProperty (MEV_PROBE_ANNOTATIONS_ROOT_FOLDER);
    if(probeAnnotationsRoot==null)
      return null;
    if(probeAnnotationsRoot.endsWith ("/")==false){
      probeAnnotationsRoot+="/";
    }
    URL probeAnnotationsRootURL = new URL(probeAnnotationsRoot);
    
    try{
      probeAnnotationsRootURL.openStream ().close ();
      return probeAnnotationsRootURL;
    }catch(IOException e){
      return (new ClassPathResource ("array_annotations")).getURL();
    }   
  }
  
  @Bean(name="affymatrix-probe-annotations")
  public ProbeAnnotations getProbeAnnotations(@Named(value="probe-annotations") URL root,
                                              @Named("probe-annotations-datasource") DataSource dataSource) throws MalformedURLException{
    String annotationsFolder = environment.getProperty (MEV_PROBE_ANNOTATIONS_AFFYMETRIX_FOLDER);
    if(annotationsFolder==null)
      return null;
    
    if(annotationsFolder.endsWith ("/")==false)
      annotationsFolder+="/";
      
    URL annotationFolderURL = new URL(root, annotationsFolder);
    
    return new SimpleProbeAnnotations (annotationFolderURL, dataSource);
  }
  

}
