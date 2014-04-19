define([], function () {
//Adds method to object with dataset and view to filter
//based on rows
	
	return function(filterLabels){
		
		var indexes = filterLabels.map(function(d){
            return this.dataset.indexOf(d);
        });
        
        var cells = [];
        //get rows from cells using indexes
        indexes.map(function(index){
           //get row by slicing using index
           var row = this.dataset.cells.values
               .slice(index* this.dataset.labels.column.length, 
            		   this.dataset.labels.column.length*(1+index));
           //push rows onto cells
           row.map(function(cell){
               cells.push(cell);
           });
        });
        
        this.view.cells.values = cells;
        this.view.labels.row = filterLabels;
	};
	
});