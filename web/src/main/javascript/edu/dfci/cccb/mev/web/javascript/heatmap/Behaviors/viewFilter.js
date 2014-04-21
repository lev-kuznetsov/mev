define(['heatmap/Behaviors/cellFilter'], function (cellFilter) {
//Adds method to object with dataset and view to filter
//cells based on filterlLabels 
	
	return function(filterLabels){
		
		var reference = this;
		
		var indexes = filterLabels.row.map(function(d){
            return reference.view.labels.row.indexOf(d);
        });
		
		indexes = indexes.filter(function(index){
			return (index > -1) ? true : false;
		})
		
        var cells = cellFilter.call(this, {row:indexes, column:this.view.labels.column}, this.view.cells);
        
        return cells;
	};
	
});