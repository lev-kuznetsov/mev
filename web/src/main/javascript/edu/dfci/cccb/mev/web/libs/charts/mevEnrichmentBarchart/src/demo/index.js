define(["mui", "mev-enrichment-barchart", "./topgo_dummy.json"], function(ng, mod, topgoJson){
	var app = ng.module("mev-enrichment-barchart-demo", arguments, arguments);
	
	app.controller('MainCtrl', ["$scope", "mevEnrichmentDataAdaptor", function($scope, mevEnrichmentDataAdaptor) {
		$scope.config={
			data: mevEnrichmentDataAdaptor(topgoJson.results)
		};
	}]);

	ng.element(document).ready(function(){
		ng.bootstrap(document, [app.name]);
	});	
});