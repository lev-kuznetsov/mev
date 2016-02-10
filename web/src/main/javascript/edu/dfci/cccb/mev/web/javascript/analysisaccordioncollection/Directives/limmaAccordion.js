"use strict";
(function () {

    define(["mui", "lodash", "crossfilter", "mev-scatter-plot"], function (ng, _, crossfilter, mevScatterPlot) {
        return function (module) {

            module.directive('limmaAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService', 'BoxPlotService',
                "$window", "$timeout",
                function (tableFilter, alertService, projection, paths, BoxPlotService, $window, $timeout) {
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
                            $scope.fields = ["logFoldChange", "averageExpression"];
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
	                                   'icon': "<=",
	                                   'default': 0.05
	                               },
	                               {
	                                   'name': 'Q-Value',
	                                   'field': "qValue",
	                                   'icon': "<="
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

                            function scatterTransformData(analysis, x, y, selections) {
                                
                                var selectedSets = _.filter(selections, function(s){return s.checked;});
                                var groups = {};
                                var count=0;
                                _.forEach(analysis.results, function(item){
                                    if(++count > 1000)
                                        return false;
                                    var groupAcc = {names: [], color: "grey"};
                                    _.map(selectedSets, function(selection){
                                        if(_.contains(selection.keys, item.id)){                        
                                            groupAcc.names.push(selection.name);
                                            groupAcc.color = selection.properties.selectionColor;
                                        }
                                    });

                                    var groupName = "none";
                                    if(groupAcc.names.length===1)
                                        groupName = groupAcc.names[0];
                                    else if(groupAcc.names.length>1)
                                        groupName = groupAcc.names.join("+");

                                    var group = groups[groupName];
                                    if(!group){
                                        groups[groupName] = group = {};
                                        group.name = group.key = groupName;                  
                                        group.names = groupAcc.names;
                                        if(group.names.length<2){
                                            group.color = groupAcc.color;
                                        }else{
                                            group.color = '#'+Math.floor(Math.random()*16777215).toString(16);
                                        }   
                                        group.shape = 'circle';
                                        group.values=[];
                                    }
                                                
                                    group.values.push({
                                        x: item[x],
                                        y: item[y],
                                        size: 10,                       
                                        sample: item.id,
                                        id: item.id                      
                                    });
                                });         
                                
                                if(Object.keys(groups).length === 1)
                                    groups["none"].name = groups["none"].key = "Selection: none";

                                return _.sortBy(groups, function(group){
                                    return group.name === "none"  ? Infinity : group.names.length;                   
                                });
                            }

                            scope.selections = scope.project.dataset.selections.row;
                            scope.scatterVm = {
                                xLabel: "logFoldChange",
                                yLabel: "averageExpression",
                                logScaleX: false,
                                logScaleY: false,
                                dragAction: "select",
                                isZoomEnabled: function(){
                                    return scope.scatterVm.dragAction === "zoom";
                                },
                                
                                updateSelection: function(){
                                    scope.scatterVm.setData(scatterTransformData(scope.analysis, scope.scatterVm.xLabel, scope.scatterVm.yLabel, scope.selections));         
                                },
                                setData: function(data){
                                    scope.scatterVm.data = data;                       
                                    scope.scatterVm.selection = undefined;
                                },
                                updateScale: function(){
                                    scope.scatterVm.setData(); 
                                }
                            };
                            scope.$watch("analysis", function(newVal){
                                scope.scatterVm.setData(scatterTransformData(newVal, scope.scatterVm.xLabel, scope.scatterVm.yLabel, scope.selections));    
                            });
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
                            
                            scope.filteredResults = undefined;
                            
                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                            };
// 
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
	                        
                            
                        }

                    };
                    
            }]);

        };
    });

})();