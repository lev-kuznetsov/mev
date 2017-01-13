define(["lodash", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "../router/GeneMAD.tpl.html",
        "./GeneMADInfo.tpl.html"
    ],
    function(_, mevAnalysisType, AnalysisParamsFactory, template, infoTemplate){"use strict";
        function GeneMADAnalysisType(mevAnalysisType, mevAnalysisParams){

            var GeneMADType = new mevAnalysisType({
                id: "genemad",
                name: "GeneMAD",
                viewModel: "GeneMADVM",
                template: template,
                params: mevAnalysisParams([]),
                info: {
                    template: infoTemplate
                }
            });
            GeneMADType.start=function() {
                var _self = this;
                var params = this.params.getValues();
                _self.parent.start.call(_self, _self, params, {}, "put");
            };
            return GeneMADType;
        }
        GeneMADAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams"];
        GeneMADAnalysisType.$name="mevGeneMADAnalysisType";
        GeneMADAnalysisType.$provider="factory";
        return GeneMADAnalysisType;
    });