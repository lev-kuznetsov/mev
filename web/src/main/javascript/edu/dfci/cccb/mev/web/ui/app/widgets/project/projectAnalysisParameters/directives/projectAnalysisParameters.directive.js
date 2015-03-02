define(["ng"], function(ng){
	var ProjectAnalysisParametersDirective = function ProjectAnalysisParametersDirective(){
		return {
			scope: {
				parameters: "=",
				analysisType: "@"
			},
			restrict: "AE",
//			template: "<div>aaaa agasdfas s</div>"	
//			templateUrl: "app/widgets/project/projectAnalysisParameters/templates/projectAnalysisParameters.inline.tpl.html"
			templateUrl: "app/widgets/project/projectAnalysisParameters/templates/projectAnalysisParameters.typeSwitch.tpl.html"
			
		}
	} 
	ProjectAnalysisParametersDirective.$inject=[];
	return ProjectAnalysisParametersDirective;
});