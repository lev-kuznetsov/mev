define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
        "./DeseqInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, SelectionSetParam, infoTemplate){"use strict";
        function DeseqAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam){

            var DeseqType = new mevAnalysisType({
                id: "deseq",
                name: "DESeq",
                viewModel: "DeseqVM",
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
            DeseqType.start=function() {
                var _self = this;
                var params = this.params.getValues();
                _self.parent.start.call(_self, _self, params, {}, "put");
            };
            return DeseqType;
        }
        DeseqAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
        DeseqAnalysisType.$name="mevDeseqAnalysisType";
        DeseqAnalysisType.$provider="factory";
        return DeseqAnalysisType;
    });