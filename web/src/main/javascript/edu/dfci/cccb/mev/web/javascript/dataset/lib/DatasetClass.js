define(['./SelectionClass'], function(SelectionClass){

	//Constructor :: Object -> $Function
	return function(id, initialData){
		console.debug("new DatasetClass(id, initialData)", id, initialData);
		var self = this;
		this.id=id;
		this.datasetName = id;
		
		this.expression = {
			values: initialData.values,
			max: initialData.max,
			min: initialData.min,
			avg: initialData.avg,
		};
		
		this.column = initialData.column;
		this.column.values = initialData.column.keys;
		this.row = initialData.row;
		this.row.values = initialData.row.keys;
		
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
			self.selections.column.values.push(new SelectionClass(returnSelection));
		});
		
		initialData.row.selections.map(function(returnSelection){
			self.selections.row.values.push(new SelectionClass(returnSelection));
		});
		
	};
	
});

