define(["ng", "./directives/layout.directive", 
        "./directives/layoutRow.directive", 
        "./directives/layoutColumn.directive",
        "./directives/layoutColumnButtons.directive",
        "./controllers/LayoutVM", 
        "./controllers/LayoutRowVM", 
        "./controllers/LayoutColumnVM",
        "./controllers/LayoutColumnButtonsVM", 
        "./services/layoutSrv"], 
function(ng, LayoutDirective, 
		LayoutRowDirective, 
		LayoutColumnDirective, 
		LayoutColumnButtonsDirective, 
		LayoutVM, 
		LayoutRowVM, 
		LayoutColumnVM,
		LayoutColumnButtonsVM,
		LayoutSrv){
	var module=ng.module("mui.widgets.common.layout", []);
	module.directive("layout", LayoutDirective);	
	module.directive("layoutRow", LayoutRowDirective);
	module.directive("layoutColumn", LayoutColumnDirective);
	module.directive("layoutColumnButtons", LayoutColumnButtonsDirective);
	module.controller("LayoutVM", LayoutVM);
	module.controller("LayoutRowVM", LayoutRowVM);	
	module.controller("LayoutColumnVM", LayoutColumnVM);
	module.controller("LayoutColumnButtonsVM", LayoutColumnButtonsVM);	
	module.service("LayoutSrv", LayoutSrv);
	module.animation(".layout-column", function(){
		return {
			//this is just an example of how to do actual animation 
			//using jQuery intead of CSS
//			enter : function(element, done) {
//		      jQuery(element).css({
//		        position:'relative',
//		        left:-10,
//		        opacity:0
//		      });
//		      jQuery(element).animate({
//		        left:0,
//		        opacity:1
//		      }, done);
//		    },
			//this event fires every time 'layou-column' is resized 
			//(because the resize is implemented by adding/removing col-0/col-12 ng-classes from the div element)
		    addClass : function(element, addedClasses, done, options){
		    	console.debug("ANIMATE addClass", element, addedClasses, done, options);
		    	done();
		    }
		  };
	});
	return module;
});