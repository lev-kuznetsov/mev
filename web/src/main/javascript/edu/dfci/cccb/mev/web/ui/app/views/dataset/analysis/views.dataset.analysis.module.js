define(["ng", "lodash",
        "./kmeans/views.dataset.analysis.kmeans.module",
        "./nmf/views.dataset.analysis.nmf.module",
        "./histogram/views.dataset.analysis.histogram.module",
        "./genesd/views.dataset.analysis.genesd.module",
        "mev-pca"
        ], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis", ["mui.widgets.analysis", 
	                                                      // "mui.views.dataset.analysis.hcl",
	                                                      "mui.views.dataset.analysis.kmeans",
	                                                      // "mui.views.dataset.analysis.fisher",
	                                                      // "mui.views.dataset.analysis.anova",
	                                                      // "mui.views.dataset.analysis.limma",
	                                                      // "mui.views.dataset.analysis.deseq",
	                                                      "mui.views.dataset.analysis.nmf",
	                                                      // "mui.views.dataset.analysis.survival",
	                                                      // "mui.views.dataset.analysis.topgo",
	                                                      "mui.views.dataset.analysis.histogram",
	                                                      "mui.views.dataset.analysis.genesd",
	                                                      // "mui.views.dataset.analysis.genemad",
	                                                      // "mui.views.dataset.analysis.voom",
	                                                      // "Mev.AnalysisAccordionCollection",
	                                                      "mev-pca"]);
	module.config(["$stateProvider", "$urlRouterProvider", "AnalysisTypes", function($stateProvider, $urlRouterProvider, AnalysisTypes){				
		$stateProvider		
		.state("root.dataset.analysis", {			
			url: "analysis/{analysisType}/{analysisId}/",			
			parent: "root.dataset",	
			displayName: "{{analysis.name}} analysis",
			templateProvider: ["$stateParams", "$http", "analysis", function($stateParams, $http, analysis){
				console.debug("root.dataset.analysis templateProvider ", $stateParams.analysisType, analysis);
				var templateUrl="app/views/dataset/analysis/default/view.analysis.default.tpl.html";
				
				if(analysis.status!=="ERROR"){
					var analysisType = AnalysisTypes[$stateParams.analysisType];				
					if(analysisType && analysisType.shortName){
						templateUrl=templateUrl.replace("default", analysisType.shortName).replace("default", analysisType.shortName);
					}
				}
				console.debug("analysis templateUrl:", templateUrl);
   	     		return $http.get(templateUrl).then(function(response){
   	     			console.debug("analysis templateProvider response:", templateUrl, response);
   	     			return response.data;
   	     		});
			}],
			controllerProvider: ["$state", "$stateParams", "analysis", function($state, $stateParams, analysis){
				console.debug("DatasetAnalysisVM", $state, $state.is("root.dataset.analysis"), AnalysisTypes[$stateParams.analysisType]);		
				
				if(analysis.status!=="ERROR"){
					return AnalysisTypes[$stateParams.analysisType].viewModel;
				}else{
					return "AnalysisDefaultVM";
				}
//				if($state.is("root.dataset.analysis")){
//					$state.go(".result", {resultId: analysis.result[0].name});
				
//				}
								
			}],
			controllerAs: "DatasetAnalysisVM",
			resolve: {
				analysis: ["$stateParams", "project", "dataset", function($stateParams, project, dataset){
					console.debug("root.dataset.analysis resolve", $stateParams, project, dataset);
					var analysis = _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });					
					console.debug("root.dataset.analysis resolve analyis", analysis);					
					return analysis;
				}]
			}
		})				
		.state("root.dataset.analysis.result", {			
			url: "result/:resultId",
			parent: "root.dataset.analysis",
			template: "<div>Result: {{AnalysisResultVM.resultId}}" +
//						"<div>{{AnalysisResultVM.result}}</div>" +
						"<project-analysis-result result-item=\"AnalysisResultVM.result\" result-name=\"{{AnalysisResultVM.result.name}}\"></project-analysis-result>" +
					  "</div>",
			controller: ["$stateParams", "result", function($stateParams, result){
				this.resultId=$stateParams.resultId;
				this.result=result;
			}],
			controllerAs: "AnalysisResultVM", 
			resolve: {
				result: ["$stateParams", "analysis", function($stateParams, analysis){
					console.debug("root.dataset.analysis.result resolve", $stateParams, analysis);
					var result = _.find(analysis.result, function(result){ return result.name===$stateParams.resultId; });					
					console.debug("root.dataset.analysis.result resolved", result);
					return result;
				}]
			}
		});
	}]);
	
	module.controller("AnalysisDefaultVM", ["$scope", "analysis", function($scope, analysis){
		this.analysis=analysis;
	}]);
	module.directive("mevAnalysis", ["$compile", function($compile){
		return {
			restrict: "AE",
			scope: {
				analysis: "=mevAnalysis",				
			},
			template: "<div mev-analysis-result></div>",			
			controller: function(){},			
			controllerAs: "MevAnalysisVM",
			link: function(scope, elm, attr, ctrl){
				
			}
		};
	}])
	.directive("mevAnalysisResult", ["$compile", function($compile){
		return {
			restrict: "AE",
			scope: {
				analysis: "=mevAnalysisResult",				
			},
			template: "<div></div>",			
			controller: function(){},			
			controllerAs: "MevAnalysisResultVM",
			link: function(scope, elm, attr, ctrl){
				
			}
		};
	}]);
	
	return module;
});