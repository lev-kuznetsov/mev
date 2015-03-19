define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.deseq", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.controller("DESeqVM", ["$scope", "$state", "$stateParams", "project", "analysis", function($scope, $state, $stateParams, project, analysis){
		
		this.analysisId=$stateParams.analysisId;
		this.analysis=analysis;
		this.project=project;
		
		project.generateView({
            viewType:'heatmapView', 
            labels:{
                row:{keys:project.dataset.row.keys}, 
                column:{keys:project.dataset.column.keys}
            },
            expression:{
                min: project.dataset.expression.min,
                max: project.dataset.expression.max,
                avg: project.dataset.expression.avg,
            }
        });
	}]);
	return module;
});