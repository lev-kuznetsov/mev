define([], function(){"use strict";
	function component(MevAnalysisType, mevAnalysisParams){
		var pcaType = new MevAnalysisType({
			id: "pca",
			name: "PCA",
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