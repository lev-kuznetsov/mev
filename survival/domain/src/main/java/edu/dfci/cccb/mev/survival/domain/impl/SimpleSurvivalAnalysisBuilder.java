package edu.dfci.cccb.mev.survival.domain.impl;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalAnalysis;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalParams;

@R ("function (data) {\n" + 
        "  baseline <- function(ds){\n" + 
        "    for(i in 1:length(ds)){\n" + 
        "      if(class(ds[1,i]) == \"factor\"){\n" + 
        "        ds[1,i] <- levels(ds[1,i])[1]\n" + 
        "      } else {\n" + 
        "        ds[1,i] <- 0\n" + 
        "      }\n" + 
        "    }\n" + 
        "    ds\n" + 
        "  } \n" + 
        "  prepare_plot_data <- function(so){\n" + 
        "    data <- cbind(so$time, -log(so$surv))\n" + 
        "    colnames(data) <- c(\"time\", \"haz\")\n" + 
        "    data <- apply(data, 1, as.list)\n" + 
        "    data  \n" + 
        "  }\n" + 
        "  \n" + 
        "  #Set trt as factor so they are treated as such\n" + 
        "  data$input$group <- as.factor(data$input$group)\n" + 
        "  attach(data$input)\n" + 
        "  cobj <- survival::coxph(survival::Surv(time, status) ~ group, data=data$input)\n" + 
        "  \n" + 
        "  control <- data$input[which(group==1),]\n" + 
        "  experiment <- data$input[which(group==2),]\n" + 
        "  \n" + 
        "  covariate_selector <- data$input[1,]\n" + 
        "  covariate_selector <- baseline(covariate_selector)\n" + 
        "  covariate_selector$group <- as.factor(1)\n" + 
        "  so_control <- survival::survfit(cobj, newdata=covariate_selector)\n" + 
        "  covariate_selector$group <- as.factor(2)\n" + 
        "  so_experiment <- survival::survfit(cobj, newdata=covariate_selector)\n" + 
        "  \n" + 
        "  detach()\n" + 
        "  \n" + 
        "  summ <- summary(cobj)\n" + 
        "  output <- list(n=summ$n, n_event=summ$nevent, coef=summ$coefficients[[1]], exp_coef=summ$coefficients[[2]], se_coef=summ$coefficients[[3]], z=summ$coefficients[[4]], pValue=summ$coefficients[[5]], ci_lower=summ$conf.int[[3]], ci_upper=summ$conf.int[[4]], logrank=list(score=summ$sctest[[1]], pValue=summ$sctest[[3]]), plot=list(control=prepare_plot_data(so_control), experiment=prepare_plot_data(so_experiment)))\n" + 
        "}")
public class SimpleSurvivalAnalysisBuilder extends AbstractDispatchedRAnalysisBuilder<SimpleSurvivalAnalysisBuilder, SimpleSurvivalAnalysis> {
    
  public SimpleSurvivalAnalysisBuilder () {
    super (SurvivalAnalysis.ANALYSIS_TYPE);
  }

  @Parameter("data") private SurvivalParams params;  
  public SimpleSurvivalAnalysisBuilder params (SurvivalParams params) {
    this.params=params;
    name(params.name ());
    return this;
  }
  
  @Result private SimpleSurvivalResult dtoResult;
  
  @Override
  protected SimpleSurvivalAnalysis result () {
    if(dtoResult==null)
      return null;
    return new SimpleSurvivalAnalysis (name(), type(), params, dtoResult);
  }
  
  @Callback
  private void cb () {
    System.out.println (dtoResult);
  }
}
