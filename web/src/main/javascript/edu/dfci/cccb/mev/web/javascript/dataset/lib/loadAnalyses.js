define(['./AnalysisClass'], function(AnalysisClass){

    //loadAnalyses :: !analysis !analyses --> null
    //  Function to reload analyses list with new values 
    return function(){
        var self = this;
        
        self.analyses = [];
        
        return self.analysis.getAll({datasetName: self.datasetName}).$promise.then(function(response){
            
        	var requests = [];
            response.names.map(function(name){
            	
            	var request = self.analysis.get({datasetName: self.datasetName, analysisName: name},
            			function(res){
                    		var analysis = new AnalysisClass(res);
                    		self.analyses.push(analysis);
                		});
            	requests.push(request.$promise);                
            });
            
            return self.$q.all(requests);
            
        }).then(function(){
        	self.analysisEventBus.analysisLoadedAll();
        });        
    };
    
});