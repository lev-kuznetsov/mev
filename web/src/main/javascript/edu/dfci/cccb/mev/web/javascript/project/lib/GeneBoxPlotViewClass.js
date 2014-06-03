define(['extend'], function(extend){
	
	//GeneBoxPlotViewClass :: !dataset, params --> View
	//
	// params = {rows, groups: [ Selections ], values: [Values], sortedValues:[Numbers]}
	return function(params){
		
		var self = this;
		
		self.id = Math.random().toString(36).substring(7);
		
		extend(self, params);
		
	}
	
})