(function () {

    define([], function () {


        return function (module) {

            module.directive('kMeansAccordion', ['alertService', 'pathService',
                function (alertService, paths) {
                    return {
                        restrict: 'E',
                        scope: {
                            analysis: "=analysis",
                            project: "=project"
                        },
                        templateUrl: paths.module + '/templates/kmeansAccordion.tpl.html',
                        link: function (scope) {
                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16),
                                dimension: undefined,
                                keys: undefined
                            };

                            scope.$watch('analysis', function (newval) {
                                if (newval) {
                                    scope.selectionParams.dimension = newval.dimension
                                }
                            })

                            function traverse(clusters) {

                                var labels = []

                                for (var i = 0; i < clusters.length; i++) {
                                    labels = labels.concat(clusters[i]);
                                };

                                return labels
                            }

                            scope.loadSelections = function (clusters) {
                                scope.selectionParams.keys = clusters
                            }

                            scope.addSelections = function () {
                                var selectionsData = {
                                    name: scope.selectionParams.name,
                                    properties: {
                                        selectionDescription: '',
                                        selectionColor: scope.selectionParams.color,
                                    },
                                    keys: traverse(scope.selectionParams.keys)
                                };

                                scope.project.dataset.selection.post({
                                        datasetName: scope.project.dataset.datasetName,
                                        dimension: scope.selectionParams.dimension

                                    }, selectionsData,
                                    function (response) {

                                        scope.project.dataset.resetSelections(scope.selectionParams.dimension)
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
                            }

                            scope.applyToHeatmap = function () {

                                var labels = traverse(scope.analysis.clusters);

                                if (scope.analysis.dimension == "column") {

                                    scope.project.generateView({
                                        viewType: 'heatmapView',
                                        labels: {
                                            row: {
                                                keys: scope.project.dataset.row.keys
                                            },
                                            column: {
                                                keys: labels
                                            }
                                        },
                                        expression: {
                                            min: scope.project.dataset.expression.min,
                                            max: scope.project.dataset.expression.max,
                                            avg: scope.project.dataset.expression.avg,
                                        },
                                        panel: {
                                            top: scope.analysis
                                        }
                                    });

                                } else {
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
                                        },
                                        panel: {
                                            side: scope.analysis
                                        }
                                    });
                                }
                            };

                        }
                    };
            }])


            return module

        }

    })

})()