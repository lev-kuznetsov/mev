define([], function(){
	
	return function (){
    	//reset analysis list
		//get analysis list
		this.apiAnalysis.getAll(function(response){
        	
			
            var prevList = response.names; 
        //clear stored analysis list
            this.clearAnalysisList();
        //for each analysis
            prevList.map(function(analysisName){
            //get analysis data
            	this.apiAnalysis.get({analysisName: analysisName},
            	function(data){
            //push analysis data to stored analysis list
            		this.addToAnalysisList(data);
            	}, function(error){
            		var message = "Could not retrieve previous analysis " + analysisName
   	                     + ". If the problem persists, please contact us.";
   	
   	                 var header = "Analysis Download Problem (Error Code: "
   	                         + error.status
   	                         + ")";
   	
   	                 this.alertService.error(message, header);
            	});
            	
            
            });
            
            
		}, function(error){
			var message = "Could not retrieve previous analysis list"
                 + ". If the problem persists, please contact us.";

             var header = "Analysis List Download Problem (Error Code: "
                     + error.status
                     + ")";

             this.alertService.error(message, header);
		});
		
			
	};
	
});