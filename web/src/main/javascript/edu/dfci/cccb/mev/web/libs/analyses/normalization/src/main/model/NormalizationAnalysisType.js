define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/BaseParam",
    "./NormalizationInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, SelectParam, BaseParam, infoTemplate){"use strict";
        function NormalizationAnalysisType(mevAnalysisType, mevAnalysisParams, mevContext, $q, $http){

            var normalizationType = new mevAnalysisType({
                id: "normalization",
                name: "Normalization",
                viewModel: "NormalizationVM",
                params: mevAnalysisParams([
                    new BaseParam({
                        id: "name"
                    }),
                    new SelectParam({
                        "id": "method",
                        "displayName": "Method",
                        "options": ['css','css2','tss', 'deseq', 'tmm','upperquantile','vst','pa'],
                        "value": "css"
                    })]),
                onSuccess: function(response){
                    console.log("normalization success", response);
                    if(response.status==="SUCCESS"){
                        $http({
                            method: "PUT",
                            url: "/dataset/" + response.params.datasetName
                            + "/analyze/normalization/" + response.params.name
                            + "/export",
                            data: {
                                name: response.params.name
                            }
                        }).then(function(){
                            $http({
                                method:"POST",
                                url:"/annotations/" + response.params.datasetName + "/annotation/row"
                                + "/export?destId="+response.params.exportName});
                            $http({
                                method:"POST",
                                url:"/annotations/" + response.params.datasetName + "/annotation/column"
                                + "/export?destId="+response.params.exportName});
                            mevContext.get("root").dataset.getAll();
                        });
                    }
                },
                info: {
                    template: infoTemplate
                }
            });
            function prepareParams(paramValues){
                paramValues.name=paramValues.method;
                paramValues.exportName=mevContext.get("root").dataset.id+"--normalized-"+paramValues.name;
                return $q.when(paramValues);
            }
            normalizationType.start=function(){
                var _self = this;
                var paramValues = this.params.getValues();
                console.debug("normalization params values", paramValues);
                prepareParams(paramValues).then(function(params){
                    console.debug("normalization out params", params);
                    _self.parent.start.call(_self, _self, params, {}, "put");
                });
            };
            return normalizationType;
        }
        NormalizationAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevContext", "$q", "$http"];
        NormalizationAnalysisType.$name="mevNormalizationAnalysisType";
        NormalizationAnalysisType.$provider="factory";
        return NormalizationAnalysisType;
    });