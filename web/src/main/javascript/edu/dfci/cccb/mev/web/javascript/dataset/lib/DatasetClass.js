define(['./SelectionClass'], function(SelectionClass){

	//Constructor :: Object -> $Function
	return function(initialData){
		
		var self = this;
		
		this.expression = {
			values: initialData.values,
			max: initialData.max,
			min: initialData.min,
			avg: initialData.avg,
		};
		
		this.row = {
			values : initialData.row.keys
		};
		
		this.column = {
				values : initialData.column.keys
			};
		
		this.selections = {
			row: {
				values: []
			},
			column: {
				values: []
			}
		};
		this.analyses = [];
		this.views = [];
		
		initialData.column.selections.map(function(returnSelection){
			self.selections.column.push(new SelectionClass(returnSelection))
		});
		
		initialData.row.selections.map(function(returnSelection){
			self.selections.row.push(new SelectionClass(returnSelection))
		});
		
	};
	
});

