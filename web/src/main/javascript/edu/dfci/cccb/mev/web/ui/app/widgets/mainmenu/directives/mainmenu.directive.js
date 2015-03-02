define([], function(){
	var MenuDirective = function(SideMenuSrv, $rootScope){
		return {			
			restrict: "AE",
			replace: true,
			templateUrl: "app/widgets/mainmenu/directives/mainmenu.tpl.html", 
			link: function(scope, elm, attrs, ctrl){
//				console.debug("MainMenu Link: ", scope.$state, scope.$state.current.name === "root.project");
				scope.hasSidePanel = scope.$state.current.name === "root.project";
				scope.settings={
					sidemenu: {
						shrink: false
					},
					header: {
						fixed: false
					},
					columnScroll: false
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
				scope.$watch("settings.sidemenu.shrink", SideMenuSrv.toggle);
			}
		};
	};
	MenuDirective.$inject=["SideMenuSrv", "$rootScope"];
	return MenuDirective;
});