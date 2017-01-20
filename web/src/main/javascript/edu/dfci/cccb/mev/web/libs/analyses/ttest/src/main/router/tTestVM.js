define(["lodash"], function(_){ "use strict";
    function tTestVM(mevBoxplotService, mevAnalysisTypes, mevGseaAnalysisType, mevPcaAnalysisType, mevHclAnalysisType,
                     mevWgcnaAnalysisType){
        
        function factory(scope, project, analysis){
            var _self = this;
            var projection = {
                ids: function(row){
                    return row.id
                }
            };
            scope.analysisTypes = mevAnalysisTypes.all();
            this.analysisId=analysis.name;
            scope.project = project;
            scope.analysis = analysis;
            this.project = project;
            this.analysis = analysis;
            scope.heatmapView = this.heatmapView = project.generateView({
                viewType:'heatmapView',
                note: analysis.name,
                labels:{
                    row:{keys:project.dataset.row.keys},
                    column:{keys:project.dataset.column.keys}
                },
                expression:{
                    min: project.dataset.expression.min,
                    max: project.dataset.expression.max,
                    avg: project.dataset.expression.avg,
                }
            });
            scope.$on("ui:resultsTable:pageChanged", function($event, pageResults){
                var control = _.isObject(analysis.params.control)
                    ? analysis.params.control
                    : _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.controlName;});
                var experiment = _.isObject(analysis.params.experiment)
                    ? analysis.params.experiment
                    : _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.experimentName;});
                scope.boxPlotGenes = mevBoxplotService.prepareBoxPlotData(project.dataset, pageResults,
                    [control, experiment],
                    analysis.randomId);
            });

            scope.headers = [{
                'name': 'ID',
                'field': "id",
                'icon': "search"
            },{
                'name': 'P-Value',
                'field': "pValue",
                'icon': ["<=", ">="],
                'default': 0.05,
                'max': 0.05,
                'min': 0.00,
                'step': 0.01
            },{
                'name': 'Log-Fold-Change',
                'field': "logFoldChange",
                'icon': [">=", "<="]
            }]

            _self.filteredResults = [];
            scope.filteredResults = _self.filteredResults;
            scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
                // _self.filteredResults.length = 0;
                // filteredResults.map(function(item){
                //     _self.filteredResults.push(item);
                // });
                scope.applyToHeatmap(filteredResults);
            });

            scope.selectionParams = {
                name: undefined,
                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
            };
            analysis.getFilteredKeys = function(dimension){
                if(dimension==="row")
                    return scope.filteredResults.map(function(item){
                        return item.id;
                    });
            };
            analysis.getOriginalInputKeys=function(dimension){
                if(dimension==="column"){
                    var selectionNames = [analysis.params.experimentName, analysis.params.controlName];

                    var keys = project.dataset.selections.unionByName("column", selectionNames);
                    keys.displayName = selectionNames.join("+");
                    return keys;
                }

            };
            scope.applyToHeatmap = function (filteredResults) {
                var labels = filteredResults.map(projection.ids);
                scope.heatmapView = scope.heatmapView.applyFilter("row", labels);
            };


        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    tTestVM.$inject=["mevBoxplotService", "mevAnalysisTypes", "mevGseaAnalysisType", "mevPcaAnalysisType", "mevHclAnalysisType",
        "mevWgcnaAnalysisType"];
    tTestVM.$name="tTestVMFactory";
    tTestVM.$provider="factory";
    return tTestVM;
});