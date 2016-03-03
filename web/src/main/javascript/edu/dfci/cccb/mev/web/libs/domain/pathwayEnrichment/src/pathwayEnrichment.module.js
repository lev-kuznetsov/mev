define(["mui", "angular-ui-router", 
	"mev-analysis", "mev-domain-common", "mev-results-table",
	"./model/PathwayEnrichmentAnalysisType",
	"./model/PathwayEnrichmentParams",
	"./router/PathwayEnrichmentState",
	"./router/PathwayEnrichmentVM",
	], function(ng){
	return ng.module("mevPathwayEnrichment", arguments, arguments);
});