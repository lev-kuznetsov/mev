define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.genesd", [])	
	.factory("GeneSDVMFactory", ["$stateParams", "tableResultsFilter", function($stateParams, resultFilter){
		
		function SigGenes(n, genes, values, headers){		
			var _self = this;
			this.genes = genes;
			this.values = values;			
			if(Array.isArray(headers)){				
				this.headers = headers;
			}else{
				this.headers = [{
                    'name': 'ID',
                    'field': "geneId",
                    'icon': "search"
                },{
                    'name': 'Deviation',
                    'field': "value",
                    'icon' : n>0 ? ">=" : "<="
                }]
				if(typeof headers === "string"){
					this.headers[1].name = headers;
				}
			}
			
			function formatData(genes, values){
				return genes.map(function(gene, i){
					return {
						geneId: gene,
						value: values[i]
					};
				});
			};			
			function getN(n){
				if(n>0)
					return formatData(genes.slice(0, n), values.slice(0, n));
				else
					return formatData(genes.slice(genes.length + n), values.slice(values.length + n));
			}
			return {
				keys: genes.slice(0, 19),
				data: getN(n),
				headers: this.headers			
			};
		}
		
		
		return function GeneSDVMFactory($scope, $stateParams, project, analysis){
			var _self=this;
			this.analysisId=$stateParams.analysisId;
			this.analysis=analysis;
			this.project=project;
			$scope.dataset=project.dataset;
			this.sigGenesTop = SigGenes(20, analysis.result.genes, analysis.result.sd, "SD");
			this.sigGenesBottom = SigGenes(-20, analysis.result.genes, analysis.result.sd, "SD");
			
			this.heatmapViewTop = project.generateView({
	            viewType:'heatmapView',
	            note: analysis.name+"_top",
	            labels:{
	                row:{keys:this.sigGenesTop.keys}, 
	                column:{keys:project.dataset.column.keys}
	            },
	            expression:{
	                min: project.dataset.expression.min,
	                max: project.dataset.expression.max,
	                avg: project.dataset.expression.avg,
	            }
	        });
			this.heatmapViewBottom = project.generateView({
	            viewType:'heatmapView',
	            note: analysis.name+"_bottom",
	            labels:{
	                row:{keys:this.sigGenesBottom.keys}, 
	                column:{keys:project.dataset.column.keys}
	            },
	            expression:{
	                min: project.dataset.expression.min,
	                max: project.dataset.expression.max,
	                avg: project.dataset.expression.avg,
	            }
	        });
			$scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
				var labels = filteredResults.map(function(gene){return gene.geneId;});
				if($event.targetScope.id === _self.heatmapViewTop.id)
					_self.heatmapViewTop = _self.heatmapViewTop.applyFilter("row", labels);
				else if($event.targetScope.id === _self.heatmapViewBottom.id)
					_self.heatmapViewBottom = _self.heatmapViewBottom.applyFilter("row", labels);
            });
		};
	}])
	.controller("GeneSDVM", ["$scope", "$stateParams", "project", "analysis", "GeneSDVMFactory",
	                        function($scope, $stateParams, project, analysis, GeneSDVMFactory){
		GeneSDVMFactory.call(this, $scope, $stateParams, project, analysis);
	}]);
	return module;
});