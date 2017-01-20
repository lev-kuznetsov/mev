  define(["mui", "lodash", "./mevResultsTable.tpl.html", "papaparse"], function(ng, _, template, papaparse){"use strict";
    function mevResultsTableDirective(mevResultsTableDefaults, mevResultsTableFilter, $timeout){
        return {
            restrict : 'E',
            scope : {
                id: "=",
                data : "=data",
                headers : "=headers",
                filters : "=?filters",
                ordering : "@",
                filterCallback : "&onFilter",
                onPaged : "&",
                onRowSelected: "&",
                selectedRows: "=?",
                top: "=mevTop",
                pagination: "=?mevPagination",
                saveAs: "=mevSaveAs",
                outFilteredResults: "=?mevOutFilteredResults"
            },
            template : template,
            controller: ["$scope", function(scope){
                if(!scope.top && !scope.pagination)
                    scope.pagination = {itemsPerPage: 20}
            }],
            link : function(scope, elem, attrs) {
                
                function getOpFromHeader(header){
                    if(header.icon==='search')
                        return "~=";                                
                    if(ng.isArray(header.icon))
                        return header.icon[0];
                    else
                        return header.icon;                                 
                }
                function doFilter(){
                    console.debug("applyFilter", scope.filters, scope.filterForm);
                     Object.keys(scope.filters).map(function(key){
                        var filter = scope.filters[key];
                        if(filter.max && !filter.value || filter.value>filter.max)
                            filter.value=filter.max;
                     });
                     scope.vm.filteredResults = mevResultsTableFilter(scope.data, scope.filters, scope.top);
                     // scope.reorderTable({field: scope.tableOrdering});
                     scope.filterForm.$setPristine();
                     notifyResultChange();
                }
                function notifyResultChange(){
                    scope.$emit("ui:resultsTable:filteredResults", scope.vm.filteredResults);
                    if(scope.filterCallback)
                        scope.filterCallback({filteredResults: scope.vm.filteredResults});
                    if(scope.outFilteredResults){
                        scope.outFilteredResults.length = 0;
                        scope.vm.filteredResults.map(function(item){
                            scope.outFilteredResults.push(item);
                        });
                    }
                }
//scope.renderedData is populated by the 'as renderedData track by $index' at the ng-repeat template
//Tried tapping renderedData when signaling changes in sort/fitlers. 
//However, this does not work if pagination is enabled because only the first page of the results 
//is rendered. We are using it to notify clients of pagination changed event
                scope.$watch("renderedData", function(newval, oldval){
                    if(newval){                     
                        console.debug("resultsTable watchCollection", newval);
                        scope.$emit("ui:resultsTable:pageChanged", newval);
                        if(scope.onPaged)
                            scope.onPaged({pageResults: newval});
                    }
                });
                if(!scope.filters){
                    scope.filters={};
                    scope.headers.map(function(header){
                        if(header.icon && header.icon != 'none'){
                            scope.filters[header.field] = {
                                    field: header.field,
                                    value: header["default"],
                                    op: header.op || getOpFromHeader(header),
                                    max: header.max
                            };
                        }
                    });
                }
                
                scope.vm={};
                // scope.vm.filteredResults = mevResultsTableFilter(scope.data, scope.filters, scope.top);      
                doFilter();

                                
//              scope.$watch('filters', function(newval, oldval){  
//              if(!angular.equals(newval, oldval)){
//                  var filters = _.cloneDeep(newval);
//                  console.debug("resultTable $watch", newval, oldval, filters);                                               
////                    scope.filterCallback({filterParams: filters});
//              }
//            }, true);
                scope.vm.applyFilter=function($event){
                     if ($event.which === 13){                       
                         doFilter();                     }
                };
                scope.vm.updateTop=function(limit){
                    scope.top.current = limit;
                    doFilter();
                };

                //Table reordering methods
                var ctr = -1;
                scope.tableOrdering = attrs.ordering || mevResultsTableDefaults.getOrdering();
                console.debug("ordering", attrs.ordering, mevResultsTableDefaults.getOrdering(), scope.tableOrdering);
                scope.reorderTable = function (header) {
                    ctr = ctr * (-1);
                    if (ctr == 1) {
                        scope.tableOrdering = header.field;
                    } else {
                        scope.tableOrdering = "-" + header.field;
                    }
                    scope.vm.filteredResults.sort(function(a, b){
                        if (a[header.field] > b[header.field]) {
                            return 1*ctr;
                          }
                          if (a[header.field] < b[header.field]) {
                            return -1*ctr;
                          }
                          // a must be equal to b
                          return 0;
                    });
                    notifyResultChange();
                };
                scope.selectedRows={};
                scope.selectRow=function(field, row, callback){
                    var value = row[field];
                    if(row.isChecked)
                        scope.selectedRows[value] = row;
                    else
                        delete scope.selectedRows[value];
                    callback(value, row, row.isChecked);
                };
                scope.vm.save=function() {
                    var tsv = papaparse.unparse({
                            fields: scope.headers.map(function (header) {
                                return header.field;
                            }),
                            data: this.filteredResults
                        },
                        {
                            quotes: false,
                            delimiter: "\t",
                            newline: "\r\n"
                        });
                    console.debug("tsv", tsv);

                    var blob = new Blob([tsv], {type: 'text/tsv;charset=utf-8;'});
                    var exportFilename = (scope.saveAs ? scope.saveAs.name : undefined) || "download.tsv"
                    if(!_.endsWith(exportFilename, ".tsv"))
                        exportFilename+=".tsv";

                    //IE11 & Edge
                    if (navigator.msSaveBlob) {
                        navigator.msSaveBlob(blob, exportFilename);
                    } else {
                        //In FF link must be added to DOM to be clicked
                        var link = document.createElement('a');
                        link.href = window.URL.createObjectURL(blob);
                        link.setAttribute('download', exportFilename);
                        document.body.appendChild(link);
                        link.click();
                        document.body.removeChild(link);
                    }

                }.bind(scope.vm);
            }   
        };

    }
    mevResultsTableDirective.$inject=['mevResultsTableDefaults', "mevResultsTableFilter", "$timeout"];
    mevResultsTableDirective.$name="mevResultsTableDirective";
    return mevResultsTableDirective;
  });
