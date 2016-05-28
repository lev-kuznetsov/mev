define(["lodash"], function(_){ "use strict";
    function WgcnaVM(mevAnalysisTypes){
        function factory($scope, project, analysis) {
            var _self = this;
            _self.project=project;
            _self.analysis=analysis;
            _self.mevNetworkWgcna  = {
                renderer: 'svg',
                edge: {
                    field: "edges",
                    source: { field: "from" },
                    target: { field: "to" }
                },
                node: {
                    color: {
                        field: "group",  scale: {
                            name: "color"
                        }
                    },
                    tooltip: {
                        fields: [{
                            "name": "name",
                            "label": "Gene"
                        }]
                    }
                },
                data: _self.analysis.result
            };

        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    WgcnaVM.$inject=["mevAnalysisTypes"];
    WgcnaVM.$name="WgcnaVMFactory";
    WgcnaVM.$provider="factory";
    return WgcnaVM;
});