define(["mui", "mev-scatter-plot", 
	"./view/pcaAnalysisDirective",
	"./model/PcaAnalysisType",
	"./router/PcaState",
	"./router/PcaStateVM",
	"angular-ui-router",
	"mev-analysis",
	"mev-domain-common",
	"mev-limma",
	"mev-edger"
], function(ng){
	return ng.module("mev-pca", arguments, arguments);
});
