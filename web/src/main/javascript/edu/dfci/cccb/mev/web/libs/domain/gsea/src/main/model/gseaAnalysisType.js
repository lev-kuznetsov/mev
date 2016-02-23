define(["lodash", "mev-analysis/src/type/model/AnalysisType",	
	"mev-analysis/src/params/model/AnalysisParamsFactory",
	"mev-analysis/src/params/model/text/TextParam",
	"mev-analysis/src/params/model/select/SelectParam",
	"mev-analysis/src/params/model/integer/IntegerParam",
	"mev-analysis/src/params/model/decimal/DecimalParam", 
	"mev-analysis/src/params/model/selectionSet/SelectionSetParam", 
	"mev-analysis/src/params/model/parentAnalysis/ParentAnalysisParam", 
	"mev-analysis/src/params/model/annotationField/AnnotationFieldParam"
	],
function(_, mevAnalsysType, 
	AnalysisParamsFactory, 
	TextParam, 
	SelectParam, 
	IntegerParam, 
	DecimalParam,
	ParentAnalysisParam,
	AnnotationFieldParam){ "use strict";
	function gseaAnalysisType(MevAnalysisType, mevAnalysisParams, MevParentAnalysisParam, MevAnnotationFieldParam, mevAnnotationRepository){

		var limmaParam = Object.create(
			new MevParentAnalysisParam({
				"id": "limma",
				"type": "LIMMA Differential Expression Analysis",
				"display": "name",
				"required": true
			}),{
				value: {
				    configurable: false,
				    get: function() { return this._value; },
				    set: function(value) { this._value = value; }
				}
			});
		var annotationFieldParam = new MevAnnotationFieldParam({
			"id": "geneSymbolField",
			"dimension": "row",
			"name": "Gene Symbol Mapping",
			"display": "name",
			"bound": "name",
			"value": "Symbol"
		});

		var gseaType = new MevAnalysisType({
			id: "gsea",
			name: "GSEA",
			params: mevAnalysisParams([
				new MevParentAnalysisParam({
					"id": "limma",
					"type": "LIMMA Differential Expression Analysis",
					"display": "name",
					"required": true
				}), 
				new MevAnnotationFieldParam({
					"id": "geneSymbolField",
					"dimension": "row",
					"name": "Gene Symbol Mapping",
					"display": "name",
					"bound": "name",
					"value": "Symbol"
				}),
				new SelectParam({	
					"id": "pAdjustMethod",
					"options": ["holm", "hochberg", "hommel", "bonferroni", "BH", "BY", "fdr", "none"],
					"value": "fdr"
				}),
				new DecimalParam({
					"id": "adjValueCutoff",
					"displayName": "adjValueCutoff",
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
			])
		});

		function prepareParams(params){
			// "name":"vvv","organism":"human","pAdjustMethod":"fdr","minGSSize":20,"adjValueCutoff":0.05,"limma":
			return prepareLimmaResult(params.limma.results, params.geneSymbolField).then(function(limmaResults){
				return {
					name: params.limma.name+"."+params.name,
					organism: params.limma.params.species,
					pAdjustMethod: params.pAdjustMethod,
					minGSSize: params.minGSSize,
					adjValueCutoff: params.adjValueCutoff,
					limma: limmaResults
				};
			});
		}

		function prepareLimmaResult(limmaResults, geneSymbolField){			
			var annotations = new mevAnnotationRepository("row");
			var geneMapping = geneSymbolField ? annotations.getMapping(geneSymbolField) : undefined;
			return geneMapping.then(function(geneMapping){				
				return limmaResults.reduce(function(result, item){
					// "SYMBOL":"IL8","logFC":8.3599,"AveExpr":10.1369,"t":79.7198,"P.Value":2.4498e-22,"adj.P.Val":2.9396e-18}
					var SYMBOL = geneMapping[item.id] ? geneMapping[item.id] : item.id;
					if(!SYMBOL){
						console.log("no gene mapping found for '" + item.id + "' in '" + geneSymbolField + "'" );					
					}else{
						var gseaItem = {
							"SYMBOL": SYMBOL,
							"logFC": item.logFoldChange,
							"AveExpr": item.averageExpression,
							"t": item.t,
							"P.Value": item.pValue,
							"adj.P.Val": item.qValue
						};
						result.push(gseaItem);				
					}
					return result;
				}, []);
				return result;
			});			
		}

		gseaType.start=function(){
			var _self = this;
			var paramValues = this.params.getValues();
			console.debug("gsea params values", paramValues);
			prepareParams(paramValues).then(function(params){
				console.debug("gsea out params", params);
				_self.parent.start.call(_self, params);	
			});			
		};

		return gseaType;
	
	}	
	gseaAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevParentAnalysisParam", "mevAnnotationFieldParam", "mevAnnotationRepository", ];
	gseaAnalysisType.$name="mevGseaAnalysisType";
	gseaAnalysisType.$provider="factory";
	return gseaAnalysisType;
});