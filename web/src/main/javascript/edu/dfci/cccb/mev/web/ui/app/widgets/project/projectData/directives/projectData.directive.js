define(["ng"], function(ng){
	var ProjectDataDirective = function ProjectDataDirective(){
		return {
			scope: {
				data: "="
			},
			restrict: "AE",
//			template: "<div>aaaa agasdfas s</div>"	
			templateUrl: "app/widgets/project/projectData/templates/projectData.tpl.html"
			
		}
	} 
	ProjectDataDirective.$inject=[];
	return ProjectDataDirective;
});