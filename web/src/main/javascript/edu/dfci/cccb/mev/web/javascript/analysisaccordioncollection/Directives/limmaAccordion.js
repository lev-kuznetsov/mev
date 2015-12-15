(function () {

    define([], function () {
        return function (module) {

            module.directive('limmaAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService', 'BoxPlotService',
                function (tableFilter, alertService, projection, paths, BoxPlotService) {
                    return {
                        restrict: 'E',
                        templateUrl: paths.module + '/templates/limmaAccordion.tpl.html',
                        scope: {
                            project: '=project',
                            analysis: "=analysis",
                            heatmapView: "=",
                            isItOpen: "@",
                            isShowHeatmapTab: "@"
                        },
                        controller: ["$scope", function($scope){
                        	$scope.headers = [
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
                                     'icon': [">=", "<="]
                                 },
                                 {
                                     'name': 'P-Value',
                                     'field': "pValue",
                                     'icon': "<=",
                                     'default': 0.05
                                 },
                                 {
                                     'name': 'Q-Value',
                                     'field': "qValue",
                                     'icon': "<="
                                 }
                             ];
                        	
                            var columnDefs = $scope.headers.map(function(header){
                            	header["headerName"]=header.name;
                            	return header;
                            });
                            	
	                        var rowData = [
//                              {make: "Toyota", model: "Celica", price: 35000},
//                              {make: "Ford", model: "Mondeo", price: 32000},
//                              {make: "Porsche", model: "Boxter", price: 72000}
	                        ];	
	                        $scope.gridOptions = {
                              columnDefs: columnDefs,
                              rowData: rowData
	                        };
                        }],
                        link: function (scope) {
                            
                            
                            scope.filteredResults = undefined;
                            
                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                            }
// if using events, must filter on "id" so as not to process events raised by other resultTables ont he same page
// to do that: (1) set unique id on the <result-table> element and (2) check targetScopeFilter in this handler
// In the end it's easier to use a callback function (such as viewGenes below)
//                            scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
//                            	var labels = filteredResults.map(projection.ids);
//                            	scope.applyToHeatmap(filteredResults);
//                            });
                            scope.viewGenes = function (filteredResults) {
                            	scope.gridOptions.api.setRowData(filteredResults);
                            	scope.filteredResults = filteredResults;
                            	scope.applyToHeatmap(filteredResults);
                                scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(scope.project.dataset, filteredResults, 
                                		[scope.analysis.params.control, scope.analysis.params.experiment], 
                                		scope.analysis.randomId);
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
	                          
                            scope.applyToHeatmap = function (filteredResults) {
                            	                                
                            	var labels = filteredResults.map(projection.ids);

                                scope.heatmapView = scope.project.generateView({
                                    viewType: 'heatmapView',
                                    note: scope.analysis.name,
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
                            scope.viewGenes(scope.analysis.results);
                        }

                    };
                    
            }])

        }
    })

})()