"use strict";
define(["mui", 	"mev-mock",
	"mev-analysis/src/params/model/text/TextParam",
	"mev-analysis/src/params/model/select/SelectParam",
	"mev-analysis/src/params/model/integer/IntegerParam",
	"mev-analysis/src/params/model/decimal/DecimalParam",
	"mev-analysis", "mev-domain-common", "bootstrap", "bootstrap/dist/css/bootstrap.min.css"
	], function(ng, TextParam, SelectParam, IntegerParam, DecimalParam, mevAnalysis){
	var demo = ng.module("demoTypes", arguments, arguments)
	.run(["$state", function($state){
		 $state.go('mock');
	}])
	.factory("mevAnalysisLauncher", function(){
		return {
			start: function(analysisType){
				console.log("started " + analysisType.id, analysisType);
			}
		};
	})	
	.factory("mevLimmaType", ["mevAnalysisType", "mevAnalysisParams", function(mevAnalysisType, mevAnalysisParams){		
		return new mevAnalysisType(
			{
				id: "limma", 
				name: "LIMMA", 
				params: mevAnalysisParams(
					[new SelectParam({
						"id": "species",
						"displayName": "Species",
						"options": ["human", "mouse"],
						"value": "human"
					}),
					new IntegerParam({
						"id": "minGSSize",
						"displayName": "Min GS Size",
						"min": 0,
						"max": Infinity,
						"value": 29
					})], function(){return true;})				
			}
		);
	}])
	.factory("mevHCLType", ["mevAnalysisType", "mevSelectionSetParam", "mevAnalysisParams", function(AnalysisType, SelectionSetParam, AnalysisParamsFactory){
		return new AnalysisType("hcl", "Hier Clustering", new AnalysisParamsFactory([
			new DecimalParam({
				"id": "pValueCutoff",
				"displayName": "",
				"min": 0,
				"max": 1,
				"value": 0.05,
				"precision": 3
			}),
			new SelectionSetParam({
				"id": "genes",
				"dimension": "row", 
				"displayName": "Genes",
				"value": "s2",
				"display": "name"
			})], function(){return true;})
		);
	}])
	.controller("demoCtrl", ["$scope", "mevLimmaType", "mevHCLType", 
	function(scope, limmaType, mevHCLType){
		scope.types = [limmaType, mevHCLType];
	}]);
	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});