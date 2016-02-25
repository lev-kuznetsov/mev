define(["lodash"], function(_){"use strict";
	function service(mevContext, mevAnalysisRest){
		this.start = function(AnalysisType, data, urlParams, method){		 
			if(!AnalysisType) AnalysisType = this;
			var project = mevContext.root();
			var dataParams = data || AnalysisType.params.getValues();
			if(mevContext.getLevel()==="bottom"){
				if(mevContext.getLevel()==="bottom"){
					dataParams.name = mevContext.current().name + "." + dataParams.name;
				}
			}
			if(!urlParams) urlParams = {};

			_.extend(urlParams, {
				datasetName : project.dataset.datasetName, 
				analysisType : AnalysisType.id
			});
			if(method==="put")
				urlParams.analysisName = dataParams.name;

			mevAnalysisRest[method || "post"](urlParams, dataParams);			
		};
	}	
	service.$inject=["mevContext", "mevAnalysisRest"];
	service.$name = "mevAnalysisLauncher";
	service.$provider = "service";
	return service;
});