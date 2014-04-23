define([], function(){
	
	//cellFilter :: !expression.values, !row.values || Array -> Array
	//		- Uses object's expression values to filter on rows and returns matching
	//		  expression valus
	return function(labels){
	//heatmap filter used to return cell rows after given labels
		var reference = this;
		
		var indexes = labels.row.map(function(label){
			return reference.data.labels.row.indexOf(label)
		});
		
		//Quick filter to make sure nothing that wasn't there got through
		indexes = indexes.filter(function(index){
			return (index > -1) ? true : false
		});
		
        var cells = [];
        //get rows from cells using indexes
        indexes.map(function(index){
           //get row by slicing using index
           var row = reference.data.cells.values
               .slice(index* labels.column.length, 
            		   labels.column.length*(1+index));
           //push rows onto cells
           row.map(function(cell){
               cells.push(cell);
           });
        });
        
        return cells
	};
	
});