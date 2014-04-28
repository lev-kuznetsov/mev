define(['extend'], function(extend){
	
	//Constructor :: Object -> [Selection]
    //  Constructor using http response data.
	return function(initialData){
		return function(){
			
		    var self = this;
		    extend(self, initialData);
		    
		}
	}
})