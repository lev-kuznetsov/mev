define(["angular", "geods/Geods.module"], function(angular, angularModule){
	angularModule.service("MevGeodsRepositorySrvc", 
	["MevGeodsSearchResourceSrvc", function(MevGeodsSearchResourceSrvc){
		
	}])
	.service("MevGeodsSearchResourceSrvc", 
	["$resource", function($resource){
		var eutilsRootUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
		var eutilsSummaryUrl=eutilsRootUrl+"esearch.fcgi";			
		return $resource(
				eutilsSummaryUrl, 
				{//default params
					db: "gds",
					retmode: "json"
				});
	}]);
});