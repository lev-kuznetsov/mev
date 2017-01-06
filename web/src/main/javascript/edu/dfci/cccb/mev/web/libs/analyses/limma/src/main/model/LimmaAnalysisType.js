define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
        "./LimmaInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, SelectionSetParam, infoTemplate){"use strict";
        function LimmaAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam){

            var limmaType = new mevAnalysisType({
                id: "limma",
                name: "LIMMA",
                viewModel: "LimmaVM",
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
                    })]),
                info: {
                    template: infoTemplate
                }
            });
            limmaType.start=function() {
                var _self = this;
                var params = this.params.getValues();
                _self.parent.start.call(_self, _self, params, {}, "put");
            };
            return limmaType;
        }
        LimmaAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
        LimmaAnalysisType.$name="mevLimmaAnalysisType";
        LimmaAnalysisType.$provider="factory";
        return LimmaAnalysisType;
    });