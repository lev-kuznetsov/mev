define([], function(){
   
    //resetSelections :: String, !setSelections, !selection --> null
    return function(dimension){
    //  Takes dimension object and makes call to get all selections, and sets
    //  selections
        
        var self = this;
        
        self.selection.getAll({
            'datasetName': self.datasetName, 
            'dimension':dimension}, function(response){

                self.setSelections(dimension, response.selections)
                
        }, function(error){
            return
        })
        
        
    };
    
});
