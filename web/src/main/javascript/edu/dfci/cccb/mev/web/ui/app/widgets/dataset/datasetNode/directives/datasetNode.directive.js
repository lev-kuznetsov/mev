define([], function(){
	var DatasetNode = function($rootScope){
		return {
			scope: {
				node: '='				
			},			
			restrict: "AE",
			templateUrl: "app/widgets/dataset/row/datasetNode/templates/datasetNode.tpl.html",			
			controller: "DatasetNodeVM",
			controllerAs: "DatasetNodeVM",
			link: function(scope, elm, attrs, ctrl){
				console.debug("datasetNode link", scope.node);				
				scope.vm={
					onClick: function(e){
						console.debug("datasetNode onClick", e);
						$rootScope.$state.go("root.dataset", {datsetId: scope.node.nodeData.name});
						e.stopPropagation();
					}	
				};
			}
		};
	};
	
	DatasetNode.$inject=["$rootScope"];
	return DatasetNode;
});