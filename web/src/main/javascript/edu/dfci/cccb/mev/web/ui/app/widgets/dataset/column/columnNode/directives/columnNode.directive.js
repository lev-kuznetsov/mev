define([], function(){
	var ColumnNode = function($rootScope){
		return {
			scope: {
				node: '='				
			},			
			restrict: "AE",
			templateUrl: "app/widgets/dataset/column/columnNode/templates/columnNode.tpl.html",			
			controller: "ColumnNodeVM",
			controllerAs: "ColumnNodeVM",
			link: function(scope, elm, attrs, ctrl){
				console.debug("columnNode link", scope.node);				
				scope.vm={
					onClick: function(e){
						console.debug("columnNode onClick", e);
						$rootScope.$state.go("root.dataset.columnSet", {setId: "new"});
						e.stopPropagation();
					}	
				};
				/*var killwatch=scope.$watchCollection("node.nodeData.selections", function(newv, oldv){
					console.debug("$watch node.nodeData", newv===oldv);
					if(newv !== oldv){
						console.debug("$watch node.nodeData", newv, oldv);
						killwatch();
						scope.$emit("ui:projectTree:dataChanged");						
					}					
				});	*/							
			}
		};
	};
	
	ColumnNode.$inject=["$rootScope"];
	return ColumnNode;
});