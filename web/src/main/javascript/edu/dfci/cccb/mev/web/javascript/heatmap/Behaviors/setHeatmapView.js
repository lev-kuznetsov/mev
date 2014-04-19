define([], function () {
//Adds method to object with dataset and view to set view
//to given rows
	return function(filterLabels){
		
		if (!filterLabels) { 
			var filterLabels = this.labels.row;
		}
		
		this.view.cells.values = this.datasetViewFilter(this.cells.values,
				this.labels.row, 
				this.labels.column, filterLabels);
		
		//set rowlabels as heatmap view row labels
		this.view.labels.row = filterLabels;
		this.view.labels.column = this.labels.column;
		
	};
})