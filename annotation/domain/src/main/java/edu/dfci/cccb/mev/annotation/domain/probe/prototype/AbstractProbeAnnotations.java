package edu.dfci.cccb.mev.annotation.domain.probe.prototype;

import lombok.Getter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;

@Accessors(fluent=true)
public abstract class AbstractProbeAnnotations implements ProbeAnnotations{

  @Getter String platformId;
  public AbstractProbeAnnotations (String platformId) {
    this.platformId=platformId;
  }

}
