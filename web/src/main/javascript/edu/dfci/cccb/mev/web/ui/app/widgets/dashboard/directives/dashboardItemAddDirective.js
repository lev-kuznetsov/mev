define(["ng"], function(ng){
	var DashbaordItemAddDirective = function DashbaordItemAddDirective($rootScope){
		return {			
			restrict: "AC",
			scope: {
				name: "@",
				targetId: "@",
				contentWidth: "@",
				contentHeight: "@"					
			},
//			template: "<button ng-click=\"vm.addItem()\">add to dash</button>",
			link: function(scope, elm, attr){				
				scope.vm={
						addItem: function(){
							$rootScope.$broadcast("ui:dashboard:addItem", {targetId: attr.targetId, name: "dashlet", title: "added from outside", content: "content content"});
							console.debug("add item click");
						}
				};
				elm.bind("click", function(){scope.$apply(scope.vm.addItem);});
			}
		};
	};
	DashbaordItemAddDirective.$inject=["$rootScope"];
	DashbaordItemAddDirective.$name = "muiDashboardItemAddDirective";
	return DashbaordItemAddDirective; 
});