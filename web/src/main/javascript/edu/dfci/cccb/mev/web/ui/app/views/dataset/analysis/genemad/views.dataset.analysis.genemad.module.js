define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.genemad", ["mui.views.dataset.analysis.genesd"])	
	.factory("GeneMADVMFactory", ["$stateParams", "SigGenes", function($stateParams, SigGenes){
		return function GeneMADVMFactory($scope, $stateParams, project, analysis){
			var _self=this;
			this.analysisId=$stateParams.analysisId;
			this.analysis=analysis;
			this.project=project;
			$scope.dataset=project.dataset;
			this.sigGenesTop = SigGenes(500, analysis.result.genes, analysis.result.mad, "MAD");			
			this.heatmapViewTop = project.generateView({
	            viewType:'heatmapView',
	            note: analysis.name+"_genesMADTop",
	            labels:{
	                row:{keys:this.sigGenesTop.keys}, 
	            }
	        });
			this.filteredResultsTop=[];
			$scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
				if($event.targetScope.id === _self.heatmapViewTop.id){
					var labels = filteredResults.map(function(gene){return gene.geneId;});
					_self.heatmapViewTop = _self.heatmapViewTop.applyFilter("row", labels);
				}
            });
		};		
	}])
	.controller("GeneMADVM", ["$scope", "$stateParams", "project", "analysis", "GeneMADVMFactory",
	                        function($scope, $stateParams, project, analysis, GeneMADVMFactory){
		GeneMADVMFactory.call(this, $scope, $stateParams, project, analysis);
	}]);
	return module;
});