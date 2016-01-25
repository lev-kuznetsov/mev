define(["angular", "../ClinicalSummary.module", "../domain/ClinicalSummaryConfig"], 
		function(angular, angularModule, ClinicalSummaryConfig){
	angularModule.service("MevClinicalSummaryConfigSrvc", [function(){
		this.get=function(){
			return ClinicalSummaryConfig;
		};
	}]);
});