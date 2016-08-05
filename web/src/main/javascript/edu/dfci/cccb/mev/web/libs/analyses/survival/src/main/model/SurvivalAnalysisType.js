define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
        "./SurvivalInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, SelectionSetParam, infoTemplate){"use strict";
        function SurvivalAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam, mevSurvivalAnalysisSrv, mevContext){

            var SurvivalType = new mevAnalysisType({
                id: "survival",
                name: "Survival Analysis",
                viewModel: "SurvivalVM",
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
                    })]),
                info: {
                    template: infoTemplate
                }
            });
            SurvivalType.start=function() {
                var _self = this;
                var params = this.params.getValues();

                mevSurvivalAnalysisSrv.getInputDataTcga(params).then(function(data){
                    var postRequest = {
                        experimentName: params.experiment.name,
                        controlName: params.control.name,
                        input: data,
                        datasetName: mevContext.get("root").name
                    };
                    _.assign(params, postRequest);
                    console.debug("Survival params", params);
                    // scope.dataset.analysis.post({
                    //     datasetName : scope.dataset.datasetName,
                    //     analysisType : "survival"
                    // }, postData);
                    _self.parent.start.call(_self, _self, params, {}, "post");
                });

            };
            return SurvivalType;
        }
        SurvivalAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam", "mevSurvivalAnalysisSrv", "mevContext"];
        SurvivalAnalysisType.$name="mevSurvivalAnalysisType";
        SurvivalAnalysisType.$provider="factory";
        return SurvivalAnalysisType;
    });