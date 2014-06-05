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
	function(MevClinicalsummaryconfigSrvc, MevClinicalSummaryRepositorySrvc){
		this.create=function(){
			return new ClinicalSummaryFactory(MevClinicalsummaryconfigSrvc.get(), MevClinicalSummaryRepositorySrvc.create());
		};
	}]);
});