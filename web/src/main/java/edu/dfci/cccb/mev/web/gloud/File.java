package edu.dfci.cccb.mev.web.gloud;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Accessors (fluent = true)
@JsonIgnoreProperties (ignoreUnknown = true)
public class File {

  private @Getter @JsonProperty String path;
  private @Getter @JsonProperty String type;
  private @Getter @JsonProperty String description;
}
