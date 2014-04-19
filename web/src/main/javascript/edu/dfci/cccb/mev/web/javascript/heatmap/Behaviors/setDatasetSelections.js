define([], function(){
	return function(dimension){
		
		function setDatasetSelectionsOnDimension(dim){
			this.apiSelections.getAll({dimension:dim},
			function(response){
				this.selections[dim] = response.selections;
			},function(error){
				var message = "Could not retrieve dataset selections"
                     + ". If the"
                     + "problem persists, please contact us.";

                 var header = "Selections Download Problem (Error Code: "
                         + error.status
                         + ")";

                 this.alertService.error(message, header);
			});
		};
    	
    	function setDatasetSelectionsOnAll(){
    		setDatasetSelectionsOnDimension('column');
    		setDatasetSelectionsOnDimension('row');
    	}
    	
    	(dimension) ? setDatasetSelectionsOnDimension(dimension) : setDatasetSelectionsOnAll()
	}
});