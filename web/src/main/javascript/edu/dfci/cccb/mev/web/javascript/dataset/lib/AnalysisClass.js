define(['extend'], function(extend){
	
	//Constructor :: Object -> $Function
    //  Used to convert http response for analysis into analysis class
	return function(initialData){
		return function(){
			var self = this;
			
			extend(self, initialData);
		}
	}
})