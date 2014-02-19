package edu.dfci.cccb.mev.annotation.domain.probe.metafile;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.Getter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.AbstractProbeAnnotationSource;

@Accessors(fluent=true)
public class MetafileProbeAnnotationSource extends AbstractProbeAnnotationSource {
  
  private @Getter String fileName;
  private @Getter String filePath;  
  private @Getter URL rootUrl;  
  
  public MetafileProbeAnnotationSource(URL root, Object[] values) throws MalformedURLException{
    this(root, (String)values[0],
         (String)values[1]);
  }

  
  public MetafileProbeAnnotationSource(URL root, String fileName, String filePath) throws MalformedURLException
  {
    super(fileName, new URL(root, filePath+"/"+fileName));
    this.rootUrl=root;    
    this.fileName=fileName;
    this.filePath=filePath;
  }
}
