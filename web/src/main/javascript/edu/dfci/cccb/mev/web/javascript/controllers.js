define ([ 'angular', 'jquery' ], function (angular, $) {

  return angular.module ('myApp.controllers', []).controller ('HeatmapCtrl',
      [ '$scope', '$routeParams', 'API', function ($scope, $routeParams, API) {

    	if (!$routeParams.datasetName){
    		$scope.heatmapId = " Mock Heatmap";
            	
    	} else {
    		$scope.heatmapId = $routeParams.datasetName;
            
    	}
        

      } ]);

});