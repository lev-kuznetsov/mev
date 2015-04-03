package edu.dfci.cccb.mev.survival.domain.contract;

import com.fasterxml.jackson.annotation.JsonSubTypes;

public interface SurvivalInputEntry {
  public String key();
  public Long time();
  public Integer status();
  public Integer group();
}
