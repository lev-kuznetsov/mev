'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('HeatmapCtrl', ['$scope', '$routeParams', '$http', function($scope, $routeParams, $http) {
    
    //Import Matrix Location Routing Parameter
    $scope.matrixLocation = $routeParams.matrixLocation;
    
    //Watch a change in route parameter?
    
      //Send HTTP Request to import data
      
    $http.get('matrix/' + $scope.matrixLocation).success(function (data) {
       $scope.dataset = data;	
	});
    
      //Somehow handle http request failure

  }])
  .controller('MyCtrl2', [function() {

  }]);
