define(["ng", "./controllers/ProjectTreeVM", 
        "./controllers/ProjectTreeAdaptor", 
        "./controllers/DatasetProjectTreeEventBus",
        "./directives/projectTree.directive", 
        "./controllers/ProjectTreeSchema",
        "./controllers/DatasetProjectTreeSchema",
        "./_projectNode/directives/projectNode.directive",
        "./_projectNode/directives/projectNodeDefault.directive"],		
function(ng, ProjectTreeVM, 
		ProjectTreeAdaptor, 
		DatasetProjectTreeEventBus,
		ProjectTreeDirective, 
		ProjectTreeSchema,
		DatasetProjectTreeSchema,
		ProjectNodeDirective, 
		projectNodeDefaultDirective){
	var module=ng.module("mui.widgets.projecttree", []);
	
	module.factory("ProjectTreeSchema", ProjectTreeSchema);
	module.factory("DatasetProjectTreeSchema", DatasetProjectTreeSchema);
	module.factory("ProjectTreeAdaptor", ProjectTreeAdaptor);
	module.service("DatasetProjectTreeEventBus", DatasetProjectTreeEventBus);
	module.controller("ProjectTreeVM", ProjectTreeVM);	
	module.directive("projectTree", ProjectTreeDirective);	
	module.directive("projectNode", ProjectNodeDirective);
	module.directive("projectNodeDefault", projectNodeDefaultDirective);
	
	return module;
});