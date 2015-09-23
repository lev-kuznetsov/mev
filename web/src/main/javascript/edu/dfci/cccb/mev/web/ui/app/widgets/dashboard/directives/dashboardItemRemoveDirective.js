define(["ng"], function(ng){
	var DashbaordItemAddDirective = function DashbaordItemAddDirective($rootScope){
		return {			
			restrict: "AC",
			scope: {
				muiDashboardItemRemove: "@"					
			},
			link: function(scope, elm, attr){				
				scope.vm={
						removeItem: function(){
							console.debug("remove item button click");
							$rootScope.$broadcast("ui:dashboard:removeItem", {name: attr.muiDashboardItemRemove});							
						}
				};
				elm.bind("click", function(){scope.$apply(scope.vm.removeItem);});
			}
		};
	};
	DashbaordItemAddDirective.$inject=["$rootScope"];
	DashbaordItemAddDirective.$name = "muiDashboardItemRemoveDirective";
	return DashbaordItemAddDirective; 
});