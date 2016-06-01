define(["lodash", "d3", "vega", "./mevNetwork.vegaspec.json", "./mevNetwork.tpl.html"], function(_, d3, vg, specJson, template){
    var directive = function mevNetworkDirective(){
        return {
            restrict: "AEC",
            scope: {
                config: "=mevNetworkGraph"
            },
            // template: "<div vega spec=\"vm.spec\"  vega-renderer=\"renderer\"></div>",
            template: template,
            controller: ["$scope", function(scope){
                if(!scope.config.renderer)
                    scope.config.renderer = "canvas";
                var defaults = {
                    edge: {
                        field: "edges",
                        source: { field: "source" },
                        target: { field: "target" },
                        weight: {
                            field: "value",
                            scale: {
                                "name": "weight",
                                "type": "linear",
                                "domain": {
                                    "data": "edges", "field": "weight"
                                },
                                "range": [1, 2]
                            }
                        }
                    },
                    node: {
                        color: {
                            field: "group",
                            value: "red"
                        },
                        tooltip: {
                            fields: [
                                {
                                    "name": "name",
                                    "label": "Name"
                                }
                            ]
                        }
                    }
                };
                if(scope.config.node && scope.config.node.color && scope.config.node.color.scale) {
                    defaults.node.color.value = undefined;
                    defaults.node.color.scale = {
                        "name": "colors",
                        "type": "ordinal",
                        "domain": {
                            "data": "nodes", "field": "color"
                        },
                        "range": "category20"
                    };
                }

                _.defaultsDeep(scope.config, defaults);

                var spec = specJson;
                //set spec globals
                spec.height = (scope.config.height || spec.height);
                spec.width = (scope.config.width || spec.width);
                spec.padding = (scope.config.padding || spec.padding);

                //config edges
                //apply edge interface to edge rows
                var edges = scope.config.data[scope.config.edge.field].map(function(edge){
                    var newEdge = edge;
                    newEdge.s = (edge.s || edge[scope.config.edge.source.field]);
                    newEdge.t = (edge.t || edge[scope.config.edge.target.field]);
                    if(scope.config.edge.source && _.isUndefined(newEdge.source))
                        Object.defineProperty(newEdge, "source", {
                            enumerable: true,
                            get: function () {
                                return this[scope.config.edge.source.field];
                            },
                            set: function (val) {
                                return this[scope.config.edge.source.field] = val
                            }
                        });
                    if(scope.config.edge.target && _.isUndefined(newEdge.target))
                        Object.defineProperty(newEdge, "target", {
                            enumerable: true,
                            get: function () {
                                return this[scope.config.edge.target.field];
                            },
                            set: function (val) {
                                return this[scope.config.edge.target.field] = val
                            }
                        });

                    if(scope.config.edge.weight && _.isUndefined(newEdge.weight))
                        Object.defineProperty(newEdge, "weight", {
                            enumerable: true,
                            get: function(){return this[scope.config.edge.weight.field]},
                            set: function(val){return this[scope.config.edge.weight.field]=val}
                        });
                    return newEdge;
                });
                //set edge data values
                _.assign(spec.data.find(function(item){
                    return item.name === "edges";
                }), {
                    values: edges
                });
                //set edge scale 
                spec.scales.push(scope.config.edge.weight.scale);
                //set edge marks
                _.assign(spec.marks.find(function(mark){
                    return mark.name === "edge"
                }).properties.update, {
                   "strokeWidth": { "scale": "weight", "field": "weight"}
                });
                
                //config nodes
                //apply node interface to node rows
                var nodes;
                if(scope.config.data.nodes){
                    nodes = scope.config.data.nodes.map(function(node){
                        var newNode = node;
                        if(scope.config.node.color && _.isUndefined(newNode.color))
                            Object.defineProperty(newNode, "color", {
                                enumerable: true,
                                get: function(){return this[scope.config.node.color.field];},
                                set: function(val){this[scope.config.node.color.field]=val;}
                            });
                        if(scope.config.node.shape && _.isUndefined(newNode.shape))
                            Object.defineProperty(newNode, "shape", {
                                enumerable: true,
                                get: function(){return this[scope.config.node.shape.field];},
                                set: function(val){this[scope.config.node.shape.field]=val;}
                            });
                        if(scope.config.node.size && _.isUndefined(newNode.size))
                            Object.defineProperty(newNode, "size", {
                                enumerable: true,
                                get: function(){return this[scope.config.node.size.field];},
                                set: function(val){this[scope.config.node.size.field]=val;}
                            });
                        return newNode;
                    });
                }else{
                    nodes = _.transform(edges, function(result, edge, index){
                        var sourceFieldName = scope.config.edge.source.field;
                        var source = edge.s;
                        var  nodeIndex = result.hash[source];
                        if(_.isUndefined(nodeIndex)){
                            result.list.push({name: edge.s});
                            result.hash[source] = result.list.length - 1;
                            edge[sourceFieldName] = result.list.length - 1;
                        }else{
                            edge[sourceFieldName] = nodeIndex;
                        }

                        var targetFieldName = scope.config.edge.target.field;
                        var target = edge.t;
                        nodeIndex = result.hash[target];
                        if(_.isUndefined(nodeIndex)){
                            result.list.push({name: edge.t});
                            result.hash[target] = result.list.length - 1;
                            edge[targetFieldName] = result.list.length - 1;
                        }else{
                            edge[targetFieldName] = nodeIndex;
                        }
                    }, {
                        hash: {},
                        list: []
                    }).list;
                }
                //set data values
                _.assign(spec.data.find(function(item){
                    return item.name === "nodes";
                }), {
                    values: nodes
                });
                //color: set scale
                if(scope.config.node.color.scale){
                    spec.scales.push(scope.config.node.color.scale);
                }
                //color: set marks
                _.assign(spec.marks.find(function(mark){
                    return mark.name === "node"
                }).properties.update, {
                    "stroke": scope.config.node.color.scale
                        ? { "scale": scope.config.node.color.scale.name || "color", "field": "color"}
                        : { "value": scope.config.node.color.value },
                    "fill": scope.config.node.color.scale
                        ? { "scale": scope.config.node.color.scale.name || "color", "field": "color"}
                        : {"value": scope.config.node.color.value }
                });
                //node tooltip
                _.assign(spec.data.find(function(data){
                    return data.name === "tooltip";
                }), {
                    "values": scope.config.node.tooltip.fields
                });

                scope.vm = {
                    spec: spec,
                    saveAsConfig: {
                        name: scope.config.name ? scope.config.name : "mev-network-graph.png",
                        selector: '.vega svg'
                    }
                };


                console.debug("spec", spec);
            }],
            link: function(scope, elm, attr, ctrl){
                function parse(spec, renderer) {
                    var vgElm = elm.find(".vega-container");
                    vg.parse.spec(spec, function(error, chart) {
                        var view = chart({
                            el: vgElm[0],
                            renderer: renderer || "canvas"
                        }).update();

                        view.onSignal("hoverNode", function(event, item){
                            console.debug(event, item);
                            console.debug("activeNode", view.data("activeNode").values());
                            console.debug("edge", view.data("activeNode").values());
                        });
                    });
                }
                parse(scope.vm.spec, scope.config.renderer);
            }
        }
    };
    directive.$name="mevNetworkGraph";
    directive.$provider="directive";
    directive.$inject=[];
    return directive;
});