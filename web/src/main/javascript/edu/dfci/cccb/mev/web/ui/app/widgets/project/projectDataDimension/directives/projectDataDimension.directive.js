define(["ng"], function(ng){
	var ProjectDataDirective = function ProjectDataDirective(){
		return {
			scope: {
				dimension: "=",
				dimensionType: "@"
			},
			restrict: "AE",
//			template: "<div>aaaa agasdfas s</div>"	
//			templateUrl: "app/widgets/project/projectDataDimension/templates/projectDataDimension.list.tpl.html"
			templateUrl: "app/widgets/project/projectDataDimension/templates/projectDataDimension.inline.tpl.html"
			
		}
	} 
	ProjectDataDirective.$inject=[];
	return ProjectDataDirective;
});