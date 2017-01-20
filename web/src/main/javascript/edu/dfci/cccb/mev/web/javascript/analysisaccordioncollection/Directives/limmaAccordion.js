"use strict";
(function () {

    define(["mui", "lodash", "crossfilter", "mev-scatter-plot"], function (ng, _, crossfilter, mevScatterPlot) {
        return function (module) {

            module.directive('limmaAccordion', ['alertService', 'projectionService', 'pathService', 'mevBoxplotService',
                "$window", "$timeout", "mevAnalysisTypes",
                function (alertService, projection, paths, BoxPlotService, $window, $timeout, mevAnalysisTypes) {
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

                            $scope.analysisTypes = mevAnalysisTypes.all();
                            $scope.fields = ["logFoldChange", "averageExpression", "logPValue"];
	                    	$scope.headers = [
								//this row just shows the row index, doesn't use any data from the row
	//              								{
	//              									headerName: "#", cellRenderer: function(params) {
	//              										return params.node.id + 1;
	//              									} 
	//              								 },
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
	                                   'icon': ["<=", ">="],
	                                   'default': 0.05
	                               },
	                               {
	                                   'name': 'q-Value',
	                                   'field': "qValue",
	                                   'icon': ["<=", ">="]
	                               }
	                           ];
	                    	
	                    	//if using events, must filter on "id" so as not to process events raised by other resultTables ont he same page
	                    	// to do that: (1) set unique id on the <result-table> element and (2) check targetScopeFilter in this handler
	                    	// In the end it's easier to use a callback function (such as viewGenes below)
                            $scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
                            	$scope.filteredResults = filteredResults;
                            	$scope.applyToHeatmap(filteredResults);                            	
                            });

                            $scope.$on("ui:resultsTable:pageChanged", function($event, pageResults){
                            	$scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData($scope.project.dataset, pageResults, 
                                		[$scope.analysis.params.control, $scope.analysis.params.experiment], 
                                		$scope.analysis.randomId); 
                            });
                            $scope.viewGenes = function (filteredResults) {

                                scope.filteredResults = filteredResults;
                                scope.applyToHeatmap(filteredResults);
                            };
                            var scope = $scope;
                            $scope.applyToHeatmap = function (filteredResults) {
                                
                            	var labels = filteredResults.map(projection.ids);
                            	
                                $scope.heatmapView = $scope.project.generateView({
                                    viewType: 'heatmapView',
                                    note: $scope.analysis.name,
                                    labels: {
                                        column: {
                                            keys: $scope.project.dataset.column.keys
                                        },
                                        row: {
                                            keys: labels
                                        }
                                    }
                                });

                            };

                            // scope.filteredResults = undefined;
                            scope.analysis.getFilteredKeys = function(dimension){
                                if(dimension==="row")
                                    return scope.filteredResults.map(function(item){
                                        return item.id;
                                    });
                            }
                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
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
                                        scope.project.dataset.resetSelections('row');
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

                            scope.scatterVm={
                                selected: {
                                    items: []
                                },
                                addSelections: function(){
                                    var userselections = scope.scatterVm.selected.items.map(projection.ids);
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
                                            scope.project.dataset.resetSelections('row');
                                            var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                            var header = "Heatmap Selection Addition";

                                            alertService.success(message, header);
                                        },
                                        function (data, status, headers, config) {
                                            var message = "Couldn't add new selection. If " + "problem persists, please contact us.";

                                            var header = "Selection Addition Problem (Error Code: " + status + ")";

                                            alertService.error(message, header);
                                        });
                                },
                                exportSelection: function(){

                                    var keys = scope.scatterVm.selected.items.map(projection.ids);
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
                                }
                            };
                            scope.$on("mev.scatterPlot.selection", function($event, selected){
                                scope.scatterVm.selected = selected;
                            });

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
                        }],
                        link: function (scope) {
                            
                            
                            ng.element('#scatterPlotTab').click(function (e) {
                                $timeout(function() {
                                    var evt = $window.document.createEvent('UIEvents'); 
                                    evt.initUIEvent('resize', true, false, $window, 0); 
                                    $window.dispatchEvent(evt);
                                });
                                    // e.preventDefault()
                                    // ng.element(this).tab('show')                                                                    
                                    // ng.element(window).trigger('resize'); // Added this line to force NVD3 to redraw the chart
                                    // scope.$apply();
                            })
	                        
                            
                        }

                    };
                    
            }]);

        };
    });

})();