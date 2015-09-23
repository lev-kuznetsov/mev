(function () {

    define([], function () {
        return function (module) {

            module.directive('limmaAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService', 'BoxPlotService',
                function (tableFilter, alertService, projection, paths, BoxPlotService) {
                    return {
                        restrict: 'E',
                        templateUrl: paths.module + '/templates/limmaAccordion.tpl.html',
                        scope: {
                            project: '=project',
                            analysis: "=analysis",
                            heatmapView: "=",
                            isItOpen: "@",
                            isShowHeatmapTab: "@"
                        },
                        link: function (scope) {
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
                                    'name': 'Average Expression',
                                    'field': "averageExpression",
                                    'icon': [">=", "<="]
                                },
                                {
                                    'name': 'P-Value',
                                    'field': "pValue",
                                    'icon': "<=",
                                    'default': 0.05
                                },
                                {
                                    'name': 'Q-Value',
                                    'field': "qValue",
                                    'icon': "<="
                                }
                            ];
                            
                            scope.filteredResults = undefined;
                            
                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                            }
                                                        
                            scope.viewGenes = function (filterParams) {
                            	
                                scope.filteredResults = tableFilter(scope.analysis.results, filterParams);                                
                                scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(scope.project.dataset, scope.filteredResults, 
                                		[scope.analysis.control, scope.analysis.experiment], 
                                		scope.analysis.randomId);
                                console.debug("limma boxPloGenes", scope.boxPlotGenes);
                                //also filter the heat map
                                scope.applyToHeatmap();
                                
                            };

                            scope.addSelections = function () {

                                var userselections = scope.filteredResults.map(projection.ids);

                                var selectionData = {
                                    name: scope.selectionParams.name,
                                    properties: {
                                        selectionDescription: '',
                                        selectionColor: scope.selectionParams.color,
                                    },
                                    keys: userselections
                                };

                                scope.project.dataset.selection.post({
                                        datasetName: scope.project.dataset.datasetName,
                                        dimension: "row"

                                    }, selectionData,
                                    function (response) {
                                        scope.project.dataset.resetSelections('row')
                                        var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                        var header = "Heatmap Selection Addition";

                                        alertService.success(message, header);
                                    },
                                    function (data, status, headers, config) {
                                        var message = "Couldn't add new selection. If " + "problem persists, please contact us.";

                                        var header = "Selection Addition Problem (Error Code: " + status + ")";

                                        alertService.error(message, header);
                                    });

                            }

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

                                scope.project.dataset.selection.export({
                                        datasetName: scope.project.dataset.datasetName,
                                        dimension: "row"

                                    }, selectionData,
                                    function (response) {
                                        scope.project.dataset.resetSelections('row');
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

                            scope.applyToHeatmap = function () {

//                                var labels = getKeys(scope.filteredResults);
                            	                                
                            	var labels = scope.filteredResults.map(projection.ids);;

                                scope.heatmapView = scope.project.generateView({
                                    viewType: 'heatmapView',
                                    note: scope.analysis.name,
                                    labels: {
                                        column: {
                                            keys: scope.project.dataset.column.keys
                                        },
                                        row: {
                                            keys: labels
                                        }
                                    },
                                    expression: {
                                        min: scope.project.dataset.expression.min,
                                        max: scope.project.dataset.expression.max,
                                        avg: scope.project.dataset.expression.avg,
                                    }
                                });

                            };
                        }

                    };
                    
            }])

        }
    })

})()