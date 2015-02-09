(function () {

    define([], function () {
        return function (module) {

            module.directive('limmaAccordion', ['$filter', 'alertService',
                function ($filter, alertService) {
                    return {
                        restrict: 'E',
                        templateUrl: '/container/view/elements/limmaAccordion',
                        scope: {
                            project: '=project',
                            analysis: "=analysis"
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
                                    'icon': "none"
                                },
                                {
                                    'name': 'P-Value',
                                    'field': "pValue",
                                    'icon': "<="
                                },
                                {
                                    'name': 'q-Value',
                                    'field': "qValue",
                                    'icon': "<="
                                }
               ];

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
                                'pValue': {
                                    field: 'pValue',
                                    value: 0.05,
                                    op: '<='
                                },
                                'qValue': {
                                    field: 'qValue',
                                    value: undefined,
                                    op: '<='
                                }
                            };
                            scope.filteredResults = undefined;
                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                            }

                            scope.viewGenes = function () {

                                var shownGenes = scope.applyFilter(scope.analysis.results);

                                var max = Number.NEGATIVE_INFINITY,
                                    min = Number.POSITIVE_INFINITY;

                                function test(d) {

                                    if (d.value > max) {
                                        max = d.value
                                    };

                                    if (d.value < min) {
                                        min = d.value
                                    };
                                };

                                scope.boxPlotGenes = {
                                    "data": shownGenes.map(function (gene, i) {
                                        return {
                                            'control': {
                                                'values': scope.analysis.control.keys.map(function (label) {

                                                    var datapoint = scope.project.dataset.expression.get([gene.id, label]);
                                                    test(datapoint);
                                                    return datapoint;
                                                })
                                            },
                                            'experiment': {
                                                'values': scope.analysis.experiment.keys.map(function (label) {

                                                    var datapoint = scope.project.dataset.expression.get([gene.id, label]);
                                                    test(datapoint);
                                                    return datapoint;
                                                })
                                            },
                                            'geneName': gene.id,
                                            'pValue': gene.pValue
                                        };
                                    }),
                                    'min': min - ((max - min) * .05),
                                    'max': max + ((max - min) * .05),
                                    'id': scope.analysis.randomId
                                };

                            };

                            scope.$watch('analysis', function (newval) {
                                if (newval) {
                                    scope.viewGenes()
                                }

                            })

                            scope.addSelections = function () {

                                var userselections = getKeys(scope.filteredResults);

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

                                var keys = getKeys(scope.filteredResults);
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

                            var ctr = -1;
                            scope.limmaTableOrdering = undefined;

                            scope.reorderLimmaTable = function (header) {

                                ctr = ctr * (-1);
                                if (ctr == 1) {
                                    scope.tableOrdering = header.field;
                                } else {
                                    scope.tableOrdering = "-" + header.field;
                                }
                            }

                            function getField(fieldName, results) {
                                var fieldValues = results.map(function (d) {
                                    return d[fieldName];
                                });
                                return fieldValues;
                            }

                            function getKeys(results) {
                                return getField('id', results);
                            };

                            scope.applyFilter = function (results) {

                                var filtered = $filter('filter')(results, {
                                    id: scope.filterParams.id.value
                                });

                                filtered = $filter('filterThreshold')(filtered, scope.filterParams.logFoldChange.value, scope.filterParams.logFoldChange.field, scope.filterParams.logFoldChange.op);
                                filtered = $filter('filterThreshold')(filtered, scope.filterParams.pValue.value, scope.filterParams.pValue.field);
                                filtered = $filter('filterThreshold')(filtered, scope.filterParams.qValue.value, scope.filterParams.qValue.field, scope.filterParams.qValue.op);
                                filtered = $filter('orderBy')(filtered, scope.tableOrdering);
                                scope.filteredResults = filtered;

                                return scope.filteredResults;
                            }

                            scope.applyToHeatmap = function () {

                                var labels = getKeys(scope.filteredResults);

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
    })

})()