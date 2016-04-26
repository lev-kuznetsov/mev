define(["lodash", "./mevHBarchart.tpl.html"], function(_, template){"use strict";
	var directive = function mevEnrichmentBarchartDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevHbarchart"
			},
			template: template,
			controller: ["$scope", "mevBarchartNvd3Adaptor", function(scope, mevBarchartNvd3Adaptor){

				function mixinGetter(dimConfig){
					if(_.isString(dimConfig.field)){
						dimConfig.get = function(d){
							return d[dimConfig.field];
						};
						dimConfig.label = dimConfig.label || dimConfig.field;
					}
					else if(_.isFunction(dimConfig.field))
						dimConfig.get = dimConfig.field;
					else
						throw new Error("DimConfig - no field specified: " + JSON.stringify(dimConfig));
				}

				function mixinDimConfig(dimConfig){
					if(!dimConfig)
						throw new Error("dimConfig is undefined " + JSON.stringify(dimConfig));
					mixinGetter(dimConfig);
					return dimConfig;
				}

				var xConfig = mixinDimConfig(scope.config.x);
				var yConfig = mixinDimConfig(scope.config.y);
				var zConfig = mixinDimConfig(scope.config.z);
				var data = scope.config.data;
				scope.data=mevBarchartNvd3Adaptor(scope.config, data);

				_.extend(zConfig, {
					colors: ["blue", "yellow"],
					min: 0,
					max: zConfig.get(_.maxBy(data, zConfig.get))
				})
				var zScale = d3.scale.linear().domain([zConfig.min, zConfig.max]).range(zConfig.colors);

				scope.options = {
		            chart: {
		                type: 'multiBarHorizontalChart',
		                height: d3.max([450, data.length*12]),
		                x: xConfig.get,
		                y: yConfig.get,
		                showControls: false,
		                showValues: true,
		                duration: 500,
		                xAxis: {
		                    showMaxMin: false
		                },
		                valueFormat: function(d){
		                	return d3.format(',.'+yConfig.precision+'f')(d);
		                },
		                yAxis: {
		                    axisLabel: 'Counts',
		                    tickFormat: function(d){
		                        return d3.format(',.'+yConfig.precision+'f')(d);
		                    }
		                },
		                barColor: function(d){
		                  return zScale(zConfig.get(d));
		                },
		                margin: {"left": 400},
		                tooltip: {
			                	contentGenerator: function(item) {
			                		// Format function for the tooltip values column.
								    var valueFormatter = function(d, i) {
								        return d;
								    };

								    // Format function for the tooltip header value.
								    var headerFormatter = function(d) {
								        return "" + ": " + d;
								    };

								    var keyFormatter = function(d, i) {
								        return d;
								    };

							        if (item === null) {
							            return '';
							        }

							        var table = d3.select(document.createElement("table"));
						            var theadEnter = table.selectAll("thead")
						                .data([item])
						                .enter().append("thead");

						            theadEnter.append("tr")
						                .append("td")
						                .attr("colspan", 3)
						                .append("strong")
						                .classed("id", true)
						                .html(zConfig.label + ": " + zConfig.get(item.data));

						            
						        

							        var tbodyEnter = table.selectAll("tbody")
							            .data([item])
							            .enter().append("tbody");

							        var trowEnter = tbodyEnter.selectAll("tr")
							                .data(function(p) { return p.series;})
							                .enter()
							                .append("tr")
							                .classed("highlight", function(p) { return p.highlight;});

							        trowEnter.append("td")
							            .classed("legend-color-guide",true)
							            .append("div")
							            .style("background-color", function(p) { return p.color;});

							        trowEnter.append("td")
							            .classed("key",true)
							            .classed("total",function(p) { return !!p.total;})
							            .html(function(p, i) { return keyFormatter(p.key, i);});

							        trowEnter.append("td")
							            .classed("value",true)
							            .html(function(p, i) {
											var fields = scope.config.tooltip ? scope.config.tooltip.fields : {};
											return _.reduce(fields,
												function(html, field, key){
													return html += ", " + key
														+ ": " +( _.isFunction(field) ? field.call(item.data, item.data) : item.data[field]);
												},
												yConfig.get(item.data));
										});

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
							        if (item.footer !== undefined)
							            html += "<div class='footer'>" + d.footer + "</div>";
							        return html;
							    }
			                },
		            }
		        };
			}],
			link: function(scope, elem, attr, ctrl){
				var zLegendConfig = {
					width: 130,
					height: 300
				};
				var d3root = d3.select(elem[0]);
				d3root.select(".controls svg").remove();
				var svgZValueLegend = d3root.select("#zLegend").append("svg");
				svgZValueLegend.attr("width", zLegendConfig.width).attr("height", zLegendConfig.height);

				var w = zLegendConfig.width, h = zLegendConfig.height;

				var key = svgZValueLegend;

				var legend = key.append("defs").append("svg:linearGradient")
				.attr("id", "gradient")
				.attr("x1", "100%")
				.attr("y1", "0%")
				.attr("x2", "100%")
				.attr("y2", "100%")
				.attr("spreadMethod", "pad");

				legend.append("stop").attr("offset", "0%").attr("stop-color", "yellow").attr("stop-opacity", 1);
				legend.append("stop").attr("offset", "100%").attr("stop-color", "blue").attr("stop-opacity", 1);

				key.append("rect").attr("width", w - 100).attr("height", h - 100).style("fill", "url(#gradient)").attr("transform", "translate(0,10)");

				var y = d3.scale.linear().range([h - 100, 0]).domain([scope.config.z.min, scope.config.z.max]).nice();

				var yAxis = d3.svg.axis().scale(y).orient("right");

				key.append("g").attr("class", "y axis").attr("transform", "translate(31,10)")
				.call(yAxis)
				.append("text").attr("y", h-100).attr("dy", ".71em").style("text-anchor", "end").text(scope.config.z.label);

			}
		};
	};
	directive.$name="mevHbarchart";
	directive.$provider="directive";
	directive.$inject=["mevBarchartNvd3Adaptor"];
	return directive;
});