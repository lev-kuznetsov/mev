define(["ng", 
        "./directives/analysisModal.directive"], 
function(ng, AnalsysModalDirective, AnalysisEventBus){
	var module = ng.module("mui.widgets.analysis.modal", []);
	module.directive("analysisModal", AnalsysModalDirective);	
	return module;
});