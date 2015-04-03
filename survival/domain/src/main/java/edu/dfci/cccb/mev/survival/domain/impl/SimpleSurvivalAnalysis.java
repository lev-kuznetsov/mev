package edu.dfci.cccb.mev.survival.domain.impl;

import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalAnalysis;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalParams;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalResult;
import edu.dfci.cccb.mev.survival.domain.prototype.AbstractSurvivalAnalysis;

@Accessors(fluent=true)
@RequiredArgsConstructor
public class SimpleSurvivalAnalysis extends AbstractAnalysis<SimpleSurvivalAnalysis>{  
  public final @Getter String name;
  public final @Getter String type;
  public final @Getter SurvivalParams params;
  public final @Getter SurvivalResult result;
}
