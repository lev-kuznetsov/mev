define(["ng"], function(ng){
	var ProjectAnalysisParametersDirective = function ProjectAnalysisParametersDirective(){
		return {
			scope: {
				parameters: "="				
			},
			restrict: "AE",
//			template: "<div>aaaa agasdfas s</div>"	
//			templateUrl: "app/widgets/project/projectAnalysisParameters/templates/projectAnalysisParameters.inline.tpl.html"
			templateUrl: "app/widgets/analysis/default/templates/defaultParameters.tpl.html"
			
		}
	} 
	ProjectAnalysisParametersDirective.$inject=[];
	return ProjectAnalysisParametersDirective;
});