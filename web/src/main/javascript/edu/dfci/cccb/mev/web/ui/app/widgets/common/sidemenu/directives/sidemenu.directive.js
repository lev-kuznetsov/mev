define(["ng"], function(ng){
	var SideMenuDirective = function SideMenuDirective(SidemenuSrv){
		
		return {			
			restrict: "E",
			transclude: true,
			template: "<div ng-mouseleave='shrink($event)' ng-mouseenter=\'open($event)\' ng-transclude><div/>",
			controller: "SideMenuVM",
			link: function(scope, elem, attr){
				scope.hello="hiiiiii";
				scope.closed=false;
				scope.test=function($event){					
					scope.closed=!scope.closed;
					console.debug("test", elem, $event, scope.closed);
				};
				
				scope.shrink=function($event){
//					console.debug("shrink!", angular.element(elem).find("accorion"));					
					if(SidemenuSrv.isShrink()){
						angular.element($event.target).closest('#sidemenu-container').addClass("shrink");						
						var openPanels=angular.element(elem).find(".panel-collapse.in");
//						console.debug("WILL collapse", openPanels);
						if(openPanels.length>0){
							openPanels.scope().isOpen=false;							
						}
					}
				};
				
				scope.open=function($event){
					angular.element($event.target).parent().parent().toggleClass("open");
					
					var targetPanel = angular.element($event.target).closest('.panel').find('.panel-collapse');
					if(SidemenuSrv.isShrink()){
//						console.debug("SHIRNK - OPEN!", elem, $event.target, targetPanel);
//						targetPanel.scope().isOpen=true;											
						angular.element($event.target).closest('#sidemenu-container').removeClass("shrink");
					}
				};
//				webkitTransitionEnd oTransitionEnd MSTransitionEnd transitionend 
				angular.element(elem).on("transitionend", function($event){							
//					console.debug("Trans ENDED", $event);
//					targetPanel.scope().isOpen=true;							
//					angular.element(elem).unbind("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd");
				});	
				
			}
		};
	};	
	
	SideMenuDirective.$inject=["SideMenuSrv"];
	return SideMenuDirective;
});