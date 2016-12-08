define(["angular", "geods/Geods.module", "angular-route", "geods/domain/GeodsSummary"], function(angular, angularModule, ngResource, GeodsSummary){
	angularModule.service("MevGeodsSummaryResourceSrvc", ["$resource", function($resource){
		var eutilsRootUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
		var eutilsSummaryUrl=eutilsRootUrl+"esummary.fcgi";			
		return $resource(
				eutilsSummaryUrl, 
				{//default params
					db: "gds",
					retmode: "json"
				});
	}]);
});