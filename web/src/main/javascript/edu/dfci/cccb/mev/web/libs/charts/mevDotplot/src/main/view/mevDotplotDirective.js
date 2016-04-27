define(["mui", "./mevDotplot.tpl.html"], function(ng, template){ "use strict";
	var directive = function mevDotplotDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevDotplot"
			},
			template: template,
			controller: ["$scope", "mevDotplotNvd3Adaptor", "mevTooltipContent", function(scope, mevDotplotNvd3Adaptor, mevTooltipContent){

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
							contentGenerator: mevTooltipContent.bind(null, scope.config)
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
	directive.$inject=["mevTooltipContent"];
	return directive;
});