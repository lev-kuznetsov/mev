define([], function(){ "use strict";
    function GeneMADVM($stateParams){
        function factory($scope, project, analysis){
            function formatData(genes, values){
                return genes.map(function(gene, i){
                    return {
                        geneId: gene,
                        value: values[i]
                    };
                });
            }

            var _self=this;
            this.analysisId=$stateParams.analysisId;
            this.analysis=analysis;
            this.project=project;
            $scope.dataset=project.dataset;
            var TOP_N = 500;
            this.sigGenesTop = formatData(analysis.result.genes.slice(0,TOP_N), analysis.result.mad.slice(0, TOP_N));
            this.headers=[{
                'name': 'MAD',
                'field': "geneId",
                'icon': "search"
            },{
                'name': 'Deviation',
                'field': "value",
                'icon' : ">="
            }];
            this.heatmapViewTop = project.generateView({
                viewType:'heatmapView',
                note: analysis.name+"_genesMADTop",
                labels:{
                    row:{keys: analysis.result.genes.slice(0,TOP_N)},
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
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    GeneMADVM.$inject=["$stateParams"];
    GeneMADVM.$name="GeneMADVMFactory";
    GeneMADVM.$provider="factory";
    return GeneMADVM;
});