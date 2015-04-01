define(["ng"], function(ng){
	var DatasetViewVM=function DatasetViewVM($scope, $stateParams, $state, dataset){
		this.dataset=dataset;
		console.debug("***dataset", dataset);
//		this.annotations=annotations;	
		
	};
	DatasetViewVM.$inject=["$scope", "$stateParams", "$state", "dataset"];
	return DatasetViewVM;
});
