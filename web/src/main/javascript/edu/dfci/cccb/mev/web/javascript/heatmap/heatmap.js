define(['jquery', 'angular', 'heatmap/behaviors', 'extend', 'd3',
        'notific8', 'api/api', 'colorbrewer/ColorBrewer'], function($, angular, behaviors, extend, d3) {
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
		.service('Heatmap.cellsFilter', [function(){
			
			return function(oldcells, rowLabels, colLabels, filterLabels){
				//Used to filter cells array assuming its an array in row major order  
                    
					//get indexes of filter labels from all labels
					var indexes = filterLabels.map(function(d){
                        return rowLabels.indexOf(d);
                    });
                    
                    var cells = []
                    //get rows from cells using indexes
                    indexes.map(function(index){
                       //get row by slicing using index
                       var row = this.oldcells.slice(index* colLabels.length, colLabels.length*(1+index))
                       //push rows onto cells
                       row.map(function(cell){
                           cells.push(cell)
                       });
                    });
                    
                    return cells
                    
			};
		}])
		.factory('heatmapDataset', ['alertService',
		    'api.dataset', 'api.dataset.analysis',
		    'api.dataset.selections', 
		    function(alertService, apiDataset, apiAnalysis, apiSelections){
			
				var dataset = {
				//Dataset constructor
			
	        		cells: {
	        			avg: undefined,
						max: undefined,
						min: undefined,
						values: undefined
	        		}, analysisList : {
	        			hierarchical: [],
	        			limma: [],
	        			kMeans: [],
	        			tTest: [],
	        			anova: []
	        		}, selections : {
	        			row: undefined,
	        			column: undefined
	        		}, labels : {
	        			row: undefined,
	        			column:undefined
	        		}, view : {
	            		cells : {
	            			avg: undefined,
	    					max: undefined,
	    					min: undefined,
	    					values: undefined
	            		},
	            		labels : {
	            			row: undefined,
	            			column:undefined
	            		},
	            		panel : {
	            			side:undefined,
	            			top:undefined
	            		}
	        		}, color : undefined //setting default color
	        	
				};
			
				extend(dataset, behaviors);
				
				Object
					.defineProperty(dataset.view, 'datasetViewFilter', 
							{value:behaviors.datasetViewFilter,
							 enumerable: true,
							 writable:false,
							 configurable: false})
	        	
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
	        	
	        	Object.defineProperty(dataset, 'apiDataset', 
	        			{value: apiDataset, 
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
			
			
		}])
		.directive('visHeatmap',[ "$routeParams", "$http", "d3colors", 
         function($routeParams, $http, d3colors) {

            return {

                restrict : 'E',
                templateUrl : "/container/view/elements/visHeatmap",
                scope: {
                	heatmapDataset: "=heatmapDataset"
                },
                link : function($scope, elems, attr) {
                	
                	var heatmap = {
                		expressions : {
                			xScale : d3.scale.ordinal(),
                			yScale : d3.scale.ordinal()
                		}, panel : {
                			top: {
                				xScale : d3.scale.ordinal(),
                    			yScale : d3.scale.ordinal().rangeRoundBands([0, 150], 0, 0),
                			},
                			side: {
                				xScale : d3.scale.ordinal().rangeRoundBands([0, 150], 0, 0),
                    			yScale : d3.scale.ordinal(),
                			}
                		}, selections : {
                			top: {
                				xScale : d3.scale.ordinal(),
                    			yScale : d3.scale.ordinal().rangeRoundBands([180, 260], 0, 0),
                			},
                			side: {
                				xScale : d3.scale.ordinal(), //.rangeRoundBands([width, width+180], 0, 0),
                    			yScale : d3.scale.ordinal(),
                			}
                		}, labels : {
                			top: {
                				xScale : d3.scale.ordinal(),
                    			yScale : d3.scale.ordinal().rangeRoundBands([180, 260], 0, 0),
                			},
                			side: {
                				height: undefined,
                				width: 30
                			}
                		}, setSize : function(rows, cols, rowselects, colselects, celldims){
                			
                			
                			this.expressions.xScale.domain(cols)
                				.rangeRoundBands([150, 150 +( cols.length * celldims.width)], 0, 0);
                			
                			this.expressions.yScale.domain(rows)
            					.rangeRoundBands([260, 260 + (rows.length * celldims.height)], 0, 0);
                			
                			this.selections.top.xScale.domain(cols)
            					.rangeRoundBands([180, 180 + (cols.length *celldims.width)], 0, 0);
                			
                			this.selections.top.yScale.domain(colselects);

                			this.selections.side.xScale
                				.domain(colselects)
                				.rangeRoundBands([150 + ( cols.length * celldims.width),
                				                  150 + ( cols.length * celldims.width) + 80], 0, 0);
                			
                			this.selections.side.yScale
                				.domain(rows)
                				.rangeRoundBands([260, 260 + (rows.length * celldims.height)], 0, 0);
                			
                			
                		}
                	};
                	
                	$scope.viewLayer = {
                		cells: undefined,
                		labels: {
                			row: undefined,
                			column: undefined
                		}
                	};
                	
                	$scope.$watch('heatmapDataset', function(newval, oldval){
                		console.log(newval.view.labels)
                		
                		if (newval){
                			
                			heatmap.setSize(newval.view.labels.row, newval.view.labels.column,
                					newval.selections.row, newval.selections.column,
                					{width:15, height:15})
                		}
                	})
                	
                	$scope.$watch('heatmapDataset.view.labels.row', function(newval){
                		console.log(newval)
                		return
                	});
                	
                	$scope.$watch('heatmapDataset.view.labels.column', function(newval){
                		console.log(newval)
                		return
                	});
                	
                	$scope.$watch('heatmapDataset.')
                	
                	
                	//when new view labels load
                		//clear panels
                		//clear cells
                		//draw cells
                		//draw labels
                		//draw selections
                }

            }; // End return obj
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
        	
        	$scope.dataset = {};
        	extend($scope.dataset, heatmapDataset)
        	
        	
        	//Initialize page
        	
        	$scope.dataset.setDataset();
        	//$scope.dataset.setHeatmapView();
        	//$scope.dataset.setDatasetAnalysisList();
        	//$scope.dataset.setDatasetSelections();
        	
        }]);
});