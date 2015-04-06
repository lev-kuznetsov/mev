package edu.dfci.cccb.mev.survival.domain.contract;

import java.util.List;
import java.util.Map;

import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalResult.Logrank;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalResult.SimpleSurvivalPlotEntry;

public interface SurvivalResult {

    public Double coef();
    public Double exp_coef();
    public Double se_coef();
    public Double z();
    public Double pValue();
    public Double ci_lower();
    public Double ci_upper();
    public Logrank logrank();
    public  Map<String, List<SimpleSurvivalPlotEntry>> plot();
    
}
