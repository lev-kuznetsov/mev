define(['extend', "lodash"], function(extend, _){

	//Constructor :: Object -> $Function [View]
    //  Constructor function for new view that takes Object of type dataset and
    //  optional params object

    function getStats(params){
        var stats = {
            min: Infinity,
            max: -Infinity,
            sum: 0,
            count: 0
        };
        _.transform(params.labels.column.keys, function(result, column, index){
            _.transform(params.labels.row.keys, function(result, row, index){
                var value = params.dataset.expression.tryGet([row, column]);
                if(!isNaN(value)){               
	                if(value < result.min) result.min = value;
	                if(value > result.max) result.max = value;
	                result.sum += value;
	                result.count++;
                }
            }, result);
        }, stats);          
        stats.avg = stats.sum / stats.count;
        return stats;
    }

	return function(params,dataset){

		var self = this;
		self.dataset=dataset;
		this.viewType = 'heatmapView';
		
		this.index2labels = function(indexPair){
		    return [  self.row[indexPair[0]]  ,  self.column[indexPair[1]]  ];
		};
		
		this.id = Math.random().toString(36).substring(7);
		this.applyFilter=function(dimension, keys){
			if(!_.isEqual(self.labels[dimension].keys, keys)){
				self.labels[dimension].keys = keys;
				var stats = getStats(this);
				_.assign(self.expression, stats);
			}			
			return extend({}, self);
		};
		
		extend(self, params);

	};
	
});