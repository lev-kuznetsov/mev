define(['q'], function(q){
	return function ValueStore(dataset, $http, $rootScope){
    	var self = this;    	
    	//init swap
    	var dataPromise = init();
    	function init(){	
			return fetchDataValues();
		}
		
    	function fetchDataValues(){   
            return q.when({}); 		
    		var valuesPromise = $http.get('/dataset/'+dataset.id+'/data/values', {params: {format: "binary"}, responseType: "arraybuffer", headers: {"Accept": "application/octet-stream"}})
			.then(function(values){
				var ab = values.data;     				
 				var dataview = new DataView(ab);
 				console.debug("swap: array", ab.byteLength);				   	      				
 				dataset.valuesBuffer = ab;
 				dataset.dataview = dataview; 		
 				$rootScope.$broadcast("mui:model:dataset:values:loaded");
 				return ab;
 			});
			return valuesPromise;
    	}
    	
    	function getItemIndex(r, c){
        	return dataset.column.keys.length * r + c;    	
        }        
        function keyToIndex(row, column){
    		var r = dataset.rowLabels2Indexes[row];
    	    var c = dataset.columnLabels2Indexes[column];
    	    return getItemIndex(r, c);
        }
    	function getByIndex(index){
    		if(dataset.dataview){
    			return dataset.dataview.getFloat32((index)*Float32Array.BYTES_PER_ELEMENT, false);
    		}
    	}
        function getByKey(labelPair){        	
        	if(dataset.dataview){        		
        		var index = keyToIndex(labelPair[0], labelPair[1]);    	    
        		return getByIndex(index);        	
        	}
        }
        
        function getSome(shownCells){
    		if(dataset.dataview){        			
    			for(var i=0; i<shownCells.length; i++){    				
    				var index = keyToIndex(shownCells[i].row, shownCells[i].column);
    				shownCells[i].index = index;
    				shownCells[i].value = getByIndex(index);
    			}
    		}
    		return q.when(shownCells);        	
        }
        
        function getDict(shownCells){        	
        	
			return dataPromise.then(function(){
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
				};
				return dict;
			});
    		
        }
        
        return {        	
        	getByKey: getByKey,  
        	getSome: getSome,
        	getDict: getDict
        };
    };
});