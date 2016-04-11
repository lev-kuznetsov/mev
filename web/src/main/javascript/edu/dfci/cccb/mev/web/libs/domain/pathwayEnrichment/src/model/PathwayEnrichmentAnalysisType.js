define(["lodash", "../router/PathwayEnrichmentState.tpl.html", "./PathwayEnrichmentParams", "mev-analysis/src/type/model/AnalysisType"],	
function(_, template){"use strict";
	function PathwayEnrichmentAnalysisType(mevAnalysisType, mevPathwayEnrichmentParams){
		
		return new mevAnalysisType({
			id: "pe",
			name: "Pathway Enrichment",
			viewModel: "PathwayEnrichmentVM",
			template: template,
			params: mevPathwayEnrichmentParams
		});
	
	}	
	PathwayEnrichmentAnalysisType.$inject=["mevAnalysisType", "mevPathwayEnrichmentParams"];
	PathwayEnrichmentAnalysisType.$name="mevPathwayEnrichmentAnalysisType";
	PathwayEnrichmentAnalysisType.$provider="factory";
	return PathwayEnrichmentAnalysisType;
});