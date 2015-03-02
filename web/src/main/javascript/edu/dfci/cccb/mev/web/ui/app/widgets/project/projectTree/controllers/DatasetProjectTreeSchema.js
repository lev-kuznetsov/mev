define([], function(){
	var DatasetProjectTreeSchema=function DatasetProjectTreeSchema(){
	return {
							
			"dataset": {
				label: "Dataset", 
				state: {
					name: ".dataset", 
					getParams: function(node){
						return {datasetId: node.nodeData.id, node: node};
					}
				}
			},
			"dataset.column": {
				label: "Column Sets", 
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
				label: "Row Sets",
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
			"dataset.analyses": {label: "Analyses"},			
			"dataset\.analyses\.[0123456789][0123456789]*": {
				state: {
					name: ".dataset.analysis",
					getParams: function(node){
						return {analysisId: node.nodeData.name, analysisType: node.nodeData.type};
					}
				}
			}
		};
	};
	
return DatasetProjectTreeSchema;
DatasetProjectTreeSchema.$inject=[];
});
