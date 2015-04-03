package edu.dfci.cccb.mev.survival.domain.impl;

import java.io.IOException;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalInputEntryTcga;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalParams;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors (fluent = true)
public class SimpleSurvivalParams implements SurvivalParams {  
  public @JsonProperty @Getter String name;
  public @JsonProperty @Getter String datasetName;  
  public @JsonProperty @Getter String experimentName;
  public @JsonProperty @Getter @JsonDeserialize (as = SimpleSelection.class) Selection experiment;
  public @JsonProperty @Getter String controlName;
  public @JsonProperty @Getter @JsonDeserialize (as = SimpleSelection.class) Selection control;
  public @JsonDeserialize (contentAs = SimpleSurvivalInputEntryTcga.class) @JsonProperty @Getter List<SurvivalInputEntryTcga> input;

//  public static class SurvivalInputEntryTcgaJsonDeserializer extends JsonDeserializer<SurvivalInputEntryTcga> {
//
//    @Override
//    public SurvivalInputEntryTcga deserialize (JsonParser jp, DeserializationContext ctxt) throws IOException,
//                                                                                          JsonProcessingException {
//      return jp.readValueAs (SimpleSurvivalInputEntryTcga.class);
//    }
//  }
}
