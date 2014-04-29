define([], function(){

    //loadAnalyses :: !analysis !analyses --> null
    //  Function to reload analyses list with new values 
    return function(){
        var self = this;
        
        self.analyses = []
        
        self.analysis.getAll({datasetName: self.datasetName}, function(response){
            response.names.map(function(name){
                var analysis = new AnalysisClass(self.analysis.get({datasetName: self.datasetName, analysisName: name}) )
                self.analyses.push(analysis);
            });
        });

        
        
        return null
    }
    
});