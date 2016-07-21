define(["lodash", "../router/TopgoState.tpl.html", "mev-analysis/src/type/model/AnalysisType",
        "mev-analysis/src/params/model/AnalysisParamsFactory",
        "mev-analysis/src/params/model/text/TextParam",
        "mev-analysis/src/params/model/select/SelectParam",
        "mev-analysis/src/params/model/integer/IntegerParam",
        "mev-analysis/src/params/model/decimal/DecimalParam",
        "mev-analysis/src/params/model/selectionSet/SelectionSetParam",
    ],
    function(_, template, mevAnalysisType, AnalysisParamsFactory,
             TextParam, SelectParam, IntegerParam, DecimalParam){"use strict";
        function TopgoAnalysisType(mevAnalysisType, mevAnalysisParams, mevSelectionSetParam){

            return new mevAnalysisType({
                id: "topgo",
                name: "TopGO",
                viewModel: "TopgoVM",
                template: template,
                params: mevAnalysisParams([
                    new mevSelectionSetParam({
                        "id": "genelist",
                        "dimension": "row",
                        "displayName": "Genes",
                        "display": "name",
                        "bound": "keys"
                    }),
                    new SelectParam({
                        "id": "species",
                        "displayName": "Species",
                        "options": ["human", "mouse"],
                        "value": "human"
                    }),
                    new SelectParam({
                        "id": "goType",
                        "options": ["BP", "CC", "MF"],
                        "value": "BP"
                    }),
                    new SelectParam({
                        "id": "testType",
                        "options": ["fisher"],
                        "value": "fisher"
                    }),
                    new SelectParam({
                        "id": "pAdjust",
                        "options": ["holm", "hochberg", "hommel", "bonferroni", "BH", "BY", "fdr", "none"],
                        "value": "fdr"
                    }),
                    new IntegerParam({
                        "id": "nodeSize",
                        "displayName": "Node Size",
                        "min": 0,
                        "max": Infinity,
                        "value": 20
                    })]),
                modelDecorator: function(analysis){

                    var topgoRowModel = {
                        getRatio: function () {
                            return Math.round((this.significantGenes / this.annotatedGenes) * 1000) / 10000;
                        },
                        getTotal: function () {
                            return this.annotatedGenes;
                        },
                        getMatched: function () {
                            return this.significantGenes;
                        },
                        getName: function () {
                            return this.goTerm;
                        },
                        getPValue: function () {
                            return this.pValue;
                        }
                    };


                    function formatResults(input){
                        return input.map(function(item){
                            return _.extend(item, topgoRowModel);
                        });
                    };

                    function isPeRow(item){
                        return _.every(_.functions(topgoRowModel), function(methodName){
                            return _.hasIn(item, methodName);
                        });
                    }

                    if(analysis && analysis.results && analysis.results.length > 0)
                        if(!isPeRow(analysis.results[0]))
                            formatResults(analysis.results);
                },
                info: {
                    template: "<p>Extract top GO terms from goana output or top KEGG pathways from kegga output.</p>"
                }
            });

        }
        TopgoAnalysisType.$inject=["mevAnalysisType", "mevAnalysisParams", "mevSelectionSetParam"];
        TopgoAnalysisType.$name="mevTopgoAnalysisType";
        TopgoAnalysisType.$provider="factory";
        return TopgoAnalysisType;
    });