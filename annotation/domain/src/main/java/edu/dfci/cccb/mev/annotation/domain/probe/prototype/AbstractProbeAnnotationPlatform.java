package edu.dfci.cccb.mev.annotation.domain.probe.prototype;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatform;

@Accessors(fluent=true)
public abstract class AbstractProbeAnnotationPlatform implements ProbeAnnotationPlatform {

  @JsonProperty private @Getter String id;
  @JsonProperty private @Getter String name;
  private @Getter URL annotationsUrl;
  public AbstractProbeAnnotationPlatform (String name, URL annotationsUrl) {
    this.id=name;
    this.name=name;
    this.annotationsUrl=annotationsUrl;
  }
}
