define(["ng"], function(ng){
	var LayoutColumnDirective = function($rootScope){
		
		return {
			restrict: "AE",					
			controller: "LayoutRowVM",
			controllerAs: "LayoutRowVM",
			link: function(scope, elem, attrs, controller){
				
				console.debug("LayoutRowDirective.link controller", controller);
				
			}
		};
	};
	
	LayoutColumnDirective.$inject=["$rootScope"];
	return LayoutColumnDirective;  
});