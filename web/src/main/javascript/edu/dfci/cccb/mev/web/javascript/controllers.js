define ([ 'angular', 'jquery' ], function (angular, $) {

  return angular.module ('myApp.controllers', []).controller ('HeatmapCtrl',
      [ '$scope', '$routeParams', 'API', 'pseudoRandomStringGenerator', function ($scope, $routeParams, API, prsg) {

    	if (!$routeParams.datasetName){
    		$scope.heatmapId = " Mock Heatmap";
            	
    	} else {
    		$scope.heatmapId = $routeParams.datasetName;
    		
    		$scope.buildPrevioiusClusters = function() {

                API.dataset.analysis.list ($routeParams.datasetName).then (
                    function (prevList) {

                      $scope.previousClusters = prevList
                          .map (function (name) {

                            var randstr = prsg (5);
                            var randstr2 = prsg (5);

                            return {
                              name : name,
                              href : "#" + randstr,
                              parentId: randstr2 ,
                              dataParent: '#' + randstr2,
                              divId : randstr,
                              datar : API.analysis.hcl.get ({
                                name : name,
                                dataset : $routeParams.datasetName
                              })
                            };

                          });

                    });

              };
            
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