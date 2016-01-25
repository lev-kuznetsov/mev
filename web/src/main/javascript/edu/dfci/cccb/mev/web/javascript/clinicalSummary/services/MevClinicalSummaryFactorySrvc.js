define(["angular", 
        "../ClinicalSummary.module",
        "../domain/ClinicalSummaryFactory",
        "../services/MevClinicalSummaryConfigSrvc",
        "../services/MevClinicalSummaryRepositorySrvc"],
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