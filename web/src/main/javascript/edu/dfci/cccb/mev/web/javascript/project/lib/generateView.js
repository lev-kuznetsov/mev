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
            
            if (params.panel && params.panel.side && self.view.panel && self.view.panel.top){
                params.panel.top = self.view.panel.top;
                params.labels.column.keys = self.view.labels.column.keys;
                
                
            } else if (params.panel && params.panel.top && self.view.panel && self.view.panel.side) {
                params.panel.side = self.view.panel.side;
                params.labels.row.keys = self.view.labels.row.keys;
                
            }
            
            self.view = new HeatmapViewClass(params) ;
        }
        
        return null;
    };
    
});