define(["ng"], function(ng){
	var ToggleSidepanelDirective = function(){
		return {
			restrict: "EA",
			scope: {
				toggleSidepanel: "@"
			},						
			template: "<button ng-click='toggle()'>"
				+ "<i class=\"fa fa-bars\"></i>"
				+ "</button>",
//			compile: function(tElm,tAttrs){
//			return function(scope, elem, attr){
//				var exp = $parse('sidepanelSrvc.toggle(direction)');
//				elem.bind('click', function(){
//					console.debug("CLIK!!! toggle 2");
//					exp(scope, {sidepanelSrvc: sidepanelSrvc, direction: scope.toggleSidepanel});
//				});			
//			}
//		}
			link: function(scope, elem, attr){
				scope.test=function(){
					console.debug("CLIK!!! toggle 1");
					sidepanelSrvc.toggle(attr.toggleSidepanel);
				};			
			}
		};
	};
	ToggleSidepanelDirective.$inject=[];
	return ToggleSidepanelDirective;
});