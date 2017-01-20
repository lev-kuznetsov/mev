define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
        "./VoomInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, SelectionSetParam, infoTemplate){"use strict";
        function VoomAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam){

            var voomType = new mevAnalysisType({
                id: "voom",
                name: "VOOM",
                viewModel: "VoomVM",
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
            voomType.start=function() {
                var _self = this;
                var params = this.params.getValues();
                _self.parent.start.call(_self, _self, params, {}, "put");
            };
            return voomType;
        }
        VoomAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
        VoomAnalysisType.$name="mevVoomAnalysisType";
        VoomAnalysisType.$provider="factory";
        return VoomAnalysisType;
    });