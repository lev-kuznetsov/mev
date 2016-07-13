define(['lodash', 'q', 'jsLru'], function(_, q, jsLru){
	return function ValueStore(dataset, source, $rootScope, mevSettings){
    	var self = this;    	
    	var lruCache = new jsLru(5);
    	//init swap
    	var dataPromise = init();
    	function init(){	
			return fetchDataValues();
		}
		
    	function fetchDataValues(){    		
    		var valuesPromise = source.get()
			.then(function(values){
				var ab = values.data;     				
 				var dataview = new DataView(ab);
 				console.debug("swap: array", ab.byteLength);				   	      				
 				dataset.valuesBuffer = ab;
 				dataset.dataview = dataview; 		
 				$rootScope.$broadcast("mui:model:dataset:values:loaded");
 				return ab;
 			})["catch"](function(e){
				throw e;
			});
			if(mevSettings.db.enabled)
				fetchDataValues64();
			return valuesPromise;
    	}

		function fetchDataValues64(){
			var deferred = q.defer();
			var worker = new Worker('/container/javascript/dataset/lib/DatasetValuesWorker.js');
			worker.postMessage({id: dataset.id});
			worker.onmessage = function(e) {
				console.debug("worker done", e)
				self.ready=true;
				deferred.resolve(e);
			};
			return deferred.promise;
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
        
        return _.assign(this, {
        	getByKey: getByKey,
        	getSome: getSome,
        	getDict: getDict
        });
    };
});