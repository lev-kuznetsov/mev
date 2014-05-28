define(['angular', './GeneBoxPlotVisualizationClass', 'd3'], 
	function(angular, GeneBoxPlotVisualizationClass, d3){
	
	return angular.module('Mev.GeneBoxPlotVisualization',[])
	.directive('geneBoxPlotVisualization',[function() {
		return {

            restrict : 'E',
            template : "",
            scope: {
            	geneBoxPlotView: "=geneBoxPlotView",
            	heatmapDataset: "=heatmapDataset",
            	project : '=project'
            },
            link : function($scope, elems, attr) {
            	
            	$scope.boxPlotVisualization = undefined;
            	
            	var svg = undefined;
            	
            	$scope.watch('geneBoxPlotView', function(newval, oldval){
            		if (newval){
            			
            			d3.select(elems[0]).append('svg').attr('id', 'box-plot-'+newval.id)
            			svg = d3.select('#'+'box-plot-'+newval.id)
            			$scope.boxPlotVisualization = new GeneBoxPlotVisualizationClass(newval, svg);
            		}
            		
            	});
            	
            	$scope.watch('boxPlotVisualization', function(newval, oldval){
            		if (newval && svg){
            			boxPlotVisualizaion.drawBoxPlot();
            		}
            	});
            }
		};
	}])
});