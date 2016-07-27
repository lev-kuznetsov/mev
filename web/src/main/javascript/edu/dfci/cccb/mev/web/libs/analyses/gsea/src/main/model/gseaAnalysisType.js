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
	function gseaAnalysisType(MevAnalysisType, mevAnalysisParams, MevParentAnalysisParam, MevAnnotationFieldParam, mevAnnotationRepository, mevAnalysisLocator, $q){

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
			"value": "Symbol"
		});

		var gseaType = new MevAnalysisType({
			id: "gsea",
			name: "GSEA",
			params: mevAnalysisParams([
				new MevParentAnalysisParam({
					"id": "parent",
					"type": ["LIMMA Differential Expression Analysis", "t-Test Analysis", "ttest", "voom", "DESeq Differential Expression Analysis", "edger"],
					"display": "name",
					"required": true
				}), 
				new MevAnnotationFieldParam({
					"id": "geneSymbolField",
					"dimension": "row",
					"name": "Gene Symbol Mapping"
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
			var limma = mevAnalysisLocator.find({name: params.parent.name});
			if(!limma)
				throw new Error("Could not find parent Limma analysis for: " + JSON.stringify(params.parent));
			return prepareLimmaResult(limma.results, params.geneSymbolField).then(function(limmaResults){
				return {
					name: params.parent.name+"."+params.name,
					organism: limma.params.species,
					pAdjustMethod: params.pAdjustMethod,
					minGSSize: params.minGSSize,
					adjValueCutoff: params.adjValueCutoff,
					limma: limmaResults
				};
			});
		}

		function prepareLimmaResult(limmaResults, geneSymbolField){			
			var annotations = new mevAnnotationRepository("row");
			var geneMapping = geneSymbolField ? annotations.getMapping(geneSymbolField) : $q.when({});
			return geneMapping.then(function(geneMapping){				
				var idField = "id";
				if(_.isFunction(limmaResults.getIdField)){
					idField = limmaResults.getIdField();
				}
				var logFoldChangeField = "logFoldChange";				
				if(_.isFunction(limmaResults.getLogFoldChangeField)){
					logFoldChangeField = limmaResults.getLogFoldChangeField();
				}
				return limmaResults.reduce(function(result, item, index, arr){
					// "SYMBOL":"IL8","logFC":8.3599,"AveExpr":10.1369,"t":79.7198,"P.Value":2.4498e-22,"adj.P.Val":2.9396e-18}					
					var SYMBOL = geneMapping[item[idField]] ? geneMapping[item[idField]] : item[idField];
					if(!SYMBOL){
						console.log("no gene mapping found for '" + item[idField] + "' in '" + geneSymbolField + "'" );					
					}else{
						var gseaItem = {
							"SYMBOL": SYMBOL,
							"logFC": item[logFoldChangeField],
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
				_self.parent.start.call(_self, _self, params);	
			});			
		};
		gseaType.modelDecorator = function(analysis){
			var gseaRowModel = {
				getTotal: function () {
					return this.setSize;
				},
				getMatched: function () {
					return this.setSize;
				},
				getName: function () {
					return this.Description;
				},
				getPValue: function () {
					return this.pvalue;
				}
			};

			function formatResults(input){
				return input.map(function(item){
					return _.extend(item, gseaRowModel);
				});
			};

			function isPeRow(item){
				return _.every(_.functions(gseaRowModel), function(methodName){
					return _.hasIn(item, methodName);
				});
			}

			if(analysis && analysis.result && analysis.result.length > 0)
				if(!isPeRow(analysis.result[0]))
					formatResults(analysis.result);
		};
		gseaType.info = {
			template: "<p>Gene Set Enrichment Analysis of Reactome Pathway (gsePathway).</p>" +
				"<p></p>Reference: <a href='https://bioconductor.org/packages/release/bioc/html/ReactomePA.html'>" +
				"https://bioconductor.org/packages/release/bioc/html/ReactomePA.html</a></p>"
		}
		return gseaType;
	
	}	
	gseaAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevParentAnalysisParam", "mevAnnotationFieldParam", "mevAnnotationRepository", "mevAnalysisLocator", "$q"];
	gseaAnalysisType.$name="mevGseaAnalysisType";
	gseaAnalysisType.$provider="factory";
	return gseaAnalysisType;
});