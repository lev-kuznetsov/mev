define([], function(){
	var ClinicalSummaryListVM = function(summaries){
		//private members
		var _self=this;
		
		//public method		
		this.getSummaries=function(){
			return summaries;
		};
		this.getTitle=function(summary){
			return "Accordion for "+summary.config.field;
		}
	};
	return ClinicalSummaryListVM;
});