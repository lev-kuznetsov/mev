package edu.dfci.cccb.mev.survival.domain.contract;

import java.util.List;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;

public interface SurvivalParams {
  public String name();
  public String datasetName();
  public String experimentName();
  public Selection experiment();
  public String controlName();
  public Selection control();
  public List<SurvivalInputEntryTcga> input();  
}
