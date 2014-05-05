define(['./SelectionClass'], function(SelectionClass){

	//Constructor :: Object -> $Function
	return function(id, initialData){
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
		this.row = initialData.row;
		
		this.analyses = [];
		this.views = [];
		
	};
	
});

