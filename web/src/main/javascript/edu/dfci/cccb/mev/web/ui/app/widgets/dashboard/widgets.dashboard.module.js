define(["mui",
        "./directives/dashboardDirective", 
        "./directives/dashboardItemDirective", 
        "./directives/dashboardItemAddDirective",
        "./directives/dashboardItemRemoveDirective",
        "./controllers/DashboardVM",
        "./services/dashboardLayoutService"],        			
function(ng, DashboardDirective, DashboardItemDirective, DashboardItemAddDirective, DashboardVM){
	"use strict";
	console.debug("mui.widgets.dashboard");
	return ng.module("mui.widgets.dashboard", arguments, arguments)
	.animation(".flex-spacing", ["$rootScope", function($rootScope){		
		
		return {
//setClass, leave, move are not triggered by dashboard panels			
//			setClass : function(element, addedClasses, done, options){
//				console.debug("ANIMATE SET", element, addedClasses, done, options);
//				done();
//			},
//			leave : function(element, done, options){
//				console.debug("ANIMATE LEAVE", element, done, options);
//				done();
//			},
//			move : function(element, done, options){
//				console.debug("ANIMATE MOVE", element, done, options);
//				done();
//			},
			//fired when panel is added to dashbaoard in row-max state
			enter : function(element, done, options){
				console.debug("ANIMATE ENTER", element, done, options);
				if(options.preparationClasses === "rowMax-add")
					$rootScope.$broadcast("mui:dashboard:panel:rowMax", element);
				done();
			},
			//fired when panel is maximized or row-maximized
			addClass : function(element, addedClasses, done, options){
		    	console.debug("ANIMATE ADD", element, addedClasses, done, options);
		    	if(addedClasses === "max")
		    		$rootScope.$broadcast("mui:dashboard:panel:max", element);
		    	else if(addedClasses==="rowMax"){
		    		$rootScope.$broadcast("mui:dashboard:panel:rowMax", element);
		    	}
		    	done();
		    },
			//fired when panel is minimized or row-minimized
		    removeClass : function(element, addedClasses, done, options){
				console.debug("ANIMATE REMOVE", element, addedClasses, done, options);
				if(addedClasses === "max")
		    		$rootScope.$broadcast("mui:dashboard:panel:min", element);
		    	else if(addedClasses==="rowMax"){
		    		$rootScope.$broadcast("mui:dashboard:panel:rowMin", element);
		    	}
				done();
			},
		};
	}]);
});