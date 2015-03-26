(function(){
	
	var deps = [
    'd3']
	
	define(deps, function(HeatmapClass, generateParams, generateView, d3){
	
		return function(module){
			
			module.directive('nmfHeatmap', ['pathService', 'alertService', function(paths, alertService){

	               return {
	                 restrict: 'E',
	                 scope: {
	                   data: "=data",
	                   project: "=project"
	                 },
	                 link: function(scope, elems, attrs){
	                	 
	                	 scope.$watch('analysis', function(newval){
	                		 if (d3.select(elems[0]).selectAll('svg')){
	                 			 d3.select(elems[0]).selectAll('svg').remove()
	             			 }
		                	 
	             			 d3.select(elems[0]).append('svg').attr('id', 'svg-Window-' + scope.data.id);
	                     	 var svg = d3.select('#svg-Window-'+scope.data.id);
		                	 
		                	 var view = new generateView({'viewType': 'heatmapView'})
		                	 var params = generateParams({})
		                	 
		                	 scope.heatmap = HeatmapClass(view, svg, params)
	                	 })
	                 }
					
				}
				
			}])
			
		}
	})
	
})()