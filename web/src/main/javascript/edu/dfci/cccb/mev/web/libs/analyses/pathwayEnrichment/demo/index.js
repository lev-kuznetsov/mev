"use strict";
define(["mui", "lodash", "mevPathwayEnrichment",
	"../data/mouse_test_data_pe.json", 
	"../data/mouse_test_data_limma.json", 
	"mev-dataset/src/main/dataset/lib/AnalysisClass",
	"mev-bs-modal",
	"mev-results-table",	
	"mev-mock",	
	"bootstrap", "bootstrap/dist/css/bootstrap.min.css"
	], 
	function(ng, _, mevPathwayEnrichment, peJson, limmaJson, AnalysisClass){
	var demo = ng.module("demo", arguments, arguments)
	.controller("demoCtrl", ["$scope", "$state", "mevPathwayEnrichmentAnalysisType", "mevMockProject",
	function(scope, $state, PathwayEnrichmentAnalysisType, mevMockProject){
		scope.PathwayEnrichmentAnalysisType = PathwayEnrichmentAnalysisType;
		scope.nodata=function(){
			$state.go("root.dataset.analysisType.pe", {datasetId: mevMockProject.dataset.id, analysisId: "nodata"});
		}
	}])
	.run(["$state", "mevMockProject", function($state, mevMockProject){
		mevMockProject.dataset.analyses.push(new AnalysisClass(peJson));
		var noData = new AnalysisClass(_.cloneDeep(peJson));
		noData.name="nodata";
		noData.result = [];
		mevMockProject.dataset.analyses.push(noData);
		$state.go("root.dataset.analysisType.pe", {datasetId: mevMockProject.dataset.id, analysisId: peJson.name});
		// $state.go("mock");
	}]);
	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});