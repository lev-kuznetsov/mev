define(["ng", "./modal/pcaModal.directive", "./scatterPlot/pca.scatterPlot.module"], 
function(ng, PcaModalDirective){	
//	var module = ng.module("mui.widgets.analysis.pca", ["mui.domain.analysis.pca"]);
//	module.directive("pcaModal", PcaModalDirective);
	var module = ng.module("mui.widgets.analysis.pca", arguments, arguments);
	return module;
});