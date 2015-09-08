define(["ng", "./modal/topgoModal.directive"], 
function(ng, TopGoModalDirective){	
	var module = ng.module("mui.widgets.analysis.topgo", ["mui.domain.analysis.topgo"]);
	module.directive("topgoModal", TopGoModalDirective);
	return module;
	
});