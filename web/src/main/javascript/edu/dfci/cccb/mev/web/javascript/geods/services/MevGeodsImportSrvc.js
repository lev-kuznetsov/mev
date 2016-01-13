define(["angular", "geods/Geods.module", "angular-route", ], 
function(angular, angularModule, ngResource){
	angularModule.service("MevGeodsImportSrvc", ["$resource", function($resource){
		var rootUrl = "/geods";
		var putUrl=rootUrl+"/import/:id";			
		return $resource(
				putUrl, null,
				{//default params
					put: {method: 'PUT'}
				});
	}]);
});