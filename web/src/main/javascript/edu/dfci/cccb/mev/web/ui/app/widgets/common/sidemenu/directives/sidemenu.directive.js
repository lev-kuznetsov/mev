"use strict";
define(["ng"], function(ng){
	var SideMenuDirective = function SideMenuDirective(SidemenuSrv, LayoutSrv, mevAnalysisTypes){
		
		return {			
			restrict: "E",
			transclude: true,
			template: "<div ng-transclude><div/>",
			controller: "SideMenuVM",
			controllerAs: "SideMenuVM",
			link: function(scope, elem, attr){				
				scope.closed=false;
				scope.test=function($event){					
					scope.closed=!scope.closed;
					console.debug("test", elem, $event, scope.closed);
				};
				
				scope.shrink=function($event){
//					console.debug("shrink!", ng.element(elem).find("accorion"));					
					if(SidemenuSrv.isShrink()){
						ng.element($event.target).closest('#sidemenu-container').addClass("shrink");						
						var openPanels=ng.element(elem).find(".panel-collapse.in");
//						console.debug("WILL collapse", openPanels);
						if(openPanels.length>0){
							openPanels.scope().isOpen=false;							
						}
					}
				};
				
				scope.open=function($event){
					ng.element($event.target).parent().parent().toggleClass("open");
					
					var targetPanel = ng.element($event.target).closest('.panel').find('.panel-collapse');
					if(SidemenuSrv.isShrink()){
//						console.debug("SHIRNK - OPEN!", elem, $event.target, targetPanel);
//						targetPanel.scope().isOpen=true;											
						ng.element($event.target).closest('#sidemenu-container').removeClass("shrink");
					}
				};
//				webkitTransitionEnd oTransitionEnd MSTransitionEnd transitionend 
				ng.element(elem).on("transitionend", function($event){							
//					console.debug("Trans ENDED", $event);
//					targetPanel.scope().isOpen=true;							
//					ng.element(elem).unbind("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd");
				});	
				scope.LayoutSrv=LayoutSrv;
			}
		};
	};	
	
	SideMenuDirective.$inject=["SideMenuSrv", "LayoutSrv"];
	return SideMenuDirective;
});