package edu.dfci.cccb.mev.annotation.domain.probe.simple;

import java.net.URL;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsFactory;
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.AbstractProbeAnnotationPlatform;

@Accessors(fluent=true)
public class SimpleProbeAnnotationPlatform extends AbstractProbeAnnotationPlatform {

  @Setter @Getter @Inject ProbeAnnotationsFactory simpleProbeAnnotationsBuilder;
  
  private @Getter final ProbeAnnotations annotations;
  
  public SimpleProbeAnnotationPlatform (String name, URL annotationsUrl) {
    super(name, annotationsUrl);
    annotations = simpleProbeAnnotationsBuilder.create(annotationsUrl.toString ());
  }
}
