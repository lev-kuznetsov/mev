'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('HeatmapCtrl', ['$scope', '$routeParams', '$http', function($scope, $routeParams, $http) {
    
    //Import Matrix Location Routing Parameter
    $scope.matrixLocation = $routeParams.matrixLocation;
    
    //Watch a change in route parameter?
    
      //Send HTTP Request to import data
      
    $http.get('data/' + $scope.matrixLocation + '.json').
    
      success(function (data) {
        $scope.dataset = data;
        $scope.colorscheme = 0;
        $scope.cellsize = 1;
	  });
    
      //Somehow handle http request failure
    $scope.setColorScheme = function(colorcode) {
		$scope.colorscheme = colorcode;
	}
	
	$scope.setCellSize = function(sizecode) {
		$scope.cellsize = sizecode;
	}

  }])
  .controller('AnalyzeCtrl', ['$scope', '$http', function($scope, $http) {
    
    //Get visualization json
    $http.get('data/visualization_data.json').
    
      success(function (data) {
        $scope.visualization_data = data;
	  });
	//Error handling
    //Get data json
  }]);
