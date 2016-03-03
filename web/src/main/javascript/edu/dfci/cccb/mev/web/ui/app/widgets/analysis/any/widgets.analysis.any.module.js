define(["ng", "./directives/anyAnalysisDirective", "./style/widgets.analysis.any.less"], 
function(ng, anyAnalysisDirective){
	return ng.module("mui.widgets.analysis.any", ["mui.widgets.analysis"], arguments);		
});