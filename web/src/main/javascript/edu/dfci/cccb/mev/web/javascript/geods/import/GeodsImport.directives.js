define(['angular', "./geodsimport.tpl.html",
        'geods/Geods.module', 
        'geods/import/GeodsImportCtrl', 
        'geods/domain/GeodsSummaryViewModel', 
        'geods/services/MevGeodsSummaryResourceSrvc',
        'geods/services/MevGeodsSearchResourceSrvc',
        'geods/services/MevGeodsSummaryFactory',
        "angular-ui-bootstrap"], 
function(angular,
		importTemplate,
		angularModule, 
		GeodsImportCtrl, 
		GeodsSummaryViewModel){
	
	angularModule.directive('mevGeodsImportDirective', 
	['MevGeodsSummaryFactory', '$modal', '$location', 'MevGeodsSearchResourceSrvc', '$rootScope',
	function(MevGeodsSummaryFactory, $modal, $location, MevGeodsSearchResourceSrvc, $rootScope){
		return {
			scope:{},
			restrict: 'EAC',			
			// templateUrl: angularModule.path+"import/geodsimport.tpl.html",
			template: importTemplate,
			link: function(scope, elm, attr, nullCtrl){
				scope.vm = new GeodsSummaryViewModel(MevGeodsSummaryFactory, scope, $modal, $location, $rootScope);
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