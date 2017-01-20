define(["mui", "jquery", "d3", "nvd3", "angular-nvd3", "lodash", "mev-chart-utils", "angular-ui-bootstrap",
	"./_directives/scatterPlotDirective", 
	"./services/adaptors/Nvd3DataAdaptor",
	"nvd3/build/nv.d3.css!", "./style/scatter.less!"], 
function(ng, $, d3, nvd3, ngnvd3, _){
	"use strict";
	return ng.module("mevScatterPlot", arguments, arguments)
	.controller("scatterCtrl", ["$scope", function(){
		var livex;	
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
						x: random() * 1000
						, y: random() * 1000
						, size: Math.random() 
						, shape: shapes[j % 6]
					});
				}
			}
			// data.push({key: "Copy 0", values: []});
			// data[0].values.map(function(item){				
			// 	data[data.length-1].values.push(item);
			// });
			return data;
		};
	}]);
});
