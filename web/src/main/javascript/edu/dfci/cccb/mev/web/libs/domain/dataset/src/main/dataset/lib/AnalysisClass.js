define(['lodash'], function(_){
	
	//Constructor :: [AnalysisResponseObject] -> $Function [Analysis]
    //  Used to convert http response for analysis into analysis class
	return function(initialData){

		var self = this;
		
		var internalViewTypes = {
		   "Hierarchical Clustering": "tree"
		}
		
		self.randomId = Math.random().toString(36).substring(7);
		
		self.viewType = internalViewTypes[initialData.type]

		    
		_.extend(self, initialData)

		
		

	}
})