define(["angular", "geods/Geods.module", "angularResource", "geods/domain/GeodsSummary"], function(angular, angularModule, ngResource, GeodsSummary){
	angularModule.service("GeodsSummaryResourceSrvc", ["$resource", function($resource){
		var eutilsRootUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
		var eutilsSummaryUrl=eutilsRootUrl+"esummary.fcgi";			
		return $resource(
				eutilsSummaryUrl, 
				{//default params
					db: "gds",
					retmode: "json"
				});
	}]);
});