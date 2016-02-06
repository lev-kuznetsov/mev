define(["mui", "angular-ui-router", 
	"mev-analysis", "mev-domain-common",
	"./model/PathwayEnrichmentAnalysisType",
	"./model/PathwayEnrichmentParams",
	"./router/PathwayEnrichmentState",
	"./router/PathwayEnrichmentVM",
	], function(ng, Analsyis, PathwayEnrichmentAnalysisType){
	return ng.module("mevPathwayEnrichment", arguments, arguments);
});