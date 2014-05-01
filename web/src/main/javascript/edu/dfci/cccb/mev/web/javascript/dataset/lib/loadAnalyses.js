define(['./AnalysisClass'], function(AnalysisClass){

    //loadAnalyses :: !analysis !analyses --> null
    //  Function to reload analyses list with new values 
    return function(){
        var self = this;
        
        self.analyses = []
        
        self.analysis.getAll({datasetName: self.datasetName}, function(response){
            
            response.names.map(function(name){
                
                self.analysis.get({datasetName: self.datasetName, analysisName: name},
                function(res){
                    var analysis = new AnalysisClass(res)
                    self.analyses.push(analysis);
                });
                
            });
        });

        
        
        return null
    }
    
});