define(["ng", "./modal/voomModal.directive"], 
function(ng, VoomModalDirective){	
	var module = ng.module("mui.widgets.analysis.voom", ["mui.domain.analysis.voom"]);
	module.directive("voomModal", VoomModalDirective);
	return module;
	
});