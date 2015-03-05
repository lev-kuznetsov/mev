define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.hcl", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.controller("HclVM", ["$scope", "$state", "$stateParams", "project", "analysis", function($scope, $state, $stateParams, project, analysis){
		
		this.analysisId=$stateParams.analysisId;
		this.analysis=analysis;
		this.project=project;
		
		$scope.isItOpen=true;
		
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