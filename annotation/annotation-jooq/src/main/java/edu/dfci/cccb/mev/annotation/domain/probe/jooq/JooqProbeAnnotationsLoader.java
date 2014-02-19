package edu.dfci.cccb.mev.annotation.domain.probe.jooq;

import static edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.Tables.MEV_PROBE_ANNOTATIONS;
import static org.jooq.impl.DSL.using;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationSource;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationSources;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.domain.probe.dal.jooq.Tables;
import edu.dfci.cccb.mev.io.utils.CCCPHelpers;
@Log4j
public class JooqProbeAnnotationsLoader implements ProbeAnnotationsLoader {

  private final DSLContext context;
  
  @Inject
  public JooqProbeAnnotationsLoader ( @Named("probe-annotations-datasource") DataSource dataSource) throws SQLException {
    context = using (dataSource.getConnection ());      
  }
  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.annotation.domain.probe.jooq.ProbeAnnotationsLoader#init(java.net.URL, java.lang.String, long)
   */
  @Override
  public int loadAll(URL rootFolder, String suffix) throws AnnotationException{
    int result = 0;
    
    try{  
      Path rootPath = Paths.get(rootFolder.toURI ());
      if(rootPath==null)
        throw new AnnotationException ("Root Folder "+rootFolder.toURI ()+" not found");
          
      URL checkForceFlagURL = new URL(rootFolder, "reload.flag");
      
      if(CCCPHelpers.UrlUtils.checkExists (checkForceFlagURL)){
        
        if(log.isDebugEnabled ())
          log.debug ("Reloading Probe Annotations from folder "+rootPath);
        
        try(DirectoryStream<Path> ds = Files.newDirectoryStream (rootPath, suffix)){
          
          context.truncate(Tables.MEV_PROBE_ANNOTATIONS).execute();        
          for(Path path : ds){
            loadUrlResource (path.toUri ().toURL ());
            result++;
          }
        }
      }else{
        if(log.isDebugEnabled ())
          log.debug ("Reload flag not set.... will not load probe annotations");
      }
    }catch(AnnotationException e){
      throw e;
    }catch(URISyntaxException | IOException e){
      throw new AnnotationException (e);
    }
    return result;
  }
  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.annotation.domain.probe.jooq.ProbeAnnotationsLoader#loadUrlResource(java.net.URL)
   */
  @Override
  public void loadUrlResource(URL url) throws AnnotationException{
    if(log.isDebugEnabled ())
      log.debug ("Importing Probe Annotations file"+url);

    try{
    context.loadInto(MEV_PROBE_ANNOTATIONS)
    .onDuplicateKeyError ()
    .loadCSV(url.openStream ())
    .fields(MEV_PROBE_ANNOTATIONS.fields ())
    .separator ('\t')
    .ignoreRows (1)    
    .execute();
    }catch(IOException e){
      throw new AnnotationException(e);
    }
  }

  @Override
  public int loadAll (ProbeAnnotationSources probeAnnotationSources) throws AnnotationException {
    for(ProbeAnnotationSource source : probeAnnotationSources.getAll ())
      loadUrlResource (source.url ());
    return 0;
  }
  
  

}
