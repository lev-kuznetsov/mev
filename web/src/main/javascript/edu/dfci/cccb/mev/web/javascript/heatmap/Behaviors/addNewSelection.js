define([], function(){
	
	return function (params){
	    	//function to add new analysis to the dataset
			
			var postData = {
	        	name: params.name,
	        };
			
			//send post request for new analysis
			this.apiAnalysis.post({analysisType:params.type}, postData,
			function(response){
			//on success, reset analysis list with new analysis added
				this.setDatasetAnalysisList()
			}, function(error){
				var message = "Could not start new analysis " + params.name
				     + ". If the"
				     + "problem persists, please contact us.";
				
				var header = "Analysis start Problem (Error Code: "
				         + error.status
				         + ")";
				
				this.alertService.error(message, header);
			});
	};
	
});