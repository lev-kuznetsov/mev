define([], function(){
	var MenuDirective = function(SideMenuSrv, $rootScope){
		return {			
			restrict: "AE",
			replace: true,
			templateUrl: "app/widgets/mainmenu/directives/mainmenu.tpl.html",
			transclude: true,
			link: function(scope, elm, attrs, ctrl){
//				console.debug("MainMenu Link: ", scope.$state, scope.$state.current.name === "root.project");
				scope.hasSidePanel = scope.$state.current.name === "root.project";
				scope.settings={
					sidemenu: {
						shrink: true
					},
					header: {
						fixed: true
					},
					footer: {
						fixed: true
					},
					columnScroll: true,
					columnContentScroll: true
				};								
				scope.toggleFixedHeader=function(){
					$rootScope.$broadcast("ui:toggleFixedHeader");
				};
				scope.toggleFixedFooter=function(){
					$rootScope.$broadcast("ui:toggleFixedFooter");
				};
				scope.toggleColumnScroll=function(){
					$rootScope.$broadcast("ui:toggleColumnScroll");					
				};
				scope.toggleColumnContentScroll=function(){
					$rootScope.$broadcast("ui:toggleColumnContentScroll");					
				}
				scope.$watch("settings.sidemenu.shrink", SideMenuSrv.toggle);
			}
		};
	};
	MenuDirective.$inject=["SideMenuSrv", "$rootScope"];
	return MenuDirective;
});