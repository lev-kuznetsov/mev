define(["lodash"], function(_){ "use strict";
    function DeseqVM(mevBoxplotService, mevAnalysisTypes, mevPcaAnalysisType, mevHclAnalysisType,
                     mevWgcnaAnalysisType, $timeout, $window){
        function factory($scope, project, analysis){
            var projection = {
                ids: function(row){
                    return row.id
                }
            };
            var _self=this;
            var scope = $scope;
            $scope.project = project;
            $scope.analysis = analysis;
            this.analysis = analysis;
            this.project = project;
            scope.analysisTypes = mevAnalysisTypes.all();
            scope.analysis.getFilteredKeys = function(dimension){
                if(dimension==="row")
                    return scope.filteredResults.map(function(item){
                        return item.id;
                    });
            };
            scope.analysis.getOriginalInputKeys=function(dimension){
                if(dimension==="column"){
                    var selectionNames = _.isObject(scope.analysis.params.control)
                        ? [scope.analysis.params.experiment.name, scope.analysis.params.control.name]
                        : [scope.analysis.params.experiment, scope.analysis.params.control]
                    var keys = scope.project.dataset.selections.unionByName("column", selectionNames);
                    keys.displayName = selectionNames.join("+");
                    return keys;
                }

            };
            this.heatmapView = scope.heatmapView = project.generateView({
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
            scope.headers = [
                {
                    'name': 'ID',
                    'field': "id",
                    'icon': "search"
                },
                {
                    'name': 'Log-Fold-Change',
                    'field': "logFoldChange",
                    'icon': [">=", "<="]
                },
                {
                    'name': 'Mean Expression Control',
                    'field': "meanExpressionControl",
                    'icon': "none"
                },
                {
                    'name': 'Mean Expression Experimental',
                    'field': "meanExpressionExperimental",
                    'icon': "none"
                },
                {
                    'name': 'P-Value',
                    'field': "pValue",
                    'icon': ["<=", ">="],
                    'default': 0.05
                },
                {
                    'name': 'q-Value',
                    'field': "qValue",
                    'icon': ["<=", ">="]
                }
            ];

            scope.filteredResults = [];
            scope.viewGenes = function(filteredResults){
                scope.applyToHeatmap(filteredResults);
            }

            scope.viewPage = function(pageResults){
                var control = _.isObject(scope.analysis.params.control)
                        ? scope.analysis.params.control
                        : _.find(scope.project.dataset.column.selections, function(selection){return selection.name===scope.analysis.params.control;});
                var experiment = _.isObject(scope.analysis.params.experiment)
                    ? scope.analysis.params.experiment
                    : _.find(scope.project.dataset.column.selections, function(selection){return selection.name===scope.analysis.params.experiment;});

                scope.boxPlotGenes = mevBoxplotService.prepareBoxPlotData(scope.project.dataset, pageResults,
                    [control, experiment],
                    scope.analysis.randomId);
            }

            scope.selectionParams = {
                name: undefined,
                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
            };

            scope.applyToHeatmap = function (filteredResults) {
                var labels = filteredResults.map(projection.ids);;
                _self.heatmapView = scope.heatmapView = scope.heatmapView.applyFilter("row", labels);
            };

        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    DeseqVM.$inject=["mevBoxplotService", "mevAnalysisTypes", "mevPcaAnalysisType", "mevHclAnalysisType",
        "mevWgcnaAnalysisType", "$timeout", "$window"];
    DeseqVM.$name="DeseqVMFactory";
    DeseqVM.$provider="factory";
    return DeseqVM;
});