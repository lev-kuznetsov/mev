define(["ng"], function(ng){
	var counter=0;
	var LayouRowtVM = function($scope){
		this.rand = counter++;
		console.debug("LayoutRowVM", $scope.$id, this.rand);
		
		var columns;
		this.addColumn=function(column){
			columns.push(column);
		};
	};
	LayouRowtVM.$injcet=["$scope"];
	return LayouRowtVM;
});