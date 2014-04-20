define([], function(){
	
	return function (){
    	//reset analysis list
		//get analysis list
		var reference = this;
		this.apiAnalysis.getAll(function(response){
        	
			
            var prevList = response.names; 
        //clear stored analysis list
            reference.clearAnalysisList();
        //for each analysis
            prevList.map(function(analysisName){
            //get analysis data
            	reference.apiAnalysis.get({analysisName: analysisName},
            	function(data){
            //push analysis data to stored analysis list
            		reference.addToAnalysisList(data);
            	}, function(error){
            		var message = "Could not retrieve previous analysis " + analysisName
   	                     + ". If the problem persists, please contact us.";
   	
   	                 var header = "Analysis Download Problem (Error Code: "
   	                         + error.status
   	                         + ")";
   	
   	                 reference.alertService.error(message, header);
            	});
            	
            
            });
            
            
		}, function(error){
			var message = "Could not retrieve previous analysis list"
                 + ". If the problem persists, please contact us.";

             var header = "Analysis List Download Problem (Error Code: "
                     + error.status
                     + ")";

             reference.alertService.error(message, header);
		});
		
			
	};
	
});