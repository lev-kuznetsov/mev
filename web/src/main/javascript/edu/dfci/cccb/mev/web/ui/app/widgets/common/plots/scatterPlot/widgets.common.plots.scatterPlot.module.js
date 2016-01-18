define(["ng", "jquery", "d3", "nvd3", "angular-nvd3", "lodash", "./_directives/scatterPlotDirective"], 
function(ng, $, d3, nvd3, ngnvd3, _, scatterPlotDirective){	
	"use strict";
	return ng.module("mui.widgets.common.plots.scatterPlot", [])	
	.directive(scatterPlotDirective.$name, scatterPlotDirective)
	.controller("scatterCtrl", ["$scope", function(){
						
		/* Random Data Generator (took from nvd3.org) */
		this.generateData = function(groups, points) {
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
			data.push({key: "Copy 0", values: []});
			data[0].values.map(function(item){				
				data[data.length-1].values.push(item);
			});
			return data;
		};
	}]);
});
