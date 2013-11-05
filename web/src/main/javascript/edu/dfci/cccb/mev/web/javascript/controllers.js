define(['angular'], function(angular){

  return angular.module('myApp.controllers', []).
  controller('HeatmapCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
          
    $scope.heatmapId = "Heatmap";
    
  }]);
  
});