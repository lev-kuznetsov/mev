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
								//this row just shows the row index, doesn't use any data from the row
//								{
//									headerName: "#", cellRenderer: function(params) {
//										return params.node.id + 1;
//									} 
//								 },
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
                            	if(Array.isArray(header.icon) ||
                            			header.icon.charAt(0) === "<" ||
                            			header.icon.charAt(0) === ">" || 
                            			header.icon.charAt(0) === "="){
                            		header.filter = "number";
                            	}
                            	
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
	                        
	                        $scope.pageSize = '20';

	                        $scope.gridOptions = {
	                            // note - we do not set 'virtualPaging' here, so the grid knows we are doing standard paging
//	                            enableSorting: true,
//	                            enableFilter: true,
	                        	enableServerSideSorting: true,
	                            enableServerSideFilter: true,
	                            enableColResize: true,
	                            columnDefs: columnDefs,
	                            afterFilterChanged: {
	                            	
	                            }
	                        };

	                        $scope.onPageSizeChanged = function() {
	                            $scope.createNewDatasource();
	                        };
	                        
	                        $scope.createNewDatasource = function () {	                            
	                            var dataSource = {
	                                rowCount: $scope.analysis.results.length, //not setting the row count, infinite paging will be used
	                                pageSize: parseInt($scope.pageSize), // changing to number, as scope keeps it as a string
	                                getRows: function (params) {
	                                    // this code should contact the server for rows. however for the purposes of the demo,
	                                    // the data is generated locally, a timer is used to give the experience of
	                                    // an asynchronous call
	                                    console.log('asking for ' + params.startRow + ' to ' + params.endRow);
//	                                    setTimeout( function() {
                                        // take a chunk of the array, matching the start and finish times
                                        var rowsThisPage = $scope.analysis.results.slice(params.startRow, params.endRow);
                                        // see if we have come to the last page. if we have, set lastRow to
                                        // the very last row of the last page. if you are getting data from
                                        // a server, lastRow could be returned separately if the lastRow
                                        // is not in the current page.
                                        var lastRow = -1;
                                        if ($scope.analysis.results.length <= params.endRow) {
                                            lastRow = $scope.analysis.results.length;
                                        }
                                        params.successCallback(rowsThisPage, lastRow);
                                        
                                        $scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData($scope.project.dataset, rowsThisPage, 
                                        		[$scope.analysis.params.control, $scope.analysis.params.experiment], 
                                        		$scope.analysis.randomId);
//	                                    }, 500);
	                                }
	                            };

	                            $scope.gridOptions.api.setDatasource(dataSource);
	                        }


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
                            	
                            	scope.filteredResults = filteredResults;
                            	scope.applyToHeatmap(filteredResults);
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
//                            scope.viewGenes(scope.analysis.results);
//                            scope.gridOptions.api.setRowData(filteredResults);
                            scope.createNewDatasource();
                            scope.gridOptions.api.sizeColumnsToFit() 
                        }

                    };
                    
            }])

        }
    })

})()