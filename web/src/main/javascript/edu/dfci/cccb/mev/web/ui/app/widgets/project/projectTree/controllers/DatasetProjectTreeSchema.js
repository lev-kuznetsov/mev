define(["lodash"], function(_){
	var DatasetProjectTreeSchema=function DatasetProjectTreeSchema(){
	function getInheritedParams(defaults, node){
		var params = defaults || {};
		if(node.nodeParent && node.nodeParent.nodeConfig){
			_.extend(params, node.nodeParent.nodeConfig.state.getParams(node.nodeParent));
		}						
		return params;
	}
	var stateBase = {
		getParams: function(node){
			return {};
		}
	}	
	
	return {
							
			"dataset": {
				label: "Dataset", 
				state: {
					name: ".dataset.home", 
					getParams: function(node){
						return {datasetId: node.nodeData.id, node: node};
					}
				}
			},
			"dataset.column": {
				label: "Sample Sets", 
				state: {
					name: ".dataset.columnSets", 
					getParams: function(node){
						return {};
					}
				}
			},
			"dataset.column.selections": {label: "Selections", skip: true},
			"dataset.column.selections.[0\-9]+": {
				state: {
					name: ".dataset.columnSet",
					getParams: function(node){
						return {setId: node.nodeData.name};
					}
				}
			},
			
			"dataset.row": {
				label: "Gene Sets",
				state: {
					name: ".dataset.rowSets", 
					getParams: function(node){
						return {};
					}
				}
			},
			"dataset.row.selections": {label: "Selections", skip: true},
			"dataset.row.selections.[0\-9]+": {
				state: {
					name: ".dataset.rowSet",
					getParams: function(node){
						return {setId: node.nodeData.name};
					}
				}
			},
			"dataset.analyses": {
				label: "Analyses",
				state: {
					name: ".dataset.analyses",
					getParams: function(node){}
				}
			},			
			"dataset\.analyses\.[0123456789][0123456789]*": {
				type: "Analysis",
				state: {
					name: ".dataset.analysis",
					getParams: function(node){
						return getInheritedParams({analysisId: node.nodeData.name, analysisType: node.nodeData.type}, node);
					},
					isDisabled: function(node){
						return node.nodeData.status==='IN_PROGRESS';
					}
				}
			}
		};
	};
	
return DatasetProjectTreeSchema;
DatasetProjectTreeSchema.$inject=[];
});
