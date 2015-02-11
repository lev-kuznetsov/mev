(function () {

    define([], function () {

        return function (module) {

            module.directive('tTestAccordion', ['tableResultsFilter', 'alertService', 'projectService', 'pathService',
                function (tableResultsFilter, alertService, projection, paths) {
                    return {
                        restrict: 'E',
                        templateUrl: paths.module + '/templates/tTestAccordion.tpl.html',
                        scope: {
                            project: "=project",
                            analysis: "=analysis"
                        },
                        link: function (scope) {

                            scope.$watch('analysis', function (newval) {
                                if (newval) {
                                    scope.tTest = scope.analysis;
                                    scope.filteredResults = scope.tTest.results;
                                }
                            });

                            scope.headers = {
                                'ID': {
                                    name: "id",
                                    sort: -1
                                },
                                'P-Value': {
                                    name: "pValue",
                                    sort: -1
                                },
                                'Log Fold Change': {
                                    name: "logFoldChange",
                                    sort: -1
                                }
                            };

                            scope.filterParams = {
                                'id': {
                                    field: 'id',
                                    value: undefined,
                                    op: "="
                                },
                                'logFoldChange': {
                                    field: 'logFoldChange',
                                    value: undefined,
                                    op: '>='
                                },
                                'pValueThreshold': {
                                    field: 'pValue',
                                    value: 0.05,
                                    op: '<='
                                }
                            };
                            
                            scope.applyFilter = function () {
                                scope.filteredResults = tableResultsFilter(scope.analysis.results, scope.filterParams);
                            };

                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
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

                            scope.getCaretCss = function (header) {
                                if (header.sort == 1) {
                                    return "caret-up";
                                } else {
                                    return "caret-down";
                                }
                            }

                            var ctr = -1;
                            scope.tTestTableOrdering = undefined;
                            scope.reorderTTestTable = function (header, $event) {

                                ctr = ctr * (-1);
                                scope.headers[header].sort = scope.headers[header].sort * (-1);
                                if (scope.headers[header].sort == 1) {
                                    scope.tTestTableOrdering = scope.headers[header].name;
                                } else {
                                    scope.tTestTableOrdering = "-" + scope.headers[header].name;
                                }
                            };
                            
                            scope.applyToHeatmap = function () {

                                var labels = scope.filteredResults.map(projection.ids);

                                scope.project.generateView({
                                    viewType: 'heatmapView',
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

        return module
    })

})()