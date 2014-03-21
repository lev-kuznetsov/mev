package edu.dfci.cccb.mev.annotation.domain.probe.prototype;

import java.util.List;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotation;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

@Accessors(fluent=true)
public abstract class AbstractProbeAnnotations implements ProbeAnnotations{
  
  @Getter String platformId;
  public AbstractProbeAnnotations (String platformId) {
    this.platformId=platformId;
  }

  
  @Override
  @SneakyThrows
  public List<ProbeAnnotation> get (Dimension dimension) {
    throw new UnsupportedOperationException("The get(Dimension dimension) method has not been implemented yet");    
  }

}
