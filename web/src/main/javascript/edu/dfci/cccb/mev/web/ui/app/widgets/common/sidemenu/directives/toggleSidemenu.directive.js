define(["ng"], function(ng){
	var ToggleSidemenuDirective = function(SideMenuSrv){
		return {
			restrict: "A",
//			scope: {},						
//			template: "<button class='navbar-toggle' ng-click='toggle()'>" +
//					"<span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span>" +
//				    "</button>",
//			template: "<button ng-click='toggleSide()'>"
////				+ "<span class='glyphicon glyphicon-list' aria-hidden='true'></span>"
//				+ "<i class=\"fa fa-bars\"></i>"
//				+ "</button>",
//			compile: function(tElm,tAttrs){
//			return function(scope, elem, attr){
//				var exp = $parse('SideMenuSrv.toggle(direction)');
//				elem.bind('click', function(){
//					console.debug("CLIK!!! toggle 2");
//					exp(scope, {SideMenuSrv: SideMenuSrv, direction: scope.toggleSidemenu});
//				});			
//			}
//		}
			controller: ["$scope", function($scope){
				$scope.toggleSide=function(){
					console.debug("CONTRA!!! toggle 1");
//					$scope.settings.sidemenu.shrink=!$scope.settings.sidemenu.shrink;
					SideMenuSrv.toggle();
				};
			}],
			link: function(scope, elem, attr){
				scope.toogle=function(){
					console.debug("CLIK!!! toggle 1");
					SideMenuSrv.toggle();
//					settings.sidemenu.shrink=!settings.sidemenu.shrink;
					
				}
//				elem.bind('click', function(){
//					console.debug("CLIK!!! toggle ");
//					SideMenuSrv.toggle(attr.toggleSidemenu);
//				});			
			}
		};
	};
	ToggleSidemenuDirective.$inject=["SideMenuSrv"];
	return ToggleSidemenuDirective;
});