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
          .directive ('analysisPanel', [ function () {
            return {
              restrict : 'A',
              templateUrl : '/container/view/elements/analysisPanel',
              link : function (scope) {

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
          .directive ('modalHierarchical', ['API', '$routeParams', function (API, $routeParams) {

            return {
              restrict : 'C',
              templateUrl : "/container/view/elements/hierarchicalbody",
              link : function (scope, elems, attrs) {
            	  
                scope.availableMetrics = [ {
                  name : 'Euclidean'
                }, {
                  name : 'Manhattan'
                } ];
                
                scope.dimensions = [{name: 'Rows', value: 'row'}, {name: 'Columns', value: 'column'}]
                
                scope.clusterInit = function() {
                	var q = {
                			name: scope.clusterName,
                			dataset: $routeParams.datasetName,
                			dimension: scope.selectedDimension,
                			metric: scope.selectedMetric,
                			algorithm: null
                			
                	}
                	
                	API.analysis.hcl.create(q);
                	
                }
                
                

              }

            };

          }])
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
          .directive ('uploadsTable', ['API', '$location', function(API, $location){
            return {
              restrict : 'A',
              scope: {
            	  uploads: '='
              },
              templateUrl : '/container/view/elements/uploadsTable',
              link : function (scope, elems, attrs) {
            	  
            	  scope.datasets = []
                
            	scope.$watch('uploads', function(newValues, oldValues){
            		
            		
            		if (newValues != undefined){
            			console.log(newValues)
            			scope.datasets = newValues;
            			
            		};
            		
            		
            	});
            	
                
              }
            }
          }])
          .directive ('uploadDrag', function () {

            return {
              restrict : 'C',
              templateUrl : '/container/view/elements/uploadDragAndDrop',
              link : function (scope, elems, attrs) {
                
                var myDropzone = new Dropzone("#uploader", {
                  
                  url: "/dataset",
                  method : "post",
                  paramName: "dataset",
                  clickable: true,
                  uploadMultiple: true,
                  previewsContainer: null,
                  addRemoveLinks: true,
                  
                  dictRemoveFile: "Remove",
                  dictCancelUpload: "Cancel",
                  dictCancelUploadConfirmation: "Are you sure?",
                  dictResponseError: "Upload Failed"
                    
                })
                .on("error", function(file){
                  alert("Errored")
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
                        url : '@',
                        dimension : '@',
                        diameter : '@'

                      },
                      templateUrl : '/container/view/elements/d3RadialTree',
                      link : function (scope, elems, attr) {

                    	var cluster = undefined;
                    	
                    	//start function
                    	
                    	var cluster = d3.layout.cluster ().size (
                                [ 360, 1 ]).sort (null).value (
                                function (d) {
                                  return d.length;
                                }).children (function (d) {
                              return d.branchset;
                            }).separation (function (a, b) {
                              return 1;
                            });
                    	
                    	var r = scope.diameter / 2;
                    	
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
                            return ("M" + s[0] + "," + s[1] + "A" + r
                                + "," + r + " 0 0," + sweep + " "
                                + m[0] + "," + m[1] + "L" + t[0] + "," + t[1]);
                         }
                        
                        var wrap = d3.select (elems[0])
                        .append ("svg").attr ("width", r * 2)
                        .attr ("height", r * 2).style (
                            "-webkit-backface-visibility",
                            "hidden");
                        
                        // Catch mouse events in Safari.
                        wrap.append ("rect").attr ("width", r * 2)
                            .attr ("height", r * 2).attr ("fill",
                                "none");
                        
                        wrap.on ("mousedown", function () {
                            wrap.style ("cursor", "move");
                            start = mouse (d3.event);
                            d3.event.preventDefault ();
                          });
                        
                        var vis = wrap.append ("g").attr (
                                "transform",
                                "translate(" + r + "," + r + ")");
                        
                        var start = null, rotate = 0, div = elems[0].childNodes[1];
                        
                        function mouse (e) {
                            return [ e.pageX - div.offsetLeft - r,
                                e.pageY - div.offsetTop - r ];
                          }
                        
                        //radial tree mouse movement
                        d3.select (wrap).on ("mouseup", function () {
                        	
                              if (start) {
                                wrap.style ("cursor", "auto");
                                var m = mouse (d3.event);
                                var delta = Math.atan2 (cross (
                                    start, m), dot (start, m))
                                    * 180 / Math.PI;
                                rotate += delta;
                                if (rotate > 360)
                                  rotate %= 360;
                                else if (rotate < 0)
                                  rotate = (360 + rotate) % 360;
                                start = null;
                                wrap.style ("-webkit-transform",
                                    null);
                                vis
                                    .attr (
                                        "transform",
                                        "translate(" + r + ","
                                            + r + ")rotate("
                                            + rotate + ")")
                                    .selectAll ("text")
                                    .attr (
                                        "text-anchor",
                                        function (d) {
                                          return (d.x + rotate) % 360 < 180 ? "start"
                                              : "end";
                                        })
                                    .attr (
                                        "transform",
                                        function (d) {
                                          return "rotate("
                                              + (d.x - 90)
                                              + ")translate("
                                              + (r - 170 + 8)
                                              + ")rotate("
                                              + ((d.x + rotate) % 360 < 180 ? 0
                                                  : 180) + ")";
                                        });
                              }
                            }).on ("mousemove", function () {
                              if (start) {
                                var m = mouse (d3.event);
                                var delta = Math.atan2 (cross (
                                    start, m), dot (start, m))
                                    * 180 / Math.PI;
                                wrap.style ("-webkit-transform",
                                    "rotateZ(" + delta + "deg)");
                              }
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
                        
                        function draw (data) {

                                  var x = newick.parse (data);
                                  var nodes = cluster.nodes (x);
                                  phylo (nodes[0], 0);

                                  var link = vis.selectAll ("path.link")
                                      .data (cluster.links (nodes)).enter ()
                                      .append ("path")
                                      .attr ("class", "link")
                                      .attr ("d", step);

                                  var node = vis.selectAll ("g.node")
                                    .data ( nodes.filter (function (n) {
                                        return n.x !== undefined;
                                      }))
                                    .enter ()
                                    .append ("g")
                                    .attr ("class", "node")
                                    
                                    .attr ("transform",function (d) {
                                        return "rotate(" + (d.x - 90)
                                            + ")translate(" + d.y + ")";
                                    });

                                  node.append ("circle").attr ("r", 2.5);

                                  var label = vis.selectAll ("text").data (
                                      nodes.filter (function (d) {
                                            return d.x !== undefined
                                                && !d.children;
                                          })
                                      ).enter ()
                                      .append("text")
                                      .attr ("dy", ".31em")
                                      .attr ("text-anchor",function (d) {
                                        return d.x < 180 ? "start" : "end";
                                      })
                                      .attr ("transform", function (d) {
                                        return "rotate(" + (d.x - 90)
                                            + ")translate(" + (r - 170 + 8)
                                            + ")rotate("
                                            + (d.x < 180 ? 0 : 180) + ")";
                                      })
                                      .text (function (d) {
                                        return d.name.replace (/_/g, ' ');
                                      });

                        } // end draw function
                        
                        if (scope.dimension == 'row') {
                    		API.analysis.hcl.row.get ($routeParams.datasetName).then(draw);
                    	} else if (scope.dimension == 'column') {
                    		API.analysis.hcl.column.get ($routeParams.datasetName).then(draw);
                    	};

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
                  function (API, $routeParams) {

                    return {

                      restrict : 'E',
                      // templateUrl : "/container/view/elements/visHeatmap",
                      link : function (scope, elems, attr) {

                        scope.width = jq ('#leftPanel').css ('width').slice (0,
                            -2) * .9;
                        scope.height = 700;
                        scope.marginleft = 20;
                        scope.marginright = 20;
                        scope.margintop = 20;
                        scope.marginbottom = 20;

                        var cellwidth = 9;

                        var margin = {
                          left : scope.marginleft,
                          right : scope.marginright,
                          top : scope.margintop,
                          bottom : scope.marginbottom
                        };

                        var width = scope.width - margin.left - margin.right;

                        var height = scope.height - margin.top - margin.bottom;

                        var window = d3.select (elems[0]);
                        
                        var leftshifter = d3.scale.linear ().rangeRound ([ 255, 0 ])

                        var rightshifter = d3.scale.linear ().rangeRound ([ 0, 255 ])

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

                        var svg = window.append ("svg").attr ("class", "chart")
                        // .attr("pointer-events", "all")
                        .attr ("width", width + margin.left + margin.right)
                            .attr ("height",
                                height + margin.top + margin.bottom);

                        var vis = svg.append ("g").attr ("class", "uncovered")

                        window
                            .append ("button")
                            .attr ("class", "btn btn-primary")
                            .text ("Cluster")
                            .on (
                                'click',
                                function () {

                                  jq
                                      .get (
                                          '/heatmap/dataset/mock/shuffleRows?format=json',
                                          function (rowreordering) {
                                            jq
                                                .get (
                                                    '/heatmap/dataset/mock/shuffleColumns?format=json',
                                                    function (colreordering) {
                                                      
                                                      cluster (heatmapcells,
                                                          colreordering)
                                                    })

                                          });

                                });

                        var rects = vis.append ("g").selectAll ("rect");

                        function cluster (hc, cols) {

                          var cellXPosition = d3.scale.ordinal ().domain (cols)
                              .rangeRoundBands ([ 0, cellwidth * cols.length ]);

                          // var cellYPosition = d3.scale.ordinal ().domain
                          // (rows)
                          // .rangeRoundBands ([ 0, cellwidth * rows.length
							// ]);

                          svg.selectAll (".cells").transition ()
                              .duration (5000).attr ("x", function (d, i) {
                                return cellXPosition (d.column)
                              })

                        }
                        ;

                        function draw (hc, cols, rows) {

                          var cellXPosition = d3.scale.ordinal ().domain (cols)
                              .rangeRoundBands ([ 0, cellwidth * cols.length ]);

                          var cellYPosition = d3.scale.ordinal ().domain (rows)
                              .rangeRoundBands ([ 0, cellwidth * rows.length ]);

                          hc.attr ({
                            "class" : "cells",
                            "height" : function (d) {
                              return cellwidth - .5;
                            },
                            "width" : function (d) {
                              return cellwidth - .5;
                            },
                            "x" : function (d, i) {
                              return cellXPosition (d.column);
                            },
                            "y" : function (d, i) {
                              return cellYPosition (d.row);
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
                              return d.rowOrder;
                            },
                            "column" : function (d, i) {
                              return d.columnOrder;
                            },
                            "rowKey" : function (d, i) {
                              return d.rowKey
                            },
                            "columnKey" : function (d, i) {
                              return d.columnKey
                            },
                          })

                        }
                        ;

                        var heatmapcells = undefined;
                        
                        var prep = function (data) {
                        	
                        	leftshifter.domain ([ data.min, data.avg ]);
                                    
                            rightshifter.domain ([ data.avg, data.max])

                            heatmapcells = rects.data (data.values).enter ()
                                .append ("rect")

                            draw (heatmapcells, data.column, data.row);

                          }
                        
                        var mockprep = function (data) {
                        	
                        	leftshifter.domain ([ -3, 0 ]);
                                    
                            rightshifter.domain ([ 0, 3])

                            heatmapcells = rects.data (data.values).enter ()
                                .append ("rect")

                            draw (heatmapcells, data.column, data.row);

                          }
                        
                        if (!$routeParams.datasetName){
                        	API.heatmap.get ("dataset/mock/data").then (mockprep);
                        } else {
                        	API.dataset.get ( $routeParams.datasetName ).then (prep);
                        }

                        

                      }

                    };
                  } ]);

    });