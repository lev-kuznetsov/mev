define(["angular", 
        "clinical/ClinicalSummary.module",        
        "clinical/domain/ClinicalSummaryListVM",
        "clinical/services/MevClinicalSummaryFactorySrvc"], 
function(angular, angularModule, ClinicalSummaryListVM){
	angularModule.service("MevClinicalSummaryViewModelFactorySrvc", ["MevClinicalSummaryFactorySrvc",
	                                                                 function(MevClinicalSummaryFactorySrvc){
		this.createList=function(){
			return new ClinicalSummaryListVM(MevClinicalSummaryFactorySrvc.getAll());
		};
	}]);
});