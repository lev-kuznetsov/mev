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
            
            if (params.panel && params.panel.side && self.views.panel && self.views.panel.top){
                params.panel.top = self.views.panel.top;
                params.labels.column.keys = self.views.labels.column.keys;
                
                
            } else if (params.panel && params.panel.top && self.views.panel && self.views.panel.side) {
                params.panel.side = self.views.panel.side;
                params.labels.row.keys = self.views.labels.row.keys;
                
            }
            
            
            self.views = new HeatmapViewClass(params);
            return self.views;
        }
        
        return null;
    };
    
});