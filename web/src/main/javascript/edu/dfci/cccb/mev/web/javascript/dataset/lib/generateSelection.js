define(['./SelectionClass'], function(SelectionClass){

	//generateSelection :: !selection.post !selections.(row | column) 
	//					   || Object, Object [, Function, Function] -> null
	return function(params, data, success, error){

	    //We're pretty much just taking the response and we're going to just add that
	    var selection = new SelectionClass(self.selection.post(params, data, success, error) )
	    
		var self = this;
		self.selections[data.dimension]
			.push(selection);
		
		return null
	}
	
})