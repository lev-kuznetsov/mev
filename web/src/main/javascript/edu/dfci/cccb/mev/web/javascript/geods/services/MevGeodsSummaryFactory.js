define(["angular", 
        "geods/Geods.module",         
        "geods/domain/GeodsSummaryFactory",
        "angular-route",
        "geods/services/MevGeodsSummaryResourceSrvc", 
        "geods/services/MevGeodsImportSrvc",
        "geods/services/MevGeodsSearchResourceSrvc"], 
function(angular, 
		angularModule, 		
		GeodsSummaryFactory){
	
	angularModule.service('MevGeodsSummaryFactory', 
		 ["MevGeodsSummaryResourceSrvc", "MevGeodsImportSrvc", "MevGeodsSearchResourceSrvc", "$q",
		 function(MevGeodsSummaryResourceSrvc, MevGeodsImportSrvc, MevGeodsSearchResourceSrvc, $q){
			return new GeodsSummaryFactory(MevGeodsSummaryResourceSrvc, MevGeodsSearchResourceSrvc, MevGeodsImportSrvc, $q);
		}]);
});