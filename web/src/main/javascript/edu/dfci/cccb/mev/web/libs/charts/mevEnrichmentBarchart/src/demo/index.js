define(["mui", "mev-enrichment-barchart", "./topgo_dummy.json"], function(ng, mod, topgoJson){
	var app = ng.module("mev-enrichment-barchart-demo", arguments, arguments);
	
	app.controller('MainCtrl', function($scope) {
		$scope.config={
			data: topgoJson.results,
			series: "Counts"
		};
	});

	ng.element(document).ready(function(){
		ng.bootstrap(document, [app.name]);
	});	
});