define(['./cellFilter'], function(cellFilter) {
	
	
	//generateRowFilterFromView :: !views, !addView, !expression, !column || Array -> null
	//	Used to filter datasets current rows and generate a new view from that
	return function(labels){
		
		var self = this;
		
		var newCells = cellFilter.call(self, labels);
		
		var newView = {
					expression:{
						values: newCells,
						max: self.expression.max,
						min: self.expression.min,
						avg: self.expression.avg,
					},
					row:{
						values: labels
					},
					column:{
						values: self.column.values
					}
				};

		
		self.addView(newView)
		
		return null;
	}
	
	
});