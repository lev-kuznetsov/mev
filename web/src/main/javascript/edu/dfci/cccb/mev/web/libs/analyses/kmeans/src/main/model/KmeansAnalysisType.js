define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, SelectionSetParam, infoTemplate){"use strict";
        function KmeansAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam){

            var kmeansType = new mevAnalysisType({
                id: "kmeans",
                name: "K-Means",
                viewModel: "KmeansVM",
                params: mevAnalysisParams([
                    new SelectParam({
                        "id": "dimension",
                        "displayName": "Clustering Dimension",
                        "options": ["column", "row"],
                        "value": "column",
                        "required": true
                    }),
                    new SelectParam({
                        "id": "k",
                        "displayName": "Clusters",
                        "options": [2, 3, 4, 5, 6, 7, 8],
                        "value": 3,
                        "required": true
                    }),
                    new SelectParam({
                        "id": "metric",
                        "displayName": "Distance Metric",
                        "options": ["euclidean"],
                        "value": "euclidean",
                        "required": true
                    }),
                    new SelectParam({
                        "id": "iterations",
                        "displayName": "Iterations",
                        "options": [100, 1000],
                        "value": 100,
                        "required": true
                    }),
                ])
            });
            
            kmeansType.start=function() {
                var _self = this;
                var params = this.params.getValues();
                var urlParams = {
                    dimension: params.dimension
                };
                _self.parent.start.call(_self, _self, params, urlParams, "put");
            };
            return kmeansType;
        }
        KmeansAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
        KmeansAnalysisType.$name="mevKmeansAnalysisType";
        KmeansAnalysisType.$provider="factory";
        return KmeansAnalysisType;
    });