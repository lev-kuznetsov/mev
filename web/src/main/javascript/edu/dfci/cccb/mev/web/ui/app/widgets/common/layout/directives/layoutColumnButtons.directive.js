define(["ng"], function(ng){
	var LayoutColumnButtons = function (LayoutSrv){
		
		return {			
//			scope: true,
			restrict: "E",					
			templateUrl: "app/widgets/common/layout/directives/layoutColumnButtons.tpl.html",
			require: ["layoutColumnButtons", "^layoutColumn"],
			controller: "LayoutColumnButtonsVM",			
			controllerAs: "vm",				
			link: function(scope, elem, attr, controllers){
				var vm = controllers[0];
				var layoutColumn = controllers[1];
				console.debug("LayoutColumnButtons.link", controllers);
			}
		};
	};	
	
	LayoutColumnButtons.$inject=["LayoutSrv"];
	return LayoutColumnButtons;
});