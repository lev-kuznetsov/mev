define(["./selectionSetNode.less"], function(){
	var selectionSetNode = function($rootScope){
		return {
			scope: {
				node: '='				
			},			
			restrict: "AE",
			templateUrl: "app/widgets/dataset/selectionSet/SelectionSetNode/selectionSetNode.tpl.html",			
			link: function(scope, elm, attrs, ctrl){
				console.debug("selectionSetNode link", scope.node);				
				scope.vm={
					delete: function(e){
						console.debug("selectionSetNode onClick", e);
						if(confirm("Delete selection '"+scope.node.nodeData.name+"'?"))
							$rootScope.$broadcast("root.dataset.selectionSet.delete", scope.node.parent.nodeData.type, scope.node.nodeData);
						e.stopPropagation();
					}	
				};
				/*
				var killwatch=scope.$watchCollection("node.nodeData.selections", function(newv, oldv){
					console.debug("$watch node.nodeData", newv===oldv);
					if(newv !== oldv){
						console.debug("$watch node.nodeData", newv, oldv);
						killwatch();
						scope.$emit("ui:projectTree:dataChanged");						
					}					
				});						
				*/		
			}
		};
	};
	
	selectionSetNode.$inject=["$rootScope"];
	return selectionSetNode;
});