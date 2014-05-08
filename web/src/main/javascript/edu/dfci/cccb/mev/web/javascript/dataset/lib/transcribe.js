define(['./cellFilter'], function(cellFilter) {
	
	
	//transcribe :: !properties(Dataset), [Labels] -> [Dataset]
	//	Used to filter dataset's current rows and generate a new offspring dataset.
    //  Is an intermediary taking a dataset and creating a smaller dataset object that
    //  generateView can use.
	return function(labels){
		
		var self = this;
		
		var offspring = Object.create(self);
		
		var newCells = cellFilter.call(self, labels.row);
		
		offspring.expression.values = newCells,

		offspring.row.keys = labels.row;
		
		offspring.column.keys = labels.column;

		return offspring;
	};
	
	
});