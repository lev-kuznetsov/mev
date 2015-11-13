define([], function(){
	
	//cellFilter :: !expression.values, !row.values, !column.values || Array -> Array
	//		- Uses object's expression values to filter on rows and returns matching
	//		  expression valus
	
	return function(labels){

		var self = this;

		var indexes = labels.map(function(label){
			return self.row.keys.indexOf(label)
		});
		
		//Quick filter to make sure nothing that wasn't there got through
		indexes = indexes.filter(function(index){
			return (index > -1) ? true : false
		});
		
        var cells = [];
        
        //get rows from cells using indexes
        indexes.map(function(index){
           //get row by slicing using index
//           var row = self.expression.values
//               .slice(index* self.column.keys.length, 
//            		   self.column.keys.length*(1+index));
           var row = self.expression.dataview.getRow(index);
           //push rows onto cells
           row.map(function(cell){
               cells.push(cell);
           });
        });
        
        return cells
	};
	
});