define([], function(){
	return function(rowLabels){
	//Updates view given row labels
		//set up reference
		var reference = this;
		

		//filter to make sure given labels are in the data
		var cleanLabels = (rowLabels) ? rowLabels.filter(function(label){
			
			return (reference.data.labels.row.indexOf(label) > -1 ) ? true : false;
		}) : reference.data.labels.row ;
		
		//if given labels to filter on, use, else, filter on data
		reference.view.cells.values =
				reference.cellFilter({row:cleanLabels, column:reference.data.labels.column});
				
		reference.view.labels.row.values = cleanLabels;
		reference.view.labels.column.values = reference.data.labels.column;
	}
});