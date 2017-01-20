define(["mui", "angular-ui-router", 
	"mev-analysis", "mev-domain-common", "mev-results-table", "mev-enrichment-dotplot",
	"./model/PathwayEnrichmentAnalysisType",
	"./model/PathwayEnrichmentParams",
	"./router/PathwayEnrichmentState",
	"./router/PathwayEnrichmentVM",
	], function(ng){
	return ng.module("mevPathwayEnrichment", arguments, arguments);
});