define(["mui", "d3", "ng-vega", "./wgcna_mouse_analysis.json", "mev-network-graph"],
    function (ng, d3, vg, wgcnaJson) {
        var demo = ng.module("mev-network-graph-demo", arguments, arguments)
            .controller("DemoCtrl", ["$scope", function ($scope) {
                $scope.mevNetworkWgcna = {
                    renderer: 'svg',
                    edge: {
                        field: "edges",
                        source: {field: "from"},
                        target: {field: "to"}
                    },
                    node: {
                        color: {
                            field: "group"
                        },
                        tooltip: {
                            fields: [{
                                    "name": "name",
                                    "label": "Gene"
                                },
                                {
                                    "name": "group",
                                    "label": "Group"
                                }]
                        }
                    },
                    selections: [{
                        "name": "s1",
                        "properties": {"selectionColor": "#14bdf8", "selectionDescription": ""},
                        "keys": ["Actb", "Cars", "Gzmb", "Ly6e", "Actg1", "Card11"]
                    },
                    {
                        "name": "s2",
                        "properties": {"selectionColor": "red", "selectionDescription": ""},
                        "keys": ["Actb", "Cars", "Gzmb", "Ly6e", "Ldha", "Mtf2"]
                    }],
                    data: wgcnaJson.result
                };
            }]);
        ng.element(document).ready(function () {
            ng.bootstrap(document, [demo.name]);
        });
    });