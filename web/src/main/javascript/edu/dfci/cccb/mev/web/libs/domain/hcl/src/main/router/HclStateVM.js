define(["lodash"], function(_){ "use strict";
	function HclStateVMFactory(){ 
		function factory(scope, project, analysis){
			var _self = this;
			scope.isItOpen=true;			
			this.project = project;
			this.analysis = analysis;
			

			this.analysisId=analysis.name;			
			
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
	                        keys: analysis.params.rows || project.dataset.row.keys
	                    },
	                    column: {
	                        keys: labels
	                    }
	                },
	                // expression: {
	                //     min: project.dataset.expression.min,
	                //     max: project.dataset.expression.max,
	                //     avg: project.dataset.expression.avg,
	                // },
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
	                        keys: analysis.params.columns || project.dataset.column.keys
	                    },
	                    row: {
	                        keys: labels
	                    }
	                },
	                // expression: {
	                //     min: project.dataset.expression.min,
	                //     max: project.dataset.expression.max,
	                //     avg: project.dataset.expression.avg,
	                // },
	                panel: {
	                    side: analysis
	                }
	            });
	        }
			  	
		}
		factory.$inject=["$scope", "project", "analysis"];		
		return factory;
	}	
	HclStateVMFactory.$inject=[];
	HclStateVMFactory.$name="HclStateVMFactoryFactory";
	HclStateVMFactory.$provider="factory";
	return HclStateVMFactory;
});