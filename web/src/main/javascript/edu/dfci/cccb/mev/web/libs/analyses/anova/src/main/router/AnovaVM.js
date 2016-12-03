define(["lodash"], function(_){ "use strict";
    function AnovaVM(mevBoxplotService, mevAnalysisTypes){
        
        function factory(scope, project, analysis){
            //variable to remove embedded pair information
            var _self = this;
            var projection = {
                ids: function(row){
                    return row.id
                }
            };
            scope.analysisTypes = mevAnalysisTypes.all();
            this.analysisId=analysis.name;
            scope.project = project;
            scope.analysis = analysis;
            this.project = project;
            this.analysis = analysis;
            this.heatmapView = project.generateView({
                viewType:'heatmapView',
                note: analysis.name,
                labels:{
                    row:{keys:project.dataset.row.keys},
                    column:{keys:project.dataset.column.keys}
                },
                expression:{
                    min: project.dataset.expression.min,
                    max: project.dataset.expression.max,
                    avg: project.dataset.expression.avg,
                }
            });
            scope.filteredResults = undefined;
            scope.cleanData = expandEmbedded(scope.analysis.results);
            scope.headers = [{
                'name': 'ID',
                'field': "id",
                'icon': "search"
            },{
                'name': 'Partner A',
                'field': 'partnerA',
                'icon': "search"
            },{
                'name': 'Partner B',
                'field': 'partnerB',
                'icon': "search"
            },{
                'name': 'P-Value',
                'field': "pValue",
                'icon': ["<=", ">="],
                'default': 0.05
            },{
                'name': 'Pairwise LFC',
                'field': 'lfc',
                'icon': [">=", "<="]
            }];

            _self.filteredResults = [];
            scope.filteredResults = _self.filteredResults;
            _self.filteredResultsUniq=[]
            scope.viewGenes = function(filteredResults){
                console.debug("anova viewGenes", filteredResults);
                _self.filteredResults = filteredResults;
                _self.filteredResultsUniq.length = 0;
                traverse(_self.filteredResults)
                .map(function(geneId){
                    _self.filteredResultsUniq.push({
                        id: geneId
                    });
                });
                scope.$emit("ui:anova:filteredResults", _.uniq(filteredResults, 'id'));
                scope.applyToHeatmap(filteredResults);
            }
            scope.viewPage = function(pageResults){
                var selectionNames = scope.analysis.params.selections || scope.analysis.params.data;
                var groups = selectionNames.map(function(selectionName){
                    return _.find(scope.project.dataset.column.selections, function(selection){return selection.name===selectionName;});
                });

                var pageResultsUniq = _.uniq(pageResults, 'id')
                scope.$emit("ui:anova:pageResults", pageResultsUniq);
                scope.boxPlotGenes = mevBoxplotService.prepareBoxPlotData(scope.project.dataset, pageResultsUniq,
                    groups,
                    scope.analysis.randomId);
            };

            scope.selectionParams = {
                name: undefined,
                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16),
                dimension: 'row'
            };

            function traverse(results) {
                return results.map(projection.ids)
                //fix#945
                //the table shows a gene multiple times (once for each pairing set)
                //the heatmap, however, needs the unique gene id's
                    .filter(function(value, index, array){
                        return array.indexOf(value) === index;
                    });

            }

            scope.applyToHeatmap = function (filteredResults) {

                var labels = traverse(filteredResults);

                _self.heatmapView = scope.project.generateView({
                    viewType: 'heatmapView',
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

            function expandEmbedded(data){
                //This function is supposed to expand the partner and ratio
                // data into their own columns

                var expanded = []

                for (var gene = 0; gene < data.length; gene++){

                    for (var pair = 0; pair < data[gene]['pairwise_log_fold_change'].length; pair++){
                        var cleanRow = {
                            "id": data[gene]['id'],
                            "pValue": data[gene]['pValue'],
                            "lfc": data[gene]['pairwise_log_fold_change'][pair]['ratio'],
                            'partnerA': data[gene]['pairwise_log_fold_change'][pair]['partnerA'],
                            'partnerB': data[gene]['pairwise_log_fold_change'][pair]['partnerB']
                        }

                        expanded.push(cleanRow)
                    }

                }

                return expanded
            }

        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    AnovaVM.$inject=["mevBoxplotService", "mevAnalysisTypes"];
    AnovaVM.$name="AnovaVMFactory";
    AnovaVM.$provider="factory";
    return AnovaVM;
});