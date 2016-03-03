define(["ng", "./controllers/ProjectTreeVM", 
        "./controllers/ProjectTreeAdaptor", 
        "./controllers/ProjectTreeAdaptor2", 
        "./controllers/DatasetProjectTreeEventBus",
        "./directives/projectTree.directive", 
        "./controllers/ProjectTreeSchema",
        "./controllers/DatasetProjectTreeSchema",
        "./_projectNode/directives/projectNode.directive",
        "./_projectNode/directives/projectNodeDefault.directive"],		
function(ng, ProjectTreeVM, 
		ProjectTreeAdaptor, 
		ProjectTreeAdaptor2, 
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
	module.factory("ProjectTreeAdaptor2", ProjectTreeAdaptor2);
	module.service("DatasetProjectTreeEventBus", DatasetProjectTreeEventBus);
	module.controller("ProjectTreeVM", ProjectTreeVM);	
	module.directive("projectTree", ProjectTreeDirective);	
	module.directive("projectNode", ProjectNodeDirective);
	module.directive("projectNodeDefault", projectNodeDefaultDirective);
	
	return module;
});