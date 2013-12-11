define (
    [ 'angular', 'jquery', 'd3', 'newick', 'dropzone', 'services', ],
    function (angular, jq, d3, newick, Dropzone) {

      return angular
          .module ('myApp.directives', [])
          .directive ('appVersion', [ 'appVersion', function (version) {
            return function (scope, elm, attrs) {
              elm.text (version);
            };
          } ])
          .directive ('appName', [ 'appName', function (name) {
            return function (scope, elm, attrs) {
              elm.text (name);
            };
          } ])
          .directive ('mainNavigation',
              [ 'mainMenuBarOptions', function (opts) {
                return {
                  restrict : 'A',
                  templateUrl : '/container/view/elements/mainNavigation',
                  link : function (scope) {
                    scope.menu = opts;
                  }
                };
              } ])
          .directive (
              'heatmapPanels',
              function () {
                return {
                  restrict : 'A',
                  templateUrl : '/container/view/elements/heatmapPanels',
                  link : function (scope, elems, attrs) {

                    jq ('#leftPanel div.well').css ('height', 1000);
                    jq ('#rightPanel div.well').css ('height',
                        $ ('#leftPanel div.well').height ());

                    jq ('#closeRight').hide ();
                    jq ('#closeLeft').hide ();

                    var margin = "2.127659574468085%"

                    scope.expandLeft = function () {

                      jq ('#leftPanel').attr ("class", "span12 marker");
                      jq ('#rightPanel').hide ();
                      jq ('#expandLeft').hide ();
                      jq ('#closeLeft').show ();
                      jq ('#leftPanel').show ();

                      jq ('vis-heatmap svg').attr ("width",
                          jq ('#leftPanel').css ('width').slice (0, -2) * .9);

                    };

                    scope.expandRight = function () {

                      jq ('#leftPanel').hide ();
                      jq ('#expandRight').hide ();
                      jq ('#closeRight').show ();
                      jq ('#rightPanel').show ();
                      jq ('#rightPanel').attr ("class", "span12 marker");
                      jq ('#rightPanel').css ({
                        "margin-left" : "0"
                      })

                    };

                    scope.expandBoth = function () {

                      jq ('#closeRight').hide ();
                      jq ('#closeLeft').hide ();
                      jq ('#expandRight').show ();
                      jq ('#expandLeft').show ();
                      jq ('#rightPanel').show ();
                      jq ('#leftPanel').show ();
                      jq ('#leftPanel').attr ("class", "span6 marker");
                      jq ('#rightPanel').attr ("class", "span6 marker");
                      jq ('#rightPanel').css ({
                        "margin-left" : margin
                      });
                      jq ('vis-heatmap svg').attr ("width",
                          jq ('#leftPanel').css ('width').slice (0, -2) * .9);

                    };

                  }
                };
              })
          .directive ('menubar', [ 'analysisOptions', function (opts) {
            return {
              restrict : 'E',
              templateUrl : '/container/view/elements/menubar',
              link : function (scope) {
                scope.links = opts;
              }
            };
          } ])
          .directive ('expressionPanel', [ function () {
            return {
              restrict : 'A',
              templateUrl : '/container/view/elements/expressionPanel',
              link : function (scope) {

              }
            };
          } ])
          .directive (
              'analysisPanel',
              [
                  'pseudoRandomStringGenerator',
                  'API',
                  '$routeParams',
                  function (prsg, API, $rP) {
                    return {
                      restrict : 'A',
                      templateUrl : '/container/view/elements/analysisPanel',
                      link : function (scope) {

                        function buildPrevioiusClusters () {

                          API.dataset.analysis.list ($rP.datasetName).then (
                              function (prevList) {

                                scope.previousClusters = prevList
                                    .map (function (name) {

                                      var randstr = prsg (5);

                                      return {
                                        name : name,
                                        href : "#" + randstr,
                                        divId : randstr,
                                        datar : API.analysis.hcl.get ({
                                          name : name,
                                          dataset : $rP.datasetName
                                        })
                                      };

                                    });

                              });

                        }
                        ;

                        buildPrevioiusClusters ();

                      }
                    };
                  } ])
          .directive ('bsprevanalysis', function () {

            return {

              restrict : 'C',
              scope : {

                bindid : '@',
                parentid : '@',
                header : '@',
                data : '@'

              }
            };

          })
          .directive ('bsTable', function () {

            return {
              scope : {
                data : "="
              },
              restrict : 'E',
              templateUrl : "/container/view/elements/table"

            };

          })
          .directive (
              'bsImgbutton',
              function () {

                return {
                  scope : {
                    icon : "@",
                    title : "@",
                    align : "@"
                  },
                  restrict : 'E',
                  template : "<button class='btn btn-success pull-{{align}}' "
                      + "title='{{title}}'>  "
                      + "<i class='icon-{{icon}}'></i> Download" + "</button>"

                };

              })
          .directive ('prevlimma', function () {

            return {

              restrict : 'C',
              templateUrl : "/container/view/elements/prevlimmashell"

            };

          })
          .directive ('bsmodal', [ '$compile', function ($compile) {

            return {

              restrict : 'E',
              scope : {

                bindid : '@',
                header : '@',
                test : '@',
                func : '&'

              },
              transclude : true,
              templateUrl : "/container/view/elements/modal"

            };

          } ])
          .directive ('modalHierarchical',
              [ 'API', '$routeParams', function (API, $routeParams) {

                return {
                  restrict : 'C',
                  templateUrl : "/container/view/elements/hierarchicalbody",
                  link : function (scope, elems, attrs) {

                    scope.availableMetrics = [ 'euclidean' ]; // TODO: Add
                                                              // manhattan,
                                                              // pearson

                    scope.availableAlgorithms = [ 'average' ]; // TODO: Add
                                                                // complete

                    scope.dimensions = [ {
                      name : 'Rows',
                      value : 'row'
                    }, {
                      name : 'Columns',
                      value : 'column'
                    } ]

                    scope.clusterInit = function () {
                      var q = {
                        name : scope.clusterName,
                        dataset : $routeParams.datasetName,
                        dimension : scope.selectedDimension,
                        metric : scope.selectedMetric,
                        algorithm : scope.selectedAlgorithm,
                        callback : function () {

                        }

                      }

                      API.analysis.hcl.create (q);

                    }

                  }

                };

              } ])
          .directive ('modalKmeans', function () {

            return {
              restrict : 'C',
              templateUrl : "/container/view/elements/kMeansBody"

            };

          })
          .directive ('modalLimma', function () {

            return {
              restrict : 'C',
              templateUrl : "/container/view/elements/limmaBody"

            };

          })
          .directive ('uploadsTable',
              [ 'API', '$location', function (API, $location) {
                return {
                  restrict : 'A',
                  scope : {
                    uploads : '='
                  },
                  templateUrl : '/container/view/elements/uploadsTable',
                  link : function (scope, elems, attrs) {

                    scope.datasets = []

                    scope.$watch ('uploads', function (newValues, oldValues) {

                      if (newValues != undefined) {

                        scope.datasets = newValues;

                      }
                      ;

                    });

                  }
                }
              } ])
          .directive (
              'uploadDrag',
              function () {

                return {
                  restrict : 'C',
                  templateUrl : '/container/view/elements/uploadDragAndDrop',
                  link : function (scope, elems, attrs) {

                    var myDropzone = new Dropzone (
                        "#uploader",
                        {

                          url : "/dataset",
                          method : "post",
                          paramName : "dataset",
                          clickable : true,
                          uploadMultiple : true,
                          previewsContainer : null,
                          addRemoveLinks : false,
                          createImageThumbnails : false,
                          previewTemplate : "<div class='dz-preview dz-file-preview'><br>"
                              + "<div class='dz-filename'><span data-dz-name></span> (<span data-dz-size></span>) <span data-dz-errormessage> ✔ </span></div>"
                              + "<div class='dz-size'><span data-dz-size></span></div>"
                              + "<div class='dz-progress'><span class='dz-upload' data-dz-uploadprogress></span></div>"
                              + "<div class ='dz-error-message'></div>"
                              + "</div>",
                          dictResponseError : "File Upload Error. Try Again",
                          dictInvalidFileType : "File Upload Error. Try Again",
                          dictDefaultMessage : "Drop files here",

                        }).on ("error", function (file) {

                    });

                  }
                };

              })
          .directive ('datasetSummary', function () {
            return {
              restrict : 'A',
              scope : {
                datasetobj : "&"
              },
              templateUrl : '/container/view/elements/datasetSummary',
            };

          })
          .directive (
              'd3RadialTree',
              [
                  'API',
                  '$routeParams',
                  function (API, $routeParams) {

                    return {
                      restrict : 'A',
                      scope : {
                        data : '=',
                        diameter : '@'

                      },
                      templateUrl : '/container/view/elements/d3RadialTree',
                      link : function (scope, elems, attr) {

                        var r = scope.diameter / 2;

                        var cluster = d3.layout.cluster ().size ([ 360, 1 ])
                            .sort (null).value (function (d) {
                              return d.length;
                            }).children (function (d) {
                              return d.branchset;
                            }).separation (function (a, b) {
                              return 1;
                            });

                        function project (d) {
                          var r = d.y, a = (d.x - 90) / 180 * Math.PI;
                          return [ r * Math.cos (a), r * Math.sin (a) ];
                        }

                        function cross (a, b) {
                          return a[0] * b[1] - a[1] * b[0];
                        }
                        function dot (a, b) {
                          return a[0] * b[0] + a[1] * b[1];
                        }

                        function step (d) {
                          var s = project (d.source), m = project ({
                            x : d.target.x,
                            y : d.source.y
                          }), t = project (d.target), r = d.source.y, sweep = d.target.x > d.source.x ? 1
                              : 0;
                          return ("M" + s[0] + "," + s[1] + "A" + r + "," + r
                              + " 0 0," + sweep + " " + m[0] + "," + m[1] + "L"
                              + t[0] + "," + t[1]);
                        }

                        var wrap = d3.select (elems[0]).append ("svg").attr (
                            "width", r * 2).attr ("height", r * 2).style (
                            "-webkit-backface-visibility", "hidden");

                        // Catch mouse events in Safari.
                        wrap.append ("rect").attr ("width", r * 2).attr (
                            "height", r * 2).attr ("fill", "none")

                        var vis = wrap.append ("g").attr ("transform",
                            "translate(" + r + "," + r + ")");

                        var start = null, rotate = 0, div = document
                            .getElementById ("vis");

                        function mouse (e) {
                          return [ e.pageX - div.offsetLeft - r,
                              e.pageY - div.offsetTop - r ];
                        }

                        wrap.on ("mousedown", function () {
                          wrap.style ("cursor", "move");
                          start = mouse (d3.event);
                          d3.event.preventDefault ();
                        });

                        function phylo (n, offset) {
                          if (n.length != null)
                            offset += n.length * 115;
                          n.y = offset;
                          if (n.children)
                            n.children.forEach (function (n) {
                              phylo (n, offset);
                            });
                        }

                        function draw (text) {

                          var x = newick.parse(text);
                          var nodes = cluster.nodes(x);
                          phylo(nodes[0], 0);
                         
                          var link = vis.selectAll("path.link")
                              .data(cluster.links(nodes))
                            .enter().append("path")
                              .attr("class", "link")
                              .attr("d", step);
                         
                          var node = vis.selectAll("g.node")
                              .data(nodes.filter(function(n) { return n.x !== undefined; }))
                            .enter().append("g")
                              .attr("class", "node")
                              .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
                         
                          node.append("circle")
                              .attr("r", 2.5);
                         
                          var label = vis.selectAll("text")
                              .data(nodes.filter(function(d) { return d.x !== undefined && !d.children; }))
                            .enter().append("text")
                              .attr("dy", ".31em")
                              .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
                              .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + (r - 170 + 8) + ")rotate(" + (d.x < 180 ? 0 : 180) + ")"; })
                              .text(function(d) { return d.name.replace(/_/g, ' '); });

                        } // end draw function

                        scope.$watch ('data', function (newval, oldval) {
                          if (newval) {
                            draw (newval);
                          }
                        });

                      } // end link
                    };

                  } ])
          .directive ('bsTable', function () {

            return {
              scope : {
                data : "="
              },
              restrict : 'E',
              templateUrl : "/container/view/elements/table.html"

            };

          })
          .directive (
              'visHeatmap',
              [
                  'API',
                  '$routeParams',
                  'alertService',
                  '$location',
                  function (API, $routeParams, alertService, $location) {

                    return {

                      restrict : 'E',
                      // templateUrl : "/container/view/elements/visHeatmap",
                      link : function (scope, elems, attr) {

                        var svgWidth = Math.floor (jq ('#leftPanel').css (
                            'width').slice (0, -2) * .8), svgHeight = Math
                            .floor (jq ('#leftPanel').css ('height').slice (0,
                                -2) * .8);

                        var heatmapMarginLeft = Math.floor (svgWidth * .05), heatmapMarginRight = Math
                            .floor (svgWidth * .1), heatmapMarginTop = Math
                            .floor (svgHeight * .05), heatmapMarginBottom = Math
                            .floor (svgHeight * .1);

                        var heatmapCellsWidth = svgWidth - heatmapMarginLeft
                            - heatmapMarginRight;

                        var heatmapCellsHeight = svgHeight - heatmapMarginTop
                            - heatmapMarginBottom;

                        var window = d3.select (elems[0]);

                        // Color Scales
                        var leftshifter = d3.scale.linear ().rangeRound (
                            [ 255, 0 ]);
                        var rightshifter = d3.scale.linear ().rangeRound (
                            [ 0, 255 ]);

                        // X Scales
                        var XLabel2Index = d3.scale.ordinal ();
                        var XIndex2Label = d3.scale.ordinal ();
                        var XIndex2Pixel = d3.scale.linear ();

                        // YScales
                        var YLabel2Index = d3.scale.ordinal ();
                        var YIndex2Label = d3.scale.ordinal ();
                        var YIndex2Pixel = d3.scale.linear ();

                        // Axis Scales
                        var xAxisd3 = d3.svg.axis ();
                        var yAxisd3 = d3.svg.axis ();

                        var svg = window.append ("svg").attr ("class", "chart")
                        // .attr("pointer-events", "all")
                        .attr ("width", svgWidth).attr ("height", svgHeight);

                        var vis = svg.append ("g");

                        var rects = vis.append ("g").attr ("class", "cells")
                            .selectAll ("rect");

                        var xlabels = vis.append ("g")
                            .attr ("class", "xlabels");

                        var ylabels = vis.append ("g")
                            .attr ("class", "ylabels");

                        function drawLabels (xAxis, yAxis) {

                          xAxis.attr (
                              "transform",
                              "translate(0,"
                                  + (heatmapMarginTop + heatmapCellsHeight)
                                  + ")").call (xAxisd3).selectAll ("text")
                              .style ("text-anchor", "end").attr ("dy",
                                  function (d, i) {
                                    return 0;
                                    // return ((XIndex2Pixel(1) -
                                    // XIndex2Pixel(0) ) / 2) + "px"
                                  }).attr ("dx", "-20px").attr ("transform",
                                  function (d) {
                                    return "rotate(-90)"
                                  });

                          yAxis.attr (
                              "transform",
                              "translate("
                                  + (heatmapMarginLeft + heatmapCellsWidth)
                                  + ")").call (yAxisd3).selectAll ("text")
                              .style ("text-anchor", "start").attr (
                                  "dy",
                                  ((YIndex2Pixel (1) - YIndex2Pixel (0)) / 2)
                                      + "px");

                        }
                        ;

                        function drawCells (hc) {

                          hc.attr ({
                            "class" : "cells",
                            "height" : function (d) {

                              return YIndex2Pixel (1) - YIndex2Pixel (0);
                            },
                            "width" : function (d) {
                              return XIndex2Pixel (1) - XIndex2Pixel (0);
                            },
                            "x" : function (d, i) {
                              return XIndex2Pixel (XLabel2Index (d.column));
                            },
                            "y" : function (d, i) {
                              return YIndex2Pixel (YLabel2Index (d.row));
                            },
                            "fill" : function (d) {
                              return cellColor (d.value);
                            },
                            "value" : function (d) {
                              return d.value;
                            },
                            "index" : function (d, i) {
                              return i;
                            },
                            "row" : function (d, i) {
                              return d.row;
                            },
                            "column" : function (d, i) {
                              return d.column;
                            }
                          });

                        }
                        ;

                        function scaleUpdates (cols, rows, min, max, avg) {

                          leftshifter.domain ([ min, avg ]); // Color Update

                          rightshifter.domain ([ avg, max ]) // Color Update

                          XLabel2Index.domain (cols).range (
                              cols.map (function (d, i) {
                                return i
                              }));

                          YLabel2Index.domain (rows).range (
                              rows.map (function (d, i) {
                                return i
                              }));

                          XIndex2Label.domain (cols.map (function (d, i) {
                            return i
                          })).range (cols.map (function (d, i) {
                            return d
                          }));

                          YIndex2Label.domain (rows.map (function (d, i) {
                            return i
                          })).range (rows.map (function (d, i) {
                            return d
                          }));

                          XIndex2Pixel.domain ([ 0, cols.length ]).range (
                              [ heatmapMarginLeft,
                                  heatmapMarginLeft + heatmapCellsWidth ]);

                          YIndex2Pixel.domain ([ 0, rows.length ]).range (
                              [ heatmapMarginTop,
                                  heatmapMarginTop + heatmapCellsHeight ]);

                          xAxisd3.scale (XIndex2Pixel).orient ("bottom").ticks (
                              cols.length).tickFormat (function (d) {
                            if (d % 1 == 0 && d >= 0 && d < cols.length) {
                              return XIndex2Label (d);

                            }
                          });

                          yAxisd3.scale (YIndex2Pixel).orient ("right").ticks (
                              rows.length).tickFormat (function (d) {
                            if (d % 1 == 0 && d >= 0 && d < rows.length) {
                              return YIndex2Label (d);
                            }
                          });
                        }
                        ;

                        function init (data) {

                          scaleUpdates (data.column.keys, data.row.keys,
                              data.min, data.max, data.avg);

                          drawCells (rects.data (data.values).enter ().append (
                              "rect"));

                          drawLabels (xlabels, ylabels);

                        }
                        ;

                        // Initial Build
                        if (!$routeParams.datasetName) {
                          alertService.error ()
                        } else {
                          API.dataset.get ($routeParams.datasetName).then (
                              init, function () {
                                // Redirect to home if errored out
                                $location.path ('/');
                              });
                        }

                        function cellColor (val, type) {

                          var color = {
                            red : 0,
                            blue : 0,
                            green : 0
                          }

                          if (type) {

                            // coloring options

                          } else {
                            // default blue-yellow
                            if (val <= 0) {
                              color.blue = leftshifter (val);

                            } else {
                              color.red = rightshifter (val);
                              color.green = rightshifter (val);
                            }
                            ;
                          }
                          ;

                          return "rgb(" + color.red + "," + color.green + ","
                              + color.blue + ")";

                        }

                      } // End Link Function

                    }; // End return obj
                  } ]);

    });