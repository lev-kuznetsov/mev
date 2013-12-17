define (
    [ 'angular', 'jquery', 'd3', 'dropzone', 'newick', 'services' ],
    function (angular, jq, d3, Dropzone, newick) {

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
              'heatmapPanels',[ '$routeParams', 'API', 'alertService',
              function ($routeParams, API, alertService) {
                return {
                  restrict : 'A',
                  templateUrl : '/container/view/elements/heatmapPanels',
                  link : function (scope, elems, attrs) {

                    scope.heatmapData = undefined;
                    scope.heatmapLeftTree = undefined;
                    scope.heatmapTopTree = undefined;
                    scope.heatmapLeftTreeName = undefined;
                    scope.heatmapTopTreeName = undefined;
                    
                    API.dataset.get ($routeParams.datasetName).then (
                        function(data){ scope.heatmapData = data;}, function () {
                          // Redirect to home if errored out
                          $location.path ('/');
                        });
                    
                   scope.updateHeatmapData = function(prevAnalysis, textForm){
                      
                      API.analysis.hcl.update({
                        dataset:$routeParams.datasetName,
                        name:prevAnalysis
                        }).then(function(){
                          
                          API.dataset.get ($routeParams.datasetName).then (
                              function(data){
                              
                                if (data.column.root) {
                                  
                                  //apply column cluster to dendogram
                                  
                                  scope.heatmapTopTree = data.column.root;
                                  
                                };
                                
                                if (data.row.root) {
                                  
                                  
                                  scope.heatmapLeftTree = data.row.root;

                                };
                                
                                //Apply new ordering and dataset to held heatmap
                                scope.heatmapData = data;
                              
                              }, function () {
                                // Redirect to home if errored out
                                $location.path ('/');
                              });
                          
                        }, function(){
                          
                          var message = "Could not update heatmap. If "
                            + "problem persists, please contact us."
                            
                            var header = "Heatmap Clustering Update Problem (Error Code: " + s + ")"
                            alertService.error(message, header);
                          
                        })
                      
                    };
                    
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
              }])
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

                        

                        scope.buildPrevioiusClusters ();

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

                    scope.availableMetrics = [ 'euclidean', 'manhattan', 'pearson' ];

                    scope.availableAlgorithms = [ 'average', 'complete', 'single' ];

                    scope.dimensions = [ {
                      name : 'Rows',
                      value : 'row'
                    }, {
                      name : 'Columns',
                      value : 'column'
                    } ];

                    scope.clusterInit = function () {
                      var q = {
                        name : scope.clusterName,
                        dataset : $routeParams.datasetName,
                        dimension : scope.selectedDimension,
                        metric : scope.selectedMetric,
                        algorithm : scope.selectedAlgorithm,
                        callback : function () {
                        	
                        	scope.buildPrevioiusClusters()

                        }

                      };

                      API.analysis.hcl.create (q);
                      
                      

                    };

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

                    }).on('complete', function(file){
                    	$("#importTabs a[href='#current']").tab('show');
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
                        
                        var r =500/ 2;
                        var resizeCoeff = .75; // Adjusts r coefficient for end size

                        var cluster = d3.layout.cluster ().size ([ 360, r*resizeCoeff ]) 
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
                            offset += n.length  * 65 ;
                          n.y = offset *.0025; //TODO: remove coefficient on algorithm correction
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
                              .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + (r * resizeCoeff) + ")rotate(" + (d.x < 180 ? 0 : 180) + ")"; })
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
                            'width').slice (0, -2) * .9), svgHeight = Math
                            .floor (jq ('#leftPanel').css ('height').slice (0,
                                -2) * .9);

                        var heatmapMarginLeft = Math.floor (svgWidth * .15), heatmapMarginRight = Math
                            .floor (svgWidth * .1), heatmapMarginTop = Math
                            .floor (svgHeight * .15), heatmapMarginBottom = Math
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
                        
                        
                        //Selections Scales
                        var colSelectionsX = d3.scale.ordinal();
                        var colSelectionsY = d3.scale.ordinal();
                        var rowSelectionsX = d3.scale.ordinal();
                        var rowSelectionsY = d3.scale.ordinal();

                        // Axis Scales
                        var xAxisd3 = d3.svg.axis ();
                        var yAxisd3 = d3.svg.axis ();

                        var svg = window.append ("svg").attr ("class", "chart")
                        // .attr("pointer-events", "all")
                        .attr ("width", svgWidth).attr ("height", svgHeight);

                        var vis = svg.append ("g");

                        var rects = vis.append ("g").attr ("class", "cells")
                            .selectAll ("rect");
                        
                        var selections = vis.append("g").attr ("class", "selections")
                            .selectAll ("rect");
                        
                        var columnSelections = selections.append("g")
                            .attr ("class", "colSelections");
                        
                        var rowSelections = selections.append("g")
                            .attr ("class", "rowSelections");

                        var xlabels = vis.append ("g")
                            .attr ("class", "xlabels");

                        var ylabels = vis.append ("g")
                            .attr ("class", "ylabels");
                        
                        function drawSelections(columnData, rowData) {
                          
                          //definitions
                          var columnCells = [], rowCells = [];
                          
                          //Data building
                          columnData.selections.forEach(function(selection){
                            selection.keys.forEach(function(key){
                              columnCells.push({
                                name: selection.name, 
                                selection: key, 
                                color: selection.color}); 
                            });
                          });
                          
                          rowData.selections.forEach(function(selection){
                            selection.keys.forEach(function(key){
                              rowCells.push({
                                name: selection.name, 
                                selection: key, 
                                color: selection.color}); 
                            });
                          });
                          
                          //Clearing canvas
                          columnSelections.selectAll("*").remove();
                          rowSelections.selectAll("*").remove();
                          
                          //Data Entering
                          
                          columnSelections.selectAll("*").remove();
                          rowSelections.selectAll("*").remove();
                          
                          //Canvas adding
                       
                          
                        };

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
                            "class" : "cell",
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
                        
                        function redrawCells () {

                          svg.selectAll('.cell')
                          .transition().delay(200).duration(2000)
                          .attr ({
                            "x" : function (d, i) {
                              return XIndex2Pixel (XLabel2Index (d.column));
                            },
                            "y" : function (d, i) {
                              return YIndex2Pixel (YLabel2Index (d.row));
                            }
                          });

                        }
                        ;

                        function scaleUpdates (cols, rows, min, max, avg) {

                          leftshifter.domain ([ min, avg ]); // Color Update

                          rightshifter.domain ([ avg, max ]) // Color Update
                          
                          //Selection Scales update
                          colSelectionsX.domain(cols.selections.map(function(d, i){return d.name}))
                            .rangeBands([ heatmapMarginLeft + heatmapCellsWidth,
                                          heatmapMarginLeft + heatmapCellsWidth + heatmapMarginRight  ]);
                          
                          colSelectionsY.domain(rows.keys)
                            .rangeBands([ heatmapMarginTop,
                                          heatmapMarginTop + heatmapCellsHeight ]);
                          
                          rowSelectionsX.domain(cols.keys)
                            .rangeBands([ heatmapMarginLeft,
                                          heatmapMarginLeft + heatmapCellsWidth ]);
                          
                          rowSelectionsY.domain(rows.selections.map(function(d, i){return d.name}))
                            .rangeBands([ heatmapMarginTop + heatmapCellsHeight,
                                          heatmapMarginTop + heatmapCellsHeight + heatmapMarginBottom ]);
                          

                          XLabel2Index.domain (cols.keys).range (
                              cols.keys.map (function (d, i) {
                                return i
                              }));

                          YLabel2Index.domain (rows.keys).range (
                              rows.keys.map (function (d, i) {
                                return i
                              }));

                          XIndex2Label.domain (cols.keys.map (function (d, i) {
                            return i
                          })).range (cols.keys.map (function (d, i) {
                            return d
                          }));

                          YIndex2Label.domain (rows.keys.map (function (d, i) {
                            return i
                          })).range (rows.keys.map (function (d, i) {
                            return d
                          }));

                          XIndex2Pixel.domain ([ 0, cols.keys.length ]).range (
                              [ heatmapMarginLeft,
                                  heatmapMarginLeft + heatmapCellsWidth ]);

                          YIndex2Pixel.domain ([ 0, rows.keys.length ]).range (
                              [ heatmapMarginTop,
                                  heatmapMarginTop + heatmapCellsHeight ]);

                          xAxisd3.scale (XIndex2Pixel).orient ("bottom").ticks (
                              cols.keys.length).tickFormat (function (d) {
                            if (d % 1 == 0 && d >= 0 && d < cols.keys.length) {
                              return XIndex2Label (d);

                            }
                          });

                          yAxisd3.scale (YIndex2Pixel).orient ("right").ticks (
                              rows.keys.length).tickFormat (function (d) {
                            if (d % 1 == 0 && d >= 0 && d < rows.keys.length) {
                              return YIndex2Label (d);
                            }
                          });
                        }
                        ;

                        function drawHeatmap (data) {
                          
                          heatmapcells = rects.data (data.values).enter ().append (
                          "rect");

                          scaleUpdates (data.column, data.row,
                              data.min, data.max, data.avg);

                          drawCells (heatmapcells);

                          drawLabels (xlabels, ylabels);

                        };
                        
                        function updateDrawHeatmap (data) {

                          scaleUpdates (data.column.keys, data.row.keys,
                              data.min, data.max, data.avg);

                          redrawCells (heatmapcells);

                          drawLabels (xlabels, ylabels);

                        };
                        
                        var heatmapcells = undefined;

                        scope.$watch('heatmapData', function(newval, oldval){

                            if (newval && !oldval) {
                              drawHeatmap(newval);
                            } else if (newval && oldval) {
                              updateDrawHeatmap(newval);
                            }
                          
                        });
                        
                        //Dendogram Stuff

                        var Cluster = d3.layout.cluster()
                          .sort(null)
                          .separation(function(a, b){ 
                            return a.parent == b.parent ? 1:1
                          })
                          .value(function(d){return d.distance;})
                          .children(function(d){return d.children;});
                        
                        var dendogramLeft = {
                            height: heatmapCellsHeight,
                            width: heatmapMarginLeft
                        };
                        
                        var dendogramTop = {
                            height: heatmapMarginTop,
                            width: heatmapCellsWidth
                        };
                        
                        var dendogramLeftWindow = svg.append("g")
                            .attr('class', 'leftDendogram');
                        
                        var dendogramTopWindow = svg.append("g")
                            .attr('class', 'topDendogram');
                        
                        
                        //Left Dendogram Builder
                        scope.$watch('heatmapTopTree', function(newval, oldval){
                        
                          if (newval) {
                          
                            var tree = newval;
                            
                            drawTree(dendogramLeftWindow, Cluster, tree, 'horizontal' )
                            
                            
                          }
                          
                        });
                        
                        scope.$watch('heatmapLeftTree', function(newval, oldval){
                          
                          if (newval) {

                            var tree = newval;
                            
                            drawTree(dendogramTopWindow, Cluster, tree, 'vertical' )
                            
                            
                          }
                          
                        });
                        
                        function drawTree(canvas, cluster, tree, type) {
                          
                          canvas.selectAll('*').remove();
                          var nodes = cluster.nodes(tree);
                          var links = cluster.links(nodes);

                          
                          
                          canvas.selectAll("path")
                              .data(links)
                            .enter().append("path")
                              .attr("d", function(d) {
                              return (type == 'horizontal') ? horizontalPath(d) : verticalPath(d)
                              })
                              .attr("stroke", function(){
                                return (type == 'horizontal') ? "blue" : "red"
                              })
                              .attr("fill", "none"); 

                          canvas.selectAll("circle").data(nodes).enter().append("circle")
                             .attr("r", 2.5)
                             .attr("cx", function(d){
     
                              return (type == 'vertical') ? (d.y * dendogramLeft.width) : (d.x * dendogramTop.width) + dendogramLeft.width;
                             })
                             .attr("cy", function(d){
                              return (type == 'vertical') ? (d.x * dendogramLeft.height) + dendogramTop.height : (d.y * dendogramTop.height);
                             })
                             .attr("fill", function(d){
                               return (type == 'horizontal') ? "blue" : "red"
                             })
                             .on("click", function(d){
                               noder(d); //TODO add selections function to this
                             }); 

                        };
                        
                        function noder(d){
                          
                          var a = [];
                          
                          if (!d.children) {
                            a.push(d.name); 
                          } else {
                            d.children.forEach(function(child){
                              noder(child).forEach(function(name){a.push(name)});
                            });
                          };
                          
                          return a;
                        };
                        
                        function horizontalPath(d) {
                          //Path function builder for TOP heatmap tree path attribute
                          
                          return "M" + ((d.target.x * dendogramTop.width)+dendogramLeft.width )  + "," + (d.target.y * dendogramTop.height ) +
                          "V" + (d.source.y * dendogramTop.height ) +
                          "H" + ((d.source.x * dendogramTop.width)+dendogramLeft.width );
                          
                          

                        };
                        function verticalPath(d) {
                          //Path function builder for LEFT heatmap tree path attribute

                          return "M" + (d.source.y * dendogramLeft.width )  + "," + ((d.source.x * dendogramLeft.height)+dendogramTop.height ) +
                          "V" + ((d.target.x * dendogramLeft.height)+dendogramTop.height ) +
                          "H" + (d.target.y * dendogramLeft.width )

                        };



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