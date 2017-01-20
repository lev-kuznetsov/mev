define(["lodash", "./mevHBarchart.tpl.html"], function(_, template){"use strict";
	var directive = function mevEnrichmentBarchartDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevHbarchart"
			},
			template: template,
			controller: ["$scope", "mevBarchartNvd3Adaptor", "mevTooltipContent", "mevChartConfig", "mevChartDimConfig", "mevChartColorDimConfig",
				function(scope, mevBarchartNvd3Adaptor, mevTooltipContent, mevChartConfig, mevChartDimConfig, mevChartColorDimConfig){
					var input = scope.config.data;
					scope.config.z.display="color";
					mevChartConfig(scope.config, {
						onUpdateColor: function() {
							scope.api.refresh();
						} 
					});
					var xConfig = scope.config.x;
					var yConfig = scope.config.y;
					var zConfig = scope.config.z;

					scope.data=mevBarchartNvd3Adaptor(scope.config, input);
					scope.options = {
		            chart: {
		                type: 'multiBarHorizontalChart',
		                height: d3.max([450, input.length*12]),
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
		                  return zConfig.scale(zConfig.get(d));
		                },
		                margin: {"left": 400},
		                tooltip: {
							contentGenerator: mevTooltipContent.bind(null, scope.config)
						},
		            }
		        };
			}],
			link: function(scope, elem, attr, ctrl){}
		};
	};
	directive.$name="mevHbarchart";
	directive.$provider="directive";
	directive.$inject=["mevBarchartNvd3Adaptor", "mevTooltipContent"];
	return directive;
});