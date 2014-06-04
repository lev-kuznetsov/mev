define(["angular", 
        "clinical/ClinicalSummary.module",        
        "clinical/domain/ClinicalSummaryListVM",
        "clinical/domain/ClinicalSummaryVM",
        "clinical/services/MevClinicalSummaryFactorySrvc"], 
function(angular, angularModule, ClinicalSummaryListVM, ClinicalSummaryVM){
	angularModule.service("MevClinicalSummaryViewModelFactorySrvc", ["MevClinicalSummaryFactorySrvc",
	                                                                 function(MevClinicalSummaryFactorySrvc){
		this.createList=function(){
			return new ClinicalSummaryListVM(MevClinicalSummaryFactorySrvc.getAll());
		};
		this.create=function(summary){
			return new ClinicalSummaryVM(summary);
		};
	}]);
});