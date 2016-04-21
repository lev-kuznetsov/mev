define(["lodash", "crossfilter"], function(_, crossfilter){
	
    var ResultsTableDirective = function ResultsTableDirective(){
        return {
            restrict : 'E',
            scope : {
            	id: "=",
            	data : "=data",
            	headers : "=headers",
                filters : "=?filters",
                ordering : "@",
                filterCallback : "&onFilter",
                onPaged : "&"
            },            
//            templateUrl : paths.module + '/templates/resultsTable.tpl.html',
            template: "<div ag-grid='gridOptions' class='ag-fresh' style='height: 100%;'></div>",
            controller: ["$scope", function($scope){            	
            	var xf = crossfilter($scope.data);
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
                	}else if(header.field === "id" || header.icon==="search"){
                		header.filterParams = {
                				values: _.map($scope.data, header.field),
                				newRowsAction: 'keep', 
                				xd: xf.dimension(function(d){
                					return d[header.field];
                				})
                		};
                	}
                	header.filterParams.apply = true;
                	return header;
                });
                
                $scope.gridOptions = {
                  columnDefs: columnDefs
                };
                
                $scope.pageSize = '20';

                $scope.gridOptions = {
                    // note - we do not set 'virtualPaging' here, so the grid knows we are doing standard paging
//                    enableSorting: true,
//                    enableFilter: true,
                	enableServerSideSorting: true,
                    enableServerSideFilter: true,
                    enableColResize: true,
                    columnDefs: columnDefs
                };
                
                	                        	                        
                $scope.createNewDatasource = function () {	                            
                    var dataSource = {
                        rowCount: $scope.data.length, //not setting the row count, infinite paging will be used
                        pageSize: parseInt($scope.pageSize), // changing to number, as scope keeps it as a string
                        getRows: function (params) {
                            // this code should contact the server for rows. however for the purposes of the demo,
                            // the data is generated locally, a timer is used to give the experience of
                            // an asynchronous call
                            console.log('asking for ' + params.startRow + ' to ' + params.endRow);
//                            setTimeout( function() {
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
//                                this.rowCount = dataAfterSortingAndFiltering.length;
                                $scope.$emit("ui:resultsTable:filteredResults", dataAfterSortingAndFiltering);
                                if($scope.filterCallback)
                                	$scope.filterCallback({filteredResults: dataAfterSortingAndFiltering});
                                if($scope.onPaged)
                    				$scope.onPaged({pageResults: rowsThisPage});
                                $scope.$emit("ui:resultsTable:pageChanged", rowsThisPage);                 
//                            }, 500);
                        }
                    };

                    $scope.gridOptions.api.setDatasource(dataSource);
                };
                
                function sortAndFilter(sortModel, filterModel) {
                    return sortData(sortModel, filterData(filterModel, $scope.data));
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
            link : {post: function(scope, elem, attrs) {
	//              scope.viewGenes(scope.analysis.results);
	//              scope.gridOptions.api.setRowData(filteredResults);
	              scope.createNewDatasource();
	              scope.gridOptions.api.sizeColumnsToFit();
            	}
            }	
        };
    };
    
    ResultsTableDirective.$inject = [];      
    ResultsTableDirective.$name = "resultsTableAg";
    ResultsTableDirective.provider="directive";
    return ResultsTableDirective;


  });
