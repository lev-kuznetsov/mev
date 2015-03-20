define(["ng"], function(ng){
	var SelectionSetsViewVM=function($scope, $stateParams, $state, dataset, dimension){
		that=this;
		this.dataset=dataset;
		$scope.selections=dataset[dimension].selections;
		$scope.dataset=dataset[dimension].selections;
	};
	SelectionSetsViewVM.$inject=["$scope", "$stateParams", "$state", "dataset", "dimension"];
	return SelectionSetsViewVM;
});
