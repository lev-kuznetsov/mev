package edu.dfci.cccb.mev.survival.domain.contract;

public interface SurvivalResult {

    public double coef();
    public double expCoef();
    public double seCoef();
    public double zScore();
    public double pValue();
}
