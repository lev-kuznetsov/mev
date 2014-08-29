(function () {
    "use strict";

    define([], function () {

        return function (http, q, rootScope) {
            var self = this;

            self.status = null;

            self.nodeData = [];
            self.edgeData = []
            self.nodeAttrs = [];
            self.edgeAttrs = [];
            self.shapes = [
                'ellipse',
                'triangle',
                'rectangle',
                'roundrectangle',
                'pentagon',
                'octagon',
                'hexagon',
                'heptagon',
                'star'
            ];

            self.typeColors = {
                'ellipse': '#992222',
                'triangle': '#222299',
                'rectangle': '#661199',
                'roundrectangle': '#772244',
                'pentagon': '#990088',
                'hexagon': '#229988',
                'heptagon': '#118844',
                'octagon': '#335577',
                'star': '#113355'
            };

            self.inspect = []

            self.fGetStatus = function (name) {
                var qfGet = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name
                    },
                    url: 'view_status.json'
                })
                    .success(function (data) {
                        self.status = data.status;
                        qfGet.resolve(data);
                    });

                return qfGet.promise;
            };

            self.fAddNode = function (name, type) {
                self.nodeData.push({
                    id: 'n' + self.nodeData.length,
                    name: name,
                    type: type
                });
            };

            self.fAddNodes = function (nodes) {
                var i = 0;
                for (i = 0; i < nodes.length; i += 1) {
                    console.log(i);
                    self.nodeData.push(nodes[i]);
                }
            };

            self.fAddEdge = function (source, target) {
                self.edgeData.push({
                    id: 'e' + self.edgeData.length,
                    source: source,
                    target: target
                });
            };

            self.fAddEdges = function (edges) {
                var i = 0;
                for (i = 0; i < edges.length; i += 1) {
                    self.edgeData.push(edges[i]);
                }
            };

            self.fNodeClick = function (value) {
                var data = window.cy.elements('node[id="' + value + '"]').data(), k;
                self.inspect = [['type','node']];
                for (k in data) {
                    self.inspect.push([k, data[k]]);
                }
                console.log(self.inspect);
                rootScope.$broadcast('showInspector');
            };

            self.fEdgeClick = function (value) {
                var data = window.cy.elements('edge[id="' + value + '"]').data(), k;
                self.inspect = [['type','edge']];
                for (k in data) {
                    self.inspect.push([k, data[k]]);
                }
                console.log(self.inspect);
                rootScope.$broadcast('showInspector');
            };

            self.fInit = function (name) {

                var  qfInit = q.defer(), qPreload = q.defer(), i = 0, dType = '';

                // Pre-load graph data
                if (self.status !== null) {
                    self.fPreload(name).then(function () {
                        qPreload.resolve();
                    });
                } else {
                    qPreload.resolve();
                }

                qPreload.promise.then(function () {
                    // Initialize data object
                    self.elements = {};
                    self.elements.nodes = [];
                    self.elements.edges = [];

                    // Edges
                    for (i = 0; i < self.edgeData.length; i += 1) {
                        self.elements.edges.push({
                            group: 'edges',
                            data: self.edgeData[i]
                        });
                    }
                    // Nodes
                    for (i = 0; i < self.nodeData.length; i += 1) {
                        self.elements.nodes.push({
                            group: dType,
                            data: self.nodeData[i]
                        });
                    }

                    // Initialize canvas
                    window.cy = cytoscape({
                        container: document.getElementById('canvas'),
                        layout: {
                            name: 'circle',
                            fit: true,
                            padding: 5
                        },
                        style: cytoscape.stylesheet()
                            .selector('node').css({
                                'background-color': 'white',
                                'border-color': '#323232',
                                'border-width': '0.5px',
                                'content': 'data(name)',
                                'text-valign': 'center',
                                'color': '#323232',
                                'min-zoomed-font-size': '10px',
                                'font-family': 'arial',
                                'font-size': '4',
                                'height': '10',
                                'width': '10',
                                'text-outline-color': 'white',
                                'text-outline-width': '0.25px'
                            })
                            .selector('edge').css({
                                'target-arrow-shape': 'triangle',
                                'width': '0.25px'
                            })
                            .selector(':selected').css({
                                'target-arrow-color': 'black',
                                'source-arrow-color': 'black'
                            })
                            .selector('.faded').css({
                                'opacity': 0.25,
                                'text-opacity': 0
                            }),

                        ready: function () {
                            var cy = this;
                            cy.on('tap', 'node', function (e) {
                                var evtTarget = e.cyTarget, nodeId = evtTarget.id();
                                self.fNodeClick(nodeId);
                            });
                            cy.on('tap', 'edge', function (e) {
                                var evtTarget = e.cyTarget, edgeId = evtTarget.id();
                                self.fEdgeClick(edgeId);
                            });

                            cy.load(self.elements, undefined, function () { qfInit.resolve(); });
                        }

                    });
                });

                return qfInit.promise;
            };

            self.fPreload = function (name) {
                var qfPreload = q.defer();
                console.log(self.status)
                http({
                    method: 'POST',
                    data: {
                        name: name,
                        status: self.status
                    },
                    url: 'view_preload.json'
                })
                    .success(function (data) {
                        console.log(data);
                        self.nodeData = data.vs;
                        self.edgeData = data.es;
                        self.nodeAttrs = data.vas;
                        self.edgeAttrs = data.eas;
                        qfPreload.resolve(data);
                        if (0.5 == data.err) {
                            alert('Too many nodes in the 1st-order neighborhood.');
                        }
                    });

                return qfPreload.promise;
            };

            self.fAutoFormat = function (na, ea) {
                // Nodes
                var max, min, diff, bin, nbin, i, coll, maxw;
                nbin = 10;
                max = Math.max.apply(null, self.nodeAttrs[na]);
                min = Math.min.apply(null, self.nodeAttrs[na]);
                diff = max - min;
                bin = diff / nbin;
                maxw = 15;
                for (i = 0; i <= nbin; i += 1) {
                    coll = window.cy.elements('node[size>=' + (min + (i * bin)) + '][size<=' + (min + (i + 1) * bin) + ']');
                    coll.css({
                        'background-color': 'rgb(255,' + parseInt(255 - (255 / nbin) * (i + 1), 10) + ',' + parseInt(255 - (255 / nbin) * (i + 1), 10) + ')',
                        width: Math.sqrt((i + 1) / nbin) * maxw + 'px',
                        height: Math.sqrt((i + 1) / nbin) * maxw + 'px'
                    });
                }

                // Edges
                nbin = 10;
                max = Math.max.apply(null, self.edgeAttrs[ea]);
                min = Math.min.apply(null, self.edgeAttrs[ea]);
                diff = max - min;
                bin = diff / nbin;
                for (i = 0; i < nbin; i += 1) {
                    coll = window.cy.elements('edge[weight>=' + (min + (i * bin)) + '][weight<=' + (min + (i + 1) * bin) + ']');
                    coll.css({
                        'line-color': 'rgb(' + parseInt(255 - (255 / nbin) * (i + 1), 10) + ',' + parseInt(255 - (255 / nbin) * (i + 1), 10) + ',' + parseInt(255 - (255 / nbin) * (i + 1), 10) + ')',
                        'target-arrow-color': 'rgb(' + parseInt(255 - (255 / nbin) * (i + 1), 10) + ',' + parseInt(255 - (255 / nbin) * (i + 1), 10) + ',' + parseInt(255 - (255 / nbin) * (i + 1), 10) + ')',
                        'width': ((1.0 / (nbin - i + 1)) * 2) + 'px'
                    });
                }
            };

            self.fSetLayout = function (layout_name) {
                window.cy.layout({name: layout_name});
            };

            self.fViewFit = function () {
                window.cy.fit();
            };

        };

    });

}());