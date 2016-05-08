define(["mui", "d3", "vega", "mev-network-graph", "./data/miserables.json", "./barchart_demo.spec.json", "./force.spec.json", "./mynet.json"],
function(ng, d3, vg, mevNetworkGraph, miserablesJson, barchartJson, networkJson, mynetJson){"use strict";
    var demo = ng.module("mev-network-graph-demo", arguments, arguments)
        .controller("DemoCtrl", ["$scope", function($scope){

            function parse(spec, id, renderer) {
                vg.parse.spec(spec, function(error, chart) {
                    var view = chart({
                        el:"#"+id,
                        renderer: renderer || "canvas"
                    }).update();

                    view.onSignal("hoverNode", function(event, item){
                       console.debug(event, item);
                       console.debug("activeNode", view.data("activeNode").values());
                    });
                });
            }
            // parse(barchartJson, "barchart");
            parse(mynetJson, "network-svg", "svg");
            parse(mynetJson, "network-canvas");

            $scope.mevNetworkGraph = {
                renderer: 'canvas',
                edge: {
                    field: "links"
                },
                data: miserablesJson
            };
            $scope.mevNetworkGraphColor = {
                renderer: 'canvas',
                edge: {
                    field: "links"
                },
                node: {
                    color: {
                        field: "group",  scale: {}
                    },
                    tooltip: {
                        fields: [{
                            "name": "name",
                            "label": "Gene"
                        }]
                    }
                },
                data: miserablesJson
            }

        }]);

    ng.element(document).ready(function(){
       ng.bootstrap(document, [demo.name]);
    });
});