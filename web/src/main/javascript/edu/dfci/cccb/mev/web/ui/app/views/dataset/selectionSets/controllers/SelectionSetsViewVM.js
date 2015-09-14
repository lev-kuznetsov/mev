define(["ng"], function(ng){
	var SelectionSetsViewVM=function($scope, $stateParams, $state, dataset, dimension){
		that=this;
		this.dataset=dataset;
		$scope.selections=dataset[dimension].selections;
		$scope.dataset=dataset[dimension].selections;
		$scope.heatmapData=dataset;
		$scope.vm={
				addRowSelection: function(){
					$state.go("root.dataset.rowSet", {datasetId: dataset.id});
				},
				addColumnSelection: function(){
					$state.go("root.dataset.columnSet", {datasetId: dataset.id});
				}
				
		};
	};
	SelectionSetsViewVM.$inject=["$scope", "$stateParams", "$state", "dataset", "dimension"];
	return SelectionSetsViewVM;
});
