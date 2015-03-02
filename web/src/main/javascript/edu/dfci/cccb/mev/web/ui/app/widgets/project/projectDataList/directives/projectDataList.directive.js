define(["ng"], function(ng){
	var ProjectDataListDirective = function ProjectDataListDirective(){
		return {
			scope: {
				datas: "="
			},
			restrict: "AE",
//			template: "<div>DATA LIST</div>"
			templateUrl: "app/widgets/project/projectDataList/templates/projectDataList.accordion.tpl.html"
		}
	};
	ProjectDataListDirective.$inject=[];
	return ProjectDataListDirective;
});