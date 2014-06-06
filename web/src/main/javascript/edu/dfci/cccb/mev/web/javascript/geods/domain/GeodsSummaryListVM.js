define([],function(){
	var GeodsSummaryListVM = function(MevGeodsSummaryFactory){
		//private				
		var _result;
		//public 
		this._self=this;
		
		this.search=function(keywords){
			_result=MevGeodsSummaryFactory.search(keywords);
			
		};
	};
	return GeodsSummaryListVM;
});