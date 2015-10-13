define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.kmeans", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("KMeansVMFactory", function(){
		return function($scope, project, analysis){
			this.analysisId=analysis.name;
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
	            this.heatmapView = project.generateView({
	                viewType: 'heatmapView',
	                note: analysis.name,
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
	                    top: analysis,
	                    side: {}
	                }
	            });
	        } else {
	            this.heatmapView = project.generateView({
	                viewType: 'heatmapView',
	                note: analysis.name,
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
	                    side: analysis,
	                    top: {}
	                }
	            });
	        }
		};
	})
	.controller("KMeansVM", ["$scope", "project", "analysis", "KMeansVMFactory", 
	                         function($scope, project, analysis, KMeansVMFactory){
		
		KMeansVMFactory.call(this, $scope, project, analysis);
		
	}]);
		
	module.$inject=[];
	return module;
});