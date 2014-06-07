define(["angular", "clinical/ClinicalSummary.module", "clinical/domain/ClinicalSummaryConfig"], 
		function(angular, angularModule, ClinicalSummaryConfig){
	angularModule.service("MevClinicalSummaryConfigSrvc", [function(){
		this.get=function(){
			return ClinicalSummaryConfig;
		};
	}]);
});