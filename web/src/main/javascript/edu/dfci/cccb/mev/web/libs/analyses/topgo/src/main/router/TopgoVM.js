define(["lodash"], function(_){ "use strict";
	function TopgoVM(mevAnalysisTypes){
		function factory($scope, project, analysis) {
			var _self = this;
			this.analysisId = analysis.name;
			this.analysis = analysis;
			this.project = project;
			this.dataset = project.dataset;
			var analysisType = mevAnalysisTypes.get("topgo");
			if(analysisType && _.isFunction(analysisType.modelDecorator))
				analysisType.modelDecorator(analysis);
			$scope.dotPlotConfig = {
				name: this.analysis.name,
				data: this.analysis.results,
				series: "Counts"
			}
			$scope.headers = [
				{
					'name': 'ID',
					'field': "goId",
					'icon': "search",
					'link': function (value) {
						return "http://amigo.geneontology.org/amigo/term/" + value;
					}
				},
				{
					'name': 'GO Term',
					'field': "goTerm",
					'icon': "search"
				},
				{
					'name': 'Annotated Genes',
					'field': "annotatedGenes",
					'icon': [">=", "<="],
					'datatype': "integer"
				},
				{
					'name': 'Significant Genes',
					'field': "significantGenes",
					'icon': [">=", "<="],
					'datatype': "integer"
				},
				{
					'name': 'Expected',
					'field': "expected",
					'icon': [">=", "<="]
				},
				{
					'name': 'P-Value',
					'field': "pValue",
					'icon': ["<=", ">="],
					'default': 0.05
				},
				{
					'name': 'Adj. P-Value',
					'field': "adj.p",
					'icon': ["<=", ">="]
				}

			];

			$scope.filteredResults = undefined;
			$scope.viewGenes = function (filteredResults) {
				$scope.filteredResults = filteredResults;
//	        	$scope.filteredResults = tableFilter(_self.analysis.results, filterParams);
//		        console.debug("topgo ", $scope.filteredResults);
			};
		}
		factory.$inject=["$scope", "project", "analysis"];
		return factory;
	}	
	TopgoVM.$inject=["mevAnalysisTypes"];
	TopgoVM.$name="TopgoVMFactory";
	TopgoVM.$provider="factory";
	return TopgoVM;
});