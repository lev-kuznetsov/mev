define(["mui", "d3", "ng-vega", "./wgcna_mouse_analysis.json", "mev-network-graph"],
function(ng, d3, vg, wgcnaJson){
    var demo = ng.module("mev-network-graph-demo", arguments, arguments)
        .controller("DemoCtrl", ["$scope", function($scope){
            $scope.mevNetworkWgcna  = {
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
                data: wgcnaJson.result
            };
        }]);
    ng.element(document).ready(function(){
        ng.bootstrap(document, [demo.name]);
    });
});