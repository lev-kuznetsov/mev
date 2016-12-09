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
                selections: project.dataset.row.selections,
                data: _self.analysis.result
            };
            _self.filteredGenes = [];
            _self.getSelection=function(){
                return _self.filteredGenes || nodes.list;
            }
            function getFilteredGenes(edges){
                return _.values(_.transform(edges, function(hash, item, key, edges){
                    if(!hash[item.to])
                        hash[item.to]={id: item.to};
                    if(!hash[item.from])
                        hash[item.from]={id: item.from};
                    return hash;
                }, {}));
            }
            _self.getFilteredEdges=function(filteredResults){
                _self.filteredGenes.length=0;
                getFilteredGenes(filteredResults)
                    .map(function(gene){
                        _self.filteredGenes.push(gene);
                    });
            };
            _self.headers = [
                {
                    'name': 'From',
                    'field': "from",
                    'icon': "search",
                },{
                    'name': 'To',
                    'field': "to",
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