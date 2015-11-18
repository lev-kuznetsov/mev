define(['./datasetStatistics', './selectionSort', './selectionHelpers', './expressionModule', 'q', 'PouchDB', 'jsLru', 'blobUtil'], 
		function( datasetStatistics, selectionSort, selectionHelpers, expressionModule, q, PouchDB, jsLru, blobUtil){
	"use strict";    
    //inverter :: [a] --> Object
    //  Function to invert an array into an object with properties of names
    //  and values of the original index.
    function inversion(){
        
        var self = this;
        var obj = Object.create(null);
        
        self.map(function(label, index){
            obj[label] = index;
        });
        
        return obj;
    }
    
    //ranger :: Number --> Array
    //  Function to create an array of a range of numbers from 0 to Number-1 of
    //  length Number
    function ranger(n){
    	var r = [];
    	for (var i=0; i<n;i++){
    		r.push(i);
    	}
    	return r;
    }
    
    function ValueStore(ds, datasetRespObj){
    	var self = this;    	
    	this.chunkSize = 10e6;
    	this.itemsPerChunk = this.chunkSize / Float64Array.BYTES_PER_ELEMENT;
    	this.ready = false;
    	var db = new PouchDB(ds.id+"_swap");
	    var lruCache = new jsLru(5);
    	//init swap
    	init(datasetRespObj.valuesBuffer);
    	
    	
		function init(ab){
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
			console.debug("swap: chunks", chunks);
			db.get("swap")["catch"](function(e){
				if(e.status===404){
					return {_id: "swap", _attachments: chunks};
//					db.put({_id: "swap", _attachments: chunks})
//					.then(function(response){						
//						self.ready = true;
//						delete datasetRespObj.valuesBuffer;
//						delete datasetRespObj.dataview;
//						console.log('swap: successfull!', ds.id, response);
//					})["catch"](function(err){
////						console.log('swap: error put 2', err);
//						throw err;
//					});;
				}else{
					throw e;
				}
			}).then(function(swap){
				swap._attachments = chunks;
				db.put(swap)
				.then(function(response) {
					self.ready = true;
					delete datasetRespObj.valuesBuffer;
					delete datasetRespObj.dataview;
					console.log('swap: datasetName successfull!', ds.id, response);
				})["catch"](function(err){
//					console.log('swap: error put', err);
					throw err;
				});
			})
		}
			
		function chunkName(index){
			return "chunk"+index;
		}
    	
    	function getItemIndex(r, c){
        	return ds.column.keys.length * r + c;    	
        }
        function getChunkForIndex(itemIndex){        	        	
        	return Math.floor(itemIndex / self.itemsPerChunk);    	
        }
        function getChunkOffset(itemIndex){        	        	
        	return itemIndex % self.itemsPerChunk;    	
        }
        function loadChunkDataView(chunkIndex){
        	return db.getAttachment("swap", chunkName(chunkIndex))
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
        		console.debug("swap: miss ", itemIndex, chunkIndex);
        		loadChunkDataView().then(function(dataview){					
					return dataview.getFloat64(chunkOffset*Float64Array.BYTES_PER_ELEMENT, false);
				})["catch"](function (err) {
				  console.log("swap getAttachment error", err);
				});
        	}
        }
//        	getFloat64(chunkOffset*Float64Array.BYTES_PER_ELEMENT, false);
        function getByKey(labelPair){
        	
    		var r = ds.rowLabels2Indexes[labelPair[0]];
    	    var c = ds.columnLabels2Indexes[labelPair[1]];
    	    var itemIndex = getItemIndex(r, c);
    	    if(self.ready)
    	    	return q.when(getByIndex(itemIndex));
    	    else
    	    	return q.when(datasetRespObj.dataview.getFloat64((r*ds.column.keys.length+c)*Float64Array.BYTES_PER_ELEMENT, false));    		
        }
        function getSome(shownCells){
        	if(self.ready){        		
        		var chunkPromises = [];
        		var chunkIndexes = {};
        		for(var i=0; i<shownCells.length; i++){
//        		var r = ds.rowLabels2Indexes[labelPairs[i][0]];
//        	    var c = ds.columnLabels2Indexes[labelPairs[i][1]];
        			var r = ds.rowLabels2Indexes[shownCells[i].row];
        			var c = ds.columnLabels2Indexes[shownCells[i].column];
        			var index = getItemIndex(r,c);
        			shownCells[i].index = index;
        			var chunkIndex = getChunkForIndex(index);        		
        			if(!chunkIndexes[chunkIndex] && !lruCache.find(chunkIndex)){        			
        				chunkPromises.push(loadChunkDataView(chunkIndex));
        				chunkIndexes[chunkIndex]=true;
        			}
        		}
        		return q.all(chunkPromises)      		
        		.then(function(){
        			for(var i=0; i<shownCells.length; i++){
        				shownCells[i].value = getByIndex(shownCells[i].index); 
        			}
        			return shownCells;
        		})["catch"](function(e){
        			throw e;
        		});
        	}else{
        		for(var i=0; i<shownCells.length; i++){
//            		var r = ds.rowLabels2Indexes[labelPairs[i][0]];
//            	    var c = ds.columnLabels2Indexes[labelPairs[i][1]];
        			var r = ds.rowLabels2Indexes[shownCells[i].row];
        			var c = ds.columnLabels2Indexes[shownCells[i].column];
        			var index = getItemIndex(r,c);
        			shownCells[i].index = index;
        			shownCells[i].value = datasetRespObj.dataview.getFloat64((r*ds.column.keys.length+c)*Float64Array.BYTES_PER_ELEMENT, false)
        		}
        		return q.when(shownCells);
        	}
        	
        }
        return {        	
        	getByKey: getByKey,  
        	getSome: getSome,
        };
    };
	//Constructor :: [String], [DatasetResponseObj] -> $Function [Dataset]
    //  Function that constructs base dataset object without angular module
    //  dependent behaviors.
	return function(datasetName, datasetRespObj){
	    
	    if (!datasetName){
	        throw TypeError('datasetName parameter not defined');
	        return null
	    }
	    
	    if (!datasetRespObj) {
	        throw TypeError('datasetRespObj parameter not defined')
	        return null
	    }
	    
	    var self = this;
		this.id = datasetName;
		
//		db.initLru(5000000); 
		this.valueStore = new ValueStore(self, datasetRespObj);   		
		this.datasetName = datasetName;
		this.expression = {
			values: datasetRespObj.values,
			data: {
				getRow: function(index){
					var row=[];
					for(var c=0; c<self.column.keys.length; c++){
						row.push({
							value: datasetRespObj.dataview.getFloat64((index+c)*Float64Array.BYTES_PER_ELEMENT, false),
							row: self.row.keys[index],
							column: self.column.keys[c]
						});						
					}
					return row;
				}
			},
			max: datasetRespObj.max,
			min: datasetRespObj.min,
			avg: datasetRespObj.avg,
			tryGet: this.valueStore.getByKey,
			getSome: this.valueStore.getSome,
//			tryGet: function(labelPair){
//				var deferred = q.defer();				
//				setTimeout(function(){
//					var r = self.rowLabels2Indexes[labelPair[0]];
//				    var c = self.columnLabels2Indexes[labelPair[1]];
//					var value = datasetRespObj.dataview.getFloat64((r*self.column.keys.length+c)*Float64Array.BYTES_PER_ELEMENT, false);
//					deferred.resolve(value);
//				}, 500);				
//				return deferred.promise;
//			},
			get: function(labelPair){
			    
			    var r = self.rowLabels2Indexes[labelPair[0]];
			    var c = self.columnLabels2Indexes[labelPair[1]];
			    
			    return {
			    	value: datasetRespObj.dataview.getFloat64((r*self.column.keys.length+c)*Float64Array.BYTES_PER_ELEMENT, false),
			    	row: labelPair[0],
			    	column: labelPair[1]
			    };
//			    return this.values[(r * self.column.keys.length) + c]
			},
			statistics : datasetStatistics,
			ranger : ranger
		};

        //Integer expressions check
	    for (var k = 0; k < datasetRespObj.values.length; k++){	
            if (datasetRespObj.values[k].value % 1 != 0) {
                self.expression.hasNonIntegerValues = true 
                break
            }
        }
	    

        this.expression.retrieve = function(input){
            return expressionModule.retrieve.call(self, input)
        } 


		this.expression.sort = selectionSort;

		this.column = datasetRespObj.column;
		this.row = datasetRespObj.row;
		
		this.columnLabels2Indexes = inversion.call(datasetRespObj.column.keys);
		this.rowLabels2Indexes = inversion.call(datasetRespObj.row.keys);
		
		this.column.indexOf = function(label){
		    return self.columnLabels2Indexes[label]
		};
		
		this.row.indexOf = function(label){
            return self.columnLabels2Indexes[label]
        };

		this.selections={
	        column: datasetRespObj.column.selections,
	        row: datasetRespObj.row.selections,
	        intersection: function(params){
	        	return selectionHelpers.selectionIntersect.call(self, params)
	        }
		}
		
		this.analyses = [];

	};
	
});

