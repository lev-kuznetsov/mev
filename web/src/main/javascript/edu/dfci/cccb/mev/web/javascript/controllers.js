define(
        ['jquery', 'angular', 'd3', 'mev-workspace', 'notific8', 'api/Api', 'alertservice/AlertService', 'angular-route', 'mev-workspace'],
        function($, angular, d3, mevWorkspace) {

            return angular
                    .module('myApp.controllers', ['Mev.Api', 'Mev.AlertService', 'mev-workspace'])
                    .controller(
                            'ImportsCtrl',
                            [
                                    '$scope',
                                    '$http',
                                    '$routeParams',
                                    'alertService',
                                    'GoogleDriveResourceService',
                                    '$state',
                                    'mevWorkspace',
                                    function($scope, $http, $routeParams, alertService, DriveResource, $state, mevWorkspace) {
                                        
                                        $('#loading').modal('hide');
                                        $scope.$on("mui:error:sessionTimeout", function(){
                                            $state.go(".sessionTimeout");
                                        });
                                        $scope.userUploads = [];
                                        
                                        $scope.loadUploads = function() {
                                            
                                            mevWorkspace.getDatasets()
                                                .then(function(dbs){
                                                    $scope.userUploads = dbs;
                                                })
                                                .catch(function(e){
                                                    var message = "Could not upload datasets. If "
                                                        + "problem persists, please contact us.";

                                                    var header = "Heatmap Update Problem (Error Code: "
                                                        + JSON.stringify(e)
                                                        + ")";

                                                    alertService
                                                        .error(
                                                            message,
                                                            header);
                                                });
                                        };
                                        

                                        $scope.googleDrive = undefined;
                                        
                                        // DriveResource.get({
                                        //
                                        // }, function(response){
                                        // 	$scope.googleDrive = response;
                                        // }, function(error){
                                        // 	console.log(error)
                                        // 	alertService.error("Couldn't pull Google Drive information!",
                                        // 			"Google Drive Error")
                                        // })

                                        $scope.loadUploads();
                                        
                                        $scope.postDriveFile = function(id){
                                        	DriveResource.post({
                                        		'id':id
                                        	},{
                                        	},function(response){
                                        	
                                        		$scope.loadUploads();
                                        	}, function(error){

                                            	console.log(error);
                                        		alertService.error("Couldn't add Google Drive file!",
                                        			"Google Drive Error");
                                        	});
                                        };
                                        
                                        //set active tab based on URL querystring
                                        //TODO: move to the directive (changing DOM in controllers is a bad idea, the DOM may not be ready yet);                                        
                                        $scope.defaultActiveTab="tcga";
                                        $scope.currentTab=$scope.defaultActiveTab;
                                        $scope.toggelTab=function(tab){
                                        	$scope.currentTab=tab;
                                        	$scope.setActiveTab(tab);
                                        };
                                        $scope.isActive=function(tab){
                                        	if($scope.currentTab==tab)
                                        		return true;
                                        	else
                                        		return false;
                                        };
                                        $scope.getActiveCSS=function(tab){
                                        	if($scope.isActive(tab)){
                                        		return "active";
                                        	}else{
                                    			return "";
                                        	}
                                        };
                                        $scope.getTabId=function(tab){
                                        	return tab+"_tab";
                                        };
                                        $scope.setActiveTab=function(tab){
//                                        	$("#"+tab).tab('show');
                                        	$("#"+tab).addClass("active");
                                        	$("#"+$scope.getTabId(tab)).addClass("active");
                                        };
                                        if(typeof $routeParams.tab=="undefined")
                                        	$scope.setActiveTab($scope.defaultActiveTab);
                                        else	
                                        	$scope.setActiveTab($routeParams.tab);
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