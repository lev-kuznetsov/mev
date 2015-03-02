define(["ng", "./controllers/ProjectListVM", "./directives/projectList.directive"], 
function(ng, ProjectListVM, projectListDirective){
	var module=ng.module("mui.widgets.projectlist", []);	
	module.controller("ProjectListVM", ["ProjectRepository", "Navigator", "$modal", "$scope", "$element", "$attrs", ProjectListVM]);
	module.directive("projectList", [projectListDirective]);
	return module;
});