define(["angular", 
        "../ClinicalSummary.module",
        "../services/MevClinicalSummaryViewModelFactorySrvc"], 
function(angular, 
		angularModule){
	
		angularModule.directive("mevClinicalSummaryList", ["MevClinicalSummaryViewModelFactorySrvc", 
		                                               function(MevClinicalSummaryViewModelFactorySrvc){
			return {
				scope: {},
				restrict: "AC",
				templateUrl: angularModule.path+"directives/ClinicalSummaryList.ngAccordion.tpl.html",
				link: function(scope, elm, attrs, nullCtrl){
					var viewModel = MevClinicalSummaryViewModelFactorySrvc.createList();
					scope.viewModel=viewModel;
				}
			};
		}]).directive("mevClinicalSummary", ["MevClinicalSummaryViewModelFactorySrvc", function(MevClinicalSummaryViewModelFactorySrvc){
			return {
				scope: {
					summary: "="
				},
				restrict: "ACE",
				templateUrl: angularModule.path+"directives/ClinicalSummary.tpl.html",
				link: function(scope, elm, attrs, nullCtrl){
					scope.viewModel=MevClinicalSummaryViewModelFactorySrvc.create(scope.summary);
				}				
			};
		}]);
});