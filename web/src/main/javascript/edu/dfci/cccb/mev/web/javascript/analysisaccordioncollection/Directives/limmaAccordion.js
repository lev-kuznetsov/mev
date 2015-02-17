(function () {

    define([], function () {
        return function (module) {

            module.directive('limmaAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService',
                function (tableFilter, alertService, projection, paths) {
                    return {
                        restrict: 'E',
                        templateUrl: paths.module + '/templates/limmaAccordion.tpl.html',
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
                                    'name': 'Q-Value',
                                    'field': "qValue",
                                    'icon': "<="
                                }
                            ];

                            scope.filterParams = {
                                'id': {
                                    field: 'id',
                                    value: undefined,
                                    op: "~="
                                },
                                'logFoldChange': {
                                    field: 'logFoldChange',
                                    value: undefined,
                                    op: '>='
                                },
                                'pValue': {
                                    field: 'pValue',
                                    value: undefined,
                                    op: '<='
                                },
                                'qValue': {
                                    field: 'qValue',
                                    value: undefined,
                                    op: '<='
                                }
                            };
                            
                            
                            scope.$watch('analysis', function (newval) {
                                if (newval) {
                                    scope.viewGenes()
                                }
                            })
                            
                            scope.$watch('filterParams.pValue.value', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.$watch('filterParams.qValue.value', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.$watch('filterParams.id.value', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.$watch('filterParams.logFoldChange.value', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.$watch('filterParams.logFoldChange.op', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.filteredResults = undefined;
                            
                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                            }

                            scope.viewGenes = function () {
                            	
                                scope.filteredResults = tableFilter(scope.analysis.results, scope.filterParams);
                                var shownGenes = scope.filteredResults

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
                                                    test(datapoint, max, min);
                                                    return datapoint;
                                                })
                                            },
                                            'experiment': {
                                                'values': scope.analysis.experiment.keys.map(function (label) {

                                                    var datapoint = scope.project.dataset.expression.get([gene.id, label]);
                                                    test(datapoint, max, min);
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