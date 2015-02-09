(function () {

    define([], function () {

        return function (module) {

            module.directive('deseqAccordion', ['$filter', 'alertService',
                function ($filter, alertService) {
                    return {
                        restrict: 'E',
                        templateUrl: module.path + '/templates/DESeqAccordion.tpl.html',
                        scope: {
                            project: "=project",
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
                            };


                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                            };

                            function traverse(results) {
                                var ids = results.map(function (d) {
                                    return d.id
                                });
                                return ids;
                            }

                            scope.addSelections = function () {

                                var keys = traverse(scope.filteredResults);
                                var selectionData = {
                                    name: scope.selectionParams.name,
                                    properties: {
                                        selectionDescription: '',
                                        selectionColor: scope.selectionParams.color
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


                        }

                    };
            }])

            return module

        }

    })


})()