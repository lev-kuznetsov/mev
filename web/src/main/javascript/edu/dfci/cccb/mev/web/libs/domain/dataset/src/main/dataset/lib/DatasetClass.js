define(['./datasetStatistics', './selectionSort', './selectionHelpers', './expressionModule', "./DatasetValues"], 
		function( datasetStatistics, selectionSort, selectionHelpers, expressionModule, DatasetValues){
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
    
    
	//Constructor :: [String], [DatasetResponseObj] -> $Function [Dataset]
    //  Function that constructs base dataset object without angular module
    //  dependent behaviors.
	return function(datasetName, datasetRespObj, $http, $rootScope){
	    
	    if (!datasetName){
	        throw new TypeError('datasetName parameter not defined');	    
	    }
	    
	    if (!datasetRespObj) {
	        throw new TypeError('datasetRespObj parameter not defined');
	    }
	    
	    var self = this;
		this.id = datasetName;
		
		this.datasetName = datasetName;

		this.column = datasetRespObj.column;
		this.row = datasetRespObj.row;
		
		this.columnLabels2Indexes = inversion.call(datasetRespObj.column.keys);
		this.rowLabels2Indexes = inversion.call(datasetRespObj.row.keys);
		
		this.column.indexOf = function(label){
		    return self.columnLabels2Indexes[label];
		};
		
		this.row.indexOf = function(label){
            return self.columnLabels2Indexes[label];
        };

		this.selections={
	        column: datasetRespObj.column.selections,
	        row: datasetRespObj.row.selections,
	        intersection: function(params){
	        	return selectionHelpers.selectionIntersect.call(self, params);
	        }
		};
		
		this.analyses = datasetRespObj.analyses || [];
		this.values = datasetRespObj.values;
		this.valueStore = new DatasetValues(this, $http, $rootScope, this);
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
			getDict: this.valueStore.getDict,
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
		this.expression.sort = selectionSort;

        //Integer expressions check
	    for (var k = 0; k < datasetRespObj.values.length; k++){	
            if (datasetRespObj.values[k].value % 1 !== 0) {
                self.expression.hasNonIntegerValues = true;
                break;
            }
        }
	    

        this.expression.retrieve = function(input){
            return expressionModule.retrieve.call(self, input);
        };


		

	};
	
});

