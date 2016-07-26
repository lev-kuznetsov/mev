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

            scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
                _self.filteredResults = filteredResults;
                scope.filteredResults = _self.filteredResults;
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
            scope.addSelections = function () {

                var keys = scope.filteredResults.map(projection.ids);

                var selectionData = {
                    name: scope.selectionParams.name,
                    properties: {
                        selectionDescription: '',
                        selectionColor: scope.selectionParams.color,
                    },
                    keys: keys
                };

                project.dataset.selection.post({
                        datasetName: project.dataset.datasetName,
                        dimension: "row"

                    }, selectionData,
                    function (response) {
                        project.dataset.resetSelections('row')
                        var message = "Added " + scope.selectionParams.name + " as new Selection!";
                        var header = "Heatmap Selection Addition";

                        alertService.success(message, header);
                    },
                    function (data, status, headers, config) {
                        var message = "Couldn't add new selection. If " + "problem persists, please contact us.";

                        var header = "Selection Addition Problem (Error Code: " + status + ")";

                        alertService.error(message, header);
                    });

            };

            scope.exportParams = {
                name: undefined,
                color: '#ffffff'
            };
            scope.exportSelection = function () {

                var keys = scope.filteredResults.map(projection.ids);

                var selectionData = {
                    name: scope.exportParams.name,
                    properties: {
                        selectionDescription: '',
                        selectionColor: scope.exportParams.color,
                    },
                    keys: keys
                };

                project.dataset.selection.export({
                        datasetName: project.dataset.datasetName,
                        dimension: "row"

                    }, selectionData,
                    function (response) {
                        project.dataset.resetSelections('row');
                        var message = "Added " + scope.exportParams.name + " as new Dataset!";
                        var header = "New Dataset Export";

                        alertService.success(message, header);
                    },
                    function (data, status, headers, config) {
                        var message = "Couldn't export new dataset. If " + "problem persists, please contact us.";

                        var header = "New Dataset Export Problem (Error Code: " + status + ")";

                        alertService.error(message, header);
                    });

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