define(['lodash'], function(_){

	//Constructor :: Object -> $Function [View]
    //  Constructor function for new view that takes Object of type dataset and
    //  optional params object
	return function(params){

		var self = this;
		
		this.viewType = 'heatmapView';
		
		this.index2labels = function(indexPair){
		    return [  self.row[indexPair[0]]  ,  self.column[indexPair[1]]  ];
		};
		
		this.id = Math.random().toString(36).substring(7);
		this.applyFilter=function(dimension, keys){
			self.labels[dimension].keys = keys;
			return _.extend({}, self);
		};
		
		_.extend(self, params);

	};
	
});