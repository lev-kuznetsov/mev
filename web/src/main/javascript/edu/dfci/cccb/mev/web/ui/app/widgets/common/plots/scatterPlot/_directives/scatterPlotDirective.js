define(["angular", "d3", "lodash"], function(angular, d3, _){
	"use strict";
	var ScatterPlotDirective = function ScatterPlotDirective(){
		return {
			restrict: "EC",
			scope: {
				inputData: "=",
				labelX: "=",
				labelY: "="
			},
			controller: "scatterCtrl",
			template: "<nvd3 options='options' data='data' config='config' api='api'></nvd3>",
			link: function(scope, elm, attrs, ctrl){				
				var _self = this;
				scope.api=undefined;
				scope.config={
					deepWatchDataDepth: 0,
					refreshDataOnly: true,
					debounce: 100
				};
				
				var _svg, _brush, _chart, _selection=[];
				function _addBrush(){
					if(_svg && _brush)
						// if(_svg.selectAll(".brush").size()===0)
						_svg.selectAll(".brush").remove();
						_svg.insert('g', ":first-child")
						.attr('class', 'brush')					
						.call(_brush);
				}
				
				if(scope.inputData === "random"){					
					scope.inputData = ctrl.generateData(2,5);					
				}				
				scope.data = scope.inputData;
				scope.options = getOptions();
				scope.$watch("inputData", function(newVal){										
					// scope.api.updateWithOptions(getOptions());
					if(newVal){
						
						console.debug("domain data", scope.inputData);
						scope.options = getOptions();
						scope.data = scope.inputData;
//						scope.api.updateWithOptions(scope.options);
//						scope.api.updateWithData(scope.inputData);
//						scope.options = getOptions();						
					}					
				});

				function getDomain(dim){
					var theDomain = [Infinity, -Infinity];
					if(scope.inputData){						
						_.map(scope.inputData, function(group){
							var domain = d3.extent(group.values, function(d){
		                    	return d[dim];
		                    });
							theDomain[0] = theDomain[0]>domain[0] ? domain[0] : theDomain[0];
							theDomain[1] = theDomain[1]<domain[1] ? domain[1] : theDomain[1];
						});						
						console.debug("theDomain "+dim, theDomain);
						return theDomain.map(function(v){return v * 1.1;});
					}
				}
				
				function getOptions(){
					return {
						chart: {
			                type: 'scatterChart',
			                showVoronoi: false,
			                interactive: true,
			                pointSize: 64,
			                pointScale: d3.scale.identity(),			                
			                callback: function(chart){
			                	console.debug("chart", chart);                	
			                	chart.dispatch.on('renderEnd', function(){
			                		_chart = chart;                    	
			                        console.log('render complete', arguments, chart);
			                        
			                        var svgDom = angular.element("svg");
			                		console.debug(".nvd3-svg", svgDom.height());
			                		
			                		_svg = d3.select(".nvd3.nv-wrap.nv-scatter");
			                		console.debug("_svg", _svg);
			                		
			                		//define brush
			                		_brush = d3.svg.brush()
			                	    .x(chart.scatter.xScale())
			                	    .y(chart.scatter.yScale());
			                		
			                		//add brush (only if graph is in unclickable mode)
			                		//~ if(!_chart.interactive())
			                		_addBrush();
			                		
			                		var selection = [];
			                		function clearSelection(){
			                			selection.length=0;
			                			_selection.length=0;
			                		}
			                		function updateSelection(){
			                			var extent = _brush.extent();
			                			var xExtent = [extent[0][0], extent[1][0]];
			                			var yExtent = [extent[0][1], extent[1][1]];
		//	                			xDim.filterRange(xExtent);
		//	                			yDim.filterRange(yExtent);
			                					            			
				            			var node = _svg.selectAll('.nv-group > path.nv-point');		            			
				                		console.debug("node", node.size(), node);                    		
				            			
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
										var count = 0;
				            			node.classed("selected", function(d){
		//		            				console.debug("d", d, d.length);
											console.debug("count", count++, d, d.length);
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
												console.debug("bad datum", d, d.length);
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
										scope.$apply(function(){
			        	        			scope.$emit("mev.scatterPlot.selection", _.clone(_selection, true));
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
			                    onlyCircles: true,	                    	                    
			                    dispatch: {
			                    	elementClick: function(){
			                    		console.debug("click", arguments);
			                    	},
			                    	brushend: function(){
			                    		console.debug("brush", arguments);
			                    	}
			                    },
			                    // xDomain: [_.min(scope.data[0].values, function(v){return v.x}), _.max(scope.data[0].values, function(v){return v.x})],
			                    xDomain: getDomain("x"),
			                	yDomain: getDomain("y"),
			                },	 
			                // tooltip: {
			                // 	contentGenerator: function (obj) {
			                // 		console.debug("tooltip", obj, _chart);			                		
			                // 		return obj.point.id + "<br>" +
			                // 		_chart.xAxis.axisLabel() + ": " + obj.point.x + "<br>" +
			                // 		_chart.yAxis.axisLabel() + ": " + obj.point.y + "<br>" + 
			                // 		"Group: " + obj.series[0].key + "<div class='series-color' style='background-color: "+obj.series[0].color+";'></div>";
			                // 	}
			                // },	
			                tooltip: {
			                	contentGenerator: function(d) {
			                		// Format function for the tooltip values column.
								    var valueFormatter = function(d, i) {
								        return _chart.xAxis.axisLabel() + ": " + d;
								    };

								    // Format function for the tooltip header value.
								    var headerFormatter = function(d) {
								        return _chart.yAxis.axisLabel() + ": " + d;
								    };

								    var keyFormatter = function(d, i) {
								        return d;
								    };

							        if (d === null) {
							            return '';
							        }

							        var table = d3.select(document.createElement("table"));
						            var theadEnter = table.selectAll("thead")
						                .data([d])
						                .enter().append("thead");

						            theadEnter.append("tr")
						                .append("td")
						                .attr("colspan", 3)
						                .append("strong")
						                .classed("id", true)
						                .html(d.point.id);

						            theadEnter.append("tr")
						                .append("td")
						                .attr("colspan", 3)
						                .classed("x-value", true)
						                .html(headerFormatter(d.series[0].value));
						        

							        var tbodyEnter = table.selectAll("tbody")
							            .data([d])
							            .enter().append("tbody");

							        var trowEnter = tbodyEnter.selectAll("tr")
							                .data(function(p) { return p.series})
							                .enter()
							                .append("tr")
							                .classed("highlight", function(p) { return p.highlight});

							        trowEnter.append("td")
							            .classed("legend-color-guide",true)
							            .append("div")
							            .style("background-color", function(p) { return p.color});

							        trowEnter.append("td")
							            .classed("key",true)
							            .classed("total",function(p) { return !!p.total})
							            .html(function(p, i) { return keyFormatter(p.key, i)});

							        trowEnter.append("td")
							            .classed("value",true)
							            .html(function(p, i) { return valueFormatter(d.value, i) });

							        trowEnter.selectAll("td").each(function(p) {
							            if (p.highlight) {
							                var opacityScale = d3.scale.linear().domain([0,1]).range(["#fff",p.color]);
							                var opacity = 0.6;
							                d3.select(this)
							                    .style("border-bottom-color", opacityScale(opacity))
							                    .style("border-top-color", opacityScale(opacity))
							                ;
							            }
							        });

							        var html = table.node().outerHTML;
							        if (d.footer !== undefined)
							            html += "<div class='footer'>" + d.footer + "</div>";
							        return html;
							    }
			                },
			                showDistX: true,
			                showDistY: true,
			                duration: 350,
			                xAxis: {
			                    axisLabel: scope.labelX,
			                    tickFormat: function(d){
			                        return d3.format('.02f')(d);
			                    }
			                },
			                yAxis: {
			                    axisLabel: scope.labelY,
			                    tickFormat: function(d){
			                        return d3.format('.02f')(d);
			                    },
			                    axisLabelDistance: -5
			                },
			                padData: false,
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
				}
			}
		};
	};
	ScatterPlotDirective.$inject=[];
	ScatterPlotDirective.$name="scatterPlot";
	ScatterPlotDirective.$provider="directive";
	return ScatterPlotDirective;
});