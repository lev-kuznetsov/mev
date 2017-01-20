define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
        "mev-analysis/src/params/model/decimal/DecimalParam",
        "mev-analysis/src/params/model/boolean/BooleanParam",
        "./AnovaInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, SelectionSetParam, DecimalParam, BooleanParam, infoTemplate){"use strict";
        function AnovaAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam){
            var AnovaType = new mevAnalysisType({
                id: "anova",
                name: "ANOVA",
                viewModel: "AnovaVM",
                params: mevAnalysisParams([
                    new DecimalParam({
                        "id": "pValue",
                        "displayName": "pValue",
                        "min": 0,
                        "max": 1,
                        "value": 0.05,
                        "precision": 3
                    }),
                    new BooleanParam({
                        "id": "multTestCorrection",
                        "displayName": "Multitest correction",
                        "value": false
                    }),
                    new mevSelectionSetParam({
                        "id": "selections",
                        "displayName": "Selections",
                        "dimension": "column",
                        "display": "name",
                        "multiselect": true,
                        "disjoint": true
                    })
                ]),
                info: {
                    template: infoTemplate
                }
            });
            AnovaType.start=function() {
                var _self = this;
                var params = this.params.getValues();
                var selectionNames = params.selections.map(function(selection){
                    return selection.name;
                })
                params.selections = selectionNames;
                params.type=undefined;
                _self.parent.start.call(_self, _self, params, {}, "put");
            };
            return AnovaType;
        }
        AnovaAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
        AnovaAnalysisType.$name="mevAnovaAnalysisType";
        AnovaAnalysisType.$provider="factory";
        return AnovaAnalysisType;
    });