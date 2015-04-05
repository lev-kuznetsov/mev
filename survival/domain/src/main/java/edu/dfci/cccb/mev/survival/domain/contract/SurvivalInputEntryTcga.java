package edu.dfci.cccb.mev.survival.domain.contract;

public interface SurvivalInputEntryTcga extends SurvivalInputEntry{  
  public Long days_to_death();
  public Long days_to_last_followup();
  public String vital_status(); 
}
