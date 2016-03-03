"use strict";
define(["ng", "lodash", "nvd3", "mev-pca"], function(ng, _, nvd3){
	var module = ng.module("mui.views.dataset.analysis.pca", arguments, arguments);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("PcaVMFactory", [function(){
		return function PcaVMFactory($scope, BoxPlotService, project, analysis){
			var _self = this;
			var _svg, _svgElement, _brush, _chart, _selection=[];
			this.curSelection = [];
			
			function _addBrush(){
	    		if(_svg && _brush)
	    			if(_svg.selectAll(".brush").size()===0)
	            		_svg.append('g')
	        		    .attr('class', 'brush')
	        		    .call(_brush);
	    	}
	    	this.addBrush=function(){
	    		_addBrush();
	    	};	    	
	    	this.removeBrush=function(){
	    		console.debug("brush", _brush);
	    		if(_svg && _brush)
	    			_svg.selectAll(".brush").remove();
	    	};
	    	
	    	this.getSelection=function(){
	    		return _self.curSelection;
	    	};
	    	$scope.sizeChanged=function(){
	    		console.debug("pca resize");
	    		window.dispatchEvent(new Event('resize'));
	    	};
	    	$scope.$on("mui:dashboard:panel:rowMax", $scope.sizeChanged);
			$scope.$on("mui:dashboard:panel:rowMin", $scope.sizeChanged);
			$scope.$on("mui:dashboard:panel:max", $scope.sizeChanged);
			$scope.$on("mui:dashboard:panel:min", $scope.sizeChanged);
			
			this.analysisId=analysis.name;
			this.analysis=analysis;
			this.project=project;
//			this.pc={
//					data: transformData(this.analysis)[0].values
//			};
			this.selectionParams = {
                 'name':undefined,
           		 'dimension': {
           			 'x': undefined,
                        'y': undefined
                    },
                    'samples':[],
                    'color': '#' + Math
                        .floor(Math.random() * 0xFFFFFF << 0)
                        .toString(16)
                };
			this.options = {
	            chart: {
	                type: 'scatterChart',
	                interactive: false,
	                pointSize: function (d) {
	                    // use function attached to the data to calculate size
	                	console.debug("pca pointSize", d);
//	                    return d.calculateSymbolSize();
	                	return 64;
	                },
	                pointScale: d3.scale.identity(),
	                callback: function(chart){
	                	console.debug("chart", chart);                	
	                	chart.dispatch.on('renderEnd', function(){
	                		_chart = chart;                    	
	                        console.log('render complete', arguments, chart);
	                        
	                        var svgDom = angular.element("svg");
	                		console.debug(".nvd3-svg", svgDom.height());
	                		var height = svgDom.height();
	                		var width = svgDom.width();
	                		
	                		_svg = d3.select(".nvd3.nv-wrap.nv-scatter");
	                		console.debug("_svg", _svg);
	                		//scale to use with brush
	                		var xScale = d3.scale.linear()
	                	    .range([0, width]);
	                		var yScale = d3.scale.linear()
	                	    .range([height, 0]);
	                		
	                		//define brush
	                		_brush = d3.svg.brush()
	                	    .x(chart.scatter.xScale())
	                	    .y(chart.scatter.yScale());
	                		
	                		//add brush (only if graph is in unclickable mode)
	                		if(!_chart.interactive())
	                			_addBrush();
	                		
	                		var selection = [];
	                		function clearSelection(){
	                			selection.length=0;
	                			_selection.length=0;
	                		};
	                		function updateSelection(){
	                			var extent = _brush.extent();
	                			var xExtent = [extent[0][0], extent[1][0]];
	                			var yExtent = [extent[0][1], extent[1][1]];
//	                			xDim.filterRange(xExtent);
//	                			yDim.filterRange(yExtent);
	                					            			
		            			var node = _svg.selectAll('.nv-group > path.nv-point');		            			
//		                		console.debug("node", node);                    		
		            			
//		            			node.each(function(d, i){
////		            				console.debug("d", d, i);
//		            				if(d.length===2){	            	
//		            					var datum = d[0];
//		            					var select = extent[0][0] <= datum.x && datum.x < extent[1][0]
//		            					&& extent[0][1] <= datum.y && datum.y < extent[1][1];
//		            					if(select){	            						
//		            						console.debug("scatter each", d, select);		            						
//		            					}
//		            					return select;
//		            				}else{
//		            					return false;
//		            				}
//		            				
//		            			});
		            			node.classed("selected", function(d){
//		            				console.debug("d", d, d.length);
		            				if(d.length===2){	            	
		            					var datum = d[0];
		            					var select = extent[0][0] <= datum.x && datum.x < extent[1][0]
		            					&& extent[0][1] <= datum.y && datum.y < extent[1][1];	            					
		            					if(select){		            						
		            						console.debug("x range", extent[0][0],  datum.x, extent[1][0]);
		            						console.debug("y range", extent[0][1], datum.y, extent[1][1]);
		            						console.debug("selected", select);
		            						selection.push(datum);
		            					}
		            					return select;
		            				}else{
		            					return false;
		            				}
		            				
		            			});
	                		}
	                		
	                		
	                		_brush.on('brush', function() {
//	                			console.debug("brush", _brush.extent(), arguments);
//	                		    var extent = _brush.extent(),
//	                		        xExtent = [extent[0][0], extent[1][0]],
//	                		        yExtent = [extent[0][1], extent[1][1]];
//	                		    xDim.filterRange(xExtent);
//	                		    yDim.filterRange(yExtent);
//	                		    updateDots();
//	                			updateSelection();
	                		});
	                		_brush.on('brushstart', function(){
	                			console.debug("brushstart", _brush.extent(), arguments);
	                			var node = _svg.selectAll('.nv-group > path.nv-point');
		                		console.debug("node", node);                    		
		            			node.classed("selected", false);
		            			clearSelection();
//		            			chart.interactive(false);
	                		});
	                		_brush.on('brushend', function(){
	                			console.debug("brushend", _brush.extent(), arguments);
	                			updateSelection();
	                			selection.map(function(item){
	                				_selection.push(item);
	                			});
	                			console.debug("brushend _selection", _selection, selection);
	                			
//	                			updateSelection(selection);
//		            			chart.interactive(true);
	                		});
   	  
	                    });
	                	                	
	                },
	                height: 450,
	                color: d3.scale.category10().range(),
//	                pointSize: function(d){
//	                	console.debug("pca.pointSize", d);
//	                	return 32;
//	                },
	                scatter: {
	                    onlyCircles: false,	                    
	                    dispatch: {
	                    	elementClick: function(){
	                    		console.debug("click", arguments);
	                    	},
	                    	brushend: function(){
	                    		console.debug("brush", arguments);
	                    	}
	                    }
	                },	 
	                tooltip: {
	                	contentGenerator: function (obj) {
	                		console.debug("tooltip", obj);
	                		return _chart.xAxis.axisLabel() + ": " + obj.point.x + "<br>"
	                		+ _chart.yAxis.axisLabel() + ": " + obj.point.y;
	                	}
	                },
	                showDistX: true,
	                showDistY: true,
	                duration: 350,
	                xAxis: {
	                    axisLabel: 'PC1',
	                    tickFormat: function(d){
	                        return d3.format('.02f')(d);
	                    }
	                },
	                yAxis: {
	                    axisLabel: 'PC2',
	                    tickFormat: function(d){
	                        return d3.format('.02f')(d);
	                    },
	                    axisLabelDistance: -5
	                },
	                zoom: {
	                    //NOTE: All attributes below are optional
	                    enabled: false,
	                    scaleExtent: [1, 10],
	                    useFixedDomain: false,
	                    useNiceScale: false,
	                    horizontalOff: false,
	                    verticalOff: false,
	                    unzoomEventType: 'dblclick.zoom'
	                }
	            }
	        };
			this.data = transformData(this.analysis);
//			this.data = generateData(1,40);
			console.debug("pca data", this.data);

			/* Random Data Generator (took from nvd3.org) */
	        function transformData(analysis) {
	            var data = [{
	            	key: analysis.name,
	            	values: _.map(analysis.x, function(item, key){
		            	return {
	            			x: item.PC1,
	            			y: item.PC2,
//	            			size: 0.555,
	            			size: Math.random(),
	            			shape: 'circle',
	            			sample: key,
	            			fill: "red",
	            			id: key
		            	};
		            })
	            }];
	            return data;
	        }
			
	        /* Random Data Generator (took from nvd3.org) */
	        function generateData(groups, points) {
	            var data = [],
//	                shapes = ['circle', 'cross', 'triangle-up', 'triangle-down', 'diamond', 'square'],
	                shapes = ['circle'],
	                random = d3.random.normal();

	            for (var i = 0; i < groups; i++) {
	                data.push({
	                    key: 'Group ' + i,
	                    values: []
	                });

	                for (var j = 0; j < points; j++) {
	                    data[i].values.push({
	                        x: random()
	                        , y: random()
	                        , size: Math.random()
	                        , shape: shapes[j % 6]
	                    });
	                }
	            }
	            return data;
	        }
		};
	}])
	.controller("PcaVM", ["$scope", "$injector", "BoxPlotService", "PcaVMFactory", "project", "analysis", 
	                        function($scope, $injector, BoxPlotService, PcaVMFactory, project, analysis){
		
		PcaVMFactory.call(this, $scope, BoxPlotService, project, analysis);
		
	}]);
	return module;
});