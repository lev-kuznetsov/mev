define(["angular", "geods/Geods.module"], function(angular, angularModule){
	angularModule.service("GeodsESearchResourceSrvc", ["$resource", function(){
		var eutilsRootUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
		var eutilsSummaryUrl=eutilsRootUrl+"esearch.fcgi";			
		return $resource(
				eutilsSummaryUrl, 
				{//default params
					db: "gds",
					retmode: "json",
					term: "(gds[Entry+Type])+AND+(cancer[Title]+AND+ovarian[Title])"
				});
	}]);
});