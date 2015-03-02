define(["ng"], function(ng){
	var ColumnSetsViewVM=function DatasetViewVM($scope, $stateParams, $state, dataset){
		that=this;
		console.debug("ColumnSetsViewVM", dataset);		
		this.dataset=dataset;
		$scope.selections=dataset.column.selections;
		$scope.dataset=dataset.column.selections;
	};
	ColumnSetsViewVM.$inject=["$scope", "$stateParams", "$state", "dataset"];
	return ColumnSetsViewVM;
});
