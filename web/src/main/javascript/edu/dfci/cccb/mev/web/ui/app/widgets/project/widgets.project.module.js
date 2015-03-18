define(["ng",        
        "./projectTree/widgets.projectTree.module"],
function(ng){	
	var module = ng.module("mui.widgets.project", ["mui.widgets.projecttree"]);			
	return module;
});