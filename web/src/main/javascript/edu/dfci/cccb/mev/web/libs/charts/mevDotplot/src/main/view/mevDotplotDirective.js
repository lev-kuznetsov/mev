define(["mui", "./mevDotplot.tpl.html"], function(ng, template){ "use strict";
	var directive = function mevDotplotDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevDotplot"
			},
			template: template,
			controller: ["$scope", "mevDotplotNvd3Adaptor", function(scope, mevDotplotNvd3Adaptor){

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


				function tooltipContent(config, item) {
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

					function isInTitle(sTitle, dimConfig, item){
						return sTitle.indexOf(dimConfig.get(item.data)) >= 0;
					}
					function addDimRow(tbodyEnter, dimConfig, item){
						var trowEnter = tbodyEnter.selectAll("tr")
							.data(function(p) { return p.series;})
							.append("tr")
							.classed("highlight", function(p) { return p.highlight;});

						trowEnter.append("td")
							.append("div");

						trowEnter.append("td")
							.classed("key",true)
							.classed("total",function(p) { return !!p.total;})
							.html(function(p, i) {
									return keyFormatter(dimConfig.label, i)
									+ ": " + dimConfig.get(item.data);
								}
							);
					}
					function addFieldRow(tbodyEnter, item, field, key){
						var trowEnter = tbodyEnter.selectAll("tr")
							.data(function(p) { return p.series;})
							.append("tr")
							.classed("highlight", function(p) { return p.highlight;});

						trowEnter.append("td")
							.append("div");

						trowEnter.append("td")
							.classed("key",true)
							.classed("total",function(p) { return !!p.total;})
							.html(function(p, i) {
									return key + ": " + ( _.isFunction(field) ? field.call(item.data, item.data) : item.data[field]);
								}
							);
					}

					var tooltip = d3.select(document.createElement("div"))
						.data([item])
						.classed("tooltip", true);

					//show the title
					var title = tooltip.append("div");
					title.classed("tooltip-title", true)
						.html(config.tooltip.title(config, item));

					var table = tooltip.append("table");
					var tbodyEnter = table.selectAll("tbody")
						.data([item])
						.enter().append("tbody");

					//under the title we show the color dimension first
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
						.html(function(p, i) {
								return config.color ?
									keyFormatter(config.color.label, i)
										+ ": " + config.color.get(item.data)
									: keyFormatter(p.key, i);
							}
						);

					//now show the dimension data
					var sTitle = title.node().innerHTML;
					if(!isInTitle(sTitle, config.x, item)) addDimRow(tbodyEnter, config.x, item);
					if(!isInTitle(sTitle, config.y, item)) addDimRow(tbodyEnter, config.y, item);
					if(config.z && !isInTitle(sTitle, config.z, item)) addDimRow(tbodyEnter, config.z, item);
					if(config.size && !isInTitle(sTitle, config.size, item)) addDimRow(tbodyEnter, config.size, item);

					//finally append any additional fields
					var fields = config.tooltip ? config.tooltip.fields : {};
					_.forEach(fields, addFieldRow.bind(null, tbodyEnter, item));
					// trowEnter.append("td")
					// 	.classed("value",true)
					// 	.html(function(p, i) {
					// 		var fields = config.tooltip ? config.tooltip.fields : {};
					// 		return _.reduce(fields,
					// 			function(html, field, key){
					// 				return html += ", " + key
					// 					+ ": " +( _.isFunction(field) ? field.call(item.data, item.data) : item.data[field]);
					// 			},
					// 			config.y.label + ": " + config.y.get(item.data)
					// 			+ ", " + config.size.label + ": " + config.size.get(item.data));
					// 	});

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

					var html = tooltip.node().outerHTML;
					if (item.footer !== undefined)
						html += "<div class='footer'>" + d.footer + "</div>";
					return html;
				}


				var xConfig = mixinDimConfig(scope.config.x);
				var yConfig = mixinDimConfig(scope.config.y);
				var sizeConfig = mixinDimConfig(scope.config.size);
				var colorConfig = mixinDimConfig(scope.config.color);
				var data = scope.config.data;
				scope.data=mevDotplotNvd3Adaptor(scope.config, data);

				_.extend(colorConfig, {
					colors: ["blue", "yellow"],
					min: 0,
					max: colorConfig.get(_.maxBy(data, colorConfig.get))
				});
				_.extend(colorConfig, {
					scale: d3.scale.linear().domain([colorConfig.min, colorConfig.max]).range(colorConfig.colors)
				});

				scope.options = {
					chart: {
						type: 'dotPlotChart',
						height: _.max([450, 10*scope.data.length]),
						x: xConfig.get,
						y: yConfig.get,
						z: sizeConfig.get,
						showControls: false,
						showValues: true,
						duration: 500,
						xAxis: {
							showMaxMin: false
						},
						valueFormat: function(d){
							return d3.format(',.4f')(d);
						},
						yAxis: {
							axisLabel: 'Ratio',
							tickFormat: function(d){
								return d3.format(',.4f')(d);
							}
						},
						barColor: function(d){
							return colorConfig.scale(colorConfig.get(d));
						},
						margin: {"left": 300},
						tooltip: {
							contentGenerator: tooltipContent.bind(null, scope.config)
						}
					}
				};
			}],
			link: function(scope, elem, attr, ctrl){
				var colorLegendConfig = {
					width: 130,
					height: 300
				};
				var d3root = d3.select(elem[0]);
				d3root.select(".controls svg").remove();
				var svgZValueLegend = d3root.select("#colorLegend").append("svg");
				svgZValueLegend.attr("width", colorLegendConfig.width).attr("height", colorLegendConfig.height);

				var w = colorLegendConfig.width, h = colorLegendConfig.height;

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

				var y = d3.scale.linear().range([h - 100, 0]).domain([scope.config.color.min, scope.config.color.max]).nice();

				var yAxis = d3.svg.axis().scale(y).orient("right");

				key.append("g").attr("class", "y axis").attr("transform", "translate(31,10)")
					.call(yAxis)
					.append("text").attr("y", h-100).attr("dy", ".71em").style("text-anchor", "end").text(scope.config.color.label);

			}
		};
	};
	directive.$name="mevDotplotDirective";
	directive.$inject=[];
	return directive;
});