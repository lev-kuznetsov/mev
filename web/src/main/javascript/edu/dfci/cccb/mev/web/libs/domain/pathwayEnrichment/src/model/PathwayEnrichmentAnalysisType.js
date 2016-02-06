"use strict";
define(["lodash", "./PathwayEnrichmentParams", "mev-analysis/src/type/model/AnalysisType"],	
function(_){
	function PathwayEnrichmentAnalysisType(mevAnalysisType, mevPathwayEnrichmentParams){
		
		return new mevAnalysisType({
			id: "pe",
			name: "Pathway Enrichment",
			params: mevPathwayEnrichmentParams
		});
	
	}	
	PathwayEnrichmentAnalysisType.$inject=["mevAnalysisType", "mevPathwayEnrichmentParams"];
	PathwayEnrichmentAnalysisType.$name="mevPathwayEnrichmentAnalysisType";
	PathwayEnrichmentAnalysisType.$provider="factory";
	return PathwayEnrichmentAnalysisType;
});