define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.project.analysis", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){				
		$stateProvider		
		.state("root.project.analysis", {			
			url: "analysis/{analysisId:int}/{analysisType}",			
			parent: "root.project",			
//			template: "<div>" +
//						"<project-analysis-parameters parameters=\"ProjectAnalysisVM.analysis.params\"></project-analysis-parameters>" +
//						"<ui-view></ui-view>" +
//					"</div>",
			templateProvider: ["$stateParams", "$http", function($stateParams, $http){
				console.debug("root.project.analysis templateProvider ", $stateParams.analysisType);
				var templateUrl="app/views/project/analysis/default/analysis.default.tpl.html";
				
				var mapTemplateAnalysisType = {
//						"Hierarchical Clustering": "hcl", //using the default latyout for HCL
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
			controller: ["$state", "$stateParams", "analysis", function($state, $stateParams, analysis, redirect){
				this.analysisId=$stateParams.analysisId;
				this.analysis=analysis;
				console.debug("ProjectAnalysisVM", $state, $state.is("root.project.analysis"));
				if($state.is("root.project.analysis")){
					$state.go(".result", {resultId: analysis.result[0].name});
				}
				
			}],
			controllerAs: "ProjectAnalysisVM",
			resolve: {
				analysis: ["$stateParams", "project", function($stateParams, project){
					console.debug("root.project.analysis resolve", $stateParams, project);
					var analysis = _.find(project.analysis, function(analysis){ return analysis.id===$stateParams.analysisId; });					
					console.debug("root.project.analysis resolve analyis", analysis);					
					return analysis;
				}],
				redirect: ["$q", "$state", "$timeout", "analysis", function($q, $state, $timeout, analysis){
//					var deffered = $q.deffer();
//					return deffered.promise.then(function(){
//						$state.go(".result", {resultId: analysis.result[0].name});
//					});
//					$timeout(function(){
//						deffered.resolve(analysis);
//					});
					return {redirect: "HIHI"};
				}]
			}
		})				
		.state("root.project.analysis.result", {			
			url: "result/:resultId",
			parent: "root.project.analysis",
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
					console.debug("root.project.analysis.result resolve", $stateParams, analysis);
					var result = _.find(analysis.result, function(result){ return result.name===$stateParams.resultId; });					
					console.debug("root.project.analysis.result resolved", result);
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