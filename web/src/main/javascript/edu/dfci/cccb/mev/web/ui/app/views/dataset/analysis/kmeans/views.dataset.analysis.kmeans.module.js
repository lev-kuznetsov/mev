define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.kmeans", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.controller("KMeansVM", ["$scope", "$state", "$stateParams", "project", "analysis", function($scope, $state, $stateParams, project, analysis){
		
		this.analysisId=$stateParams.analysisId;
		this.analysis=analysis;
		this.project=project;
		
		$scope.isItOpen=true;
		function traverse(clusters) {

            var labels = []

            for (var i = 0; i < clusters.length; i++) {
                labels = labels.concat(clusters[i]);
            };

            return labels
        }
		
		var labels = traverse(analysis.clusters);
		
        
        if (analysis.dimension == "column") {
            project.generateView({
                viewType: 'heatmapView',
                labels: {
                    row: {
                        keys: project.dataset.row.keys
                    },
                    column: {
                        keys: labels
                    }
                },
                expression: {
                    min: project.dataset.expression.min,
                    max: project.dataset.expression.max,
                    avg: project.dataset.expression.avg,
                },
                panel: {
                    top: analysis
                },
                scrollableContainer: "[layout-column='right']"
            });
        } else {
            project.generateView({
                viewType: 'heatmapView',
                labels: {
                    column: {
                        keys: project.dataset.column.keys
                    },
                    row: {
                        keys: labels
                    }
                },
                expression: {
                    min: project.dataset.expression.min,
                    max: project.dataset.expression.max,
                    avg: project.dataset.expression.avg,
                },
                panel: {
                    side: analysis
                },
                scrollableContainer: "[layout-column='right']"
            });
        }
	}]);
		
	module.$inject=[];
	return module;
});