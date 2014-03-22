package edu.dfci.cccb.mev.annotation.domain.probe.metafile;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsFactory;
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.AbstractProbeAnnotationPlatform;

@Accessors(fluent=true)
public class MetafileProbeAnnotationPlatform extends AbstractProbeAnnotationPlatform {
  
  private @Getter String fileName;
  private @Getter String filePath;  
  private @Getter String type;
  private @Getter URL rootUrl;  
  private @Setter @Getter @Inject ProbeAnnotationsFactory annotationsFactory;
  private final @Getter ProbeAnnotations annotations;
  
  @Inject
  public MetafileProbeAnnotationPlatform(Object[] values, 
                                         @Named("probe-annotations-root")URL root,
                                         ProbeAnnotationsFactory annotationsFactory) throws MalformedURLException{
    this(
         (String)values[0],
         (String)values[1],
         (String)values[2],
         root, 
         annotationsFactory
        );
  }
  
  public MetafileProbeAnnotationPlatform(String fileName, String filePath, String type, URL root, ProbeAnnotationsFactory annotationsFactory) throws MalformedURLException
  {
    super(fileName, new URL(root, filePath+fileName));
    this.rootUrl=root;    
    this.fileName=fileName;
    this.filePath=filePath;
    this.type=type;
    annotations=annotationsFactory.create (fileName, type);
  }

}
