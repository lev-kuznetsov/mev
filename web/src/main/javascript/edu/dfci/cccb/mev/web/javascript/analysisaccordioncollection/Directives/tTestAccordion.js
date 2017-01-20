(function () {

    define(["lodash"], function (_) {

        return function (module) {

            module.directive('tTestAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService', 'BoxPlotService', 'mevAnalysisTypes',
                function (resultsFilter, alertService, projection, paths, BoxPlotService, mevAnalysisTypes) {
                    return {
                        restrict: 'E',
                        templateUrl: paths.module + '/templates/tTestAccordion.tpl.html',
                        scope: {
                            project: "=project",
                            analysis: "=analysis",
                            heatmapView: "=",
                            isItOpen: "@"
                        },
                        controller: ["$scope", function(scope){
                            scope.analysisTypes = mevAnalysisTypes.all();
                        	scope.$on("ui:resultsTable:pageChanged", function($event, pageResults){
                				var control = _.find(scope.project.dataset.column.selections, function(selection){return selection.name===scope.analysis.params.controlName;});
                	        	var experiment = _.find(scope.project.dataset.column.selections, function(selection){return selection.name===scope.analysis.params.experimentName;});
                				scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(scope.project.dataset, pageResults, 
                						[control, experiment],
                						scope.analysis.randomId);
                			});

                            scope.headers = [{
                                'name': 'ID',
                                'field': "id",
                                'icon': "search"
                            },{
                                'name': 'P-Value',
                                'field': "pValue",
                                'icon': ["<=", ">="],
                                'default': 0.05,
                                'max': 0.05,
                                'min': 0.00,
                                'step': 0.01
                            },{
                                'name': 'Log-Fold-Change',
                                'field': "logFoldChange",
                                'icon': [">=", "<="]
                            }]

                            scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
                                scope.filteredResults = filteredResults;
                                scope.applyToHeatmap(filteredResults);
                            });

                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                            };
                            scope.analysis.getFilteredKeys = function(dimension){
                                if(dimension==="row")
                                    return scope.filteredResults.map(function(item){
                                        return item.id;
                                    });
                            };
                            scope.analysis.getOriginalInputKeys=function(dimension){
                                if(dimension==="column"){
                                    var selectionNames = [scope.analysis.params.experimentName, scope.analysis.params.controlName];

                                    var keys = scope.project.dataset.selections.unionByName("column", selectionNames);
                                    keys.displayName = selectionNames.join("+");
                                    return keys;
                                }

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

                            scope.applyToHeatmap = function (filteredResults) {
                                var labels = filteredResults.map(projection.ids);
                                scope.heatmapView = scope.heatmapView.applyFilter("row", labels);
                            };


                        }],
                        link: function (scope, elem, attrs) {                        	
                                                        
                            
                        }

                    };
            }])
        }

        return module
    })

})()