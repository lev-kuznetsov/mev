ctrl.controller('GeneSelectCtrl', ['$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {

	
	$scope.tuples = [["GNDE3", "23", "yes"],
	                ["GNDE4", "15", "no"],
	                ["GNDE5", "18", "yes"],
	                ["GNDE7", "5", "no"],
	                ["GNDE12", "26", "no"]];
	
	$scope.headers = ["Column1", "Column2", "Column3"];
	
	$scope.fieldFilters = [];
	
	$scope.remAll = function() {
		$scope.fieldFilters =[];
	}
	
	$scope.remFilter = function(input){
		console.log(input)
		if ($scope.fieldFilters.length >= 1) {
			$scope.fieldFilters = $scope.fieldFilters.filter( function(filt){
				return filt.variable != input.variable
			})
		}
		
	}
	
	$scope.addFilter = function(input){

		if ($scope.fieldFilters.indexOf(input) < 0) {
			$scope.fieldFilters.push({variable:input, value:"Insert Value", operator:"="});
		}
		
	}
	
	$scope.queryOptions = [];
	
	$scope.reqQuery = function(reqPage) {
		
		if (queryOptions) {
			
			console.log($scope.queryOptions)
			
			$http({
				method:"PUT",
				//url:"heatmap/"+$routeParams.geneset+"/annotation/"+"row"+ "/filter",
				params: {
					format:"json",
					page: reqPage,
					request: $scope.queryOptions
				}
			})
			.success( function(data) {
				$scope.tuples = data;
			})
			.error(function(){
				alert("error!")
			});
			
		}
		
		
		
	}


    
}]);