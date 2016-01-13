define(["angular", 
        "../ClinicalSummary.module",        
        "../domain/ClinicalSummaryListVM",
        "../domain/ClinicalSummaryVM",
        "./MevClinicalSummaryFactorySrvc"], 
function(angular, angularModule, ClinicalSummaryListVM, ClinicalSummaryVM){
	angularModule.service("MevClinicalSummaryViewModelFactorySrvc", ["MevClinicalSummaryFactorySrvc",
	                                                                 function(MevClinicalSummaryFactorySrvc){
		this.createList=function(){
			return new ClinicalSummaryListVM(MevClinicalSummaryFactorySrvc.create().getAll());
		};
		this.create=function(summary){
			return new ClinicalSummaryVM(summary);
		};
	}]);
});