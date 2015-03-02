define(["ng", "./controllers/ProjectLabelVM", "./directives/projectLabel.directive"], 
function(ng, ProjectLabelVM, ProjectLabelDirective){
	var module=ng.module("mui.widgets.projectlabel", []);	
	module.controller("ProjectLabelVM", ["Navigator","$modal", "$scope", "$element", "$attrs", ProjectLabelVM]);
	module.directive("projectLabel", [ProjectLabelDirective]);
	return module;
});