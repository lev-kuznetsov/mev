(function () {

    define(["lodash"], function (_) {

        return function (module) {

            module.directive('tTestAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService', 'BoxPlotService',
                function (resultsFilter, alertService, projection, paths, BoxPlotService) {
                    return {
                        restrict: 'E',
                        templateUrl: paths.module + '/templates/tTestAccordion.tpl.html',
                        scope: {
                            project: "=project",
                            analysis: "=analysis",
                            isItOpen: "@"
                        },
                        link: function (scope, elem, attrs) {                        	
                                                        
                            scope.headers = [{
                                'name': 'ID',
                                'field': "id",
                                'icon': "search"
                            },{
                                'name': 'P-Value',
                                'field': "pValue",
                                'icon': "<=",
                                'default': 0.05,
                                'max': 0.05,
                                'min': 0.00,
                                'step': 0.01
                            },{
                                'name': 'Log-Fold-Change',
                                'field': "logFoldChange",
                                'icon': [">=", "<="]
                            }]
                            
                            scope.viewGenes = function (filterParams) {
                            	scope.tTest = scope.analysis;
                                scope.filteredResults = resultsFilter(scope.analysis.results, filterParams);
                            	scope.$emit("ui:filteredResults", scope.filteredResults);
                            	
	                            //also filter heatmap
	                            scope.applyToHeatmap();
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