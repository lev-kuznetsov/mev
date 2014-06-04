define(["angular", 
        "clinical/ClinicalSummary.module",
        "clinical/services/MevClinicalSummaryViewModelFactorySrvc"], 
function(angular, 
		angularModule){
	
		angularModule.directive("mevClinicalSummaryList", ["MevClinicalSummaryViewModelFactorySrvc", 
		                                               function(MevClinicalSummaryViewModelFactorySrvc){
			console.debug("creating directive mevClinicalSummary");
			return {
				scope: {},
				restrict: "AC",
				templateUrl: angularModule.path+"directives/ClinicalSummaryList.ngAccordion.tpl.html",
				link: function(scope, elm, attrs, nullCtrl){
					var viewModel = MevClinicalSummaryViewModelFactorySrvc.createList();
					console.debug("==>MevClinicalSummaryViewModelFactorySrvc.getSummaries()", viewModel.getSummaries());
					scope.viewModel=viewModel;
				}
			};
		}]).directive("mevClinicalSummary", [function(){
			return {
				scope: {
					viewModel: "=summary"
				},
				restrict: "ACE",
				templateUrl: angularModule.path+"directives/ClinicalSummary.tpl.html",
				link: function(scope, elm, attrs, nullCtrl){			
				}				
			};
		}]);
});