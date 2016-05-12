define(["mui", "./mevDotplot.tpl.html"], function(ng, template){ "use strict";
	var directive = function mevDotplotDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevDotplot"
			},
			template: template,
			controller: ["$scope", "mevDotplotNvd3Adaptor", "mevTooltipContent", "mevChartConfig", "mevChartDimConfig", "mevChartColorDimConfig",
				function(scope, mevDotplotNvd3Adaptor, mevTooltipContent, mevChartConfig, mevChartDimConfig, mevChartColorDimConfig){
					var input = scope.config.data;
					mevChartConfig(_.extend(scope.config, {
						onUpdateColor: function() {
							scope.api.refresh();
						} 
					}));
					var xConfig = scope.config.x;
					var yConfig = scope.config.y;
					var sizeConfig = scope.config.size;
					var colorConfig = scope.config.color;

					scope.data=mevDotplotNvd3Adaptor(scope.config, input);
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
				
			}
		};
	};
	directive.$name="mevDotplotDirective";
	directive.$inject=["mevTooltipContent"];
	return directive;
});