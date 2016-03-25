define(["../router/HclState.tpl.html", 
"mev-analysis/src/params/model/select/SelectParam",
"mev-analysis/src/params/model/parentAnalysis/ParentAnalysisParam",
"mev-analysis/src/params/model/selectionSet/SelectionSetParam", 
], function(template, SelectParam){"use strict";
	function component(MevAnalysisType, mevAnalysisParams, MevParentAnalysisParam, mevSelectionSetParam){
		var hclType = new MevAnalysisType({
			id: "hcl",
			name: "HCL",
			viewModel: "HclStateVM",
			template: template,

			params: mevAnalysisParams([
				// new MevParentAnalysisParam({
				// 	"id": "limma",
				// 	"type": "LIMMA Differential Expression Analysis",
				// 	"display": "name",
				// 	"required": true
				// })
				new SelectParam({
					"id": "metric",
					"displayName": "Distance Metric",
					"options": ["euclidean", "manhattan", "pearson"],
					"value": "euclidean",
					"required": true
				}),
				new SelectParam({
					"id": "linkage",
					"displayName": "Linkage Criteria Algorithm",
					"options": ["complete", "average", "single"],
					"value": "complete",
					"required": true
				}),
				new SelectParam({
					"id": "dimension",
					"displayName": "Clustering Dimension",
					"options": [{display: "column", bound: "column"}, {display: "row", bound: "row"}],
					"value": {display: "column", bound: "column"},
					"required": true,
					"display": "display",					
					"bound": "bound",					
					"allowAll": true
				}),
				new mevSelectionSetParam({
					"id": "columns",
					"dimension": "column", 
					"displayName": "Sample Set",
					"display": "name",
					"bound": "keys",
					"required": true,
					"allowAll": "root"
				}),
				new mevSelectionSetParam({
					"id": "rows",
					"dimension": "row", 
					"displayName": "Gene Set",
					"display": "name",
					"bound": "keys",
					"required": true,
					"allowAll": "root"
				})
			])
		});
		// hclType.start=function(){			
		// 	var paramValues = this.params.getValues();			
		// 	this.parent.start.call(this, this, paramValues);	
		// };
		return hclType;
	}
	component.$name="mevHclAnalysisType";	
	component.$inject=["mevAnalysisType", "mevAnalysisParams", "mevParentAnalysisParam", "mevSelectionSetParam"];
	component.$provider="factory";
	return component;
});