define(["lodash"], function(_){ "use strict";
    function VoomVM(mevBoxplotService, mevAnalysisTypes, mevGseaAnalysisType, mevPcaAnalysisType, mevHclAnalysisType,
                     mevWgcnaAnalysisType, $timeout, $window){
        function factory($scope, project, analysis){
            var _self = this;
            this.analysisId=analysis.name;
            this.analysis=analysis;
            this.project=project;
            this.analysisTypes = mevAnalysisTypes.all();
            this.heatmapView = project.generateView({
                viewType:'heatmapView',
                note: analysis.name,
                labels:{
                    row:{keys:project.dataset.row.keys},
                    column:{keys:project.dataset.column.keys}
                }
            });
//			{
//	            "logFC": 0.3858,
//	            "t": 9.5164,
//	            "_row": "Sell",
//	            "AveExpr": 14.5262,
//	            "P.Value": 1.5797e-15,
//	            "adj.P.Val": 6.1609e-14,
//	            "B": 24.7832
//	        },
            this.analysis.getFilteredKeys = function(dimension){
                if(dimension==="row")
                    return _self.filteredResults.map(function(item){
                        return item._row;
                    });
            };
            this.analysis.results;
            this.analysis.results.getIdField=function(){
                return "_row";
            };
            this.analysis.results.getLogFoldChangeField=function(){
                return "logFC";
            }


            this.analysis.getOriginalInputKeys=function(dimension){
                if(dimension==="column"){
                    var selectionNames = _.isObject(_self.analysis.params.$$experiment)
                        ? [_self.analysis.params.$$experiment.name, _self.analysis.params.$$control.name]
                        : [_self.analysis.params.control.name, _self.analysis.params.experiment.name];

                    var keys = _self.project.dataset.selections.unionByName("column", selectionNames);
                    keys.displayName = selectionNames.join("+");
                    return keys;
                }

            };
            this.headers = [{
                    'name': 'ID',
                    'field': "_row",
                    'icon': "search"
                },
                {
                    'name': 'Log-Fold-Change',
                    'field': "logFC",
                    'icon': [">=", "<="]
                },
                {
                    'name': 'Average Expression',
                    'field': "AveExpr",
                    'icon': [">=", "<="]
                },
                {
                    'name': 'P-Value',
                    'field': "P.Value",
                    'icon': "<=",
                    'default': 0.05,
                    'max': 0.05,
                    'min': 0.00,
                    'step': 0.01
                },
                {
                    'name': 'q-Value',
                    'field': "adj.P.Val",
                    'icon': "<="
                },
                {
                    'name': 't',
                    'field': "t",
                    'icon': "<="
                },
                {
                    'name': 'B',
                    'field': "B",
                    'icon': "<="
                }];
            _self.filteredResults=[];
            this.udpateFilteredView = function (filteredResults) {
                var labels = filteredResults.map(function(item){return item._row;});
                _self.heatmapView = _self.heatmapView.applyFilter("row", labels);
            };
            this.updatePageView = function (pageResults) {
                var selections = _.isObject(_self.analysis.params.$$control)
                    ? [_self.analysis.params.$$control, _self.analysis.params.$$experiment]
                    : [_self.analysis.params.control, _self.analysis.params.experiment]
                _self.boxPlotGenes = mevBoxplotService.prepareBoxPlotData(_self.project.dataset, pageResults,
                    selections,
                    _self.analysis.randomId, "_row");
            };
            $scope.$on("ui:resultsTable:pageChanged", function($event, results){
            });
            $scope.$on("ui:resultsTable:filteredResults",function($event, results){
//				var control = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.controlName;});
//	        	var experiment = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.experimentName;});
//
//	       		$scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(project.dataset, results,
//	         		[control, experiment],
//	         		analysis.randomId);
            });
        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    VoomVM.$inject=["mevBoxplotService", "mevAnalysisTypes", "mevGseaAnalysisType", "mevPcaAnalysisType", "mevHclAnalysisType",
        "mevWgcnaAnalysisType", "$timeout", "$window"];
    VoomVM.$name="VoomVMFactory";
    VoomVM.$provider="factory";
    return VoomVM;
});