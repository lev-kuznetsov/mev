define([], function(){
	
	var SingleFieldSummary = function(config, dataPromise){
		//private members		
		
		//private methods
		function formatTitle(string){
			
			var returnstring = "";
			var arr = string.split('_');
			arr.map(function(word){
				returnstring = returnstring.concat(word.charAt(0).toUpperCase() + word.slice(1) + " ")
			})
			
			return returnstring;
		}
		
		//public members
		var _self=this;
		this.data=null;
		this.config=config;
		dataPromise.then(function(data){
			_self.data=data;
		});
		
		//public methods
		this.getTitle=function(){
			if(typeof config.title=="undefined"){
				return formatTitle(config.field);
			}else{
				return config.title;
			};
		};		
	};
	//prototype methods
	
	return SingleFieldSummary;
});