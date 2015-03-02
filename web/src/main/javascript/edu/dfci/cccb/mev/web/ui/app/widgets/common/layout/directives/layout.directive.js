define(["ng"], function(ng){
	var LayoutDirective = function LayoutDirective(LayoutSrv){
		
		return {			
			restrict: "E",
			transclude: true,			
			template: "app/widgets/common/directives/layout.tpl.html",
			controller: "LayoutVM",
			link: function(scope, elem, attr){				
			}
		};
	};	
	
	LayoutDirective.$inject=["LayoutSrv"];
	return LayoutDirective;
});