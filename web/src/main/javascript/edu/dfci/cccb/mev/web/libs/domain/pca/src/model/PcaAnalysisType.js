define(["../router/PcaState.tpl.html"], function(template){"use strict";
	function component(MevAnalysisType, mevAnalysisParams){
		var pcaType = new MevAnalysisType({
			id: "pca",
			name: "PCA",
			viewModel: "PcaStateVM",
			template: template,
			params: mevAnalysisParams([
				// new mevSelectionSetParam({
				// 	"id": "samplelist",
				// 	"dimension": "column", 
				// 	"displayName": "Genes",
				// 	"display": "name",
				// 	"bound": "keys"
				// })
			])
		});
		return pcaType;
	}
	component.$name="mevPcaAnalysisType";	
	component.$inject=["mevAnalysisType", "mevAnalysisParams"];
	component.$provider="factory";
	return component;
});