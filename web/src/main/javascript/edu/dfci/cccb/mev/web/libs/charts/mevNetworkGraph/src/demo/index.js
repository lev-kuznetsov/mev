define(["mui", "d3", "vega", "mev-network-graph", "./data/miserables.json", "./data/miserables.edges.json",  "./barchart_demo.spec.json", "./force.spec.json", "./mynet.json"],
function(ng, d3, vg, mevNetworkGraph, miserablesJson, miserablesEdgesJson, barchartJson, networkJson, mynetJson){"use strict";
    var demo = ng.module("mev-network-graph-demo", arguments, arguments)
        .controller("DemoCtrl", ["$scope", function($scope){
            $scope.vm = {};
            function parse(spec, id, renderer) {
                vg.parse.spec(spec, function(error, chart) {
                    var view = chart({
                        el:"#"+id,
                        renderer: renderer || "canvas"
                    }).update();

                    view.onSignal("hoverNode", function(event, item){
                       console.debug(event, item);
                       console.debug("activeNode", view.data("activeNode").values());
                       console.debug("edges_transformed", view.data("edges_transformed").values());
                    });
                    view.onSignal("hoverEdge", function(event, item){
                        console.debug(event, item);
                        console.debug("activeEdge", view.data("activeEdge"));
                        $scope.$apply(function(scope) {
                            scope.$emit("edge:active:toggle", { event: event, item:  item, view: view });
                        });
                    });
                });
            }
            // parse(barchartJson, "barchart");
            parse(networkJson, "network-svg", "svg");
            parse(mynetJson, "network-canvas");

            $scope.$on("edge:active:toggle", function(event, item){
                $scope.vm.activeEdge = item.item;
                var tooltip = ng.element(".edge-tooltip");
                tooltip.css({
                    position: "absolute",
                    border: "solid 5px black",
                    "background-color": "red",
                    top: item.view._el.offsetTop + item.item.y,
                    left: item.view._el.offsetLeft + item.item.x
                });
            });

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

            $scope.mevNetworkGraphEdgesOnly = {
                renderer: 'canvas',
                edge: {
                    field: "links",
                    source: {
                        field: "source"
                    },
                    target: {
                        field: "target"
                    }
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
                data: miserablesEdgesJson
            };
        }]);
    
    ng.element(document).ready(function(){
       ng.bootstrap(document, [demo.name]);
    });
});