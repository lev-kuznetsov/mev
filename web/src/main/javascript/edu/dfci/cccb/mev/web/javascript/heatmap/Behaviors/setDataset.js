define(['jquery'], function($){
	return function (){
    	//Pull original dataset from http and set the dataset
    		$('#loading').modal();
    		//http request data
    		
    		var reference = this;
    		
    		this.apiDataset.get(function(result){
    			$('#loading').modal('hide');
            //set cells
    			reference.cells.avg = result.avg;
    			reference.cells.max = result.max;
    			reference.cells.min = result.min;
    			reference.cells.values = result.values;
    			
			//set labels
				reference.labels.row = result.row.keys;
				reference.labels.column = result.column.keys;
				

	        	reference.setHeatmapView();
					
            }, function(error){
            	 var message = "Could not retrieve dataset info"
                     + ". If the"
                     + "problem persists, please contact us.";

                 var header = "Heatmap Download Problem (Error Code: "
                         + error.status
                         + ")";

                 reference.alertService.error(message, header);
                 
                 $('#loading').modal('hide');
                 
            });
    		
    	};
})