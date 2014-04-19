define([], function () {
//Adds method to object with dataset and view to filter
//based on rows
	
	return function(filterLabels){
		
		var reference = this;
		
		var indexes = filterLabels.map(function(d){
            return reference.labels.row.indexOf(d);
        });
        
        var cells = [];
        //get rows from cells using indexes
        indexes.map(function(index){
           //get row by slicing using index
           var row = reference.cells.values
               .slice(index* reference.labels.column.length, 
            		   reference.labels.column.length*(1+index));
           //push rows onto cells
           row.map(function(cell){
               cells.push(cell);
           });
        });
        
        this.view.cells.values = cells;
        this.view.labels.row = filterLabels;
	};
	
});