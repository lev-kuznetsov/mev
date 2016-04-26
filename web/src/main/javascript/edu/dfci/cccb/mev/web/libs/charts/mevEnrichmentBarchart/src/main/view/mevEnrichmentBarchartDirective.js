define(["lodash", "./mevEnrichmentBarchart.tpl.html"], function(_, template){"use strict";
	var directive = function mevEnrichmentBarchartDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevEnrichmentBarchart"
			},
			template: template,
			controller: ["$scope", function(scope){

				_.extend(scope.config, {
					series: "Match",
					x: {
						field: function(d){
							return d.getName();
						},
						label: "name"
					},
					y: {
						field: function(d){
							return d.getMatched()
						},
						label: "Match",
						precision: 0,
						sort: "desc"
					},
					z: {
						field: function(d){
							return d.getPValue();
						},
						label: "pValue"
					},
					tooltip: {
						fields: {
							"Total": function(d){
								return d.getTotal();
							},
							"Ratio": function(d){
								return d.getRatio();
							}
						}
					}
				});
			}],
			link: function(scope, elem, attr, ctrl){}
		};
	};
	directive.$name="mevEnrichmentBarchart";
	directive.$provider="directive";
	directive.$inject=[];
	return directive;
});