define(["../router/PcaState.tpl.html", 
"mev-analysis/src/params/model/parentAnalysis/ParentAnalysisParam",
"mev-analysis/src/params/model/selectionSet/SelectionSetParam", 
], function(template){"use strict";
	function component(MevAnalysisType, mevAnalysisParams, MevParentAnalysisParam, mevSelectionSetParam){
		var pcaType = new MevAnalysisType({
			id: "pca",
			name: "PCA",
			viewModel: "PcaStateVM",
			template: template,
			params: mevAnalysisParams([
				// new MevParentAnalysisParam({
				// 	"id": "limma",
				// 	"type": "LIMMA Differential Expression Analysis",
				// 	"display": "name",
				// 	"required": true
				// })
				new mevSelectionSetParam({
					"id": "sampleList",
					"dimension": "column", 
					"displayName": "Sample Set",
					"display": "name",
					"bound": "keys",
					"required": true
				}),
				new mevSelectionSetParam({
					"id": "geneList",
					"dimension": "row", 
					"displayName": "Gene Set",
					"display": "name",
					"bound": "keys",
					"required": true
				})
			]),
			info: {
				template: "Performs a principal components analysis on the given data matrix" +
					"<p>Reference: <a href='https://stat.ethz.ch/R-manual/R- devel/library/stats/html/prcomp.html'>" +
					"https://stat.ethz.ch/R-manual/R- devel/library/stats/html/prcomp.html</a></p>"
			}
		});
		pcaType.start=function(){			
			var paramValues = this.params.getValues();			
			this.parent.start.call(this, this, paramValues, {analysisName: paramValues.name}, "put");	
		};
		return pcaType;
	}
	component.$name="mevPcaAnalysisType";	
	component.$inject=["mevAnalysisType", "mevAnalysisParams", "mevParentAnalysisParam", "mevSelectionSetParam"];
	component.$provider="factory";
	return component;
});