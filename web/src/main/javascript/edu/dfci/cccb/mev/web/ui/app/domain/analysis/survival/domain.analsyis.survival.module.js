define(["ng", "./SurvivalColumnList", "./SurvivalAnalysisSrv"], 
function(ng, SurvivalColumnList, SurvivalAnalysisSrv){
	var module = ng.module("mui.domain.analysis.survival", []);
	module.service("SurvivalColumnList", SurvivalColumnList);
	module.service("SurvivalAnalysisSrv", SurvivalAnalysisSrv)
	return module;
});