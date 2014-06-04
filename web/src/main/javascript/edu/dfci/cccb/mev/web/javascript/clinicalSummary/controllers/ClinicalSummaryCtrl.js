define(["angular", 
        "clinical/ClinicalSummary.module", 
        "clinical/services/MevClinicalSummaryRepositorySrvc",
        "clinical/services/MevClinicalSummaryConfigSrvc",
        "clinical/services/MevClinicalSummaryFactorySrvc"], 
function(angular, angularModule){
	console.debug("loading MevClinicalSummaryCtrl file");
	angularModule.controller("MevClinicalSummaryCtrl", ["MevClinicalSummaryRepositorySrvc", 
	                                                    "MevClinicalSummaryConfigSrvc",
	                                                    "MevClinicalSummaryFactorySrvc",
                                           function(MevClinicalSummaryRepositorySrvc, 
                                        		   MevClinicalSummaryConfigSrvc,
                                        		   MevClinicalSummaryFactorySrvc){
		console.debug("init MevClinicalSummaryCtrl");
		var repository = MevClinicalSummaryRepositorySrvc.create();
		console.debug("repository", repository);
		repository.getData(["ethnicity"]).then(function(data){
			console.debug("repository.getData=",data);
		});
		console.debug("MevClinicalSummaryConfigSrvc.get()", MevClinicalSummaryConfigSrvc.get());
		console.debug("MevClinicalSummaryFactorySrvc.get()", MevClinicalSummaryFactorySrvc.getAll());
		
	}]);
});