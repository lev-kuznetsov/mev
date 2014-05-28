define(["angular", "geods/Geods.module", "angularResource", ], 
function(angular, angularModule, ngResource){
	angularModule.service("MevGeodsImportSrvc", ["$resource", function($resource){
		var eutilsRootUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
		var eutilsSummaryUrl=eutilsRootUrl+"esummary.fcgi";			
		return $resource(
				eutilsSummaryUrl, 
				{//default params
					db: "gds",
					retmode: "json"
				});
	}]).factory('GeodsSummaryFactory', ['GeodsSummaryResourceSrvc', function(GeodsSummaryResourceSrvc){
		console.debug("GeodsSummaryFactory init");
		return {
			create: function(id){
				return new GeodsSummary(GeodsSummaryResourceSrvc, id);			
			}
		};
	}]);
});