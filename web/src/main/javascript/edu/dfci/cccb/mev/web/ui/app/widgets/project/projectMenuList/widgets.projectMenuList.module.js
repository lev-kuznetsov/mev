define(["ng", "./controllers/ProjectMenuListVM", "./directives/projectList.directive"], 
function(ng, ProjectListVM, projectListDirective){
	var module=ng.module("mui.widgets.projectMenulist", []);	
	module.controller("ProjectListVMxx", ["ProjectRepository", "Navigator", "$modal", "$scope", "$element", "$attrs", ProjectListVM]);
	module.directive("projectListxxx", [projectListDirective]);
	return module;
});