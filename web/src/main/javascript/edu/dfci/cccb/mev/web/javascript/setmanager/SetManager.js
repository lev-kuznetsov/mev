define(['jquery','mui'], function(jquery, ng){
	return ng.module( 'Mev.SetManager', ["mevDomainCommon"])
		.directive('selectionSetManager', [function (){
			  return {				  		  
				  scope: {
					  heatmapId: '@',
					  heatmapData: '=',
					  selections: '='
				  },
				  controller: 'SelectionSetManagerCtl',				  
				  restrict : 'EA',
				  templateUrl : '/container/view/elements/setmanager/selectionSetManager',
				  link: function(scope, elm, attr){
				      return
				  }
			  };
		}])
		.directive('selectionSetList', [function (){
			return {
				scope: {
					selections: '=mevSelections'
					, baseUrl: '@mevBaseUrl'
					, setSelected: '&mevSayHello'
					, dimension: '@mevDemintion'
				},
				restrict : 'EA',
				transclude : true,
				replace : true,
				controller: 'SelectionSetManagerCtl',
				templateUrl : '/container/view/elements/setmanager/selectionSetList',
				link : function (scope, iElement, iAttrs, controller){
					
				}				
			};
		}])
		.directive('selectionSetEditForm', [function (){
			return {
				restrict: 'EA',
				replace: true,
				require: '^selectionSetList',
				templateUrl : '/container/view/elements/setmanager/selectionSetEditForm',				
			};
		}])		
		.controller('SelectionSetManagerCtl', 
	    ['$scope', '$element', '$attrs', '$routeParams', '$http', 'alertService', "mevContext", "$rootScope",
		function($scope, $element, $attrs, $routeParams, $http, alertService, mevContext, $rootScope){
			
			$scope.sayHelloCtl = function() {
				alert($scope.heatmapId + ":" + $scope.dataset.selections.column.values.length + ":" + $scope.$id);
			};		
			
			//alert(angular.toJson($attrs) );						
			$scope.annotationsUrl="about:blank";
			
			$scope.setSelected = function(item){
				$scope.selectedItem = item;
				$scope.selectedItemTmp = angular.copy(item);
				//alert("selected: " + item.name);
			};
			$scope.saveItem = function(item){
				//alert("save: " + item.name + " old: " + $scope.selectedItem.name);
				$scope.selectedItem.name = item.name;
				$scope.selectedItem.properties = item.properties;
			};
			$scope.showAnnotations = function(selection, dimention, annotationSource){
				$scope.$emit('ViewAnnotationsEvent', selection, dimention, annotationSource);				
			};
			
			$scope.selectionParams = {column:{name:undefined},
									  row:{name:undefined},
									  special:undefined};
			$scope.exportParams = {column:{name:undefined},
					  row:{name:undefined},
					  special:undefined};
			
			function getSelected(dimension){
			    return $scope.heatmapData[dimension].selections.filter(function(d){
                    // return (d.setSelectionChecked === true) ? true : false
                    return d.setSelectionChecked;
                        
                })
			};
			$scope.getSelected = getSelected;
			
			function pushNewSelection(dimension, selectedSets){
				var selection = {
					name: $scope.selectionParams[dimension].name,
					properties: {
						selectionDescription: '',
						selectionColor: '#'+('00000'+(Math.random()*0xFFFFFF<<0)).toString(16).substr(-6),
					},
					keys: selectedSets
				};

				$http({
                   method:"POST",
				   url:"/dataset/" + ($routeParams.datasetName || $scope.heatmapData.id) + "/"
					+ dimension
					+ "/selection/",
				   data: selection
				})
               .success(function(response){
                        $scope.$emit('SeletionAddedEvent', dimension);
                        var message = "Added selection with name " + $scope.selectionParams[dimension].name + ".";
                        var header = "Heatmap Selection Addition";
				   		$rootScope.$broadcast("mui:dataset:selections:added", dimension.toLowerCase(), selection);
                        alertService.success(message,header);
               })
               .error(function(data, status, headers, config) {
                    var message = "Couldn't add new selection. If "
                        + "problem persists, please contact us.";
               
                     var header = "Heatmap Selection Problem (Error Code: "
                        + status
                        + ")";
                             
                     alertService.error(message,header);
               });
			};
			
			function pushNewDataset(dimension, selectedSets){
				var selectionData = {
                    name: $scope.exportParams[dimension].name,
                    properties: {
                        selectionDescription: '',
                        selectionColor: '#ffffff',
                    },
                    keys: selectedSets
                };
				mevContext.current().selection.export({
                    datasetName: $routeParams.datasetName || $scope.heatmapData.id,
                    dimension: dimension

                }, selectionData,
                function (response) {
                	mevContext.current().resetSelections(dimension);
                    var message = "Added " + $scope.exportParams[dimension].name + " as new Dataset!";
                    var header = "New Dataset Export";

                    alertService.success(message, header);
                },
                function (data, status, headers, config) {
                    var message = "Couldn't export new dataset. If " + "problem persists, please contact us.";

                    var header = "New Dataset Export Problem (Error Code: " + status + ")";

                    alertService.error(message, header);
                });

/*
		    $http({
                    method:"POST", 
               url:"/dataset/" + ($routeParams.datasetName || $scope.heatmapData.id) + "/" 
                + dimension
                + "/selection/export",
               data:{
                   name: $scope.exportParams[dimension].name,
                   properties: {
                       selectionDescription: '',
                           selectionColor: '#ffffff',                     
                       },
                        keys: selectedSets
                    }
               })
               .success(function(response){
                        $scope.$emit('SeletionAddedEvent', dimension);
                        var message = "Exported new dataset with name " + $scope.exportParams[dimension].name + ".";
                        var header = "Export New Dataset";
                 
                 	  //  $http({
                    //        method:"POST", 
                    //        url:"/annotations/" + $scope.heatmapData.id + "/annotation/row" 
    	               //     + "/export?destId="+$scope.exportParams[dimension].name});
                	   // $http({
                    //        method:"POST", 
                    //        url:"/annotations/" + $scope.heatmapData.id + "/annotation/column" 
    	               //     + "/export?destId="+$scope.exportParams[dimension].name});
                 
                        alertService.success(message,header);
               })
               .error(function(data, status, headers, config) {
                    var message = "Couldn't export new dataset. If "
                        + "problem persists, please contact us.";
               
                     var header = "Export New Dataset (Error Code: "
                        + status
                        + ")";
                             
                     alertService.error(message,header);
               });*/
			};
			
			$scope.addDifferenceSelection = function(dimension){
                var selectedGroups = getSelected(dimension)
                 
                if (selectedGroups.length > 1){
                    
                    var sumOfElements = {};
                    
                    selectedGroups.map(function(group){
                        group.keys.map(function(element){
                            sumOfElements.hasOwnProperty(element)? sumOfElements[element]++ : sumOfElements[element] = 1
                        })
                    });
                    
                    var difference = Object.getOwnPropertyNames(sumOfElements).filter(function(element){
                        return (sumOfElements[element] === 1 
                                && $scope.selectionParams.special[dimension].keys.indexOf(element) > -1) ? true : false
                    })
                    
                    if (difference.length > 0){
                        pushNewSelection(dimension, difference)
                        
                    } else {
                        var message = "Difference has no values.";
                        
                        var header = "Heatmap Selection Info";
                                
                        alertService.info(message,header);
                    }
                    
                     
                 } else {
                     var message = "Cannot merge a single selection set.";
                
                      var header = "Heatmap Selection Info";
                              
                      alertService.info(message,header);
                 };
            };
			
			$scope.addIntersectionSelection = function(dimension){
			    var selectedGroups = getSelected(dimension)
	             
                if (selectedGroups.length > 1){
                    
                    var sumOfElements = {};
                    
                    selectedGroups.map(function(group){
                        group.keys.map(function(element){
                            sumOfElements.hasOwnProperty(element)? sumOfElements[element]++ : sumOfElements[element] = 1
                        })
                    });
                    
                    var intersection = Object.getOwnPropertyNames(sumOfElements).filter(function(element){
                        return (sumOfElements[element] == selectedGroups.length) ? true : false
                    })
                    
                    if (intersection.length > 0){
                        pushNewSelection(dimension, intersection)
                    } else {
                        var message = "Intersection has no values.";
                        
                        var header = "Heatmap Selection Info";
                                
                        alertService.info(message,header);
                    }
                    
                     
                 } else {
                     var message = "Cannot merge a single selection set.";
                
                      var header = "Heatmap Selection Info";
                              
                      alertService.info(message,header);
                 };
			};

			$scope.addMergedSelection = function(dimension){
				
				var selectedGroups = getSelected(dimension)
			 
				if (selectedGroups.length > 1){
					
					var newSelectionElements = [];
					
					selectedGroups.map(function(group){
						group.keys.map(function(j){
							if (newSelectionElements.indexOf(j) < 0){
							    newSelectionElements.push(j)
							}
						})
					});
					
					pushNewSelection(dimension, newSelectionElements);
					
			         
			     } else {
			    	 var message = "Cannot merge a single selection set.";
				
				      var header = "Heatmap Selection Info";
				              
				      alertService.info(message,header);
			     };
			};		
			
			$scope.exportSelection = function(dimension){
				
				var selectedGroups = getSelected(dimension);
			 
				if (selectedGroups.length > 0){
					
					var newSelectionElements = [];
					
					selectedGroups.map(function(group){
						group.keys.map(function(j){
							if (newSelectionElements.indexOf(j) < 0){
							    newSelectionElements.push(j)
							}
						})
					});
					
					pushNewDataset(dimension, newSelectionElements);
					
			         
			     } else {
			    	 var message = "Please check of at least one selection to export.";
				
				      var header = "Export Selection as New Dataset Info";
				              
				      alertService.info(message,header);
			     };
			};
			
			$scope.addItem = function(item){		
//ap:no need to manually add selection to the dataset because we'll reload them from the server by emitting the 'SelectionAddedEvent'				
//				$scope.$apply(function(){
//					
////					if(item.dimension.toLowerCase()=="column"){
//						//remove selection if already present
//						var dimension = item.dimension.toLowerCase();
//						$scope.dataset[dimension].selections = jquery.grep($scope.dataset[dimension].selections, function(e, i){
//								return e.name==item.name;
//							}, true);
//						//re-add the updated selection
//						$scope.dataset[dimension].selections.push(item);
////					}else{
////						$scope.dataset.row.selections = jquery.grep($scope.dataset.row.selections, function(e, i){return e.name==item.name;}, true);
////						$scope.dataset.row.selections.push(item);						
////					}
//				});
				$scope.$emit('SeletionAddedEvent', item.dimension.toLowerCase());
			};
			
			
		}]);
});

