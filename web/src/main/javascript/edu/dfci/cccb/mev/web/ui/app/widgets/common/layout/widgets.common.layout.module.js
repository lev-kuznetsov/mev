define(["ng", "./directives/layout.directive", 
        "./directives/layoutRow.directive", 
        "./directives/layoutColumn.directive",
        "./directives/layoutColumnButtons.directive",
        "./controllers/LayoutVM", 
        "./controllers/LayoutRowVM", 
        "./controllers/LayoutColumnVM",
        "./controllers/LayoutColumnButtonsVM", 
        "./services/layoutSrvc"], 
function(ng, LayoutDirective, 
		LayoutRowDirective, 
		LayoutColumnDirective, 
		LayoutColumnButtonsDirective, 
		LayoutVM, 
		LayoutRowVM, 
		LayoutColumnVM,
		LayoutColumnButtonsVM,
		LayoutSrvc){
	var module=ng.module("mui.widgets.common.layout", []);
	module.directive("layout", LayoutDirective);	
	module.directive("layoutRow", LayoutRowDirective);
	module.directive("layoutColumn", LayoutColumnDirective);
	module.directive("layoutColumnButtons", LayoutColumnButtonsDirective);
	module.controller("LayoutVM", LayoutVM);
	module.controller("LayoutRowVM", LayoutRowVM);	
	module.controller("LayoutColumnVM", LayoutColumnVM);
	module.controller("LayoutColumnButtonsVM", LayoutColumnButtonsVM);	
	module.service("LayoutSrv", LayoutSrvc);
	return module;
});