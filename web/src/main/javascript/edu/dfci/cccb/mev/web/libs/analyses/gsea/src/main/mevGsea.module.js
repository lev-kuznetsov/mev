define(["mui", "angular-ui-router", "mev-analysis", "mev-annotations", "mev-results-table", "mev-hbarchart",
	"./model/gseaAnalysisType",
	"./state/GseaState",
	"./state/GseaVM"
	], function(mui){
	return mui.module("mevGsea", arguments, arguments);
});