package edu.dfci.cccb.mev.survival.domain.contract;

public interface SurvivalInputEntry {
  public String key();
  public Long time();
  public Integer status();
  public Integer group();
}
