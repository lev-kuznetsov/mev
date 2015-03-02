define([], function(){
	var ProjectAnalysisResultListDirective = function ProjectAnalysisResultListDirective($compile){
		
		return {
			restrict: "AE",
			scope: {
				result: "="
			},
			templateUrl: "app/widgets/project/projectAnalysisResultList/templates/projectAnalysisResultList.tpl.html",
			link: function(scope, elem, attr){
				
			}
			
		}
	}
	
	ProjectAnalysisResultListDirective.$inject=["$compile"];
	return ProjectAnalysisResultListDirective;
});