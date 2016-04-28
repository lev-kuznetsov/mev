define(["mui", "lodash", "mev-hbarchart", "./topgo_dummy.json", "./gsea.json"], function(ng, _, mod, topgoJson, gseaJson){
	var app = ng.module("mev-hbarchart-demo", arguments, arguments);
	
	app.controller('DemoCtrl', ["$scope", function($scope) {
		$scope.topgoConfig={
			data: topgoJson.results,
			series: "Match",
			x: {
				field: function(d){
					return d.goTerm;
				}
			},
			y: {
				field: "significantGenes",
				precision: 0,
				sort: "desc"
			},
			z: {
				field: "pValue"
			},
			tooltip: {
				fields: {
					"Total": function(d){
						return d["annotatedGenes"];
					},
					"Expected": "expected"
				}
			}
		};
		$scope.gseaConfig={
			data: gseaJson.result,
			series: "Score",
			x: {
				field: function(d){
					return d.Description;
				}
			},
			y: {
				field: "enrichmentScore",
				precision: 4,
				sort: "desc"
			},
			z: {
				field: "p.adjust"
			}
		};
	}]);

	ng.element(document).ready(function(){
		ng.bootstrap(document, [app.name]);
	});	
});