define(['angular', 'd3', './lib/HeatmapVisualizationClass', './lib/generateParams'], 
function(angular, d3, HeatmapVisualizationClass, generateParams){
	return angular.module('Mev.heatmapvisualization', [])
	.directive('visHeatmap',[ "$routeParams", "$http",
         function($routeParams, $http) {

            return {

                restrict : 'E',
                templateUrl : "/container/view/elements/visHeatmap",
                scope: {
                	heatmapDataset: "=heatmapDataset",
                	selections : "=selections",
                	selectionAdd : "&selectionAdd"
                },
                link : function($scope, elems, attr) {
                	
                	var scrollable = $("div.tab-content"), delay = 50, timer = null;
                	
                	var position = {
                			top: scrollable.scrollTop(),
                			height:scrollable.height()
                	};
                	
                	$scope.availableColors = [
                	"red", "orange", "yellow", "green", "blue", "violet",
                	"pink", "white", "black"
                	]
                	
                	$scope.currentColors = {
                		name: "Orange-Pink-Green",
                		color1: 'orange',
                		color2: 'pink',
                		color3: 'green',
                	}
                	
                	$("div.tab-content").on("scroll", function(e){
                        
                		
                		position = {
                    			top: scrollable.scrollTop(),
                    			height:scrollable.height()
                    	};
                		
                        timer = setTimeout(function(){
                        	
                        	$scope.$apply(function(){
                        		$scope.visualization.updateCells(position);
                        	});
                        	
                        	
                        }, delay);
                        
                    });
                	
                	d3.select('vis-Heatmap').append('svg').attr('id', 'svg-Window');
	
                	
                	var svg = d3.select('#svg-Window');
                	
                	$scope.visualization = undefined;
                	
                	
                	//When dataset information comes, generate new visualization.
                	$scope.$watch('heatmapDataset', function(newval){
                		
                		if (newval){
                			var params = 
                				new generateParams({
                					colors:$scope.currentColors});
                			$scope.visualization = new HeatmapVisualizationClass(newval,svg, params)
                		}
                		
                	});
                	
                	//When visualization information comes, print cells, selections,
                	//	and clear current top and side panes
                	$scope.$watch('visualization', function(newval){
                		if(newval){
                			$scope.visualization.updateCells(position);
                		}
                		
                	})
                	
                	//When new side/top pane information comes, print it
                	
                	//When new selections come, print them
                	
                	//Selections modifier
                	
                	scope.selectionParams = {
                		row : {
	                	
	                		name : undefined,
	                		color : '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16)
	                		labels : []
	                	},
	                	column : {
		                	
	                		name : undefined,
	                		color : '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16)
	                		labels : []
	                	}
                	
                	}
                	
                	//addSelection [Selection] --> null
                	scope.addSelection = function(selection, dimension){
                		var selectionsData = {
                            name: scope.selectionParams[dimension].name,
                            properties: {
                                selectionDescription: '',
                                selectionColor:scope.selectionParams[dimension].color,                     
                            },
                            keys:selection[dimension].labels
                        };
                        
                        selectionsAdd({
                            datasetName : $routeParams.datasetName,
                            dimension : dimension

                        }. selectionsData,
                        function(response){
                                scope.$broadcast('SeletionAddedEvent', 'row');
                                var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                var header = "Heatmap Selection Addition";
                        
                                scope.selectionParams[dimension].color = '#'+Math
                                    .floor(Math.random()*0xFFFFFF<<0)
                                    .toString(16);
                                scope.selectionParams[dimension].name = undefined;

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
                	} 

                }

            }; // End return obj
        }])
})