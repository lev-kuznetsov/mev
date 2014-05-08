define(['extend'], function(extend){

	//Constructor :: Object -> $Function [View]
    //  Constructor function for new view that takes Object of type dataset and
    //  optional params object
	return function(params){

		var self = this;
		
		this.viewType = 'heatmapView';
		
		this.index2labels = function(indexPair){
		    return [  self.row[indexPair[0]]  ,  self.column[indexPair[1]]  ]
		};

		extend(self, params)

	};
	
});