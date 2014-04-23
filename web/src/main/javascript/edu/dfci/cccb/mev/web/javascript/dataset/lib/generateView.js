define([], function(){
	
	
	//generateView :: !view.get, !views || Object -> null
	//		Modifies dataset object to add a view.
	return function(initialData){

		var multipleViews = false; //maybe change later?
		
		var self = this;
		
		if (multipleViews){
			self.views.push(self.view.get(initialData));
		} else {
			self.views = [self.view.get(initialData)];
		}
		
		return null;
		
	}
	
})