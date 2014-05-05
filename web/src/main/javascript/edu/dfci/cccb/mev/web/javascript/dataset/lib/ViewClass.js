define([], function(){

	//Constructor :: Object -> $Function
	return function(initialData){
		
		var self = this;
		
		this.expression = {
			values: initialData.expression.values,
			max: initialData.expression.max,
			min: initialData.expression.min,
			avg: initialData.expression.avg,
		};
		
		this.row = {
			keys : initialData.row.keys
		};
		
		this.column = {
				keys : initialData.column.keys
			};
		
		this.panel = {
			top: undefined,
			side: undefined
		};
		
	};
	
});