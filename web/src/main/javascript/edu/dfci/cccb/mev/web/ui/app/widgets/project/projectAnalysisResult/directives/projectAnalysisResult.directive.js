define([], function(){
	var ProjectAnalysisResultDirective = function ProjectAnalysisResultDirective($compile){
		
		return {
			restrict: "AE",
			scope: {
				resultItem: "=",
				resultName: "@"
			},
			templateUrl: "app/widgets/project/projectAnalysisResult/templates/projectAnalysisResult.tpl.html",
			link: function(scope, elem, attr){
				
			}
			
		}
	}
	
	ProjectAnalysisResultDirective.$inject=["$compile"];
	return ProjectAnalysisResultDirective;
});