define(
        ['jquery', 'angular', 'd3', 'notific8', 'api/Api', 'alertservice/AlertService'],
        function($, angular, d3) {

            return angular
                    .module('myApp.controllers', ['Mev.Api', 'Mev.AlertService'])
                    .controller(
                            'ImportsCtrl',
                            [
                                    '$scope',
                                    '$http',
                                    'alertService',
                                    function($scope, $http, alertService) {
                                        
                                        $('#loading').modal('hide');

                                        $scope.userUploads = [];

                                        $scope.loadUploads = function() {

                                            $http({
                                                method : 'GET',
                                                url : '/dataset',
                                                params : {
                                                    format : 'json'
                                                }
                                            })
                                            .success(function(data, status, headers, config) {
                                                  $scope.userUploads = data; 
                                                  
                                             })
                                             .error(function(data, status, headers, config) {
                                                        var message = "Could not upload datasets. If "
                                                            + "problem persists, please contact us.";

                                                        var header = "Heatmap Update Problem (Error Code: "
                                                            + status
                                                            + ")";

                                                        alertService
                                                            .error(
                                                                    message,
                                                                    header);
                                              });
                                        };

                                        $scope.loadUploads();

                                    }])
                    .controller(
                            'MainPanelController',
                            [
                                    'MevSelectionService',
                                    '$scope',
                                    '$element',
                                    '$attrs',
                                    'alertService',
                                    '$routeParams',
                                    function(MevSelectionService, $scope, $element, $attrs , alertService, $routeParams) {

                                    	if($routeParams.datasetName){                                    		
                                    		 $scope.baseUrl = '/annotations/'
                                                 + $routeParams.datasetName
                                                 + '/annotation';
                                             $scope.annotationsUrl = $scope.baseUrl
                                                 + '/column/new/dataset/';
                                    	}else{
                                    		$scope.annotationsUrl="about:blank";
                                    	}
                                    		

                                        $scope.tabs = {};
                                        if ($scope.tabs != undefined) {
                                            $scope.tabs.activeTab = 'visualize';
                                            $scope.tabs.visualizeTabActive = true;
                                            $scope.tabs.annotationsTabActive = false;
                                        }
                                        $scope.setActiveTab = function(name) {
                                            $scope.tabs.activeTab = name;
                                            if (name == 'annotations') {
                                                $scope.tabs.visualizeTabActive = false;
                                                $scope.tabs.annotationsTabActive = true;
                                            } else {
                                                $scope.tabs.visualizeTabActive = true;
                                                $scope.tabs.annotationsTabActive = false;
                                            }
                                        };
                                        
                                        $scope.isActiveTab = function(name) {
                                            if ($scope.tabs.activeTab == name)
                                                return true;
                                            else
                                                return false;
                                        };

                                        $scope.$on('ViewAnnotationsEvent',
                                                        function(event, selection, dimension, annotationSource) {
//                                                            var annotationsUrl = $scope.baseUrl;
                                        					var annotationsUrl = '/annotations/'
						                                            + $scope.project.dataset.datasetName
						                                            + '/annotation/'
                                                                    + dimension
                                                                    + "/";
                                                            if (typeof selection != 'undefined') {
                                                                annotationsUrl += selection.name
                                                                        + "/";
                                                            } else {
                                                                annotationsUrl += "new/";
                                                            }
                                                            if (typeof annotationSource != 'undefined') {
                                                                annotationsUrl += annotationSource
                                                                        + "/";
                                                            } else {
                                                                annotationsUrl += "dataset/";
                                                            }

                                                        var randomProjectId=Math.floor(Math.random()*11)
                                                        if (typeof selection != 'undefined') {
                                                        	if(typeof selection.properties.selectionFacetLink != 'undefined')
                                                        		annotationsUrl += selection.properties.selectionFacetLink;
                                                        	else{
                                                        		var facetUrl = "{\"facets\":[{\"c\":{\"type\":\"text\",\"name\":\"ID\",\"columnName\":\"MEVID\",\"mode\":\"regex\",\"caseSensitive\":false,\"query\":\""+
                                                        		selection.keys.join("|")
                                                        		+"\"}}]}"; 
                                                        		
                                                        		annotationsUrl += "project?project=MEV-"+randomProjectId+"&ui="+window.escape(facetUrl);
                                                        		;
                                                        	}
                                                        }else{
                                                        	annotationsUrl += "?"+randomProjectId;
                                                        }                                                       ;
                                                        $scope.annotationsUrl = annotationsUrl;                                                             
                                                        var elm = document.querySelector('#annotationsTabLink');
                                                        $(elm).trigger('click');
                                                    });//end:ViewAnnotationsEvent                     
                                        
                                       
                                       $scope.$on('SeletionAddedEvent', function(event, dimensionType){
                                    	  

                                    	  if(dimensionType === 'column' || dimensionType === 'row'){

                                    	      $scope.project.dataset.resetSelections(dimensionType);


                                    	  } else {
                                    	      alertService.error(
                                                      "Invalid dimension type:"+dimensionType,
                                                      "Selection Add Event Error ");
                                    	  }
                                       }); //end:SelectionAddedEvent
                                       

                                       $scope.$on('ViewVisualizeTabEvent',
                                           function(event) {                                    	   		
                                               	var elm = document.querySelector('#heatmapTabLink');
                                               	$(elm).trigger('click');
                                               	event.preventDefault(); //mark event as handled, so further propagation will have no affect
                                       		});//end:ViewVisualizeTabEvent
                                       
                                    }]);

        });