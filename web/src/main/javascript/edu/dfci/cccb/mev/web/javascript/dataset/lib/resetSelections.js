define([], function(){
   
    //resetSelections :: String, !setSelections, !selection --> null
    return function(dimension){
    //  Takes optional dimension string and makes call to get all selections, and sets
    //  selections
        
        var self = this;
        
        if(dimension){
            
            return self.selection.getAll({
                'datasetName': self.datasetName, 
                'dimension':dimension}, function(response){

                    self.setSelections(dimension, response.selections);
                    return response.selections;
                    
            });
            
            
        } else {
            
            var row = self.selection.getAll({
                'datasetName': self.datasetName, 
                'dimension':'row'}, function(response){

                    self.setSelections('row', response.selections);
                    return response.selections;
                    
            });
            
            
            return row.$promnise.then(function(){
            	self.selection.getAll({
                    'datasetName': self.datasetName, 
                    'dimension':'column'}, function(response){
                    	
                        self.setSelections('column', response.selections);
                        return response.selections;
                        
                });
            });
            
            
        };
        
        
        
        
    };
    
});
