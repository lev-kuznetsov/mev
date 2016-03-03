define(['./HeatmapViewClass'], function(HeatmapViewClass){

    //generateView :: !view(View), Object -> !view(View)
    //  Function to apply new view given an object of view instructions.
    return function(params){

        var self = this;
        
        if(params.viewType == 'heatmapView'){
            
            if (params.panel){
                params.selectionParams = {
                        row : {
                            
                            name : undefined,
                            color : '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16),
                            labels : []
                        },
                        column : {
                            
                            name : undefined,
                            color : '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16),
                            labels : []
                        }
                    
                    };
            }
            
            if(!params.expression){
            	params.expression = {
	                min: self.dataset.expression.min,
	                max: self.dataset.expression.max,
	                avg: self.dataset.expression.avg,
	            };
            }
            
            if(params.labels && !params.labels.column){
            	params.labels.column = {keys: self.dataset.column.keys};
            }
            if(params.labels && !params.labels.row){
            	params.labels.row = {keys: self.dataset.row.keys};
            }
            
            //old functionality would show both analysis on the same heatmap if their display 
            //did not conflict with each other (ex: row clustering and column clustering)
            //now you have to specifically send the "merge" flag to activate this begavior
            if(params.merge){
	            if (params.panel && params.panel.side && self.views.panel && self.views.panel.top){
	                params.panel.top = self.views.panel.top;
	                params.labels.column.keys = self.views.labels.column.keys;                
	            } else if (params.panel && params.panel.top && self.views.panel && self.views.panel.side) {
	                params.panel.side = self.views.panel.side;
	                params.labels.row.keys = self.views.labels.row.keys;   
	            }
        	}
            if(self.views.viewType && //this is an update to an existing view
            		_.isEqual(self.views.expression, params.expression) && //expression values are not beind updated
            		_.isEqual(self.views.labels.column.keys, params.labels.column.keys) && //column order is the same 
            		_.isEqual(self.views.labels.row.keys, params.labels.row.keys) &&
                    _.isEqual(self.views.note, params.note)){ //row order is the same                    
            	return self.views; //-> no need to generate a new view
            }
            
            function getStats(params){
                var stats = {
                    min: Infinity,
                    max: -Infinity,
                    sum: 0,
                    count: 0
                };
                _.transform(params.labels.column.keys, function(result, column, index){
                    _.transform(params.labels.row.keys, function(result, row, index){
                        var value = self.dataset.expression.tryGet([row, column]);
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

            if(params.labels.column.keys.length < self.dataset.column.keys.length || params.labels.row.keys.length < self.dataset.row.keys.length){
                var stats = getStats(params);
                _.assign(params.expression, stats);
            }
            
            self.views = new HeatmapViewClass(params, self.dataset);
            return self.views;
        }
        
        return null;
    };
    
});