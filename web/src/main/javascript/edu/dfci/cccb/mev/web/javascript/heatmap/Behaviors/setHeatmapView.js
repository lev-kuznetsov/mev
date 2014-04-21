define([], function () {
//Adds method to object with dataset and view to set view
//to given rows
	return function(filterLabels){
		
		var reference = this;
		var filt = (filterLabels)? filterLabels : this.labels.row
		
		this.datasetViewFilter({row:filt, column:reference.labels.column});
		
	};
})