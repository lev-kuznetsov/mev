define(["ng"], function(ng){
	var ProjectAnalysisListDirective = function ProjectAnalysisListDirective(){
		return {
			scope: {
				analyses: "="
			},
			restrict: "AE",
//			template: "<div>DATA LIST</div>"
			templateUrl: "app/widgets/project/projectAnalysisList/templates/projectAnalysisList.accordion.tpl.html"
		}
	};
	ProjectAnalysisListDirective.$inject=[];
	return ProjectAnalysisListDirective;
});