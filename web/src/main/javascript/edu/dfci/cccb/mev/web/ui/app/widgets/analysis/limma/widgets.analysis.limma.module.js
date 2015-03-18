define(["ng", "./directives/limmaResult/limmaResult.directive",
        "./directives/limmaTopGoResult/limmaTopGoResult.directive",
        "./directives/limmaParameters/limmaParameters.directive"], 
function(ng, LimmaResultDirective, LimmaTopGoResultDirective, LimmaParametersDirective){
	var module = ng.module("mui.widgets.analysis.limma", []);
	module.directive("limmaResult", LimmaResultDirective);
	module.directive("limmaTopGoResult", LimmaTopGoResultDirective);
	module.directive("limmaParameters", LimmaParametersDirective);
	return module;
});