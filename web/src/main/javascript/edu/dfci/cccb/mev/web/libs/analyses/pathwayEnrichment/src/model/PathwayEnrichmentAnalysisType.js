define(["lodash", "../router/PathwayEnrichmentState.tpl.html", "./PathwayEnrichmentInfo.tpl.html", "./PathwayEnrichmentParams", "mev-analysis/src/type/model/AnalysisType"],
function(_, template, infoTemplate){"use strict";
	function PathwayEnrichmentAnalysisType(mevAnalysisType, mevPathwayEnrichmentParams){

		return new mevAnalysisType({
			id: "pe",
			name: "Pathway Enrichment",
			viewModel: "PathwayEnrichmentVM",
			template: template,
			params: mevPathwayEnrichmentParams,
			modelDecorator: function(analysis){

				var peRowModel = {
					getRatio: function(){
						return Math.round(eval(this.GeneRatio) * 1000) / 10000;
					},
					getTotal: function(){
						return parseInt(this.GeneRatio.split("/")[1]);
					},
					getMatched: function(){
						return this.Count;
					},
					getName: function(){
						return this.Description;
					},
					getPValue: function(){
						return this.pvalue;
					}
				}


				function formatResults(input){
					return input.map(function(item){
						return _.extend(item, peRowModel);
					});
				};

				function isPeRow(item){
					return _.every(_.functions(peRowModel), function(methodName){
						return _.has(item, methodName);
					});
				}

				if(analysis && analysis.result && analysis.result.length > 0)
					if(!isPeRow(analysis.result[0]))
						formatResults(analysis.result);
			},
			info: {
				template: infoTemplate
			}
		});
	
	}	
	PathwayEnrichmentAnalysisType.$inject=["mevAnalysisType", "mevPathwayEnrichmentParams"];
	PathwayEnrichmentAnalysisType.$name="mevPathwayEnrichmentAnalysisType";
	PathwayEnrichmentAnalysisType.$provider="factory";
	return PathwayEnrichmentAnalysisType;
});