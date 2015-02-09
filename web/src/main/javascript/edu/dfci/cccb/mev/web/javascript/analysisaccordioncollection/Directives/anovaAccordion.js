(function () {

    define([], function () {


        return function (module) {

            module.directive('anovaAccordion', ['$filter', 'alertService',
                function ($filter, alertService) {
                    return {
                        restrict: 'E',
                        templateUrl: '/container/view/elements/anovaAccordion',
                        scope: {
                            analysis: "=analysis",
                            project: '=project',
                        },
                        link: function (scope) {

                            scope.headers = [
                                {
                                    'name': 'ID',
                                    'value': "id"
                                },
                                {
                                    'name': 'P-Value',
                                    'value': "pValue"
                                },
                                {
                                    'name': 'Pairwise LFC',
                                    'value': 'pairwise_log_fold_change'
                                }
                            ]

                            scope.filterParams = {
                                'id': '',
                                'pValue': undefined
                            };

                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16),
                                dimension: 'row'
                            };

                            var ctr = -1;
                            scope.tableOrdering = undefined;
                            scope.reorderTable = function (header) {

                                ctr = ctr * (-1);
                                if (ctr == 1) {
                                    scope.tableOrdering = header.value;
                                } else {
                                    scope.tableOrdering = "-" + header.value;
                                }
                            };

                            function traverse(results) {
                                var step1 = $filter('filter')(results, {
                                    id: scope.filterParams.id
                                });

                                var step2 = $filter('filterThreshold')(step1, scope.filterParams.pValue, 'pValue');

                                var step3 = step2.map(function (d) {
                                    return d.id;
                                });

                                return step3;
                            }

                            scope.addSelections = function () {

                                var keys = traverse(scope.analysis.results);
                                var selectionsData = {
                                    name: scope.selectionParams.name,
                                    properties: {
                                        selectionDescription: '',
                                        selectionColor: scope.selectionParams.color,
                                    },
                                    keys: keys
                                };

                                scope.project.dataset.selection.post({
                                        datasetName: scope.project.dataset.datasetName,
                                        dimension: scope.selectionParams.dimension

                                    }, selectionsData,
                                    function (response) {

                                        scope.project.dataset.resetSelections('row')
                                        var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                        var header = "Heatmap Selection Addition";

                                        scope.selectionParams.color = '#' + Math
                                            .floor(Math.random() * 0xFFFFFF << 0)
                                            .toString(16)

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

                                var keys = traverse(scope.analysis.results);
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

                                var labels = traverse(scope.analysis.results);

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
            
            return module

        }
    })


})()