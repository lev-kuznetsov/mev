"use strict";
define(["mui", "mevPathwayEnrichment",
	"../data/mouse_test_data_pe.json", 
	"../data/mouse_test_data_limma.json", 
	"mev-dataset/src/main/dataset/lib/AnalysisClass",
	"mev-bs-modal",
	"mev-results-table",	
	"mev-mock",	
	"bootstrap", "bootstrap/dist/css/bootstrap.min.css"
	], 
	function(ng, mevPathwayEnrichment, peJson, limmaJson, AnalysisClass){
	var demo = ng.module("demo", arguments, arguments)
	.controller("demoCtrl", ["$scope", "mevPathwayEnrichmentAnalysisType", function(scope, PathwayEnrichmentAnalysisType){
		scope.PathwayEnrichmentAnalysisType = PathwayEnrichmentAnalysisType;
	}])
	.run(["$state", "mevMockProject", function($state, mevMockProject){
		mevMockProject.dataset.analyses.push(new AnalysisClass(peJson));
		$state.go("root.dataset.analysisType.pe", {datasetId: mevMockProject.dataset.id, analysisId: peJson.name});
		// $state.go("mock");
	}]);
	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});