define(['./datasetStatistics', './selectionSort'], 
		function( datasetStatistics, selectionSort){
    
    //inverter :: [a] --> Object
    //  Function to invert an array into an object with properties of names
    //  and values of the original index.
    function inversion(){
        
        var self = this;
        var obj = Object.create(null);
        
        self.map(function(label, index){
            obj[label] = index;
        })
        
        return obj;
    }
    
    //ranger :: Number --> Array
    //  Function to create an array of a range of numbers from 0 to Number-1 of
    //  length Number
    function ranger(n){
    	var r = [];
    	for (var i=0; i<n;i++){
    		r.push(i)
    	}
    	return r;
    }

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
		
		this.datasetName = datasetName;
		this.expression = {
			values: datasetRespObj.values,
			max: datasetRespObj.max,
			min: datasetRespObj.min,
			avg: datasetRespObj.avg,
			get: function(labelPair){
			    
			    var r = self.rowLabels2Indexes[labelPair[0]];
			    var c = self.columnLabels2Indexes[labelPair[1]];
			    
			    
			    return this.values[(r * self.column.keys.length) + c]
			},
			statistics : datasetStatistics,
			ranger : ranger
		};

		
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
		        row: datasetRespObj.row.selections
		}
		
		this.analyses = [];

	};
	
});

