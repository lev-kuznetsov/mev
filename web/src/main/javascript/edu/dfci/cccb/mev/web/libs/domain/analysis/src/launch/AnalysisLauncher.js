"use strict";
define([], function(){
	function service(mevContext, mevAnalysisRest){
		this.start = function(AnalysisType, params){		 
			var project = mevContext.root();
			mevAnalysisRest.post({
        				datasetName : project.dataset.datasetName, 
        				analysisType : AnalysisType.id
        			}, params || AnalysisType.params.getValues());
		};
	}	
	service.$inject=["mevContext", "mevAnalysisRest"];
	service.$name = "mevAnalysisLauncher";
	service.$provider = "service";
	return service;
});