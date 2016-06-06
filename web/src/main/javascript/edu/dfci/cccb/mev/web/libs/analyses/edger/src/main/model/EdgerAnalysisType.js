define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam){"use strict";
        function EdgerAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam){

            var edgerType = new mevAnalysisType({
                id: "edger",
                name: "EdgeR",
                viewModel: "EdgerVM",
                params: mevAnalysisParams([
                    new mevSelectionSetParam({
                        "id": "experiment",
                        "displayName": "Experiment",
                        "dimension": "column",
                        "display": "name"
                    }),
                    new mevSelectionSetParam({
                        "id": "control",
                        "displayName": "Control",
                        "dimension": "column",
                        "display": "name"
                    }),
                    new SelectParam({
                        "id": "method",
                        "displayName": "Method",
                        "options": ["fdr", "holm", "hochberg", "BH", "BY", "bonferroni", "none"],
                        "value": "fdr"
                    })])
            });
            edgerType.start=function() {
                var _self = this;
                var params = this.params.getValues();
                _self.parent.start.call(_self, _self, params, {}, "put");
            };
            return edgerType;
        }
        EdgerAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
        EdgerAnalysisType.$name="mevEdgerAnalysisType";
        EdgerAnalysisType.$provider="factory";
        return EdgerAnalysisType;
    });