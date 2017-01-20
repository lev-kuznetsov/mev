define(["lodash", "../router/WgcnaState.tpl.html",
    "mev-analysis/src/type/model/AnalysisType",
    "mev-analysis/src/params/model/AnalysisParamsFactory",
    "mev-analysis/src/params/model/select/SelectParam",
    "mev-analysis/src/params/model/decimal/DecimalParam",
    "mev-analysis/src/params/model/integer/IntegerParam",
    "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
    "./WgcnaInfo.tpl.html"
], function(_, template,
            AnalsysType,
            AnalysisParamsFactory,
            SelectParam,
            DecimalParam,
            IntegerParam,
            selectionSetParam,
            infoTemplate
            ){"use strict";
    function component(MevAnalysisType, mevAnalysisParams, mevSelectionSetParam){
        var wgcnaType = new MevAnalysisType({
            id: "wgcna",
            name: "WGCNA",
            viewModel: "WgcnaVM",
            template: template,
            params: mevAnalysisParams([
                new SelectParam({
                    "id": "distMethod",
                    "displayName": "Distance",
                    "options": ["euclidean", "maximum", "manhattan", "canberra", "binary", "minkowski"],
                    "value": "euclidean"
                }),
                new DecimalParam({
                    "id": "WeightFilter",
                    "displayName": "Weight Filter",
                    "min": 0,
                    "max": 1,
                    "value": 0.9,
                    "precision": 3
                }),
                new IntegerParam({
                    "id": "sizeLimit",
                    "displayName": "Size Limit",
                    "min": 0,
                    "max": 1000,
                    "value": 1000
                }),
                new mevSelectionSetParam({
                    "id": "sampleList",
                    "dimension": "column",
                    "displayName": "Sample Set",
                    "display": "name",
                    "bound": "keys",
                    "required": true,
                    "allowAll": "root"
                }),
                new mevSelectionSetParam({
                    "id": "geneList",
                    "dimension": "row",
                    "displayName": "Gene Set",
                    "display": "name",
                    "bound": "keys",
                    "required": true,
                    "allowAll": "root",
                    "max": 1000
                })
            ]),
            info: {
                template: infoTemplate
            }
        });
        wgcnaType.start=function(){
            var paramValues = this.params.getValues();
            this.parent.start.call(this, this, paramValues, {analysisName: paramValues.name}, "put");
        };
        return wgcnaType;
    }
    component.$name="mevWgcnaAnalysisType";
    component.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
    component.$provider="factory";
    return component;
});