define([], function(){
	var RowNode = function($rootScope){
		return {
			scope: {
				node: '='				
			},			
			restrict: "AE",
			templateUrl: "app/widgets/dataset/row/rowNode/templates/rowNode.tpl.html",			
			controller: "RowNodeVM",
			controllerAs: "RowNodeVM",
			link: function(scope, elm, attrs, ctrl){
				console.debug("rowNode link", scope.node);				
				scope.vm={
					onClick: function(e){
						console.debug("rowNode onClick", e);
						$rootScope.$state.go("root.dataset.rowSet", {setId: "new"});
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
				});		*/						
			}
		};
	};
	
	RowNode.$inject=["$rootScope"];
	return RowNode;
});