define(["lodash"], function(_){"use strict";
	function service(mevContext, mevAnalysisRest){
		this.start = function(AnalysisType, data, urlParams, method){		 
			if(!AnalysisType) AnalysisType = this;
			var project = mevContext.root();
			var dataParams = data || AnalysisType.params.getValues();

			if(!urlParams) urlParams = {};
			_.defaults(urlParams, {
				datasetName : project.dataset.datasetName, 
				analysisType : AnalysisType.id
			});
			if(method==="put")
				urlParams.analysisName = dataParams.name;

			
			if(mevContext.getLevel()==="bottom"){
				dataParams.name = mevContext.current().name + "." + urlParams.analysisType + "_" + dataParams.name;
				if(urlParams.analysisName)
					urlParams.analysisName = dataParams.name;
			}else{
				if(urlParams.analysisName && urlParams.analysisName.toLowerCase() === urlParams.analysisType.toLowerCase()){
					//do not prefix
				}else{
					dataParams.name = urlParams.analysisType + "_" + dataParams.name;
					if(urlParams.analysisName)
						urlParams.analysisName = urlParams.analysisType + "_" + urlParams.analysisName;
				}
			}						
			
			mevAnalysisRest[method || "post"](urlParams, dataParams);			
		};
	}	
	service.$inject=["mevContext", "mevAnalysisRest"];
	service.$name = "mevAnalysisLauncher";
	service.$provider = "service";
	return service;
});