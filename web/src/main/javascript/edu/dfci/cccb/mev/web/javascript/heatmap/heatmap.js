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
		    					setScales: function() {
									
									var reference = this;
									
			            			this.cells.xScale.domain(reference.labels.column.values)
			            				.rangeRoundBands(
			            					[reference.params.panel.side.width,
			            					 reference.params.panel.side.width 
			            					 	+ (reference.labels.column.values.length * reference.params.cell.width)], 0, 0);
			            			
			            			this.cells.yScale.domain(reference.labels.row.values)
			        					.rangeRoundBands(
			        						[reference.params.panel.top.height
			        						 	+ reference.params.selections.column.height
			        						 	+ reference.params.labels.column.height , 
			        						 reference.params.panel.top.height
			        						 	+ reference.params.selections.column.height
			        						 	+ reference.params.labels.column.height + (reference.labels.row.values.length * reference.params.cell.height)], 0, 0);
			            			
			            			this.selections.column.xScale
			            				.domain(reference.labels.column.values)
			        					.rangeRoundBands(
				            					[reference.params.panel.side.width,
				            					 reference.params.panel.side.width
				            					 	+(reference.labels.column.values.length * reference.params.cell.width)], 0, 0);
			            			
			            			this.selections.column.yScale
				            			.domain(reference.selections.column.values.map(function(selection){
				            				return selection.name
				            			}))
				            			.rangeRoundBands(
				            					[reference.params.panel.top.height + reference.params.labels.column.height,
				            					 reference.params.panel.top.height + reference.params.labels.column.height
				            					 	+ reference.params.selections.column.height], 0, 0);
			
			            			this.selections.row.xScale
			            				.domain(reference.selections.row.values.map(function(selection){
			                				return selection.name
			                			}))
			            				.rangeRoundBands([reference.params.panel.side.width 
			            				                  	+ ( reference.labels.column.values.length * reference.params.cell.width),
			            				                  reference.params.panel.side.width
			            				                  	+ ( reference.labels.column.values.length * reference.params.cell.width)
			            				                  	+ reference.params.selections.row.width], 0, 0);
			            			
			            			this.selections.row.yScale
			            				.domain(reference.labels.row.values)
			            				.rangeRoundBands([reference.params.panel.top.height 
			            				                  	+ reference.params.labels.column.height
			            				                  	+ reference.params.selections.column.height,
							            				  reference.params.panel.top.height 
			            				                  	+ reference.params.labels.column.height
			            				                  	+ reference.params.selections.column.height 
			            				                  	+ (reference.labels.row.values.length * reference.params.cell.height)], 0, 0);
			            			
			            			
		            			},
		            			params : {
		            				cell: {height:25, width:10},
		            				panel:{
		            					side:{
		            						width: 150
		            					},
		            					top : {
		            						height: 150
		            					}
		            				},
		            				labels:{
		            					column:{
		            						height:30
		            					},
		            					row: {
		            						width: 50
		            					}
		            				},
		            				selections :{
		            					column:{
		            						height: 80
		            					},
		            					row:{
		            						width:80
		            					}
		            				}
		            			},
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
		    			
		            }, function(error){
		            	 var message = "Could not retrieve dataset info"
		                     + ". If the"
		                     + "problem persists, please contact us.";

		                 var header = "Heatmap Download Problem (Error Code: "
		                         + error.status
		                         + ")";

		                 alertService.error(message, header);
		                 
		            });
					

				
					extend(dataset, behaviors);
					
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
		.directive('visHeatmap',[ "$routeParams", "$http", "d3colors",
         function($routeParams, $http, d3colors) {

            return {

                restrict : 'E',
                templateUrl : "/container/view/elements/visHeatmap",
                scope: {
                	heatmapDataset: "=heatmapDataset"
                },
                link : function($scope, elems, attr) {
                	
                	var scrollable = $("div.tab-content"), delay = 50, timer = null;
                	
                	$scope.position = {
                		top : scrollable.scrollTop(),
                        height : scrollable.height(),
                	};
                	
                	$("div.tab-content").on("scroll", function(e){
                        
                		
                        timer = setTimeout(function(){
                        	
                        	$scope.visualization.update();
                        	
                        }, delay);
                        
                    });
                	
                	$scope.visualization = {
                		cells: [],
                		labels:{
                			rows:[]
                		},
                		update: function(){
                			//complete update function
                		}
                	}
                	
                	$scope.$watch('visualization.labels.rows', function(newval){
                		if(newval && newval.length > 0) {
                			drawCells
                		}
                	})
                	
                	d3.select('vis-Heatmap').append('svg').attr('id', 'svg-Window');
                	var svg = d3.select('#svg-Window');
                	
                	svg.append('g').attr("id", "heatmap-Cells");
                	var heatmapCells = d3.select('#heatmap-Cells');
                	
            		svg.append('g').attr("id", "side-Panel");
            		var sidePanel = d3.select('#side-Panel');
            		
            		svg.append('g').attr("id", "top-Panel");
            		var topPanel = d3.select('#top-Panel');
            		
            		svg.append('g').attr("id", "column-Selections")
            		var columnSelections = d3.select('#column-Selections');
            		
            		svg.append('g').attr("id", "column-Labels")
            		var columnLabels = d3.select('#column-Labels');
            		
            		svg.append('g').attr("id", "row-Selections")
            		var rowSelections = d3.select('#row-Selections');
            		
            		svg.append('g').attr("id", "row-Labels")
            		var rowLabels = d3.select('#row-Labels');
                	
                	function setSVG(){
                		
                		console.log($scope.heatmapDataset.view.params)
                		svg.attr('height', 
                				$scope.heatmapDataset.view.params.panel.top.height
                				+ $scope.heatmapDataset.view.params.labels.column.height
                				+ $scope.heatmapDataset.view.params.selections.column.height
                				+($scope.heatmapDataset.view.labels.row.values.length * $scope.heatmapDataset.view.params.cell.height ) + 50 )
                		svg.attr('width', 
                				$scope.heatmapDataset.view.params.panel.side.width
                				+ $scope.heatmapDataset.view.params.labels.row.width
                				+ $scope.heatmapDataset.view.params.selections.row.width
                				+ ($scope.heatmapDataset.view.labels.column.values.length * $scope.heatmapDataset.view.params.cell.width) + 50 )
                				
                		svg.append('rect').attr({
                					x: $scope.heatmapDataset.view.params.panel.side.width,
                					y: $scope.heatmapDataset.view.params.panel.top.height
                    				+ $scope.heatmapDataset.view.params.labels.column.height
                    				+ $scope.heatmapDataset.view.params.selections.column.height,
                					width:($scope.heatmapDataset.view.labels.column.values.length * $scope.heatmapDataset.view.params.cell.width),
                					height: ($scope.heatmapDataset.view.labels.row.values.length * $scope.heatmapDataset.view.params.cell.height ),
                					fill: 'black'

                				});
                		
                	}

                	$scope.$watch('heatmapDataset.view.labels.row.values', function(newval, oldval){
                		
                		if (newval && 
                				newval.length > 0 && 
                				$scope.heatmapDataset.view.labels.column.values &&
                				$scope.heatmapDataset.view.labels.column.values.length > 0){
                			
                			setSVG()
                        	
                			$scope.heatmapDataset.view.setScales()
                        	console.log($scope.heatmapDataset.view)
                		}
                			
                	});
                	
                	$scope.$watch('heatmapDataset.view.labels.column.values', function(newval, oldval){
                		
                		if (newval && 
                				newval.length > 0 && 
                				$scope.heatmapDataset.view.labels.row.values &&
                				$scope.heatmapDataset.view.labels.row.values.length > 0){
                			
                			setSVG()
                        	
                			$scope.heatmapDataset.view.setScales()
                        	console.log($scope.heatmapDataset.view)
                		}
                			
                	});
                	
                	
                	
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
        	
        	$scope.dataset = heatmapDataset($routeParams.datasetName);
        	$scope.$watch('dataset.data', function(newval){
        		
        		if (newval){
        			$scope.dataset.viewUpdate();
        		}
        	});
        	
        	
        	
        	
        }]);
});