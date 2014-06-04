define(["angular", 
        "clinical/ClinicalSummary.module",
        "clinical/services/MevClinicalSummaryRepositorySrvc"], 
function(angular, 
		angularModule){
	
		angularModule.directive("mevClinicalSummary", ["MevClinicalSummaryRepositorySrvc", function(MevClinicalSummaryRepositorySrvc){
			console.debug("creating directive mevClinicalSummary");
			return {
				restrict: "AC",
				template: "<div>MevClinicalSummaryRepositorySrvc ...</div>"			
			};
		}]);
});