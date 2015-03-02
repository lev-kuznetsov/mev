define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){				
		$stateProvider		
		.state("root.dataset.analysis", {			
			url: "analysis/{analysisType}/{analysisId}/",			
			parent: "root.dataset",			
			templateProvider: ["$stateParams", "$http", function($stateParams, $http){
				console.debug("root.dataset.analysis templateProvider ", $stateParams.analysisType);
				var templateUrl="app/views/dataset/analysis/default/analysis.default.tpl.html";
				
				var mapTemplateAnalysisType = {
						"Hierarchical Clustering": "hcl", //using the default latyout for HCL
						"LIMMA Differential Expression Analysis": "limma"
				};
				var analysisType = mapTemplateAnalysisType[$stateParams.analysisType];
				
				if(analysisType){
					templateUrl=templateUrl.replace("default", analysisType).replace("default", analysisType);
				}
				console.debug("analysis templateUrl:", templateUrl);
   	     		return $http.get(templateUrl).then(function(response){
   	     			console.debug("analysis templateProvider response:", templateUrl, response);
   	     			return response.data;
   	     		});
			}],
			controller: ["$scope", "$state", "$stateParams", "project", "analysis", function($scope, $state, $stateParams, project, analysis){
				this.analysisId=$stateParams.analysisId;
				this.analysis=analysis;
				this.project=project;
				$scope.isItOpen=true;
//				console.debug("DatasetAnalysisVM", $state, $state.is("root.dataset.analysis"));
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