define([], function(){
	var Facet = function(promise){
		var self=this;
		
		//unwrap
		var _data;		
		promise.then(function(raw){
			_data=raw;
		});
		
		//public
		self.getType=function(){
			return _data.type;
		};
	};
	return Facet;
});