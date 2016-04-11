define(['q'], function(q){
	return function ValueStore(dataset, $http, $rootScope){
    	var self = this;    	
    	self.values = dataset.values;

        function getItemIndex(r, c){
            return dataset.column.keys.length * r + c;      
        }        

        function keyToIndex(row, column){
            var r = dataset.rowLabels2Indexes[row];
            var c = dataset.columnLabels2Indexes[column];
            return getItemIndex(r, c);
        }
        function getByIndex(index){            
            return self.values[index].value;            
        }
        function getByKey(labelPair){        	
        	var index = keyToIndex(labelPair[0], labelPair[1]);            
            return getByIndex(index);
        }
        
        function getSome(shownCells){
		     			
			for(var i=0; i<shownCells.length; i++){    				
				var index = keyToIndex(shownCells[i].row, shownCells[i].column);
				shownCells[i].index = index;
				shownCells[i].value = getByIndex(index);
			}
    		
    		return q.when(shownCells);        	
        }
        
        function getDict(shownCells){        	
        	var dict = {};    		
			for(var i=0; i<shownCells.length; i++){
				
				var rowName = shownCells[i].row;
				var columnName = shownCells[i].column;				
				if(!dict[rowName]){    					
					dict[rowName] = {};
				}
				if(!dict[rowName][columnName]){    					
					dict[rowName][columnName] = {
						value: getByKey([rowName, columnName])
					};
				}   				
			}
			return dict;
        }
        
        return {        	
        	getByKey: getByKey, 
        	getSome: getSome,
        	getDict: getDict
        };
    };
});