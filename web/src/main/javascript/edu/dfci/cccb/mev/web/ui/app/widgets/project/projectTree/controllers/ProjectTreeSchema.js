define([], function(){
	var ProjectTreeSchema=function ProjectTreeSchema(){
		return {
			
			"dataset": {label: "Datasets"},
			"dataset\.[0\-9]+": {label: "Dataset", state: {name: ".dataset", getParams: function(node){
				return {datasetId: node.nodeData.id, node: node};
			}}},
			"dataset\.[0\-9]+\.dataref": {label: "", skip: true},
			"dataset\.[0\-9]+\.dataref\.column": {label: "Samples", 
				state: {
					name: ".dataset.samples", 
					getParams: function(node){return {};}
				}
			},
//			"dataset\.[0\-9]+\.dataref\.column\.keys": {label: "keys"},
			"dataset\.[0\-9]+\.dataref\.row": {label: "Genes/Probes", 
				state: {
					name: ".dataset.rows", 
					getParams: function(node){return {};}
				}
			},
//			"dataset\.[0\-9]+\.dataref\.row\.keys": {label: "keys"},
			
			"analysis": {label: "Analyses"},
			"analysis\.[0\-9]+": {label: "Analysis", 
				state: {
					name: ".analysis",
					getParams: function(node){
						return {analysisId: node.nodeData.id, analysisType: node.nodeData.type};
					}
				}},
//			"analysis\.[0\-9]+\.name": {label: "name"},
//			"analysis\.[0\-9]+\.type": {label: "type"},
//			"analysis\.[0\-9]+\.params": {label: "parameters"},
//			"analysis\.[0\-9]+\.params\.dataref": {label: "data"},
			"analysis\.[0\-9]+\.result": {label: "Results", skip: true},
			"analysis\.[0\-9]+\.result\.[0\-9]+": {label: "result",
				state: {
					name: ".analysis.result",
					getParams: function(node){
						return {resultId: node.nodeData.name};
					}
				}},
//			"analysis\.[0\-9]+\.result\.[0\-9]+\.type": {label: "type"},
			
//			"dashboards": {label: "Dashboards"},
//			"dashboards\.[0\-9]+": {label: "dashboard"},
			
//			"history": {label: "History"},
			
			
			
			
		};
	};
	
	return ProjectTreeSchema;
	ProjectTreeSchema.$inject=[];
});