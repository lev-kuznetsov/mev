define(["clinical/domain/ClinicalSummaryVM"], function(ClinicalSummaryVM){
	var ClinicalSummaryListVM = function(summaries){
		//private members
		var _self=this;
		
		//public method		
		this.getSummaries=function(){
			return summaries;
		};
		this.getSummaryVM=function(summary){
			return new ClinicalSummaryVM(summary);
		};
		
	};
	return ClinicalSummaryListVM;
});