package edu.dfci.cccb.mev.annotation.domain.probe.simple;

import java.net.URL;

import lombok.experimental.Accessors;

import edu.dfci.cccb.mev.annotation.domain.probe.prototype.AbstractProbeAnnotationSource;

@Accessors(fluent=true)
public class SimpleProbeAnnotationSource extends AbstractProbeAnnotationSource {

  public SimpleProbeAnnotationSource (String name, URL url) {
    super(name, url);
  }
  
}
