define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.topgo", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("TopGoVMFactory", ["tableResultsFilter", function(tableFilter){
		return function TopGOVMFactory($scope, project, analysis){
			var _self = this;
			this.analysisId=analysis.name;
			this.analysis=analysis;
			this.project=project;
			this.dataset=project.dataset;			
			
			$scope.headers = [
	            {
	                'name': 'ID',
	                'field': "goId",
	                'icon': "search",
	                'link': function(value){
	                	return "http://amigo.geneontology.org/amigo/term/"+value;
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
	                'icon': "<=",
	                'default': 0.05
	            },
	            {
	                'name': 'Adj. P-Value',
	                'field': "pValue",
	                'icon': "<="
	            }
	            
	        ];
	        
	        $scope.filteredResults = undefined;
	        $scope.viewGenes = function (filteredResults) {
	        	$scope.filteredResults = filteredResults;
//	        	$scope.filteredResults = tableFilter(_self.analysis.results, filterParams);                                
//		        console.debug("topgo ", $scope.filteredResults);
		    };		
		};
	}])
	.controller("TopGoVM", ["$scope", "$state", "$stateParams", "tableResultsFilter", "project", "analysis", "TopGoVMFactory",
	                        function($scope, $state, $stateParams, tableFilter, project, analysis, TopGoVMFactory){
		TopGoVMFactory.call(this, $scope, project, analysis, tableFilter);
		console.debug("TopGoVMFactory", $scope);
	}]);
	return module;
});