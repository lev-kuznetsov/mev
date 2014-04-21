define([], function(){
	return function(indexes, origCells){
		
		var reference = this;
		
		indexes.row = indexes.row.filter(function(index){
			return (index > -1) ? true : false;
		})
        
        var cells = [];
        //get rows from cells using indexes
        indexes.row.map(function(index){
           //get row by slicing using index
           var row = origCells.values
               .slice(index* indexes.column.length, 
            		   indexes.column.length*(1+index));
           //push rows onto cells
           row.map(function(cell){
               cells.push(cell);
           });
        });
        
        return cells
	};
	
});