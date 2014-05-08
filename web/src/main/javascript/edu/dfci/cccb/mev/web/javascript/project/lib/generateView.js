define(['./HeatmapViewClass'], function(HeatmapViewClass){

    //generateView :: !view(View), Object -> !view(View)
    //  Function to apply new view given an object of view instructions.
    return function(params){

        var self = this;
        
        if(params.viewType == 'heatmapView'){
            
            self.view = new HeatmapViewClass(params) ;
        }
        
        return null;
    };
    
});