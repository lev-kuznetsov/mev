define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
        "./EdgerInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, SelectionSetParam, infoTemplate){"use strict";
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
                        "display": "name",
                        "required": true,
                        "disjoint": "control"
                    }),
                    new mevSelectionSetParam({
                        "id": "control",
                        "displayName": "Control",
                        "dimension": "column",
                        "display": "name",
                        "required": true
                    }),
                    new SelectParam({
                        "id": "method",
                        "displayName": "Method",
                        "options": ["fdr", "holm", "hochberg", "BH", "BY", "bonferroni", "none"],
                        "value": "fdr"
                    })]),
                info: {
                    template: infoTemplate
                }
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