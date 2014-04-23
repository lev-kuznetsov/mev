define(['angular', 'd3', './lib/viewLayer'], function(angular, d3, viewLayer){
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
                	
                	$("div.tab-content").on("scroll", function(e){
                        
                		
                		$scope.visualization.position.top = scrollable.scrollTop();
                		$scope.visualization.position.height = scrollable.height();
                		
                        timer = setTimeout(function(){
                        	
                        	$scope.$apply(function(){
                        		$scope.visualization.updateRows(
                        				$scope.heatmapDataset.view.labels.row.values);
                        	});
                        	
                        	
                        }, delay);
                        
                    });
                	
                	d3.select('vis-Heatmap').append('svg').attr('id', 'svg-Window');
	
                	$scope.visualization = undefined
                	
                	$scope.$watch('heatmapDataset.view.params', function(newval){
                		if (newval){
                			$scope.visualization = viewLayer(d3.select('#svg-Window'), scrollable, $scope.heatmapDataset.view.params)
                		}
                		
                	})
                	
                	$scope.$watch('heatmapDataset.view.labels.row.values', function(newval, oldval){
                		
                		if (newval && 
                				newval.length > 0 && 
                				$scope.heatmapDataset.view.labels.column.values &&
                				$scope.heatmapDataset.view.labels.column.values.length > 0){
                			
                			$scope.visualization.setSVG($scope.heatmapDataset.view.labels.row.values)
                        	
                			$scope.heatmapDataset.view.setScales()
                			
                		}
                			
                	});
                	
                	
                	$scope.$watch('heatmapDataset.view.labels.column.values', function(newval, oldval){
                		
                		if (newval && 
                				newval.length > 0 && 
                				$scope.heatmapDataset.view.labels.row.values &&
                				$scope.heatmapDataset.view.labels.row.values.length > 0){
                			
                			$scope.visualization.setSVG($scope.heatmapDataset.view.labels.row.values)
                        	
                			$scope.heatmapDataset.view.setScales()
                		}
                			
                	});
                	
                	;
                    
                    
                    $scope.$watch('visualization.labels.row', function(newval, oldval){
                    	
                    	if ($scope.heatmapDataset.view
                    			.viewCellFilter){
                    		
                    		
                        	$scope.visualization.cells = $scope.heatmapDataset.view
                        			.viewCellFilter({row:newval, 
                        				column:$scope.heatmapDataset.view.labels.column.values}) ;
                    	
                        	
                    	}
                    	
                    	
                    });
                    
                    $scope.$watch('visualization.cells', function(newval){
                    	drawScrollCells()
                    });
                    
                    function drawScrollCells() {
                    	
                    	var expressions = $scope.visualization.d3Selections.heatmapCells.selectAll('rect')
                    		.data($scope.visualization.cells,
                    				function(d){
                    					return [d.column, d.row]
                    				});
                    	
                    	expressions.enter()
                    		.append('rect')
                    			.attr({
                    				'x': function(d) {
                    					return $scope.heatmapDataset.view.cells.xScale(d.column);
                    				},
	                    			'y': function(d) {
	                					return $scope.heatmapDataset.view.cells.yScale(d.row);
	                				},
	                				'height': $scope.heatmapDataset.view.params.cell.height 
	                					- $scope.heatmapDataset.view.params.cell.padding,
	                				'width': $scope.heatmapDataset.view.params.cell.width 
	                					- $scope.heatmapDataset.view.params.cell.padding,
	                				'fill': 'red',
                    			})
                    			.append('title')
                    				.text(function(d){
                    					return "Sample: " + d.column + " " +
                    						"Gene: " + d.row + " " +
                    						"Value: " + d.value 
                    				})
                    	
                    };
                	
                	//when new view labels load
                		//clear panels
                		//clear cells
                		//draw cells
                		//draw labels
                		//draw selections
                }

            }; // End return obj
        }])
})