(function () {

    define(["lodash"], function (_) {


        return function (module) {

            module.directive('anovaAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService', 'BoxPlotService',
                function (tableFilter, alertService, projection, paths, BoxPlotService) {
                    return {
                        restrict: 'E',
                        templateUrl:paths.module + '/templates/anovaAccordion.tpl.html',
                        scope: {
                            analysis: "=analysis",
                            project: '=project',
                            heatmapView: "=",
                            isItOpen: '@'
                        },
                        controller: ["$scope", function(scope){
                            //variable to remove embedded pair information
                            scope.filteredResults = undefined;
                            scope.cleanData = expandEmbedded(scope.analysis.results);
                            scope.headers = [{
                                'name': 'ID',
                                'field': "id",
                                'icon': "search"
                            },{
                                'name': 'Partner A',
                                'field': 'partnerA',
                                'icon': "search"
                            },{
                                'name': 'Partner B',
                                'field': 'partnerB',
                                'icon': "search"
                            },{
                                'name': 'P-Value',
                                'field': "pValue",
                                'icon': ["<=", ">="],
                                'default': 0.05
                            },{
                                'name': 'Pairwise LFC',
                                'field': 'lfc',
                                'icon': [">=", "<="]
                            }
                            ];

                            scope.viewGenes = function(filteredResults){
                                console.debug("anova viewGenes", filteredResults);
                                scope.filteredResults = filteredResults;
//                            	scope.cleanData = filteredResults;
//                            	scope.filteredResults = tableFilter(scope.cleanData, filterParams);
                                scope.$emit("ui:anova:filteredResults", _.uniq(filteredResults, 'id'));
                                scope.applyToHeatmap(filteredResults);
                            }
                            scope.viewPage = function(pageResults){
                                var groups = scope.analysis.params.data.map(function(selectionName){
                                    return _.find(scope.project.dataset.column.selections, function(selection){return selection.name===selectionName;});
                                });

                                pageResultsUniq = _.uniq(pageResults, 'id')
                                scope.$emit("ui:anova:pageResults", pageResultsUniq);
                                scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(scope.project.dataset, pageResultsUniq,
                                    groups,
                                    scope.analysis.randomId);
                            };

                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16),
                                dimension: 'row'
                            };

                            function traverse(results) {

                                return results.map(projection.ids)
                                    //fix#945
                                    //the table shows a gene multiple times (once for each pairing set)
                                    //the heatmap, however, needs the unique gene id's
                                    .filter(function(value, index, array){
                                        return array.indexOf(value) === index;
                                    });
                            }


                            scope.addSelections = function () {

                                var keys = traverse(scope.filteredResults);
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

                                var keys = traverse(scope.filteredResults);
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

                            scope.applyToHeatmap = function (filteredResults) {

                                var labels = traverse(filteredResults);

                                scope.heatmapView = scope.project.generateView({
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

                            function expandEmbedded(data){
                                //This function is supposed to expand the partner and ratio
                                // data into their own columns

                                var expanded = []

                                for (var gene = 0; gene < data.length; gene++){

                                    for (var pair = 0; pair < data[gene]['pairwise_log_fold_change'].length; pair++){
                                        var cleanRow = {
                                            "id": data[gene]['id'],
                                            "pValue": data[gene]['pValue'],
                                            "lfc": data[gene]['pairwise_log_fold_change'][pair]['ratio'],
                                            'partnerA': data[gene]['pairwise_log_fold_change'][pair]['partnerA'],
                                            'partnerB': data[gene]['pairwise_log_fold_change'][pair]['partnerB']
                                        }

                                        expanded.push(cleanRow)
                                    }

                                }

                                return expanded
                            }

                        }],
                        link: function (scope) {                        	
                        }
                    };
            }])
            
            return module

        }
    })


})()