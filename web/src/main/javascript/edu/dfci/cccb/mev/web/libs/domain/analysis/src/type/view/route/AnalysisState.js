define(["ng", "lodash", "./AnalysisState.tpl.html"], 
function(ng, _, defaultTemplate){
	var module = ng.module("mui.views.dataset.analysis", arguments, arguments);
	module.config(["$stateProvider", "$urlRouterProvider", "$q", "mevAnalysisTypes", function($stateProvider, $urlRouterProvider, $q, mevAnalysisTypes){				
		$stateProvider		
		.state("root.dataset.analysisx", {			
			url: "analysisx/{analysisId}/",			
			parent: "root.dataset",	
			displayName: "{{analysis.name}} analysis",
			templateProvider: ["$stateParams", "$http", "analysis", function($stateParams, $http, analysis){				
				if(analysis.status!=="ERROR"){
					var analysisType = mevAnalysisTypes.get(analysis.params.analysisType);				
					if(analysisType && _.isFunction(analysisType.template)){
						return analysisType.template;
					}
				}
				return defaultTemplate;
			}],
			controllerProvider: ["$state", "$stateParams", "analysis", function($state, $stateParams, analysis){
				if(analysis.status!=="ERROR"){
					var analysisType = mevAnalysisTypes.get(analysis.params.analysisType);				
					return analysisType.viewModel;
				}else{
					return function(){
						this.analysis = analysis;
					}
				}
//				if($state.is("root.dataset.analysis")){
//					$state.go(".result", {resultId: analysis.result[0].name});
				
//				}
								
			}],
			controllerAs: "analysisxvm",
			resolve: {
				analysis: ["$stateParams", "project", "dataset", function($stateParams, project, dataset){					
					return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });															
				}]
			}
		})				
		
	}]);
	
	return module;
});