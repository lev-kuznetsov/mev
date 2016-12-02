define(["lodash"], function(_){ "use strict";
    function LimmaVM(mevBoxplotService, mevAnalysisTypes, mevGseaAnalysisType, mevPcaAnalysisType, mevHclAnalysisType,
                     mevWgcnaAnalysisType, $timeout, $window){
        function factory($scope, project, analysis)
        {
            var projection = {
                ids: function(row){
                    return row.id
                }
            };
            var _self=this;
            var scope = $scope;
            $scope.project = project;
            $scope.analysis = analysis;
            $scope.analysisTypes = mevAnalysisTypes.all();
            this.analysis = analysis;
            this.project = project;
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
            $scope.filteredResults = [];
            _self.filteredResults  = $scope.filteredResults
            $scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
                $scope.applyToHeatmap(filteredResults);
            });

            $scope.$on("ui:resultsTable:pageChanged", function($event, pageResults){
                $scope.boxPlotGenes = mevBoxplotService.prepareBoxPlotData($scope.project.dataset, pageResults,
                    [$scope.analysis.params.control, $scope.analysis.params.experiment],
                    $scope.analysis.randomId);
            });
            $scope.viewGenes = function (filteredResults) {
                scope.applyToHeatmap(filteredResults);
            };
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

            scope.analysis.getFilteredKeys = function(dimension){
                if(dimension==="row")
                    return scope.filteredResults.map(function(item){
                        return item.id;
                    });
            };
            scope.analysis.getOriginalInputKeys = function(dimension){
                if(dimension==="column"){
                    var selectionNames = _.isObject(scope.analysis.params.control)
                        ? [scope.analysis.params.experiment.name, scope.analysis.params.control.name]
                        : [scope.analysis.params.experiment, scope.analysis.params.control]
                    var keys = project.dataset.selections.unionByName("column", selectionNames);
                    keys.displayName = selectionNames.join("+");
                    return keys;
                }
            };
            scope.selectionParams = {
                name: undefined,
                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
            };

            _self.scatterVm={
                selected: {
                    items: []
                }
            };
            scope.$on("mev.scatterPlot.selection", function($event, selected){
                _self.scatterVm.selected = selected;
                
            });

            scope.exportParams = {
                name: undefined,
                color: '#ffffff'
            };

            this.resize=function(){
                $timeout(function() {
                    var evt;
                    if($window.document.createEvent){
                        evt = $window.document.createEvent('UIEvents');
                        evt.initUIEvent('resize', true, false, $window, 0);
                    }else{
                        evt = new Event('resize');
                    }
                    $window.dispatchEvent(evt);
                });
            }
        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    LimmaVM.$inject=["mevBoxplotService", "mevAnalysisTypes", "mevGseaAnalysisType", "mevPcaAnalysisType", "mevHclAnalysisType",
        "mevWgcnaAnalysisType", "$timeout", "$window"];
    LimmaVM.$name="LimmaVMFactory";
    LimmaVM.$provider="factory";
    return LimmaVM;
});