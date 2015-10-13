define(["ng", "./modal/pcaModal.directive"], 
function(ng, PcaModalDirective){	
	var module = ng.module("mui.widgets.analysis.pca", ["mui.domain.analysis.pca"]);
	module.directive("pcaModal", PcaModalDirective);
	return module;
});