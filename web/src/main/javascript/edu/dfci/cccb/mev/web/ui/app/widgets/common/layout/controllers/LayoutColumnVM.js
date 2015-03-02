define(["ng"], function(ng){
	var counter=10;
	var LayoutColumnVM = function($scope){
		this.rand = counter++;
		console.debug("LayoutColumnVM", $scope.$id, this.rand);
		this.dosomething=function(){};
		this.ngClass=function(){
			
		};
	};
	
	LayoutColumnVM.$inject=["$scope"];
	return LayoutColumnVM;
});