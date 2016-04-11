define(["mui", "mev-scatter-plot", 
	"./view/pcaAnalysisDirective",
	"./model/PcaAnalysisType",
	"./router/PcaState",
	"./router/PcaStateVM",
	], function(ng){
	return ng.module("mev-pca", arguments, arguments);
});
