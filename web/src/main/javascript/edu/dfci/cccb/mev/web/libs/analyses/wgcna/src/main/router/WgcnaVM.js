define(["lodash"], function(_){ "use strict";
    function WgcnaVM(mevAnalysisTypes){
        function factory($scope, project, analysis) {
            var _self = this;
            _self.project=project;
            _self.analysis=analysis;
            var nodes = _.reduce(_self.analysis.result.edges, function(result, item){
                if(!result.map[item.from]){
                    result.map[item.from] = {
                        id: item.from
                    };
                    result.list.push(result.map[item.from]);
                }

                if(!result.map[item.to]){
                    result.map[item.to] = {
                        id: item.to
                    };
                    result.list.push(result.map[item.to]);
                }

                return result;
            }, {
                map: {},
                list: []
            });
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
            _self.getSelection=function(){
                return nodes.list;
            }

            _self.headers = [
                {
                    'name': 'From',
                    'field': "s",
                    'icon': "search",
                },{
                    'name': 'To',
                    'field': "t",
                    'icon': "search",
                },{
                    'name': 'Method',
                    'field': "method",
                    'icon': "search",
                },{
                    'name': 'Weight',
                    'field': "weight",
                    'icon': ["<=", ">="]
                }
            ]
        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    WgcnaVM.$inject=["mevAnalysisTypes"];
    WgcnaVM.$name="WgcnaVMFactory";
    WgcnaVM.$provider="factory";
    return WgcnaVM;
});