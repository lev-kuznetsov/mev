define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.genesd", [])	
	.factory("GeneSDVMFactory", ["$stateParams", "SigGenes", "alertService", function($stateParams, SigGenes, alertService){
		
		return function GeneSDVMFactory($scope, $stateParams, project, analysis){
			var _self=this;
			this.analysisId=$stateParams.analysisId;
			this.analysis=analysis;
			this.project=project;
			$scope.dataset=project.dataset;
			this.sigGenesTop = SigGenes(20, analysis.result.genes, analysis.result.sd, "SD");
			this.sigGenesBottom = SigGenes(-20, analysis.result.genes, analysis.result.sd, "SD");
			$scope.filteredResultsTop = this.sigGenesTop;
			$scope.filteredResultsBottom = this.sigGenesBottom;
			
			this.heatmapViewTop = project.generateView({
	            viewType:'heatmapView',
	            note: analysis.name+"_geneSDTop",
	            labels:{
	                row:{keys:this.sigGenesTop.keys}, 
	            }
	        });
			this.heatmapViewBottom = project.generateView({
	            viewType:'heatmapView',
	            note: analysis.name+"_geneSDBottom",
	            labels:{
	                row:{keys:this.sigGenesBottom.keys}, 
	            }
	        });
			
			$scope.selectionParams = {};
			$scope.addSelections = function (filteredResults) {
                var keys = filteredResults.map(function(item){return item.geneId;});                
                var selectionData = {
                    name: $scope.selectionParams.name,
                    properties: {
                        selectionDescription: '',
                        selectionColor: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                    },
                    keys: keys
                };

                project.dataset.selection.post({
                        datasetName: project.dataset.datasetName,
                        dimension: "row"

                    }, selectionData,
                    function (response) {
                        project.dataset.resetSelections('row');
                        var message = "Added " + $scope.selectionParams.name + " as new Selection!";
                        var header = "Heatmap Selection Addition";

                        alertService.success(message, header);
                    },
                    function (data, status, headers, config) {
                        var message = "Couldn't add new selection. If " + "problem persists, please contact us.";

                        var header = "Selection Addition Problem (Error Code: " + status + ")";

                        alertService.error(message, header);
                    });
            };
            
            $scope.exportParams = {
            		name: undefined,
                    color: '#ffffff'
            };
            $scope.exportSelection = function (filteredResults) {
                var keys = filteredResults.map(function(item){return item.geneId;});
                var selectionData = {
                    name: $scope.exportParams.name,
                    properties: {
                        selectionDescription: '',
                        selectionColor: $scope.exportParams.color,
                    },
                    keys: keys
                };

                _self.project.dataset.selection.export({
                        datasetName: _self.project.dataset.datasetName,
                        dimension: "row"

                    }, selectionData,
                    function (response) {
                    	_self.project.dataset.resetSelections('row');
                        var message = "Added " + $scope.exportParams.name + " as new Dataset!";
                        var header = "New Dataset Export";

                        alertService.success(message, header);
                    },
                    function (data, status, headers, config) {
                        var message = "Couldn't export new dataset. If " + "problem persists, please contact us.";

                        var header = "New Dataset Export Problem (Error Code: " + status + ")";

                        alertService.error(message, header);
                    });

            };
            
			$scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
				var labels = filteredResults.map(function(gene){return gene.geneId;});
				if($event.targetScope.id === _self.heatmapViewTop.id){					
					$scope.filteredResultsTop = filteredResults;
					_self.heatmapViewTop = _self.heatmapViewTop.applyFilter("row", labels);
				}
				else if($event.targetScope.id === _self.heatmapViewBottom.id){					
					$scope.filteredResultsBottom = filteredResults;
					_self.heatmapViewBottom = _self.heatmapViewBottom.applyFilter("row", labels);
				}
            });
		};
	}])
	.controller("GeneSDVM", ["$scope", "$stateParams", "project", "analysis", "GeneSDVMFactory",
	                        function($scope, $stateParams, project, analysis, GeneSDVMFactory){
		GeneSDVMFactory.call(this, $scope, $stateParams, project, analysis);
	}]);
	return module;
});