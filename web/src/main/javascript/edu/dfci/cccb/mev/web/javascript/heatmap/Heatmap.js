define(['jquery', 
        'angular',
        'extend',
        'd3',
        'dataset/Dataset',
        'notific8', 'api/Api', 'colorbrewer/ColorBrewer'],
    function($, angular, extend, d3) {
	
	return angular
		.module('Mev.heatmap', ['Mev.Api', 'Mev.Dataset', 'd3colorBrewer'])
		.value('Heatmap.availableColors', ["Green-Black-Red",
                                           "Yellow-Black-Blue",
                                           "Red-White-Blue"])
        .service('Heatmap.saveCluster', [function(){
        	
        	return function(cluster) {

	        	var svg = d3.select("#svgWrapperHclTree_"+cluster.name).select("svg");
	        	 var html = svg
	        	 .attr("version", 1.1)
	             .attr("xmlns", "http://www.w3.org/2000/svg")
	             .node().parentNode.innerHTML;
	        	  
	        	 var imgsrc = 'data:image/svg+xml;base64,'+ btoa(html);
	//           var img = '<img src="'+imgsrc+'">';
	//           d3.select("#svgdataurl").html(img);
	        	  
	        	  
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
//	                 var canvasdata = canvas.toDataURL("image/png");
//	                 var pngimg = '<img src="'+canvasdata+'">';
//	                 d3.select("#pngdataurl").html(pngimg);
//	                 var a = document.createElement("a");
//	                 a.download = "sample.png";
//	                 a.href = canvasdata;
//	                 a.click();
	            };
        	};
        }])
		.controller('HeatmapCtrl', [
		'$scope', 
		'$routeParams',
        '$location',
        'DatasetResourceService',
        'DatasetFactory',
        function($scope, $routeParams,  $loc,DatasetResourceService, DatasetFactory) {
			
			//case where there's no datasetName
			if (!$routeParams.datasetName) {
				$('#loading').modal('hide');
				$loc.path('/datasets'); //return back to datasets
				return
			};
			
			$scope.dataset = undefined;
			
			DatasetResourceService.get({
				datasetName: $routeParams.datasetName
			}, function(response){
				$scope.dataset = DatasetFactory.get(response)
			});
			
			$scope.$watch('dataset', function(newval, oldval){
				if(newval && !oldval){
					$scope.dataset.addView($scope.dataset);
				}
				
			})

			
        	
        }]);
});