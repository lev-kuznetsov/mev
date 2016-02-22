define(["mui", "mev-analysis", 
	"mev-analysis/src/params/model/AnalysisParamsFactory",
	"mev-analysis/src/params/model/text/TextParam",
	"mev-analysis/src/params/model/select/SelectParam",
	"mev-analysis/src/params/model/integer/IntegerParam",
	"mev-analysis/src/params/model/decimal/DecimalParam",
	"mev-analysis/src/params/model/parentAnalysis/ParentAnalysisParam", 
	"mev-mock", "mev-domain-common"
	], function(ng, mevAnalysis, ParamsClass, TextParam, SelectParam, IntegerParam, DecimalParam){ "use strict";
	var demo = ng.module("demo", arguments, arguments)
	.run(["$state", function($state){
		$state.go("mock");
	}])
	.controller("demoCtrl", ["$scope", "mevAnalysisParams", "mevSelectionSetParam", "mevParentAnalysisParam",
	function(scope, mevAnalysisParams, SelectionSetParam, ParentAnalysisParam){
		// scope.textParam = {name: new TextParam({value: "myname"})};

		scope.params = mevAnalysisParams([
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
			}), 
			new ParentAnalysisParam({
				"id": "limma",
				"type": "LIMMA Differential Expression Analysis",
				"display": "name", 
				"bound": "results"
			}),
			Object.create(
				new ParentAnalysisParam({
					"id": "limma2",
					"type": "LIMMA Differential Expression Analysis",
					"display": "name",
					"required": true
				}),
				{
					value: {
					    configurable: false,
					    get: function() { 
					    	return this._value; 
					    },
					    set: function(value) { 
					    	this._value = value;
					    	console.log('Setting `o.bar` to', value); 
					    }
					}
				}
			)
		]);
		console.log("params", scope.params);
	}]);
	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});