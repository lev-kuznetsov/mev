define(["ng"], function(ng){
	var ProjectDataSelectionListDirective = function ProjectDataSelectionListDirective(){
		return {
			scope: {
				dimension: "="				
			},
			restrict: "AE",
//			template: "<div>aaaa agasdfas s</div>"	
//			templateUrl: "app/widgets/project/projectDataSelectionList/templates/projectDataSelectionList.inline.tpl.html"
			templateUrl: "app/widgets/project/projectDataSelectionList/templates/projectDataSelectionList.tpl.html"
			
		}
	} 
	ProjectDataSelectionListDirective.$inject=[];
	return ProjectDataSelectionListDirective;
});