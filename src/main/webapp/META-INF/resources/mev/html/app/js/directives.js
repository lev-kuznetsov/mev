'use strict';

/* Directives */


angular.module('myApp.directives', [])
.directive('appVersion', ['version', function(version) {
	return function(scope, elm, attrs) {
	  elm.text(version);
	};
}])
.directive('visHeatmap', [function() {

	return {
			
		restrict: 'E',
		scope: {
			inputdata:"=",
			inputcolor:"="
		},
		link: function (scope, element, attrs) {
			
			scope.$watch('inputdata', function(newdata, olddata) {
				
				scope.visparams = {
					width : 600,
					height : 700,
					columnlabelgutter : 80,
					rowlabelgutter : 80,
				};

				scope.heldcolumns = 0;
				scope.heldrows = 0;
				scope.helddata = 0;
				scope.cellXPosition = 0;
				scope.cellYPosition = 0;
				
				scope.colorScaleForward = 0;
				scope.colorScaleReverse = 0;
				scope.greenColorControl = 0;
				scope.blueColorControl = 0;
				scope.redColorControl = 0;
				
				var threshold = 150;
				
				var buildvisualization = function() {
					
					svg.selectAll("rect")
						.data(scope.helddata)
						.enter()
						.append("rect")
						.attr({
							"height": function(){
								return .95*(scope.cellYPosition(1) - scope.cellYPosition(0));
							},
							"width": function(){
								return .95*(scope.cellXPosition(1) - scope.cellXPosition(0));
							},
							"x": function(d, i) {
								return scope.cellXPosition(i%scope.heldcolumns);
							},
							"y": function(d, i) {
								return scope.cellYPosition(Math.floor(i/scope.heldcolumns));
							},
							"fill": function(d) {
								return "rgb(" + scope.redColorControl(d, scope.inputcolor) + "," + scope.greenColorControl(d, scope.inputcolor) + ","+ scope.blueColorControl(d, scope.inputcolor)+")";
							},
							"value": function(d) {
								return d;
							},
							"index": function(d, i) {
								return i;
							},
							"row": function(d, i) {
								return Math.floor(i/scope.heldcolumns);
							},
							"column": function(d, i) {
								return i%scope.heldcolumns;
							},
							
						});
						
					svg.selectAll("text")
						.data(scope.heldrowlabels)
						.enter()
						.append("text")
						.text(function(d, i){
							return scope.heldrowlabels[i]
						})
						.attr({
							"x": function(d, i) {
								return scope.cellXPosition(0) - scope.visparams.rowlabelgutter;
							},
							"y": function(d, i) {
								return scope.cellYPosition(i) +15;
							},
							"text-align": "center"
						});
						
					var xAxis = d3.svg.axis().scale(scope.cellXPosition).orient("bottom");

					svg.append('g').call(xAxis);
				}

				if (typeof olddata == 'undefined' && typeof newdata == 'object') {
					
					scope.heldcolumns = scope.inputdata.columns;
					scope.heldrows = scope.inputdata.rows;
					scope.heldcolumnlabels = scope.inputdata.columnlabels;
					scope.heldrowlabels = scope.inputdata.rowlabels;
					scope.helddata = scope.inputdata.data;
					
					var svg = d3.select(element[0])
						.append("svg")
						.attr("width", scope.visparams.width )
						.attr("height", scope.visparams.height);
						
					scope.colorScaleForward = function(j) {	 
						var value = d3.scale.linear()
							.domain([d3.min(scope.helddata), d3.max(scope.helddata)])
							.rangeRound([0, 255]);
						var output = 0;
						if (value(j) >= threshold ) {
							var layer2 = d3.scale.linear()
								.domain([125,255])
								.rangeRound([0,255]);
							output = layer2(value(j));  	
						}
						return output;
					};

					scope.colorScaleReverse = function(j) {	 
						var value = d3.scale.linear()
							.domain([d3.min(scope.helddata), d3.max(scope.helddata)])
							.rangeRound([255, 0]);
						var output = 0;
						if ( value(j) >= threshold ) {
							var layer2 = d3.scale.linear()
								.domain([255,125])
								.rangeRound([255, 0]);
							output = layer2(value(j));  	
						}
						return output;
					};

					scope.redColorControl = function(j, code) {
						var output = 0;
						if (code == 0) {
							output = scope.colorScaleForward(j);
						} else {
							output = scope.colorScaleForward(j);
						}
						return output;
					};

					scope.blueColorControl = function(j, code) {
						var output = 0;
						if (code == 1) {
							output = scope.colorScaleReverse(j);
						}
						return output;
					};

					scope.greenColorControl = function(j, code) {
						var output = 0;

						if (code == 0) {
							output = scope.colorScaleReverse(j);
						} else {
							output = scope.colorScaleForward(j);
						}

						return output;
					};
					
					
					scope.cellXPosition = d3.scale.ordinal()
						.domain(d3.range(scope.heldcolumns))
						.rangeRoundBands([scope.visparams.rowlabelgutter, scope.visparams.width], .05);
								
					scope.cellYPosition = d3.scale.ordinal()
						.domain(d3.range(scope.heldrows))
						.rangeRoundBands([scope.visparams.columnlabelgutter, scope.visparams.height], .05);
					
					buildvisualization();

				}
			});
		}
	}
}]);
