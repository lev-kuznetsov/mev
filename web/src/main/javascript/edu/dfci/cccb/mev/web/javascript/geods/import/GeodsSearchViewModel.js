define([],function(){
	var GeodsSearchViewModel = function(GeodsSummaryFactory){
		var self=this;
		var _GeodsSummaryFactory=GeodsSummaryFactory;
		this.searchById=function(id){
			console.debug("vm.searchById", id);
			this.summary=_GeodsSummaryFactory.create(id);		
		};
		this.import=function(){
			
		};
	};		
	return GeodsSearchViewModel;
});