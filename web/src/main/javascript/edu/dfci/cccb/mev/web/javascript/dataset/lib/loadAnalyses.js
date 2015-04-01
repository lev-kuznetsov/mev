define(['./AnalysisClass'], function(AnalysisClass){

    //loadAnalyses :: !analysis !analyses --> null
    //  Function to reload analyses list with new values 
    return function(){
        var self = this;
        
        self.analyses = [];
        
        return self.analysis.getAll({datasetName: self.datasetName}).$promise.then(function(response){
            
        	var requests = [];
        	var analyses = [];
            response.names.map(function(name){
            	
            	var request = self.analysis.get({datasetName: self.datasetName, analysisName: name},
            			function(res){
                    		var analysis = new AnalysisClass(res);
                    		var sessionStorageKey = self.datasetName+"."+name;
        					console.debug("sessionStorageKey get", sessionStorageKey);
                    		params = JSON.parse(sessionStorage.getItem(self.datasetName+"."+name));
                    		analysis.params = params;
                    		console.debug("LoadAnalysis", analysis.name, analysis);
                    		analyses.push(analysis);
                		});
            	requests.push(request.$promise);                
            });
            
            return self.$q.all(requests).then(function(response){
            	self.analyses.length=0;
            	console.debug("qall", response);
            	analyses.map(function(analysis){
            		self.analyses.push(analysis);
            	});
            	return self.analyses;
            });
            
        }).then(function(response){
        	console.debug("qall2", response);
        	self.analysisEventBus.analysisLoadedAll();
        });        
    };
    
});