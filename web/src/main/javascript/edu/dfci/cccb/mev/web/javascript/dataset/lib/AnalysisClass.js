define(['extend'], function(extend){
	
	//Constructor :: [AnalysisResponseObject] -> $Function [Analysis]
    //  Used to convert http response for analysis into analysis class
	return function(initialData){

		var self = this;
		
		var internalViewTypes = {
		   "Hierarchical Clustering": "tree"
		}
		
		self.viewType = internalViewTypes[initialData.type]

		    
		extend(self, initialData)

		
		

	}
})