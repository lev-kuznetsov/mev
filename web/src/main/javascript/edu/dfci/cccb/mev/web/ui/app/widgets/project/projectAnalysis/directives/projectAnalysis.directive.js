define(["ng"], function(ng){
	var ProjectAnalysisDirective = function ProjectAnalysisDirective(){
		return {
			scope: {
				analysis: "="
			},
			restrict: "AE",
//			template: "<div>aaaa agasdfas s</div>"	
			templateUrl: "app/widgets/project/projectAnalysis/templates/projectAnalysis.tpl.html"
			
		}
	} 
	ProjectAnalysisDirective.$inject=[];
	return ProjectAnalysisDirective;
});