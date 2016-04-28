"use strict";
define(["lodash",
	"mev-analysis/src/params/model/AnalysisParamsFactory",
	"mev-analysis/src/params/model/text/TextParam",
	"mev-analysis/src/params/model/select/SelectParam",
	"mev-analysis/src/params/model/integer/IntegerParam",
	"mev-analysis/src/params/model/decimal/DecimalParam", 
	"mev-analysis/src/params/model/selectionSet/SelectionSetParam", 
	], function(_, AnalysisParamsFactory,
		TextParam, SelectParam, IntegerParam, DecimalParam){	
	//	private @Getter @Setter @Parameter int minGSSize = 20;
	//  private @Getter @Setter @Parameter double pvalueCutoff = 0.25;
	//  private @Getter @Setter @Parameter String pAdjustMethod = "fdr";
	//  private @Getter @Setter @Parameter String organism = "human";
	//  private @Getter @Setter @Parameter ("geneList") Collection<String> genelist;

	
	function PathwayEnrichmentParams(mevAnalysisParams, mevSelectionSetParam){
		
		var self=this;		
		
		var params = mevAnalysisParams([
			new SelectParam({
				"id": "organism",
				"displayName": "Species",
				"options": ["human", "mouse"],
				"value": "human"
			}),
			new SelectParam({	
				"id": "pAdjustMethod",
				"options": ["holm", "hochberg", "hommel", "bonferroni", "BH", "BY", "fdr", "none"],
				"value": "fdr"
			}),
			new DecimalParam({
				"id": "pvalueCutoff",
				"displayName": "pValueCutoff",
				"min": 0,
				"max": 1,
				"value": 0.05,
				"precision": 3
			}),
			new IntegerParam({
				"id": "minGSSize",
				"displayName": "Min GS Size",
				"min": 0,
				"max": Infinity,
				"value": 20
			}),
			new mevSelectionSetParam({
				"id": "genelist",
				"dimension": "row", 
				"displayName": "Genes",
				"display": "name",
				"bound": "keys"
			})]);

		return params;
	};
	PathwayEnrichmentParams.$inject=["mevAnalysisParams", "mevSelectionSetParam"];
	PathwayEnrichmentParams.$name="mevPathwayEnrichmentParams";
	PathwayEnrichmentParams.$provider="service";
	return PathwayEnrichmentParams;
});