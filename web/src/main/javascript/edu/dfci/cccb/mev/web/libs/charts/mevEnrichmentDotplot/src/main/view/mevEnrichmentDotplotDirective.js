define(["lodash", "./mevEnrichmentDotplot.tpl.html"], function(_, template){"use strict";
	var directive = function mevEnrichmentDotplotDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevEnrichmentDotplot"
			},
			template: template,
			controller: ["$scope", "mevDotplotNvd3Adaptor", function(scope, mevDotplotNvd3Adaptor, mevEnrcihmentDataAdaptor){
				_.extend(scope.config, {
					series: {
						label: "Ratio",
						sort: {
							field: function(d){
								return d.getMatched();
							},
							order: "desc"
						}
					},
					x: {
						field: function(d){
							return d.getName();
						},
						label: "Name"
					},
					y: {
						field: function(d){
							return d.getRatio()
						},
						label: "Ratio",
						precision: 0
					},
					color: {
						field: function(d){
							return d.getPValue();
						},
						label: "pValue"
					},
					size: {
						field: function(d){
							return d.getMatched();
						},
						label: "Match"
					},
					tooltip: {
						fields: {
							"Total": function(d){
								return d.getTotal();
							}
						}
					}
				});
			}],
			link: function(scope, elem, attr, ctrl){}
		};
	};
	directive.$name="mevEnrichmentDotplot";
	directive.$provider="directive";
	directive.$inject=["mevDotplotNvd3Adaptor"];
	return directive;
});
