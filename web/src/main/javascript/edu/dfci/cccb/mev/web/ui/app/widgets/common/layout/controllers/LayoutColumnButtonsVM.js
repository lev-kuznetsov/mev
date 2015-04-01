define(["ng"], function(ng){
	
	var LayoutColumnButtonsVM = function($scope){		
		console.debug("LayoutColumnButtonsCtrl", $scope.$id, this.rand);
		this.canExpandLeft=function(){
			return true;
		};
		this.canExpandRight=function(){
			return true;
		};
	};
	
	LayoutColumnButtonsVM.$inject=["$scope"];
	return LayoutColumnButtonsVM;
});