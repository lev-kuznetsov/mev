"use strict";
define([], function(){
	function service(mevContext, mevAnalysisRest){
		this.start = function(AnalysisType){		 
			var project = mevContext.root();
			mevAnalysisRest.post({
        				datasetName : project.dataset.datasetName, 
        				analysisType : AnalysisType.id
        			}, AnalysisType.params.getValues());
		};
	}	
	service.$inject=["mevContext", "mevAnalysisRest"];
	service.$name = "mevAnalysisLauncher";
	service.$provider = "service";
	return service;
});