define ([ 'angular', 'jquery' ], function (angular, $) {

  return angular.module ('myApp.controllers', []).controller ('HeatmapCtrl',
      [ '$scope', '$routeParams', 'API', function ($scope, $routeParams, API) {

    	if (!$routeParams.datasetName){
    		$scope.heatmapId = " Mock Heatmap";
            	
    	} else {
    		$scope.heatmapId = $routeParams.datasetName;
            
    	}
        

      } ])
      .controller ('ImportsCtrl',
      [ '$scope', 'API', function ($scope, API) { 
    	  
    	  $scope.userUploads = [];
  
    	  $scope.loadUploads = function(){
    		  
    		  $scope.userUploads = API.user.datasets.get().then(function(data){
              	return data;
              });
    		  
    	  };
    	  
    	  $scope.loadUploads();
    	  
      }]);
  

});