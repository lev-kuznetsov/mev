define(["angular", 
        "clinical/ClinicalSummary.module",
        "clinical/services/MevClinicalSummaryFactorySrvc"], 
function(angular, angularModule){
	console.debug("loading MevClinicalSummaryCtrl file");
	angularModule.controller("MevClinicalSummaryCtrl", ["MevClinicalSummaryFactorySrvc",
                                           function(MevClinicalSummaryFactorySrvc){				
		
	}]);
});