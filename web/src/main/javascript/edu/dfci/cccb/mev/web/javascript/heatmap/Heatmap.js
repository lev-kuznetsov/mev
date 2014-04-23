define(['jquery', 
        'angular',
        'extend', 
        'dataset/Dataset',
        'heatmapview/HeatmapView',
        'd3',
        'notific8', 'api/api', 'colorbrewer/ColorBrewer'],
    function($, angular, extend, Dataset, View, d3) {
	
	return angular
		.module('Mev.heatmap', ['Mev.api', 'd3colorBrewer'])
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
		.factory('heatmapDataset', ['alertService',
		    'api.dataset', 'api.dataset.analysis',
		    'api.dataset.selections', 
		    function(alertService, apiDataset, apiAnalysis, apiSelections){
			
			//Object builder for heatmapDataset
			
				return function(datasetName){
					
					var dataset = {
						datasetName : datasetName,
					};
					
					apiDataset.get({datasetName:datasetName},
				    function(result){
		            //set cells
		    			
		    			Object.defineProperty(dataset, 'data', {
		    				value : {
		    			
		    					cells: {
		    						avg : result.avg,
		    						max : result.max,
		    						min : result.min,
		    						values : result.values,
		    						
		    					},
		    					labels : {
		    						row : result.row.keys,
		    						column : result.column.keys
		    					}, 
		    					selections : {
									row : result.row.selections,
									column : result.column.selections
								}
		    				}, 
		    				enumerable : true,
		    				writable :true,
		    				
		    			});
		    			
		    			Object.defineProperty(dataset, 'view', {
		    				value : {
		    					cells: {
		    						avg : result.avg,
		    						max : result.max,
		    						min : result.min,
		    						values : result.values,
		    						xScale : d3.scale.ordinal(),
		    						yScale : d3.scale.ordinal()
		    					},
		    					labels : {
		    						row : {
		    							values:result.row.keys
		    						},
		    						column : {
		    							values:result.column.keys,
		    						}
		    					}, 
		    					selections : {
		    						row : {
		    							values: result.row.selections,
		    							xScale : d3.scale.ordinal(), //.rangeRoundBands([width, width+180], 0, 0),
		    			    			yScale : d3.scale.ordinal(),
		    						},
		    						column : {
		    							values: result.column.selections,
		    							xScale : d3.scale.ordinal(),
		    							yScale : d3.scale.ordinal(),	
		    							
		    						}
		    					},
		    					panel: {
		    						top: {
		    							xScale : d3.scale.ordinal(),
		    			    			yScale : d3.scale.ordinal().rangeRoundBands([0, 150], 0, 0),
		    						},
		    						side: {
		    							xScale : d3.scale.ordinal().rangeRoundBands([0, 150], 0, 0),
		    			    			yScale : d3.scale.ordinal(),
		    						}
		    					}
		    				}, 
		    				enumerable : true,
		    				writable :true,
		    				
		    			});
		    			
		    			extend(dataset.view, View);
						extend(dataset, Dataset);
		    			
		            }, function(error){
		            	 var message = "Could not retrieve dataset info"
		                     + ". If the"
		                     + "problem persists, please contact us.";

		                 var header = "Heatmap Download Problem (Error Code: "
		                         + error.status
		                         + ")";

		                 alertService.error(message, header);
		                 
		            });
					
					
		        	//Add some AngularJS modules to the dataset
		        	Object.defineProperty(dataset, 'apiAnalysis', 
		        			{value: apiAnalysis, 
		        			 enumerable: true,
		        			 configurable: false,
		        			 writeable: false
		        			}); 
		        	
		        	Object.defineProperty(dataset, 'apiSelections', 
		        			{value: apiSelections, 
		        			 enumerable: true,
		        			 configurable: false,
		        			 writeable: false
		        			});

		        	Object.defineProperty(dataset, 'alertService', 
		        			{value: alertService, 
		        			 enumerable: true,
		        			 configurable: false,
		        			 writeable: false
		        			});
		        	
		        	return dataset;
				};
			
			
		}])
		.controller('HeatmapCtrl', [
		'$scope', 
		'$routeParams', 
		'pseudoRandomStringGenerator',
        '$location',
        'heatmapDataset',
        'Heatmap.availableColors',
        function($scope, $routeParams, prsg,  $loc, heatmapDataset,
        		availableColors) {
			
			//case where there's no datasetName
			if (!$routeParams.datasetName) {
				$('#loading').modal('hide');
				$loc.path('/datasets'); //return back to datasets
				return
			};
        	
        	$scope.availableColors = availableColors;
        	
        	$scope.dataset = heatmapDataset($routeParams.datasetName);
        	$scope.$watch('dataset.data', function(newval){
        		
        		if (newval){
        			$scope.dataset.viewUpdate();
        		}
        	});
        	
        	
        	
        	
        }]);
});