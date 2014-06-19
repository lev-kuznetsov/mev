define(["angular", 
        "clinical/ClinicalSummary.module",
        "clinical/domain/ClinicalSummaryFactory",
        "clinical/services/MevClinicalSummaryConfigSrvc",
        "clinical/services/MevClinicalSummaryRepositorySrvc"],
function(angular, 
		angularModule,
		ClinicalSummaryFactory){
	angularModule.service("MevClinicalSummaryFactorySrvc", ["MevClinicalSummaryConfigSrvc", 
	                                                        "MevClinicalSummaryRepositorySrvc", 
	function(MevClinicalSummaryConfigSrvc, MevClinicalSummaryRepositorySrvc){
		this.create=function(){
			return new ClinicalSummaryFactory(MevClinicalSummaryConfigSrvc.get(), MevClinicalSummaryRepositorySrvc.create());
		};
	}]);
});