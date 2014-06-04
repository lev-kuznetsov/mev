define([], function(){
	var ClinicalsummaryVM=function(summary){
		//private members
		var _self = this;
		
		//public members
		this.getTitle=function(){
			return "Summary for "+summary.config.field;
		};
		this.getFieldName=function(){
			return summary.config.field;
		};
		this.getChartType=function(){
			return summary.config.chartType;
		};
		this.getData=function(){
			return summary.data;
		};
		this.toString=function(){
			return JSON.stringify(summary.data);
		};
	};
	return ClinicalsummaryVM;
});