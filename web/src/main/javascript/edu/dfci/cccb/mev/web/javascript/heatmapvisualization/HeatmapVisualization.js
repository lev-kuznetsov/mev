define(['angular', 'd3', './lib/HeatmapVisualizationClass', './lib/generateParams'], 
function(angular, d3, HeatmapVisualizationClass, generateParams){
	return angular.module('Mev.heatmapvisualization', [])
	.directive('visHeatmap',[ "$routeParams", "$http",
         function($routeParams, $http) {

            return {

                restrict : 'E',
                templateUrl : "/container/view/elements/visHeatmap",
                scope: {
                	heatmapDataset: "=heatmapDataset"
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
                	
                	$scope.$watch('heatmapDataset', function(newval){
                		
                		if (newval){
                			var params = 
                				new generateParams({
                					colors:$scope.currentColors});
                			$scope.visualization = new HeatmapVisualizationClass(newval,svg, params)
                		}
                		
                	});
                	
                	$scope.$watch('visualization', function(newval){
                		if(newval){
                			$scope.visualization.updateCells(position);
                		}
                		
                	})

                }

            }; // End return obj
        }])
})