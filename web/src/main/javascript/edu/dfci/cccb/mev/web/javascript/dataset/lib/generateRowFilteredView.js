define(['./cellFilter'], function(cellFilter) {
	
	
	//generateRowFilterFromView :: !views, !addView, !expression, !column || Array -> null
	//	Used to filter datasets current rows and generate a new view from that.
    //  Is an intermediary taking a dataset and creating a smaller dataset object that
    //  generateView can use.
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

		
		self.generateView(newView)
		
		return null;
	}
	
	
});