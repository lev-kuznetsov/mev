define(["ng"], function(ng){
	var SidePanelDirective = function SidePanelDirective(sidepanelSrvc){
		
		function toggle(menu, content, direction) {
			console.debug("toggling ....", menu, content);
			content.removeClass('no-transition');
			if(direction==="right"){
				if (menu.is(':visible') && content.hasClass('col-md-8')) {
					// Slide out
					menu.animate({
						right : -(menu.outerWidth() + 10)
					}, function() {menu.hide(100);});
					content.removeClass('col-md-8').addClass('col-md-12');				
				} else {
					// Slide in
					menu.show(500).animate({right : 0});
					content.removeClass('col-md-12').addClass('col-md-8');
				}
				if (content.hasClass('col-md-12') && menu.is(':hidden')) {
					menu.animate({right : 0}, function() {
						menu.show(100);
					});
					content.removeClass('no-transition');
					content.removeClass('col-md-12').addClass('col-md-8');
				}
			}else if(direction==="left"){
				if (menu.is(':visible') && content.hasClass('col-md-8')) {
					// Slide out
					menu.animate({
						left : -(menu.outerWidth()+10)
					}, function() {
						menu.hide(400);						
					});
//					content.removeClass('col-md-8').addClass('col-md-12');								
				} else {
					// Slide in
					menu.show(400).animate({left : 0});
					content.removeClass('col-md-12').addClass('col-md-8');
				}
				
				if (content.hasClass('col-md-12') && menu.is(':hidden')) {
					menu.animate({left : 0}, function() {
						menu.show(100);
					});
					content.removeClass('no-transition');
					content.removeClass('col-md-12').addClass('col-md-8');
				}
			}
		}	
		
		return {
			restrict: "EA",
//			transclude: true,
//			replace: true,
			scope: {
				direction: "@",
				sidepanel: "@",
				contentId: "@"
			},
//			template: "<div ng-transclude></div>",
			link: function(scope, elem, attr){
				
				scope.$watch(function(){
//					console.debug("watching ... "+scope.sidepanel, sidepanelSrvc.isCollapsed(scope.sidepanel));
					return sidepanelSrvc.isCollapsed(scope.sidepanel);
				}, function(newValue, oldValue){			
//					console.debug("change ... ", newValue, oldValue);
					if(newValue!==oldValue){
//						console.debug("changed !!! ", newValue, oldValue);
						toggle(elem, ng.element('#'+scope.contentId), scope.sidepanel);
					}
				});
			}
		};
	};
	SidePanelDirective.$inject=["sidepanelSrvc"];
	return SidePanelDirective;
});