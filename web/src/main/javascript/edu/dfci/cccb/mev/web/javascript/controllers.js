define ([ 'angular', 'jquery' ], function (angular, $) {

  return angular.module ('myApp.controllers', []).controller ('HeatmapCtrl',
      [ '$scope', '$routeParams', 'API', function ($scope, $routeParams, API) {

        $scope.heatmapId = "Heatmap";
        
        API.heatmap.get('mock/data');

      } ]);

});