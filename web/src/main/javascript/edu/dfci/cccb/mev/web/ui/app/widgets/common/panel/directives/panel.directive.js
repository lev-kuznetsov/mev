define(["ng"], function(ng){
	var PanelDirective = function PanelDirective(PanelSrv){
		
		return {			
			restrict: "E",
			transclude: true,
			template: "<div ng-transclude><div/>",
			controller: "PanelVM",
			link: function(scope, elem, attr){				
			}
		};
	};	
	
	PanelDirective.$inject=["PanelSrv"];
	return PanelDirective;
});