define(['./ViewClass'], function(ViewClass){
	
	
	//generateView :: !views || Object -> null
	//		Modifies dataset object to add a view.
	return function(initialData){

		var multipleViews = false; //maybe change later?
		
		var self = this;
		
		var view = new ViewClass(initialData);
		
		if (multipleViews){
			self.views.push(view);
		} else {
			self.views = [view];
		}
		
		return null;
		
	}
	
})