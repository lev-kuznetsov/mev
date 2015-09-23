define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.hcl", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("HclVMFactory", [function(){
		return function HclVMFactory($scope, project, analysis){
			$scope.isItOpen=true;
			this.analysisId=analysis.name;
			this.analysis=analysis;
			this.project=project;
			
			var labels = traverse(analysis.root);
			function traverse(tree) {
	            var leaves = {
	                '0': [],
	                '1': []
	            };

	            if (tree.children.length > 0) {
	                for (var i = 0; i < tree.children.length; i++) {
	                    leaves[i] = (!tree.children[i].children) ? [tree.children[i].name] : traverse(tree.children[i])
	                }
	            };

	            return leaves[0].concat(leaves[1]);
	        };		
	        
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
	                    top: analysis
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
	                    side: analysis
	                }
	            });
	        }
		};
	}])
	.controller("HclVM", ["$scope", "$stateParams", "HclVMFactory", "project", "analysis", 
	                      function($scope, $stateParams, HclVMFactory, project, analysis){
		
		HclVMFactory.call(this, $scope, project, analysis);
		
	}]);
		
	module.$inject=[];
	return module;
});