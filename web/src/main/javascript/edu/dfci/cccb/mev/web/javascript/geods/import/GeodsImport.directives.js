define(['angular', 
        'geods/Geods.module', 
        'geods/import/GeodsImportCtrl', 
        'geods/domain/GeodsSummaryViewModel', 
        'geods/services/MevGeodsSummaryResourceSrvc',
        'geods/services/MevGeodsSearchResourceSrvc',
        'geods/services/MevGeodsSummaryFactory'], 
function(angular, 
		angularModule, 
		GeodsImportCtrl, 
		GeodsSummaryViewModel){
	
	angularModule.directive('mevGeodsImportDirective', 
	['MevGeodsSummaryFactory', '$modal', '$location', 'MevGeodsSearchResourceSrvc',
	function(MevGeodsSummaryFactory, $modal, $location, MevGeodsSearchResourceSrvc){
		return {
			scope:{},
			restrict: 'EAC',			
			templateUrl: angularModule.path+"import/geodsimport.tpl.html",
			link: function(scope, elm, attr, nullCtrl){
				scope.vm = new GeodsSummaryViewModel(MevGeodsSummaryFactory, scope, $modal, $location);
			}
		};
	}])
	.directive('mevGeodsSearchResultsDirective',
	[function(){
		return {
			scope:{},
			restrict: 'AE',
			termplateUrl: angularModule.path+"import/GeodsSearchResults.tpl.html",
			link: function(scope, elm, attrs, nullCtrl){
				
			}
		}
	}]);
});