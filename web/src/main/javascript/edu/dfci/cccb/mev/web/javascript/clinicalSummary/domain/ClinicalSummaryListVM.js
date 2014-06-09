define([], function(){
	var ClinicalSummaryListVM = function(summaries){
		//private members
		var _self=this;
		
		function SplitArrayIntoRows(array, columns) {
	        if (array.length <= columns) {
	            return [array];
	        };

	        var rowsNum = Math.ceil(array.length / columns);

	        var rowsArray = new Array(rowsNum);

	        for (var i = 0; i < rowsNum; i++) {
	            var columnsArray = new Array(columns);
	            for (var j = 0; j < columns; j++) {
	                var index = i * columns + j;

	                if (index < array.length) {
	                    columnsArray[j] = array[index];
	                } else {
	                    break;
	                }
	            }
	            rowsArray[i] = columnsArray;
	        }
	        return rowsArray;
	    };

	    function SplitArrayIntoColumns(array, columns) {
	        if (array.length <= columns) {
	            return array;
	        };

	        var rowsNum = Math.ceil(array.length / columns);

	        var columnsArray = new Array(columns);
	        for (var j = 0; j < columns; j++) {	            
	            var rowsArray = new Array(rowsNum);
	            for (var i = 0; i < rowsNum; i++) {    
	                var index = j * rowsNum + i;

	                if (index < array.length) {
	                	rowsArray[i] = array[index];
	                } else {
	                    break;
	                }
	            }
	            columnsArray[j] = rowsArray;
	        }
	        console.debug("SplitArrayIntoColumns", columnsArray);
	        return columnsArray;
	    };
	    var _summaries = SplitArrayIntoColumns(summaries, 2); 
		//public method		
		this.getSummaries=function(){						
			return _summaries;
		};
		this.getTitle=function(summary){
			return summary.getTitle();
		};
		
	};
	return ClinicalSummaryListVM;
});