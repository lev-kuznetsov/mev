package edu.dfci.cccb.mev.dataset.domain.simple;

import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractSelection;

@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors (fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleSelection extends AbstractSelection {
  @NonNull @Getter @JsonProperty private String name;
  @NonNull @Getter @JsonProperty private Properties properties;
  @NonNull @Getter @JsonProperty private List<String> keys;
}
