define(["ng"], function(ng){
	var RowSetsViewVM=function DatasetViewVM($scope, $stateParams, $state, dataset){
		that=this;
		console.debug("RowSetsViewVM", dataset);		
		this.dataset=dataset;
		$scope.selections=dataset.row.selections;
		$scope.dataset=dataset.row.selections;
	};
	RowSetsViewVM.$inject=["$scope", "$stateParams", "$state", "dataset"];
	return RowSetsViewVM;
});
