define(["mui", "mev-scatter-plot", 
	"./view/pcaAnalysisDirective",
	"./model/PcaAnalysisType",
	"./router/PcaState",
	"./router/PcaStateVM",
	"angular-ui-router"
	], function(ng){
	return ng.module("mev-pca", arguments, arguments);
});
