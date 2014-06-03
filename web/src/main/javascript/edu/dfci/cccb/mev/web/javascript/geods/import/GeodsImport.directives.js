define(['angular', 'geods/Geods.module', 'geods/import/GeodsImportCtrl', 'geods/import/GeodsSearchViewModel', 'geods/services/GeodsSummaryResourceSrvc'], function(angular, angularModule, GeodsImportCtrl, GeodsSearchViewModel){
	angularModule.directive('mevGeodsImportDirective', ['GeodsSummaryFactory', '$modal', '$location',
	                                                    function(GeodsSummaryFactory, $modal, $location){
		return {
			scope:{},
			restrict: 'EAC',			
			templateUrl: angularModule.path+"import/geodsimport.tpl.html",
			link: function(scope, elm, attr, nullCtrl){
				console.debug("linking MevGeodsImportDirective");
//				scope.message="mevGeodsImportDirective";
				scope.vm = new GeodsSearchViewModel(GeodsSummaryFactory, scope, $modal, $location);				
			}
		};
	}]);
});