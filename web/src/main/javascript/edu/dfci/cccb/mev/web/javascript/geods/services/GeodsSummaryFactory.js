define(["angular", "geods/Geods.module", "angularResource", "geods/domain/GeodsSummary", "geods/services/GeodsSummaryResourceSrvc", "geods/services/MevGeodsImportSrvc"], function(angular, angularModule, ngResource, GeodsSummary){
	angularModule.factory('GeodsSummaryFactory', ['GeodsSummaryResourceSrvc', 'MevGeodsImportSrvc', function(GeodsSummaryResourceSrvc, MevGeodsImportSrvc){
		console.debug("GeodsSummaryFactory init");
		return {
			create: function(id){
				return new GeodsSummary(GeodsSummaryResourceSrvc, MevGeodsImportSrvc, id);			
			}
		};
	}]);
});