define(["lodash", "./mevHBarchart.tpl.html"], function(_, template){"use strict";
	var directive = function mevEnrichmentBarchartDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevHbarchart"
			},
			template: template,
			controller: ["$scope", "mevBarchartNvd3Adaptor", "mevTooltipContent", "mevChartDimConfig",
				function(scope, mevBarchartNvd3Adaptor, mevTooltipContent, mevChartDimConfig){

				var xConfig = mevChartDimConfig(scope.config.x);
				var yConfig = mevChartDimConfig(scope.config.y);
				var zConfig = mevChartDimConfig(scope.config.z);
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
							contentGenerator: mevTooltipContent.bind(null, scope.config)
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
	directive.$inject=["mevBarchartNvd3Adaptor", "mevTooltipContent"];
	return directive;
});