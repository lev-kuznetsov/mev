define(['lodash'], function(_){
	
	//Constructor :: Object -> [Selection]
    //  Constructor using http response data.
	return function(initialData){
		return function(){
			
		    var self = this;
		    _.extend(self, initialData);
		    
		}
	}
})