package edu.dfci.cccb.mev.annotation.domain.probe.contract;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

public interface ProbeAnnotations {  
  String platformId();
  List<ProbeAnnotation> get(Dimension dimension);
  InputStream getAsStream(Dimension dimension);  
  void loadUrlResource (URL url) throws AnnotationException;
}
