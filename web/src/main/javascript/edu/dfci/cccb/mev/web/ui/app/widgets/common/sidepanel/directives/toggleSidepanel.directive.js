define(["ng"], function(ng){
	var ToggleSidepanelDirective = function(sidepanelSrvc){
		return {
			restrict: "A",
//			scope: {
//				toggleSidepanel: "@"
//			},	
			controller: "ToggleSidepanelVM",
			controllerAs: "ToggleSidepanelVM",
//			template: "<button class='navbar-toggle' ng-click='toggle()'>" +
//					"<span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span>" +
//				    "</button>",
//			template: "<button ng-click='toggle()'>"
////				+ "<span class='glyphicon glyphicon-list' aria-hidden='true'></span>"
//				+ "<i class=\"fa fa-bars\"></i>"
//				+ "</button>",
//			compile: function(tElm,tAttrs){
//			return function(scope, elem, attr){
//				var exp = $parse('sidepanelSrvc.toggle(direction)');
//				elem.bind('click', function(){
//					console.debug("CLIK!!! toggle 2");
//					exp(scope, {sidepanelSr	vc: sidepanelSrvc, direction: scope.toggleSidepanel});
//				});			
//			}
//		}
			link: function(scope, elem, attr){
				scope.sideMenuVM={
					toggle: function(){
						console.debug("GOGGLE!!!", attr);
						sidepanelSrvc.toggle(attr.toggleSidepanel);						
					},
					isCollapsed: function(){
//						console.debug("IS COLLAPSED!!! toggle 1", sidepanelSrvc.isCollapsed(attr.toggleSidepanel));
						return sidepanelSrvc.isCollapsed(attr.toggleSidepanel);
					}
				};
//				elem.bind('click', function(){
//					console.debug("CLIK!!! toggle ");
//					sidepanelSrvc.toggle(attr.toggleSidepanel);
//				});			
			}
		};
	};
	ToggleSidepanelDirective.$inject=["sidepanelSrvc"];
	return ToggleSidepanelDirective;
});