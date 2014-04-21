define(['heatmap/Behaviors/cellFilter'], function (cellFilter) {
//Adds method to object with dataset and view to filter
//cells and set the view to the filtered cells based on rows
	
	return function(filterLabels){
		
		var reference = this;
		
		var indexes = filterLabels.row.map(function(d){
            return reference.labels.row.indexOf(d);
        });
		
		indexes = indexes.filter(function(index){
			return (index > -1) ? true : false;
		})
		
        var cells = cellFilter.call(this, {row:indexes, column:this.labels.column}, this.cells);
        
        this.view.cells.values = cells;
        this.view.cells.avg = this.cells.avg;
        this.view.cells.min = this.cells.min;
        this.view.cells.max = this.cells.max;
        
        this.view.labels.row = filterLabels.row;
        this.view.labels.column = filterLabels.column;
        
	};
	
});