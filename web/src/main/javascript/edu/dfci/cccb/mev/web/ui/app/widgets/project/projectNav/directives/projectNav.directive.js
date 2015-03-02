define(["ng"], function(){
	
	var ProjectNavDirective = function ProjectNavDirective(){
		return {
			scope: {
				project: "="
			},
			restrict: "AE",
			templateUrl: "app/widgets/project/projectNav/templates/projectNav.tpl.html"
		}
	};
	return ProjectNavDirective; 
	ProjectNavDirective.$inject=[];
});