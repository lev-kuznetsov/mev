define([], function(){
	
	var SingleFieldSummary = function(config, dataPromise){
		//private members
		//private methods
		//public members
		var _self=this;
		this.data=null;
		this.config=config;
		dataPromise.then(function(data){
			_self.data=data;
		});
		
		//public methods
	};
	//prototype methods
	
	return SingleFieldSummary;
});