"use strict";
define(["mui", "mev-analysis", "angular-mocks", 
	"mev-analysis/src/params/model/decimal/DecimalParam",
	"mev-analysis/src/params/model/integer/IntegerParam",
	"mev-analysis/src/params/model/select/SelectParam",
	"mev-analysis/src/params/model/selectionSet/SelectionSetParam"
	], function(ng, mevAnalysis, ngMocks, DecimalParam, IntegerParam, SelectParam){

	return describe("mevAnalysisParamsFactory tests", function(){
		
		var AnalysisParamsFactory;
		var SelectionSetParamFactory;
		var SelectionSetAggregator;

		beforeEach(module("ngMock"));
		beforeEach(module(mevAnalysis.name));
		beforeEach(function(){
			SelectionSetAggregator = function(){return [{name: "s1", x: 1},{name: "s2", x: 2}, {name: "s3", x: 3}]};
			module(function($provide){
				$provide.value("mevSelectionSetAggregator", SelectionSetAggregator);
			});
		});


		beforeEach(inject(function(_mevAnalysisParams_, _mevSelectionSetParam_){
			AnalysisParamsFactory = _mevAnalysisParams_;			
			SelectionSetParamFactory = _mevSelectionSetParam_;
		}));

		it("ensure AnalysisParamsFactory.getValues works with object initializer", function(){ 	
			var params = AnalysisParamsFactory({
				"species": {
					"type": "select",				
					"displayName": "Species",
					"options": ["human", "mouse"],
					"value": "human"
				},
				"pAdjust": {
					"type": "select",				
					"options": ["holm", "hochberg", "hommel", "bonferroni", "BH", "BY", "fdr", "none"],
					"value": "fdr"
				},
				"pValueCutoff": {
					"type": "decimal",
					"min": 0.0,
					"max": 1.0,
					"precision": 2				
				},
				"minGSSize": {
					"type": "interger",
					"value": 20
				},
				"genes": {
					"type": "@geneSet"
				}
			});
			expect(params.getValues()).toEqual({name: undefined, species: "human", pAdjust: "fdr", pValueCutoff: undefined, minGSSize: 20, genes: undefined});						     								
		});		 			

		it("ensure AnalysisParamsFactory.getValues works with array initializer", function(){ 	
			var params = AnalysisParamsFactory([
				new SelectParam({
					"id": "species",
					"displayName": "Species",
					"options": ["human", "mouse"],
					"value": "human"
				}),
				new SelectParam({
					"id": "pAdjust",
					"type": "select",				
					"options": ["holm", "hochberg", "hommel", "bonferroni", "BH", "BY", "fdr", "none"],
					"value": "fdr"
				}),
				new IntegerParam({
					"id": "minGSSize",
					"displayName": "Min GS Size",
					"min": 0,
					"max": Infinity,
					"value": 20
				}),
				new DecimalParam({
					"id": "pValueCutoff",
					"displayName": "",
					"min": 0,
					"max": 1,
					"value": 0.05,
					"precision": 3
				}),
				new SelectionSetParamFactory({	
					"id": "genes",
					"dimension": "row", 
					"displayName": "Genes",
					"value": "s2"
				})
			]);
			expect(params.getValues()).toEqual({name: undefined, species: "human", pAdjust: "fdr", pValueCutoff: 0.05, minGSSize: 20, genes: "s2"});						     								
		});		 			



	});
});
