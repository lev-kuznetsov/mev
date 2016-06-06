define(["lodash", "d3", "vega", "./mevNetwork.vegaspec.json", "./mevNetwork.tpl.html", "./mevNetwork.less"], function(_, d3, vg, specJson, template){
    var randomColor = (function(){
        var golden_ratio_conjugate = 0.618033988749895;
        var h = Math.random();

        var hslToRgb = function (h, s, l){
            var r, g, b;

            if(s == 0){
                r = g = b = l; // achromatic
            }else{
                function hue2rgb(p, q, t){
                    if(t < 0) t += 1;
                    if(t > 1) t -= 1;
                    if(t < 1/6) return p + (q - p) * 6 * t;
                    if(t < 1/2) return q;
                    if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
                    return p;
                }

                var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
                var p = 2 * l - q;
                r = hue2rgb(p, q, h + 1/3);
                g = hue2rgb(p, q, h);
                b = hue2rgb(p, q, h - 1/3);
            }

            return '#'+Math.round(r * 255).toString(16)+Math.round(g * 255).toString(16)+Math.round(b * 255).toString(16);
        };

        return function(){
            h += golden_ratio_conjugate;
            h %= 1;
            return hslToRgb(h, 0.5, 0.60);
        };
    })();

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
                        source: { field: "from" },
                        target: { field: "to" },
                        weight: {
                            field: "value",
                            scale: {
                                "name": "weight",
                                "type": "linear",
                                "domain": {
                                    "data": "edges",
                                    "field": "weight"
                                },
                                "range": [1, 2]
                            }
                        },
                        tooltip: {
                            fields: [{
                                name: "weight",
                                label: "Weight"
                            }]
                        }
                    },
                    node: {
                        field: "nodes",
                        key: {
                            field: "name"
                        },
                        color: {
                            field: "group",
                            value: "steelblue",
                            legend: {
                                "fill": "color",
                                "orient": "right"
                            }
                        },
                        tooltip: {
                            fields: [{
                                "name": "name",
                                "label": "Name"
                            }]
                        }
                    },
                    selections: []
                };
                if(scope.config.node && scope.config.node.color && scope.config.node.color.field){

                    if(scope.config.node.color.field==="group") {
                        //if color field is "group" assume we are displaying user selections
                        //we'll generate the color scale based on selection colors if they exist
                        defaults.node.color.scale = {
                            "name": "color",
                            "type": "ordinal",
                            "domain": ["none"],
                            "range": [defaults.node.color.value]
                        };
                        //remove the color.value so vega does not use it in place of the scale
                        defaults.node.color.value = undefined;
                    }else{
                        //user provided color field, so remove color.value
                        //we'll have to use a scale
                        defaults.node.color.value = undefined;

                        //if user supplied a color field other than 'group',
                        // then use d3's provided 'color10' scale
                        defaults.node.color.scale = {
                            "name": "color",
                            "type": "ordinal",
                            "domain": {
                                "data": "nodes", "field": "color"
                            },
                            "range": "category10"
                        };
                    }

                }
                _.defaultsDeep(scope.config, _.cloneDeep(defaults));

                function buildSpec(){
                    var spec = _.cloneDeep(specJson);
                    //set spec globals
                    spec.height = (scope.config.height || spec.height);
                    spec.width = (scope.config.width || spec.width);
                    spec.padding = (scope.config.padding || spec.padding);

                    //config edges
                    //d3's Force Layout requires that edges have the
                    // "source" and "target" fields be indexes in the nodes array
                    //So here we remember the original value in "origSource"/"origTarget" fields
                    //Unless "source" and "target" are numeric and nodes array is specified
                    // - in this case we assume "source" and "target" are already node indexes
                    var edges = scope.config.data[scope.config.edge.field];
                    var nodes = scope.config.data[scope.config.node.field];
                    var hasNodes = nodes ? true : false;
                    if(scope.config.edge.source.field === "source" && hasNodes){
                        var errorEdge = edges.find(function(edge){
                            return !_.isInteger(edge.source);
                        });
                        if(errorEdge)
                            throw new Error("Edge's source must be an index in the nodes array: " + JSON.stringify(errorEdge))
                    }
                    if(scope.config.edge.target.field === "target" && hasNodes){
                        var errorEdge = edges.find(function(edge){
                            return !_.isInteger(edge.target);
                        });
                        if(errorEdge)
                            throw new Error("Edge's target must be an index in the nodes array: " + JSON.stringify(errorEdge))
                    }

                    //apply edge interface to edge rows
                    var data = _.transform(edges, function(data, edge, key, edges){
                        var sourceFieldName = scope.config.edge.source.field;
                        var targetFieldName = scope.config.edge.target.field;

                        var sourceKey = edge[sourceFieldName];
                        var targetKey = edge[targetFieldName];
                        function getNodeIndex(data, edge, key, field){
                            var index = data.nodeMap[key];
                            if(_.isUndefined(index)){
                                index = _.findIndex(data.nodes, function(node){
                                    return node.name === key;
                                });
                                if(index===-1)
                                    throw new Error(field + " node not found for edge: " + JSON.stringify(edge));
                                edge[field] = index;
                                data.nodeMap[key] = index;
                            }else{
                                edge[field] = index;
                            }
                        }
                        function addNode(data, edge, key, field){
                            var index = data.nodeMap[key];
                            if(_.isUndefined(index)){
                                data.nodes.push({name: key});
                                data.nodeMap[key] = data.nodes.length - 1;
                                edge[field] = data.nodes.length - 1;
                            }else{
                                edge[field] = index;
                            }

                        }

                        if(hasNodes && (!_.isInteger(edge.source) || !_.isInteger(edge.target))){
                            getNodeIndex(data, edge, sourceKey, "source");
                            getNodeIndex(data, edge, targetKey, "target");
                        }else if(!hasNodes){
                            addNode(data, edge, sourceKey, "source");
                            addNode(data, edge, targetKey, "target");
                        }

                        if(scope.config.edge.weight && _.isUndefined(edge.weight))
                            Object.defineProperty(edge, "weight", {
                                enumerable: true,
                                get: function(){return this[scope.config.edge.weight.field]},
                                set: function(val){return this[scope.config.edge.weight.field]=val}
                            });
                        return edge;
                    }, {edges: edges, nodes: nodes || [], nodeMap: {}});
                    //set edge data values
                    _.assign(spec.data.find(function(item){
                        return item.name === "edges";
                    }), {
                        values: data.edges
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
                    var checkedSelections = getCheckedSelections(scope.config.selections);
                    data.selections = [{name: "none", color: "steelblue"}];
                    data.nodes.forEach(function(node){
                        var groups = _.cloneDeep(checkedSelections);
                        node.groups = node.groups || [];
                        if(groups.length>0){
                            groups.forEach(function(group){
                                group.keyMap = (group.keyMap || {});
                                if(group.keyMap[node.name]){
                                    node.groups.push(group.name);
                                }else{
                                    if(group.keys.find(function(key){
                                            return key===node.name;
                                        })
                                    ){
                                        group.keyMap[node.name] = true;
                                        node.groups.push(group.name);
                                    }

                                }
                            });
                        }
                        if(node.groups.length === 0){
                            node.group = "none";
                            node.color = "steelblue";
                        }else{
                            node.group = _.orderBy(node.groups).join("+");
                            if(!scope.config.node.color.scale.domain.find(function(groupName){
                                    return groupName === node.group;
                                })){
                                if(node.groups.length === 1){
                                    var group = groups.find(function(group){
                                        return group.name === node.groups[0];
                                    });
                                    scope.config.node.color.scale.domain.unshift(node.group);
                                    scope.config.node.color.scale.range.unshift(group.properties.selectionColor);
                                    data.selections.unshift({name: node.group, color: group.properties.selectionColor});
                                }else{
                                    var color = randomColor();
                                    scope.config.node.color.scale.domain.unshift(node.group)
                                    scope.config.node.color.scale.range.unshift(color);
                                    data.selections.unshift({name: node.group, color: color});
                                }
                            }

                            if(node.groups.length === 1){
                                var group = groups.find(function(group){
                                    return group.name === node.groups[0];
                                });
                                node.group = node.groups[0];
                                node.color = group.properties.selectionColor;
                            }else{
                                var existingGroupIndex = _.findIndex(scope.config.node.color.scale.domain, function(groupName){
                                    return groupName === node.group;
                                });
                                node.color = scope.config.node.color.scale.range[existingGroupIndex];
                            }

                        }

                        // if(scope.config.node.color && _.isUndefined(node.color))
                        //     Object.defineProperty(node, "color", {
                        //         enumerable: true,
                        //         get: function(){return this[scope.config.node.color.field];},
                        //         set: function(val){this[scope.config.node.color.field]=val;}
                        //     });
                        if(scope.config.node.shape && _.isUndefined(node.shape))
                            Object.defineProperty(node, "shape", {
                                enumerable: true,
                                get: function(){return this[scope.config.node.shape.field];},
                                set: function(val){this[scope.config.node.shape.field]=val;}
                            });
                        if(scope.config.node.size && _.isUndefined(node.size))
                            Object.defineProperty(node, "size", {
                                enumerable: true,
                                get: function(){return this[scope.config.node.size.field];},
                                set: function(val){this[scope.config.node.size.field]=val;}
                            });
                        return node;
                    });

                    //set data values
                    _.assign(spec.data.find(function(item){
                        return item.name === "nodes";
                    }), {
                        values: data.nodes
                    });
                    _.assign(spec.data.find(function(item){
                        return item.name === "selections";
                    }), {
                        values: _.orderBy(data.selections, function(selection){
                          if(selection.name==="none")
                              return "zzzzzzzzzzzzzzz";
                          else
                              return selection.name;
                        })
                    });
                    //color: set scale
                    if(scope.config.node.color.scale){
                        spec.scales.push(scope.config.node.color.scale);
                        // spec.legends.push(scope.config.node.color.legend);
                    }
                    //color: set marks
                    _.assign(spec.marks.find(function(mark){
                        return mark.name === "node"
                    }).properties.update,
                        // {
                        //     "stroke": scope.config.node.color.scale
                        //         ? { "scale": scope.config.node.color.scale.name || "color", "field": "color"}
                        //         : { "value": scope.config.node.color.value },
                        //     "fill": scope.config.node.color.scale
                        //         ? { "scale": scope.config.node.color.scale.name || "color", "field": "color"}
                        //         : {"value": scope.config.node.color.value }
                        // }
                        {
                            "stroke": {
                                "field": "color"
                            },
                            "fill": {
                                "field": "color"
                            }
                        }
                    );
                    //node tooltip
                    _.assign(spec.data.find(function(data){
                        return data.name === "tooltip";
                    }), {
                        "values": scope.config.node.tooltip.fields
                    });
                    return spec;
                }

                function getCheckedSelections(selections){
                    if(!_.isArray(selections))
                        return [];
                    return _.filter(selections, function(s){return s.checked;});
                }

                scope.vm = {
                    defaults: defaults,
                    buildSpec: buildSpec,
                    saveAsConfig: {
                        name: scope.config.name ? scope.config.name : "mev-network-graph.png",
                        selector: '.vega svg'
                    },
                    getCheckedSelections: getCheckedSelections

                };


                console.debug("spec", scope.vm.spec);
            }],
            link: function(scope, elm, attr, ctrl){
                function parse(spec, renderer) {
                    var vgElm = elm.find(".vega-container");
                    vgElm.empty();
                    vg.parse.spec(spec, function(error, chart) {
                        var view = scope.vm.view = chart({
                            el: vgElm[0],
                            renderer: renderer || "canvas"
                        }).update();

                        view.onSignal("hoverNode", function(signal, item){
                            console.debug(event, item);
                            scope.$apply(function(scope) {
                                if(scope.config.edge.tooltip)
                                    scope.$emit("mev:network:node:active:toggle", signal, item, view);
                            });
                        });

                        view.onSignal("hoverEdge", function(signal, item){
                            console.debug(event, item);
                            scope.$apply(function(scope) {
                                if(scope.config.edge.tooltip)
                                    scope.$emit("mev:network:edge:active:toggle", signal, item, view);
                            });
                        });
                        scope.vm.view.update();
                    });
                }
                parse(scope.vm.buildSpec(), scope.config.renderer);
                scope.$on("mev:network:edge:active:toggle", function(event, signal, item, view){
                    scope.vm.tooltip = {
                        item: item,
                        position: {
                            top: item.y + view._el.offsetTop + 10,
                            left: item.x + view._el.offsetLeft + 10,
                        },
                        config: scope.config.edge.tooltip
                    };
                    var tooltip = elm.find(".mev-network-graph-tooltip");
                });
                scope.$on("mev:network:node:active:toggle", function(event, signal, item, view){
                    scope.vm.tooltip = {
                        item: item,
                        position: {
                            top: item.y + view._el.offsetTop + 10,
                            left: item.x + view._el.offsetLeft + 10,
                        },
                        config: scope.config.node.tooltip
                    };
                    var tooltip = elm.find(".mev-network-graph-tooltip");
                });
                scope.vm.updateSelection = function(){
                    scope.config.node.color.scale = _.cloneDeep(scope.vm.defaults.node.color.scale);
                    // parse(scope.vm.buildSpec(), scope.config.renderer);
                    var spec = scope.vm.buildSpec();
                    scope.vm.view.data("nodes").update(function(node){
                        return true;
                    }, "color", function(node){
                        var newNode = _.find(_.find(spec.data, {"name": "nodes"}).values, function(newNode){
                           return node.name === newNode.name;
                        });
                        return newNode.color;
                    });
                    scope.vm.view.data("selections")
                        .remove(function(selection){
                            return true;
                        })
                        .insert(_.find(spec.data, {"name": "selections"}).values);
                    scope.vm.view.update();
                }

            }
        }
    };
    directive.$name="mevNetworkGraph";
    directive.$provider="directive";
    directive.$inject=[];
    return directive;
});