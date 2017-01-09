define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
        "mev-analysis/src/params/model/decimal/DecimalParam",
        "mev-analysis/src/params/model/boolean/BooleanParam",
        "./tTestInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, SelectionSetParam, DecimalParam, BooleanParam, infoTemplate){"use strict";
        function tTestAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam){
            var oneSampleParamsConstraint = {
                paramId: "type",
                value: "one sample"
            };
            var twoSampleParamsConstraint = {
                paramId: "type",
                value: "two sample"
            };
            var tTestType = new mevAnalysisType({
                id: "ttest",
                name: "t-Test",
                viewModel: "tTestVM",
                params: mevAnalysisParams([
                    new SelectParam({
                        "id": "type",
                        "displayName": "Type",
                        "options": ["two sample"],
                        "value": "two sample"
                    }),
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
                        "required": true,
                        "constraint": twoSampleParamsConstraint,
                    }),
                    new DecimalParam({
                        "id": "pValue",
                        "displayName": "pValue",
                        "min": 0,
                        "max": 1,
                        "value": 0.05,
                        "precision": 3
                    }),
                    new DecimalParam({
                        "id": "userMean",
                        "displayName": "Mean",
                        "constraint": oneSampleParamsConstraint
                    }),
                    new BooleanParam({
                        "id": "multTestCorrection",
                        "displayName": "Multitest correction",
                        "value": false
                    }),
                    new BooleanParam({
                        "id": "assumeEqualVariance",
                        "displayName": "Equal Variance",
                        "value": false,
                        "constraint": twoSampleParamsConstraint
                    })
                ]),
                info: {
                    template: infoTemplate
                }
            });
            tTestType.start=function() {
                var _self = this;
                var params = this.params.getValues();
                params.type=undefined;
                var urlParams = {
                    analysisType: params.type==="one sample"
                        ? "one_sample_ttest"
                        : "two_sample_ttest"
                }
                _self.parent.start.call(_self, _self, params, urlParams, "put");
            };
            return tTestType;
        }
        tTestAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
        tTestAnalysisType.$name="mevtTestAnalysisType";
        tTestAnalysisType.$provider="factory";
        return tTestAnalysisType;
    });