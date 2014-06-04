define(["angular", "clinical/ClinicalSummary.module", "clinical/domain/ClinicalSummaryConfig"], function(angular, angularModule, clinicalSummaryConfig){
	angularModule.service("MevClinicalSummaryConfigSrvc", [function(){
		this.get=function(){
			return clinicalSummaryConfig;
		};
	}]);
});