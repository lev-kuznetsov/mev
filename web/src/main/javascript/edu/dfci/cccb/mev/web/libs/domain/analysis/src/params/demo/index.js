"use strict";
define(["mui", "mev-analysis", 
	"mev-analysis/src/params/model/AnalysisParamsFactory",
	"mev-analysis/src/params/model/text/TextParam",
	"mev-analysis/src/params/model/select/SelectParam",
	"mev-analysis/src/params/model/integer/IntegerParam",
	"mev-analysis/src/params/model/decimal/DecimalParam",
	"mev-mock", "mev-domain-common"
	], function(ng, mevAnalysis, ParamsClass, TextParam, SelectParam, IntegerParam, DecimalParam){
	var demo = ng.module("demo", arguments, arguments)
	.run(["$state", function($state){
		$state.go("mock");
	}])
	.factory("mevSelectionSetAggregator", function(){
		return function(){
			return [{name: "s1", x: 1},{name: "s2", x: 2}, {name: "s3", x: 3}];
		};
	})
	.controller("demoCtrl", ["$scope", "mevAnalysisParams", "mevSelectionSetParam", 
	function(scope, AnalysisParams, SelectionSetParam){
		// scope.textParam = {name: new TextParam({value: "myname"})};

		scope.params = AnalysisParams([
			new SelectParam({
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
			}),
			new DecimalParam({
				"id": "pValueCutoff",
				"displayName": "",
				"min": 0,
				"max": 1,
				"value": 0.051,
				"precision": 3
			}),
			new SelectionSetParam({
				"id": "genes",
				"dimension": "row", 
				"displayName": "Genes",
				"display": "name",
				"bound": "keys",
				"value": null
			})]);
		console.log("params", scope.params);
	}]);
	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});