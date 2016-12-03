define(["lodash"], function(_){ "use strict";
    function EdgerVM(mevBoxplotService, mevAnalysisTypes, mevGseaAnalysisType, mevPcaAnalysisType, mevHclAnalysisType, mevWgcnaAnalysisType){
        function factory($scope, project, analysis) {
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
                    var selectionNames = [_self.analysis.params.experiment.name, _self.analysis.params.control.name];

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
                    'name': 'Log-Counts-per-Mill',
                    'field': "logCPM",
                    'icon': [">=", "<="]
                },
                {
                    'name': 'P-Value',
                    'field': "PValue",
                    'icon': "<=",
                    'default': 0.05,
                    'max': 0.05,
                    'min': 0.00,
                    'step': 0.01
                }];
            if(this.analysis.results && this.analysis.results.length>0)
                if(!_.isUndefined(this.analysis.results[0].FWER))
                    this.headers.push({
                        'name': 'P-Adjust (FWER)',
                        'field': "FWER",
                        'icon': ["<=", ">="]
                    });
                else if(!_.isUndefined(this.analysis.results[0].FDR))
                    this.headers.push({
                        'name': 'P-Adjust (FDR)',
                        'field': "FDR",
                        'icon': ["<=", ">="]
                    });
            this.filteredResults = [];
            this.udpateFilteredView = function (filteredResults) {
                var labels = filteredResults.map(function(item){return item._row;});
                _self.heatmapView = _self.heatmapView.applyFilter("row", labels);
                // _self.boxPlotGenes = mevBoxplotService.prepareBoxPlotData(_self.project.dataset, filteredResults,
                //     [_self.analysis.params.control, _self.analysis.params.experiment],
                //     _self.analysis.randomId, "_row");
            };
            this.updatePageView = function (pageResults) {
                _self.boxPlotGenes = mevBoxplotService.prepareBoxPlotData(_self.project.dataset, pageResults,
                    [_self.analysis.params.control, _self.analysis.params.experiment],
                    _self.analysis.randomId, "_row");
            };
            //if using events, must filter on "id" so as not to process events raised by other resultTables ont he same page
            // to do that: (1) set unique id on the <result-table> element and (2) check targetScopeFilter in this handler
            // In the end it's easier to use a callback function (such as viewGenes below)
            // $scope.$on("ui:resultsTable:filteredResults", function($event, filteredResults){
            //     _self.filteredResults = filteredResults;
            //     var labels = filteredResults.map(function(item){return item._row;});
            //     _self.heatmapView = _self.heatmapView.applyFilter("row", labels);
            // });
            //
            // $scope.$on("ui:resultsTable:pageChanged", function($event, pageResults){
            //     _self.boxPlotGenes = mevBoxplotService.prepareBoxPlotData(_self.project.dataset, pageResults,
            //         [_self.analysis.params.control, _self.analysis.params.experiment],
            //         _self.analysis.randomId, "_row");
            // });

        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    EdgerVM.$inject=["mevBoxplotService", "mevAnalysisTypes", "mevGseaAnalysisType", "mevPcaAnalysisType", "mevHclAnalysisType", "mevWgcnaAnalysisType"];
    EdgerVM.$name="EdgerVMFactory";
    EdgerVM.$provider="factory";
    return EdgerVM;
});