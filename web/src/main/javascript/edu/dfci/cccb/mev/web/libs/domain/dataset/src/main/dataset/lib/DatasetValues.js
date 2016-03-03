define(['q', 'pouchdb', 'jsLru', 'blobUtil'], function(q, PouchDB, jsLru, blobUtil){
	return function ValueStore(dataset, $http, $rootScope){
    	var self = this;    	
    	this.chunkSize = 10e6;
    	this.itemsPerChunk = this.chunkSize / Float64Array.BYTES_PER_ELEMENT;
    	this.ready = false;
    	var db = new PouchDB(dataset.id);
	    var lruCache = new jsLru(5);
    	//init swap
    	init();
    	function fetchDataValues(){
    		var deferred = q.defer();
    		var worker = new Worker('/container/javascript/dataset/lib/DatasetValuesWorker.js');
    		worker.postMessage({id: dataset.id});
    		worker.onmessage = function(e) {
    		  console.debug("worker done", e)
    		  self.ready=true;    		  
    		  deferred.resolve(e);
    		};
    		return deferred.promise;
//    		var valuesPromise = $http.get('/dataset/'+dataset.id+'/data/values', {params: {format: "binary"}, responseType: "arraybuffer", headers: {"Accept": "application/octet-stream"}})
//			.then(function(values){
//				var ab = values.data;     				
// 				var dataview = new DataView(ab);
// 				console.debug("swap: array", ab.byteLength);				   	      				
// 				dataset.valuesBuffer = ab;
// 				dataset.dataview = dataview;
// 				
// 				return ab;
// 			});
//			return valuesPromise;
    	}
    	
    	function chunkDataValues(ab){
    		var chunks = {};
			for (var i = 0, size = 0; size < ab.byteLength; size += self.chunkSize, i++){
//				promise = db.lru.put("values"+i, new Blob([ab.slice(size, size + chunkSize)]), "application/octet-binary");
				chunks[chunkName(i)]={
					type : "application/octet-stream",
					data : new Blob([ab.slice(size, size + self.chunkSize)]),
					content_type : "application/octet-stream"
				};
				console.debug("chunk", i);
			};
			return chunks;
    	}
    	
    	function saveDataValues(chunks){
//    		console.debug("swap: chunks", chunks);
    		var doc = {_id: "values", _attachments: chunks};
    		return db.put(doc)
			.then()["catch"](function(err){
//				console.log('swap: error put', err);
				throw err;
			});
    	}
    	
    	function ready(response) {
			self.ready = true;
			delete dataset.valuesBuffer;
			delete dataset.dataview;
			console.log('swap: datasetName successfull!', dataset.id, response);
			$rootScope.$broadcast("mui:model:dataset:values:loaded");
			return response;
		}
    	
    	
		function init(){
		
			db.get("values")
			["catch"](function(e){
				if(e.status===404){
					return fetchDataValues();
//						.then(chunkDataValues)
//						.then(saveDataValues);					
				}else{
					throw e;
				}
			}).then(ready);
		}
			
		function chunkName(index){
			return "chunk"+index;
		}
    	
    	function getItemIndex(r, c){
        	return dataset.column.keys.length * r + c;    	
        }
        function getChunkForIndex(itemIndex){        	        	
        	return Math.floor(itemIndex / self.itemsPerChunk);    	
        }
        function getChunkOffset(itemIndex){        	        	
        	return itemIndex % self.itemsPerChunk;    	
        }
        function loadChunkDataView(chunkIndex){
        	return db.getAttachment("values", chunkName(chunkIndex))
    		.then(function(blob){
    			return blobUtil
    			.blobToArrayBuffer(blob)
    			.then(function(arrayBuff){
			    	var ab = arrayBuff;		    	
			    	var dataview = new DataView(ab);
			    	if(!lruCache.find(chunkIndex))
						lruCache.put(chunkIndex, dataview);
			    	return dataview;
				})["catch"](function(e){
					throw e;
				});
			})["catch"](function(e){
				throw e;
			});
        }
        function getByIndex(itemIndex){
        	
        	var chunkIndex = getChunkForIndex(itemIndex);
        	var chunkOffset = getChunkOffset(itemIndex);        	
        	var dataview = lruCache.get(chunkIndex);        	        	
        	if(dataview){
        		return dataview.getFloat64(chunkOffset*Float64Array.BYTES_PER_ELEMENT, false);
        	}else{        		
//        		console.debug("swap: miss ", itemIndex, chunkIndex);
        		loadChunkDataView().then(function(dataview){					
					return dataview.getFloat64(chunkOffset*Float64Array.BYTES_PER_ELEMENT, false);
				})["catch"](function (err) {
				  console.log("swap getAttachment error", err);
				});
        	}
        }
//        	getFloat64(chunkOffset*Float64Array.BYTES_PER_ELEMENT, false);
        function getByKey(labelPair){
        	
    		var r = dataset.rowLabels2Indexes[labelPair[0]];
    	    var c = dataset.columnLabels2Indexes[labelPair[1]];
    	    var itemIndex = getItemIndex(r, c);
    	    if(self.ready)
    	    	return q.when(getByIndex(itemIndex));
    	    else
    	    	return q.when(dataset.dataview.getFloat64((r*dataset.column.keys.length+c)*Float64Array.BYTES_PER_ELEMENT, false));    		
        }
        
        function prefetchChunks(shownCells){
        	var chunkPromises = [];
    		var chunkIndexes = {};
    		for(var i=0; i<shownCells.length; i++){
//    		var r = dataset.rowLabels2Indexes[labelPairs[i][0]];
//    	    var c = dataset.columnLabels2Indexes[labelPairs[i][1]];
    			var r = dataset.rowLabels2Indexes[shownCells[i].row];
    			var c = dataset.columnLabels2Indexes[shownCells[i].column];
    			var index = getItemIndex(r,c);
    			shownCells[i].index = index;
    			var chunkIndex = getChunkForIndex(index);        		
    			if(!chunkIndexes[chunkIndex] && !lruCache.find(chunkIndex)){        			
    				chunkPromises.push(loadChunkDataView(chunkIndex));
    				chunkIndexes[chunkIndex]=true;
    			}
    		}
    		return chunkPromises;
        }
        
        function getSome(shownCells){
        	if(self.ready){        		
        		var chunkPromises = prefetchChunks(shownCells);
        		return q.all(chunkPromises)      		
        		.then(function(){
        			for(var i=0; i<shownCells.length; i++){
        				shownCells[i].value = getByIndex(shownCells[i].index); 
        			}
        			return shownCells;
        		})["catch"](function(e){
        			console.error("ERROR", e);	            	
	            });
        	}else{
        		if(dataset.dataview){        			
        			for(var i=0; i<shownCells.length; i++){
//            		var r = dataset.rowLabels2Indexes[labelPairs[i][0]];
//            	    var c = dataset.columnLabels2Indexes[labelPairs[i][1]];
        				var r = dataset.rowLabels2Indexes[shownCells[i].row];
        				var c = dataset.columnLabels2Indexes[shownCells[i].column];
        				var index = getItemIndex(r,c);
        				shownCells[i].index = index;
        				shownCells[i].value = dataset.dataview.getFloat64((r*dataset.column.keys.length+c)*Float64Array.BYTES_PER_ELEMENT, false);
        			}
        		}
        		return q.when(shownCells);
        	}
        	
        }
        
        function getDict(shownCells){
        	var chunkPromises = prefetchChunks(shownCells);
        	var dict = {};
    		return q.all(chunkPromises)      		
    		.then(function(){
    			for(var i=0; i<shownCells.length; i++){
    				shownCells[i].value = getByIndex(shownCells[i].index);
    				var rowName = shownCells[i].row;
    				var columnName = shownCells[i].column;    				
    				if(!dict[rowName]){    					
    					dict[rowName] = {};
    				}
    				if(!dict[rowName][columnName]){    					
    					dict[rowName][columnName] = {
    						value: shownCells[i].value
    					};
    				}    				
    			}
    			return dict;
    		})["catch"](function(e){
    			throw e;
    		});
        }
        
        return {        	
        	getByKey: getByKey,  
        	getSome: getSome,
        	getDict: getDict
        };
    };
});