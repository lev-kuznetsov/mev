(function () {

    define(["lodash", "crossfilter"], function (_, crossfilter) {
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
                        	var xf = crossfilter($scope.analysis.results);
                            var columnDefs = $scope.headers.map(function(header){
                            	header["headerName"]=header.name;
                            	if(Array.isArray(header.icon) ||
                            			header.icon.charAt(0) === "<" ||
                            			header.icon.charAt(0) === ">" || 
                            			header.icon.charAt(0) === "="){
                            		header.filter = "number";
                            		header.filterParams =  {newRowsAction: 'keep'};
                            		header.filterParams.xd = xf.dimension(function(d){
                    					return Number(d[header.field]);
                    				});
                            	}else if(header.field === "id"){
                            		header.filterParams = {
                            				values: _.pluck($scope.analysis.results, header.field), 
                            				newRowsAction: 'keep', 
                            				xd: xf.dimension(function(d){
                            					return d[header.field];
                            				})
                            		};
                            	}
                            	header.filterParams.apply = true;

    	                        
                            	
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
	                            columnDefs: columnDefs
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
//                                        setTimeout( function() {
                                            // take a chunk of the array, matching the start and finish times
                                        	lastParams = params;
                                        	
                                            var dataAfterSortingAndFiltering = sortAndFilter(params.sortModel, params.filterModel);
                                            var rowsThisPage = dataAfterSortingAndFiltering.slice(params.startRow, params.endRow);
                                            // see if we have come to the last page. if we have, set lastRow to
                                            // the very last row of the last page. if you are getting data from
                                            // a server, lastRow could be returned separately if the lastRow
                                            // is not in the current page.
                                            var lastRow = -1;
                                            if (dataAfterSortingAndFiltering.length <= params.endRow) {
                                                lastRow = dataAfterSortingAndFiltering.length;
                                            }
                                            params.successCallback(rowsThisPage, lastRow);
//                                            this.rowCount = dataAfterSortingAndFiltering.length;
                                            $scope.$emit("ui:resultsTable:pageChanged", rowsThisPage);
                                            $scope.$emit("ui:resultsTable:filteredResults", dataAfterSortingAndFiltering);
//                                        }, 500);
                                    }
                                };

	                            $scope.gridOptions.api.setDatasource(dataSource);
	                        };
	                        
	                        function sortAndFilter(sortModel, filterModel) {
	                            return sortData(sortModel, filterData(filterModel, $scope.analysis.results));
	                        }

	                        function sortData(sortModel, data) {
	                            var sortPresent = sortModel && sortModel.length > 0;
	                            if (!sortPresent) {
	                                return data;
	                            }
	                            // do an in memory sort of the data, across all the fields
	                            var resultOfSort = data.slice();
	                            resultOfSort.sort(function(a,b) {
	                                for (var k = 0; k<sortModel.length; k++) {
	                                    var sortColModel = sortModel[k];
	                                    var valueA = a[sortColModel.colId];
	                                    var valueB = b[sortColModel.colId];
	                                    // this filter didn't find a difference, move onto the next one
	                                    if (valueA==valueB) {
	                                        continue;
	                                    }
	                                    var sortDirection = sortColModel.sort === 'asc' ? 1 : -1;
	                                    if (valueA > valueB) {
	                                        return sortDirection;
	                                    } else {
	                                        return sortDirection * -1;
	                                    }
	                                }
	                                // no filters found a difference
	                                return 0;
	                            });
	                            return resultOfSort;
	                        }

	                        function filterData(filterModel, data) {
	                            var filterPresent = filterModel && Object.keys(filterModel).length > 0;
	                            if (!filterPresent) {
	                                return data;
	                            }

	                            var xd, results;
	                            _.forEach(filterModel, function(cri, fieldName){
	                            	var header = _.find($scope.headers, {field: fieldName});	                            	
	                            	if(header.filterParams){	                            		
	                            		xd = header.filterParams.xd;
	                            		if(xd){	    
	                            			if(Array.isArray(cri)){
	                            				xd.filterFunction(function(d){
	                            					return cri.indexOf(d) >= 0;
	                            				});
	                            			}
	                            			else if (cri.type===1) {
	                            				//equals
	                            				xd.filterExact(cri.filter);
	                            			} else if (cri.type===2) {
	                            				//less
	                            				xd.filterRange([-Infinity, cri.filter]);
	                            			} else if(cri.type===3){
	                            				//greater
	                            				xd.filterRange([cri.filter, Infinity]);
	                            			} else {
	                            				throw "Invalid numeric filter type: " + cri.type + ", " + cri.filter; 
	                            			};
	                            		};	                            	
	                            	}	                            			
	                            });
	                            if(xd){	                            	
	                            	results = xd.top(Infinity);
	                            	return results;
	                            }
	                            return resultOfFilter;
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
                            scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){                            	
                            	scope.applyToHeatmap(filteredResults);                            	
                            });
                            scope.$on("ui:resultsTable:pageChanged", function($event, pageResults){
                            	scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(scope.project.dataset, pageResults, 
                                		[scope.analysis.params.control, scope.analysis.params.experiment], 
                                		scope.analysis.randomId); 
                            });
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