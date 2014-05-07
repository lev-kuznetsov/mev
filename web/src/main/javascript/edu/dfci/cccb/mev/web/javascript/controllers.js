define(
        ['jquery', 'angular', 'd3', 'notific8', 'api/api'],
        function($, angular, d3) {

            return angular
                    .module('myApp.controllers', ['Mev.api'])
                    .controller(
                            'HeatmapCtrl',
                            [
                                    '$scope',
                                    '$routeParams',
                                    '$http',
                                    'pseudoRandomStringGenerator',
                                    '$rootScope',
                                    '$location',
                                    'logger',
                                    'alertService',
                                    'api.dataset',
                                    'api.dataset.analysis',
                                    function($scope, $routeParams, $http, prsg, 
                                    		$rS, $loc, log, alertService, 
                                    		apiDataset, apiAnalysis) {

                                        if (!$routeParams.datasetName) {
                                            
                                            $('#loading').modal('hide');
                                            $loc.path('/datasets');
                                            return

                                        };

                                        $scope.pageLoading = true;
                                        $('#loading').modal();

                                        $scope.heatmapId = $routeParams.datasetName;

                                        $scope.colorOptions = [
                                                "Green-Black-Red",
                                                "Yellow-Black-Blue",
                                                "Red-White-Blue"];

                                        $scope.selectedColor = "Yellow-Black-Blue";

                                        $scope.defaultColors = function() {
                                            $scope.selectedColor = "Yellow-Black-Blue";
                                        };
                                        $scope.defaultColors();

                                        $scope.heatmapData = undefined;
                                        

                                        $scope.heatmapViews = {
                                                top: undefined,
                                                side: undefined
                                                
                                        }
                                        
                                        $scope.previousHCLClusters = undefined;
                                        $scope.previousLimmaAnalysis = undefined;
                                        $scope.previousKMeansClusters = undefined;
                                        $scope.previousAnalysisList = undefined;
                                        $scope.clickSelectionMode = false;
                                        $scope.previousTTest = undefined;
                                        
                                        apiDataset.get(function(result){
                                        		$('#loading').modal('hide');
                                            	$scope.heatmapData = result;
                                            	$scope.heatmapData.firstRows = $scope.heatmapData.row.keys;
                                        	
                                        }, function(error){
                                        	 var message = "Could not retrieve dataset "
                                                 + $routeParams.datasetName
                                                 + ". If the"
                                                 + "problem persists, please contact us.";

                                             var header = "Heatmap Download Problem (Error Code: "
                                                     + error.status
                                                     + ")";

                                             alertService.error(message, header);
                                             
                                             $('#loading').modal('hide');
                                             $loc.path('/datasets');
                                        });
                                        
                                        
                                        
                                        $scope.buildPreviousAnalysisList = function() {

                                            $scope.previousHCLClusters = [];

                                            $scope.previousLimmaAnalysis = [];
                                            $scope.previousKMeansClusters = [];
                                            $scope.previousAnova = [];
                                            $scope.previousTTest = [];
                                            
                                            apiAnalysis.getAll(function(response){
                                            	
                                                    var prevList = response.names;
                                                    
                                                    $scope.previousAnalysisList = prevList;

                                                    prevList.map(function(name) {

                                                        apiAnalysis.get({analysisName:name}, function(data) {

                                                        	console.log(data)
                                                            var randstr = prsg(5);
                                                            var randstr2 = prsg(5);

                                                            if (data.type == "Hierarchical Clustering") {

                                                                data.name = name;
                                                                data.href = "#" + randstr;
                                                                data.parentId = randstr2;
                                                                data.dataParent = '#' + randstr2;
                                                                    data.divId = randstr;
                                                                    
                                                                    
     
                                                                    $scope.previousHCLClusters
                                                                            .push(data);

                                                                } else
                                                                    if (data.type == "LIMMA Differential Expression Analysis") {

                                                                    $scope.previousLimmaAnalysis
                                                                            .push({
                                                                                name : name,
                                                                                href : "#"
                                                                                        + randstr,
                                                                                parentId : randstr2,
                                                                                dataParent : '#'
                                                                                        + randstr2,
                                                                                divId : randstr,
                                                                                datar : data
                                                                            });
                                                            } else if (data.type == "K-means Clustering"){
                                                                $scope.previousKMeansClusters.push(data);

                                                            } else if (data.type == "Anova Analysis") {
                                                                $scope.previousAnova.push(data);

                                                            } else if (data.type == "t-Test Analysis"){                                                                                                    
                                                                $scope.previousTTest.push(data);

                                                            }

                                                		}, function(error) {
                                                            var message = "Could not retrieve previous analysis"
                                                                    + name
                                                                    + ". If the"
                                                                    + "problem persists, please contact us.";

                                                            var header = "Heatmap Download Problem (Error Code: "
                                                                    + error.status
                                                                    + ")";

                                                            alertService.error(message, header);
                                            			});
                                                    });
                                                    
                                            }, function(error) {
                                            		var message = "Could not download previous analysis list. If "
                                                        + "problem persists, please contact us.";

	                                                var header = "Previous Analysis Download Problem (Error Code: "
	                                                        + error.status
	                                                        + ")";
	
	                                                alertService.error(message, header);
	                                                $loc.path('/');
                                            	
                                            });
                                            
                                        };
                                        
                                        $scope.updateHierarchical = function(inputData){
                                            
                                            $http({
                                                method : 'POST',
                                                url : '/dataset/'
                                                        + $routeParams.datasetName
                                                        + '/analysis/'
                                                        + inputData.name,
                                                params : {
                                                    format : 'json'
                                                }
                                            })
                                            .success(function(data, status, headers, config) {
                                                    
                                                    inputData.keys = data.keys
                                                    inputData.dimension = data.type
                                                    
                                                    if (inputData.dimension == 'row') {
                                                        
                                                        $scope.heatmapViews.side = inputData;
                                                        $scope.heatmapData.row.keys = inputData.keys;
                                                        
                                                    } else if (inputData.dimension == 'column') {
                                                        
                                                        $scope.heatmapViews.top = inputData;
                                                        $scope.heatmapData.column.keys = inputData.keys;
                                                        
                                                    }
                                                    $scope.$broadcast('ViewVisualizeTabEvent');
                                                })
                                                .error(function(data, status, headers, config) {
                                                            var message = "Could not update heatmap. If "
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
                                        
                                        $scope.updateKMeans = function(inputData){
                                            
                                            var keys = [];
                                            for (var i=0; i<inputData.clusters.length; i++){
                                                for (var j=0; j<inputData.clusters[i].length; j++){
                                                    keys.push(inputData.clusters[i][j])
                                                }
                                            }
                                            inputData.keys = keys;
                                            if (inputData.dimension == 'row') {
                                                
                                                $scope.heatmapViews.side = inputData;
                                                $scope.heatmapData.row.keys = inputData.keys;
                                                
                                            } else if (inputData.dimension == 'column') {
                                                
                                                $scope.heatmapViews.top = inputData;
                                                $scope.heatmapData.column.keys = inputData.keys;
                                                
                                            }
                                        };

                                        $scope.updateHeatmapView = function(inputData) {

                                            $scope.$broadcast('ViewVisualizeTabEvent');

                                            if (inputData.type == "Hierarchical Clustering"){
                                                $scope.updateHierarchical(inputData)
                                            } else if (inputData.type == "K-means Clustering") {
                                                $scope.updateKMeans(inputData)
                                            }
                           
                                        };
                                        
                                        $scope.saveImage = function(cluster) {

                                        	var svg = d3.select("#svgWrapperHclTree_"+cluster.name).select("svg");
                                        	 var html = svg
                                        	 .attr("version", 1.1)
                                             .attr("xmlns", "http://www.w3.org/2000/svg")
                                             .node().parentNode.innerHTML;
                                        	  
                                        	 var imgsrc = 'data:image/svg+xml;base64,'+ btoa(html);
//                                        	 var img = '<img src="'+imgsrc+'">';
//                                        	 d3.select("#svgdataurl").html(img);
                                        	  
                                        	  
                                        	 var canvas = document.querySelector("#canvasHclTree_"+cluster.name);
                                        	 var context = canvas.getContext("2d");
                                        	 canvas.width=svg.attr('width');
                                        	 canvas.height=svg.attr('height');
                                        	 
                                        	 var image = new Image;
                                        	 image.src = imgsrc;
                                        	 image.onload = function() {
                                        		                                         		 
                                            	 canvas.style.opacity = 1;
                                            	 context.beginPath();
                                                 context.rect(0, 0, canvas.width, canvas.height);                                                 
                                            	 context.fillStyle ="#FFFFFF";
                                            	 context.fill();
                                            	 
                                            	 context.drawImage(image, 0, 0);
                                            	 
	                                        	 canvas.toBlob(function(blob) {
	                                        		    saveAs(blob, cluster.name+".png");
	                                        	 });
//	                                        	 var canvasdata = canvas.toDataURL("image/png");
//	                                        	  
//	                                        	 var pngimg = '<img src="'+canvasdata+'">';
//	                                        	 d3.select("#pngdataurl").html(pngimg);
//	                                        	  
//	                                        	 var a = document.createElement("a");
//	                                        	 a.download = "sample.png";
//	                                        	 a.href = canvasdata;
//	                                        	 a.click();
                                        	 };
                           
                                        };
                                        
                                        $scope.buildPreviousAnalysisList();

                                    }])
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
                                    function(MevSelectionService, $scope, $element, $attrs , alertService) {

                                        $scope.baseUrl = '/annotations/'
                                                + $scope.heatmapId
                                                + '/annotation';
                                        $scope.annotationsUrl = $scope.baseUrl
                                                + '/column/new/dataset/';

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
                                                            var annotationsUrl = $scope.baseUrl
                                                                    + "/"
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
                                    	  
                                    	  if(dimensionType=='column'){
                                        	  MevSelectionService.getColumnSelectionQ().then(function(d){
                                        		  $scope.heatmapData.column.selections=d.selections;
                                        	  });
                                    	  }else if(dimensionType=='row'){
                                        	  MevSelectionService.getRowSelectionQ().then(function(d){
                                                  $scope.heatmapData.row.selections=d.selections;
                                              });
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