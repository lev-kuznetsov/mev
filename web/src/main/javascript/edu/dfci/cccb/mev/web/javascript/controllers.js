define ([ 'jquery', 'angular'], function ($, angular) {

  return angular.module ('myApp.controllers', []).controller ('HeatmapCtrl',
      [ '$scope', '$routeParams', 'API', 'pseudoRandomStringGenerator', '$rootScope', '$location', function ($scope, $routeParams, API, prsg, $rS, $loc) {

    	if (!$routeParams.datasetName){
    		$loc.path('/');
            	
    	} else {
    		$scope.heatmapId = $routeParams.datasetName;
    		
    		
    		$scope.heatmapData = undefined;
            $scope.heatmapLeftTree = undefined;
            $scope.heatmapTopTree = undefined;
            $scope.heatmapLeftTreeName = undefined;
            $scope.heatmapTopTreeName = undefined;
            
    		$scope.buildPreviousAnalysisList = function() {
    		  
    		  $scope.previousHCLClusters = [];
    		  
    		  $scope.previousLimmaAnalysis = [];

                API.dataset.analysis.list ($routeParams.datasetName).then (
                    function (prevList) {

                      $scope.previousAnalysisList = prevList
                          .map (function (name) {

                            
                            
                            //$rS.$apply();

                             API.analysis.get ({
                                name : name,
                                dataset : $routeParams.datasetName
                              }).then(function(d){ 
                                
                                var randstr = prsg (5);
                                var randstr2 = prsg (5);
                                
                                 if (d.type == "Hierarchical Clustering"){ 
                                     $scope.previousHCLClusters.push({
                                       name : name,
                                       href : "#" + randstr,
                                       parentId: randstr2 ,
                                       dataParent: '#' + randstr2,
                                       divId : randstr,
                                       datar :d 
                                     });
                                 } else if (d.type == "LIMMA Differential Expression Analysis") {
                                   $scope.previousLimmaAnalysis.push({
                                     name : name,
                                     href : "#" + randstr,
                                     parentId: randstr2 ,
                                     dataParent: '#' + randstr2,
                                     divId : randstr,
                                     datar :d 
                                   });
                                 }
                              });                               
                              

                          });
                      

                    });

              };
            
    	}
        

      } ])
      .controller ('ImportsCtrl',
      [ '$scope', 'API', function ($scope, API) { 
    	  
    	  $scope.userUploads = [];
  
    	  $scope.loadUploads = function(){
    		  
    	    $scope.$apply(function(){
    	    	API.user.datasets.get().then(function(data){
    	               $scope.userUploads = data;
    	            });
    	    });

    	  };
    	  
    	  API.user.datasets.get().then(function(data){
    	    $scope.userUploads = data;
    	  });
    	  
    	  
      }])
      .controller('MainPanelController', ['$scope', '$element', '$attrs', function($scope, $element, $attrs){
			
    	  $scope.$on('ViewAnnotationsEvent', function(event, selection, dimension){
				//alert('view annot!: ' + angular.toJson(event) + "\nselection :" + selection.name);
				//var iframe = angular.element("#annotationsIframe");				
				var url = $scope.baseUrl+"/"+dimension+"/"+selection.name+"/"+selection.properties.selectionFacetLink;
				$scope.annotationsUrl=url;
				var annotationsTab = angular.element("#annotationsTabLink");				
				annotationsTab.trigger("click");
			});
    	  
    	  ///annotations/{{heatmapId}}/annotation/column    	  
    	  $scope.baseUrl='/annotations/'+$scope.heatmapId+'/annotation';
    	  $scope.annotationsUrl=$scope.baseUrl+'/column/new/';
		}]);
  

});