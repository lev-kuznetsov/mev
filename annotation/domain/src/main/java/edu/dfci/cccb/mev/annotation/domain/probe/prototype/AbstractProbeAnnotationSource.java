package edu.dfci.cccb.mev.annotation.domain.probe.prototype;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationSource;

@Accessors(fluent=true)
public abstract class AbstractProbeAnnotationSource implements ProbeAnnotationSource {

  @JsonProperty private @Getter String name;
  private @Getter URL url;
  public AbstractProbeAnnotationSource (String name, URL url) {
    this.name=name;
    this.url=url;
  }

  

}
