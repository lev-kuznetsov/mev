define(["ng"], function(ng){
	var LayoutVM = function($scope){
//		console.debug("LayoutVM", $scope.$id);
		this.columns=[];
	};
	LayoutVM.$injcet=["$scope"];
	return LayoutVM;
});