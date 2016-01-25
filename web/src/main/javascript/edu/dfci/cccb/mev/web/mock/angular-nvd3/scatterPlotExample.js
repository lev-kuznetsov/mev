'use strict';

angular.module('mainApp.controllers', ['nvd3'])
    .controller('scatterChartCtrl', function($scope){
    	var _svg, _chart, _brush;
    	$scope.chart=undefined;
    	function _addBrush(){
    		if(_svg && _brush)
    			if(_svg.selectAll(".brush").size()===0)
            		_svg.append('g')
        		    .attr('class', 'brush')
        		    .call(_brush);
    	}
    	$scope.addBrush=function(){
    		addBrush();
    	};
    	$scope.removeBrush=function(){
    		console.debug("brush", _brush);
    		if(_svg && _brush)
    			_svg.selectAll(".brush").remove();
    	};
    	$scope.data = generateData(1,40); 
    	var xf = crossfilter($scope.data[0].values);    	
    	var xDim = xf.dimension(function(d) { return d.x; });
    	var yDim = xf.dimension(function(d) { return d.y; });
    	console.debug("crossfilter", xf, xDim, yDim);
        $scope.options = {
            chart: {
                type: 'scatterChart',
                interactive: false,
                pointSize: function (d) {
                    // use function attached to the data to calculate size
                	console.debug("pca pointSize", d);
//                    return d.calculateSymbolSize();
                	return 64;
                },
                pointScale: d3.scale.identity(),
                callback: function(chart){
                	console.debug("chart", chart);                	
                	chart.dispatch.on('renderEnd', function(){
                		_chart = chart;                    	
                        console.log('render complete', arguments, chart);
//                        chart.pointActive(function (d) {
//                            // d has x, y, size, shape, and series attributes.
//                            // here, we disable all points that are not a circle
//                        	console.debug("active", d)
//                            return d.shape !== 'circle';
//                        })
                        
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
                		
                		//add brush
                		if(!_chart.interactive())
                			_addBrush();
//                		_svg.append('g')
//                		    .attr('class', 'brush')
//                		    .call(_brush);
//                		
//                		function logExtent(extent){
//                			console.debug()
//                		}
                		
                		var node = _svg.selectAll('.nv-group > path.nv-point');
            			var tip = d3.tip().attr('class', 'd3-tip').html(function(d) { return d; });
            			_svg.call(tip);
            			node.on('mouseover', tip.show)
            			.on('mouseout', tip.hide);
                		
                		_brush.on('brush', function() {
//                			console.debug("brush", _brush.extent(), arguments);
//                		    var extent = _brush.extent(),
//                		        xExtent = [extent[0][0], extent[1][0]],
//                		        yExtent = [extent[0][1], extent[1][1]];
//                		    xDim.filterRange(xExtent);
//                		    yDim.filterRange(yExtent);
//                		    updateDots();
                		});
                		_brush.on('brushstart', function(){
                			console.debug("brushstart", _brush.extent(), arguments);
                			var node = _svg.selectAll('.nv-group > path.nv-point');
	                		console.debug("node", node);                    		
	            			node.classed("selected", false);
	            			chart.interactive(false);
                		});
                		_brush.on('brush', function(){
                			console.debug("brushend", _brush.extent(), arguments);
                			var extent = _brush.extent();
                			var xExtent = [extent[0][0], extent[1][0]];
                			var yExtent = [extent[0][1], extent[1][1]];
                			xDim.filterRange(xExtent);
                			yDim.filterRange(yExtent);
                			
//                			var dots = _svg.selectAll('.nv-point-clips > clippath')
//            		        .data(xDim.top(Infinity));
//	            			var selected = xDim.top(Infinity);
//	            			console.debug("selcted", selected);
	            			
	            			var node = _svg.selectAll('.nv-group > path.nv-point');
	            			
//	                		console.debug("node", node);                    		
	            			node.each(function(d, i){
//	            				console.debug("d", d, i);
	            				if(d.length===2){	            	
	            					var datum = d[0];
	            					var select = extent[0][0] <= datum.x && datum.x < extent[1][0]
	            					&& extent[0][1] <= datum.y && datum.y < extent[1][1];
	            					if(select){	            						
	            						console.debug("each", d, select);
	            					}
	            					return select;
	            				}else{
	            					return false;
	            				}
	            				
	            			});
	            			node.classed("selected", function(d){
//	            				console.debug("d", d, d.length);
	            				if(d.length===2){	            	
	            					var datum = d[0];
	            					var select = extent[0][0] <= datum.x && datum.x < extent[1][0]
	            					&& extent[0][1] <= datum.y && datum.y < extent[1][1];	            					
//	            					if(select)
	            						console.debug("x range", extent[0][0],  datum.x, extent[1][0]);
	            						console.debug("y range", extent[0][1], datum.y, extent[1][1]);
	            						console.debug("selected", select);
	            					return select;
	            				}else{
	            					return false;
	            				}
	            				
	            			});
	            			chart.interactive(true);
//                			updateDots();
                		});
                        
                		
//                        var brush = d3.svg.brush()
//                        .x(x)
//                        .y(y)
//                        .on("brushstart", brushstart)
//                        .on("brush", brushmove)
//                        .on("brushend", brushend);
                    	
                    	// Clear the previously-active brush, if any.
                    	  function brushstart(p) {
                    		  console.debug("brushstart", arguments);
//                    	    if (brushCell !== this) {
//                    	      d3.select(brushCell).call(brush.clear());
//                    	      x.domain(domainByTrait[p.x]);
//                    	      y.domain(domainByTrait[p.y]);
//                    	      brushCell = this;
//                    	    }
                    	  }

                    	  // Highlight the selected circles.
                    	  function brushmove(p) {
                    		  console.debug("brushmove", arguments);
//                    	    var e = brush.extent();
//                    	    _svg.selectAll("circle").classed("hidden", function(d) {
//                    	      return e[0][0] > d[p.x] || d[p.x] > e[1][0]
//                    	          || e[0][1] > d[p.y] || d[p.y] > e[1][1];
//                    	    });
                    	  }

                    	  // If the brush is empty, select all circles.
                    	  function brushend() {
                    		  console.debug("brushend", arguments);
//                    	    if (brush.empty()) _svg.selectAll(".hidden").classed("hidden", false);
                    	  }
                    	  
                    });
                	                	
                	
                	
                	console.debug("callback", chart, chart.dispatch, chart.scatter.dispatch);                	
                },
                dispatch: {
                	brush: function(brushExtent){
                		console.debug("brush", brushExtent);
                	}
                },
                height: 450,
                color: d3.scale.category10().range(),
                scatter: {
                    onlyCircles: false,
                },
                showDistX: true,
                showDistY: true,
              //tooltipContent: function(d) {
              //    return d.series && '<h3>' + d.series[0].key + '</h3>';
              //},
                duration: 350,
                xAxis: {
                    axisLabel: 'X Axis',
                    tickFormat: function(d){
                        return d3.format('.02f')(d);
                    }
                },
                yAxis: {
                    axisLabel: 'Y Axis',
                    tickFormat: function(d){
                        return d3.format('.02f')(d);
                    },
                    axisLabelDistance: -5
                },
                zoom: {
                    //NOTE: All attributes below are optional
                    enabled: true,
                    scaleExtent: [1, 10],
                    useFixedDomain: false,
                    useNiceScale: false,
                    horizontalOff: false,
                    verticalOff: false,
                    unzoomEventType: 'dblclick.zoom'
                }
            }
        };

        
        /* Random Data Generator (took from nvd3.org) */
        function generateData(groups, points) {
            var data = [],
                shapes = ['circle', 'cross', 'triangle-up', 'triangle-down', 'diamond', 'square'],
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
                        , shape: shapes[0]
                    	, group: i
                    	, j: j
                    });
                }
            }
            return data;
        }
    });