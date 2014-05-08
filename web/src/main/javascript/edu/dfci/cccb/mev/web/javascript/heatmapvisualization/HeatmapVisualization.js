define(['angular', 'd3', './lib/HeatmapVisualizationClass', './lib/generateParams', 'colorbrewer/ColorBrewer'], 
function(angular, d3, HeatmapVisualizationClass, generateParams){
	return angular.module('Mev.heatmapvisualization', ['d3colorBrewer'])
	.directive('heatmapSettings',[function() {

        return {
            restrict : 'E',
            scope : {
                currentColors : '=currentColors',
                availableColorGroups : '=availableColorGroups'
            },
            templateUrl : "/container/view/elements/heatmapSettingsModalBody",
        }
	}])
	.directive('visHeatmap',[ "$routeParams", "$http", "d3colors",
         function($routeParams, $http, d3colors) {

            return {

                restrict : 'E',
                templateUrl : "/container/view/elements/visHeatmap",
                scope: {
                	heatmapView: "=heatmapView",
                	heatmapDataset: "=heatmapDataset",
                	project : '=project'
                },
                link : function($scope, elems, attr) {
                	
                	var scrollable = $("div.tab-content"), delay = 50, timer = null;
                	
                	var position = {
                			top: scrollable.scrollTop(),
                			height:scrollable.height()
                	};
                	
                	$scope.availableColorGroups = Object.getOwnPropertyNames(d3colors);
                	
                	$scope.currentColors = {
                	    group : "BuBkYl",
                	    colors:{
                	        low: d3colors['BuBkYl'][3][0],
                            mid: d3colors['BuBkYl'][3][1],
                            high: d3colors['BuBkYl'][3][2],
                	    }
                		
                	};
                	
                	$scope.$watch('currentColors.group', function(newval){
                	    if(newval && $scope.heatmapDataset){

                	            $scope.avaiblableColors = d3colors[newval][3];
                                $scope.currentColors.group = newval;
                                $scope.currentColors.colors.low = d3colors[newval][3][0];
                                $scope.currentColors.colors.mid = d3colors[newval][3][1];
                                $scope.currentColors.colors.high = d3colors[newval][3][2];
                                
                                var params = new generateParams({colors:$scope.currentColors.colors});
                                $scope.visualization = new HeatmapVisualizationClass($scope.heatmapView,svg, params);

                	    }
                	});
         	
                	$("div.tab-content").on("scroll", function(e){
                        
                		
                		position = {
                    			top: scrollable.scrollTop(),
                    			height:scrollable.height()
                    	};
                		
                        timer = setTimeout(function(){
                        	
                        	$scope.$apply(function(){
                        		$scope.visualization.updateCells(position, $scope.heatmapDataset);
                        	});
                        	
                        	
                        }, delay);
                        
                    });
                	
                	d3.select('vis-Heatmap').append('svg').attr('id', 'svg-Window');
	
                	
                	var svg = d3.select('#svg-Window');
                	
                	$scope.visualization = undefined;
                	
                	
                	//When dataset information comes, generate new visualization.
                	$scope.$watchCollection('heatmapView', function(newval){
                		
                		if (newval){
                		    
                			var params = 
                				new generateParams({
                					colors:{
                					    low:d3colors['BuBkYl'][3][0],
                					    mid:d3colors['BuBkYl'][3][1],
                					    high:d3colors['BuBkYl'][3][2]
                					}
                				});
                			$scope.visualization = new HeatmapVisualizationClass(newval,svg, params)
                		}
                		
                	});
                	
                	//When visualization information comes, print cells, selections,
                	//	and clear current top and side panes
                	$scope.$watch('visualization', function(newval){

                		if(newval){
                			$scope.visualization.updateCells(position, $scope.heatmapDataset);
                		}
                		
                		if ($scope.selections){
                            $scope.visualization.drawSelections($scope.selections.column, "column");
                            $scope.visualization.drawSelections($scope.selections.row, "row");
                		}
                		
                		if ($scope.visualization.view.panel && $scope.visualization.view.panel.top) {
                		    $scope.visualization.drawTopPanel($scope.visualization.view.panel.top);
                		}
                		
                		if ($scope.visualization.view.panel && $scope.visualization.view.panel.side) {
                            $scope.visualization.drawSidePanel($scope.visualization.view.panel.side);
                        }
                		
                	});
                	
                	
                	//When new selections come, print them
                	
                	$scope.$watch('heatmapDataset.selections.column', function(newval){

                	    if (newval && $scope.visualization){
                	        $scope.visualization.drawSelections(newval, "column")
                	    };
                	});
                	
                	$scope.$watchCollection('heatmapDataset.selections.row', function(newval){

                	    if (newval && $scope.visualization){
                            $scope.visualization.drawSelections(newval, "row")
                        };
                    });

                	
                	//When panel elements come in, print them
                	$scope.$watch('visualization.view.panel.top', function(newval, oldval) {
 
                        if(newval && $scope.visualization){
                            $scope.visualization.drawTopPanel($scope.visualization.view.panel.top);
                        }
                    });
                	
                	$scope.$watch('visualization.view.panel.side', function(newval, oldval) {

                        if(newval && $scope.visualization){
                            $scope.visualization.drawSidePanel($scope.visualization.view.panel.side);
                        }
                    });
                	
                	

                	
                	//addSelection [Selection] --> null
                	$scope.addSelection = function(selection, dimension){
                		var selectionsData = {
                            name: $scope.view.selectionParams[dimension].name,
                            properties: {
                                selectionDescription: '',
                                selectionColor: $scope.view.selectionParams[dimension].color,                     
                            },
                            keys:$scope.view.selectionParams[dimension].labels
                        };
                        
                        $scope.project.selection.post({
                            datasetName : $routeParams.datasetName,
                            dimension : dimension

                        }, selectionsData,
                        function(response){
                                scope.$broadcast('SeletionAddedEvent', 'row');
                                var message = "Added " + $scope.selectionParams.name + " as new Selection!";
                                var header = "Heatmap Selection Addition";
                        
                                $scope.view.selectionParams[dimension].color = '#'+Math
                                    .floor(Math.random()*0xFFFFFF<<0)
                                    .toString(16);
                                $scope.view.selectionParams[dimension].name = undefined;

                                alertService.success(message,header);
                        },
                        function(data, status, headers, config) {
                            var message = "Couldn't add new selection. If "
                                + "problem persists, please contact us.";

                             var header = "Selection Addition Problem (Error Code: "
                                + status
                                + ")";

                             alertService.error(message,header);
                        });
                	};

                }

            }; // End return obj
        }])
})