package edu.dfci.cccb.mev.normalization.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(fluent = true)
public class Normalization extends AbstractAnalysis<Normalization> {

  private @JsonProperty @Getter @Setter NormalizationParameters params;
  private @Getter() Dataset result;
  public Normalization (NormalizationParameters params, Dataset normalized) {
    this.name(params.name());
    this.type ("normalization");
    this.params = params;
    this.result = normalized;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors(fluent = true )
  public static class NormalizationParameters {
    private @JsonProperty @Getter String method;
    private @JsonProperty @Getter String name;
    private @JsonProperty @Getter String exportName;
  }
}
