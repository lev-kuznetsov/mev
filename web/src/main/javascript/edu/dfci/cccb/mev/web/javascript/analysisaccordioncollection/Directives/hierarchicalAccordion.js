(function () {

    define(['d3'], function (d3) {

        return function (module) {
            module.directive('hierarchicalAccordion', ['pathService',
                function (paths) {

                    return {
                        restrict: 'E',
                        scope: {
                            analysis: "=analysis",
                            project: "=project"

                        },
                        templateUrl:paths.module + '/templates/hierarchicalAccordion.tpl.html',
                        link: function (scope, elems, attr) {

                            scope.applyToHeatmap = function () {

                                var labels = traverse(scope.analysis.root);

                                if (scope.analysis.dimension == "column") {

                                    scope.project.generateView({
                                        viewType: 'heatmapView',
                                        labels: {
                                            row: {
                                                keys: scope.project.dataset.row.keys
                                            },
                                            column: {
                                                keys: labels
                                            }
                                        },
                                        expression: {
                                            min: scope.project.dataset.expression.min,
                                            max: scope.project.dataset.expression.max,
                                            avg: scope.project.dataset.expression.avg,
                                        },
                                        panel: {
                                            top: scope.analysis
                                        }
                                    });

                                } else {
                                    scope.project.generateView({
                                        viewType: 'heatmapView',
                                        labels: {
                                            column: {
                                                keys: scope.project.dataset.column.keys
                                            },
                                            row: {
                                                keys: labels
                                            }
                                        },
                                        expression: {
                                            min: scope.project.dataset.expression.min,
                                            max: scope.project.dataset.expression.max,
                                            avg: scope.project.dataset.expression.avg,
                                        },
                                        panel: {
                                            side: scope.analysis
                                        }
                                    });
                                }
                            };

                            function traverse(tree) {

                                var leaves = {
                                    '0': [],
                                    '1': []
                                };

                                if (tree.children.length > 0) {
                                    for (var i = 0; i < tree.children.length; i++) {
                                        leaves[i] = (!tree.children[i].children) ? [tree.children[i].name] : traverse(tree.children[i])
                                    }
                                };

                                return leaves[0].concat(leaves[1]);
                            };

                            var padding = 20;

                            var labelsGutter = 50;

                            var panel = {
                                height: 200 + padding,
                                width: pageWidth = 700,
                            };

                            var Cluster = d3.layout
                                .cluster()
                                .sort(null)
                                .separation(
                                    function (a, b) {
                                        return a.parent == b.parent ? 1 : 1
                                    })
                                .value(
                                    function (d) {
                                        return d.distance;
                                    })
                                .children(
                                    function (d) {
                                        return d.children;
                                    });



                            var xPos = d3.scale
                                .linear()
                                .domain([0, 1])
                                .range([padding, panel.width - padding]);

                            var yPos = d3.scale
                                .linear()
                                .domain([0, 1])
                                .range([padding, panel.height - padding - labelsGutter]);

                            function Path(d) {
                                // Path function builder for TOP
                                // heatmap tree path attribute

                                return "M" + (xPos(d.target.x)) + "," + (yPos(d.target.y)) + "V" + (yPos(d.source.y)) + "H" + (xPos(d.source.x));

                            };

                            function noder(d) {

                                var a = [];

                                if (!d.children) {
                                    a.push(d);
                                } else {
                                    d.children.forEach(function (child) {
                                        noder(child).forEach(function (j) {
                                            a.push(j)
                                        });
                                    });
                                };

                                return a;
                            };

                            function drawAnalysisTree(canvas, cluster, tree, type) {

                                canvas.selectAll('*')
                                    .remove();

                                var nodes = cluster.nodes(tree);

                                var links = cluster
                                    .links(nodes);

                                var labels = noder(tree);

                                if (labels.length <= 50) {
                                    canvas
                                        .selectAll(
                                            "text")
                                        .data(labels)
                                        .enter()
                                        .append(
                                            "text")

                                    .attr(
                                        "transform",
                                        function (d) {
                                            return "translate(" + xPos(d.x) + "," + yPos(d.y) + ")rotate(90)"
                                        })
                                        .attr(
                                            "text-anchor",
                                            "start")
                                        .attr("dx", 5)
                                        .attr("dy", 3)
                                        .text(
                                            function (d) {
                                                return d.name
                                            })

                                };

                                canvas
                                    .selectAll("path")
                                    .data(links)
                                    .enter()
                                    .append("path")
                                    .attr("d", function (d) {
                                        return Path(d)
                                    })
                                    .attr("stroke", function () {
                                        return "grey"
                                    }).attr("fill", "none");

                                canvas.selectAll("circle").data(nodes)
                                    .enter()
                                    .append("circle")
                                    .attr("r", 2.5)
                                    .attr("cx", function (d) {
                                        return xPos(d.x);
                                    })
                                    .attr("cy", function (d) {
                                        return yPos(d.y);
                                    })
                                    .attr("fill", function (d) {
                                        return "red"
                                    })
                                    .on("click", function (d) {
                                        //
                                    });

                            };
                            scope.$watch('analysis', function (newval, oldval) {
                                if (newval) {
                                    //the svg is declared in the templste along with <style>
                                    //the styles are needed to support 'Save Image' function
                                    //                        d3.select(elems[0]).select('div#svgPlace').append('svg');

                                    var svg = d3.select(elems[0]).select('div#svgPlace').select('svg')

                                    svg.attr({
                                        width: panel.width,
                                        height: panel.height + padding
                                    });

                                    svg.append("g");

                                    var panelWindow = d3
                                        .select(elems[0])
                                        .select("svg")
                                        .select("g");



                                    drawAnalysisTree(
                                        panelWindow,
                                        Cluster,
                                        newval.root,
                                        "horizontal");
                                }
                            });

                            scope.saveImage = function (cluster) {

                                var svg = d3.select(elems[0]).select("svg")
                                console.debug("svg", svg);

                                var html = svg
                                    .attr("version", 1.1)
                                    .attr("xmlns", "http://www.w3.org/2000/svg")
                                    .node().parentNode.innerHTML;

                                var imgsrc = 'data:image/svg+xml;base64,' + btoa(html);
                                //                	 var img = '<img src="'+imgsrc+'">';
                                //                	 d3.select("#svgdataurl").html(img);


                                //                	 var canvas = document.querySelector("#canvasHclTree_"+cluster.name);                	 
                                var canvas = elems.find("canvas")[0];
                                console.debug("canvas", canvas);
                                var context = canvas.getContext("2d");
                                canvas.width = svg.attr('width');
                                canvas.height = svg.attr('height');

                                var image = new Image;
                                image.src = imgsrc;
                                image.onload = function () {

                                    canvas.style.opacity = 1;
                                    context.beginPath();
                                    context.rect(0, 0, canvas.width, canvas.height);
                                    context.fillStyle = "#FFFFFF";
                                    context.fill();

                                    context.drawImage(image, 0, 0);

                                    canvas.toBlob(function (blob) {
                                        saveAs(blob, cluster.name + ".png");
                                    });
                                    //                    	 var canvasdata = canvas.toDataURL("image/png");
                                    //                    	  
                                    //                    	 var pngimg = '<img src="'+canvasdata+'">';
                                    //                    	 d3.select("#pngdataurl").html(pngimg);
                                    //                    	  
                                    //                    	 var a = document.createElement("a");
                                    //                    	 a.download = "sample.png";
                                    //                    	 a.href = canvasdata;
                                    //                    	 a.click();
                                };

                            };

                        } // end link
                    };

    }])
        }
    })

})()