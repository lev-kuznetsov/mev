define(["lodash"], function(_){ "use strict";
	function HclStateVMFactory(){ 
		function factory(scope, project, analysis){
			var _self = this;
			scope.isItOpen=true;			
			this.project = project;
			this.analysis = analysis;
			

			this.analysisId=analysis.name;			
			
			var labelsColum = analysis.result.column ? traverse(analysis.result.column) : null;
			var labelsRow = analysis.result.row ? traverse(analysis.result.row) : null;
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
	        
	        this.heatmapView = project.generateView({
	                viewType: 'heatmapView',
	                note: analysis.name,
	                labels: {
	                    row: {
	                        keys: analysis.params.rows || project.dataset.row.keys
	                    },
	                    column: {
	                        keys: analysis.params.columns || project.dataset.column.keys
	                    }
	                },
	                panel: {}
	                // expression: {
	                //     min: project.dataset.expression.min,
	                //     max: project.dataset.expression.max,
	                //     avg: project.dataset.expression.avg,
	                // },	                
	            });

	        if (analysis.result.column) {
	        	_.assign(this.heatmapView.labels.column, {
	        		keys: labelsColum
	        	});
	            _.assign(this.heatmapView.panel, {
                    top: analysis
                });
	        }
	        if(analysis.result.row) {
	        	_.assign(this.heatmapView.labels.row, {
	        		keys: labelsRow
	        	});
	            _.assign(this.heatmapView.panel, {
                    side: analysis
                });
	        }
			  	
		}
		factory.$inject=["$scope", "project", "analysis"];		
		return factory;
	}	
	HclStateVMFactory.$inject=[];
	HclStateVMFactory.$name="HclStateVMFactory";
	HclStateVMFactory.$provider="factory";
	return HclStateVMFactory;
});