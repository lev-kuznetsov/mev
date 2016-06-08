define([], function () {

        return function (module) {

            module.directive('deseqAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService', 'BoxPlotService', 'mevAnalysisTypes',
                function (tableFilter, alertService, projection, paths, BoxPlotService, mevAnalysisTypes) {
                    return {
                        restrict: 'E',
                        templateUrl: paths.module + '/templates/DESeqAccordion.tpl.html',
                        scope: {
                            project: "=project",
                            analysis: "=analysis",
                            heatmapView: "=",
                            isItOpen: "@"
                        },
                        controller: ["$scope", "mevAnalysisTypes", function(scope, mevAnalysisTypes){
                            scope.analysisTypes = mevAnalysisTypes.all();

                            scope.analysis.getFilteredKeys = function(dimension){
                                if(dimension==="row")
                                    return scope.filteredResults.map(function(item){
                                        return item.id;
                                    });
                            };
                            scope.analysis.getOriginalInputKeys=function(dimension){
                                if(dimension==="column"){
                                    var selectionNames = [scope.analysis.params.experiment, scope.analysis.params.control];

                                    var keys = scope.project.dataset.selections.unionByName("column", selectionNames);
                                    keys.displayName = selectionNames.join("+");
                                    return keys;
                                }

                            };
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

                            scope.filteredResults = undefined;

//                            scope.applyFilter = function () {
//
//                                scope.filteredResults = tableFilter(scope.analysis.results, scope.filterParams)
//
//                                return scope.filteredResults;
//                            };


                            scope.viewGenes = function(filteredResults){
                                scope.filteredResults = filteredResults;
                                //and filter the heatmap
//	                       		scope.$emit("ui:filteredResults", scope.filteredResults);
                                scope.applyToHeatmap(filteredResults);
                            }

                            scope.viewPage = function(pageResults){
                                var control = _.find(scope.project.dataset.column.selections, function(selection){return selection.name===scope.analysis.params.control;});
                                var experiment = _.find(scope.project.dataset.column.selections, function(selection){return selection.name===scope.analysis.params.experiment;});
                                scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(scope.project.dataset, pageResults,
                                    [control, experiment],
                                    scope.analysis.randomId);
                            }

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

                            scope.applyToHeatmap = function (filteredResults) {
                                var labels = filteredResults.map(projection.ids);;
                                scope.heatmapView = scope.heatmapView.applyFilter("row", labels);
                            };

                        }],
                        link: function (scope) {            

                        }

                    };
            }])

            return module

        }

})