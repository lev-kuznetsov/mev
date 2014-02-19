define (
    [ 'angular', 'jquery', 'd3', 'dropzone', 'services' ],
    function (angular, jq, d3, Dropzone) {


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
          .directive ('heatmapNavigation',[ function () {
                return {
                  restrict : 'A',
                  templateUrl : '/container/view/elements/heatmapNavigation'
                };
              } ])
          .directive (
              'heatmapPanels',[ '$routeParams', 'API', 'alertService', '$location',
              function ($routeParams, API, alertService, $location) {
                return {
                  restrict : 'A',
                  templateUrl : '/container/view/elements/heatmapPanels',
                  link : function (scope, elems, attrs) {

                    scope.heatmapData = undefined;
                    scope.heatmapLeftTree = undefined;
                    scope.heatmapTopTree = undefined;
                    scope.heatmapLeftTreeName = undefined;
                    scope.heatmapTopTreeName = undefined;
                    
                    document.title = "MeV: " + $routeParams.datasetName;

                    API.dataset.get ($routeParams.datasetName).then (
                        function(data){ 
                          scope.heatmapData = data;
                          
                        }, function(data){
                        	//return home if error
                        	$location.path('/');
                        });
                    
                    
                    var rightPanel = jq('#rightPanel'),
                    leftPanel = jq('#leftPanel'),
                    centerTab = jq('div#tab'),
                    pageWidth = jq('body').width() - 50,
                    showSidePanel = true;
                    
                    rightPanel.css('height', "80%");
                    leftPanel.css('height', "80%");
                    
                    var isDragging = false;
                    
                    centerTab.mousedown(function(mouse){
                    	isDragging = true;
                    	mouse.preventDefault();
                    });
                    
                    jq(document).mouseup(function(){
                    	isDragging = false;
                    }).mousemove(function(mouse){
                    	if(isDragging && mouse.pageX < pageWidth*(9/10) && mouse.pageX > 0 ){
                    		showSidePanel = true;
                    		leftPanel.css("width", mouse.pageX);
                    		rightPanel.css("width", pageWidth - mouse.pageX);
                    		leftPanel.children().show();
                    	}
                    	
                    	if(isDragging && mouse.pageX < pageWidth*(1/7) && mouse.pageX > 0 ){
                    		leftPanel.children().hide();
                    		jq('div#tab').click()
                    	}
                    	
                    });
                    
                    jq('div#tab').click(function(){
                    	
                      if (showSidePanel) {
                        leftPanel.css("width", 0);
                        leftPanel.children().hide();
                        rightPanel.css("width", pageWidth - 30);
                        showSidePanel = false;
                      } else {
                        console.log("clicked")
                        leftPanel.css("width", pageWidth*(1/3));
                        leftPanel.children().show();
                        rightPanel.css("width", pageWidth*(2/3) - 30);
                        showSidePanel = true;
                      }
                    		
                    })
                    
                    

                    scope.showLimmaTables = true;
                    
                    

                  }
                };
              }])
            .directive ('sideNavigationBar', [ function () {
            return {
              restrict : 'E',
              templateUrl : '/container/view/elements/sideNavigationBar',
              link: function(scope) {
            	  
            	  
            	  	scope.clusterAnalysisClickOpen = function(id) {
            	  		
            	  		jq('a#clustersTabLink').trigger("click");
            	  		
            	  		jq(id.href).collapse("show");
            	  		
            	  		jq('div.fixed-height').animate({
            	  			scrollTop: jq(id.dataParent).offset().top
            	  		}, 200);
            	  	}
            	  	
            	  	scope.limmaAnalysisClickOpen = function(id) {
            	  		
            	  		jq('a#limmaTabLink').trigger("click");
            	  		
            	  		jq(id.href).collapse("show");
            	  		
            	  		jq('div.fixed-height').animate({
            	  			scrollTop: jq(id.dataParent).offset().top
            	  		}, 200);
            	  	}
            }
            };
          } ])
          .directive ('limmaAccordionList', [ function () {
            return {
              restrict : 'E',
              templateUrl : '/container/view/elements/limmaAccordion'
              
            };
          } ])
          .directive ('clusterAccordionList', [ function () {
            return {
              restrict : 'E',
              templateUrl : '/container/view/elements/clusterAccordion'
              
            };
          } ])
          .directive ('analysisMenuBar', [ function () {
            return {
              restrict : 'E',
              templateUrl : '/container/view/elements/analysisMenuBar'
              
            };
          } ])
          .directive ('expressionPanel', ['$routeParams', function ($routeParams) {
            return {
              restrict : 'AC',
              templateUrl : '/container/view/elements/expressionPanel',
              link : function (scope) {
                
                scope.buildPreviousAnalysisList ();
                
                scope.datasetName = $routeParams.datasetName;
                
                

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
                        	
                        	scope.buildPreviousAnalysisList()

                        }

                      };

                      API.analysis.hcl.create (q);
                      
                      resetSelections()
                      
                    };
                    
                    function resetSelections() {
                      scope.clusterName = "";
                      scope.selectedDimension = "";
                      scope.selectedMetric = "";
                      scope.selectedAlgorithm = "";
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
          .directive ('modalLimma', [ "API", "$routeParams", function (API, $routeP) {

            return {
              restrict : 'C',
              templateUrl : "/container/view/elements/limmaBody",
              link : function (scope, elems, attrs){
                
                scope.dimensions = [
                  {name: "Row", value: "row"},
                  {name:"Column", value:"column"}];
                
                
                scope.$watch('analysisDimension', function(newval, oldval){
                  if (newval) {
                    API.dataset.selections.get($routeP.datasetName, newval.value).then(function(d){
                      scope.selections = d;
                    })
                  }
                  
                });
                
                scope.limmaInit = function(){
                	
                  params = {
                    dataset: $routeP.datasetName, 
                    name: scope.analysisName,
                    dimension: scope.analysisDimension.value, 
                    experiment: scope.analysisExperiment, 
                    control: scope.analysisControl,
                    callback: scope.buildPreviousAnalysisList
                		  
                  };
                  
                  API.analysis.limma.create(params);
                  
                  resetSelections();

                };
                
                function resetSelections() {
                  scope.analysisName = "";
                  scope.analysisDimension = "";
                  scope.analysisControl = "";
                  scope.analysisExperiment = "";
                }
                
                

              }

            };

          }])
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
              'uploadDrag', [ "API",
              function (API) {

                return {
                  restrict : 'C',
                  templateUrl : '/container/view/elements/uploadDragAndDrop',
                  link : function (scope, elems, attrs) {
                	  
                	jq('#upload-button').click(function(){
                		jq('#upload-input').click();
                	});
                	
                	jq('#upload-input').on("change", function() {
                		
                		var input = document.getElementById('upload-input'),
                		files = new Array();
                		
                		for (var i = 0; i < input.files.length; i++) {
                			files.push(input.files[i]);
                			
                			if (files.length == input.files.length){
                				files.map(function(file){
                					
                					var formdata = new FormData;
                        			formdata.append('upload', file);
                        			formdata.append('name', file.name);
                        			var xhr = new XMLHttpRequest();
                        			
                        			xhr.upload.addEventListener("progress", function(e){
                        				return;
                        			});
                        			
                        			xhr.onreadystatechange = function() {
                        				if(xhr.readyState == 4 && xhr.status == 200){
                        					
                        					
                        					scope.loadUploads();
                        					
                        					
                        				};
                        			};
                        			
                        			xhr.open("POST", "/dataset", true);
                        			xhr.send(formdata);
                				});
                			};
                		};
                		
                		
                	});
                   

                  }
                };

              }])
          .directive ('datasetSummary', function () {
            return {
              restrict : 'A',
              scope : {
                datasetobj : "&"
              },
              templateUrl : '/container/view/elements/datasetSummary',
            };

          })
          .directive ('d3RadialTree', ['API','$routeParams', 
             function (API, $routeParams) {

                    return {
                      restrict : 'A',
                      scope : {
                        data : '=',
                        diameter : '@'

                      },
                      templateUrl : '/container/view/elements/d3RadialTree',
                      link : function (scope, elems, attr) {
                    	
                    	var padding = 20;
                    	var dendogram = {
                          height: 200 + padding,
                          width: pageWidth = jq('body').width() * (2/5) // Nicely define width
                        };
                    	
                    	var svg = d3.select(elems[0]).append("svg")
                    	  .attr({width: dendogram.width, height: (dendogram.height + (padding))});
                    	
                    	var Cluster = d3.layout.cluster()
                          .sort(null)
                          .separation(function(a, b){ 
                            return a.parent == b.parent ? 1:1
                          })
                          .value(function(d){return d.distance;})
                          .children(function(d){return d.children;});
                        
                        var dendogramWindow = svg.append("g")
                            .attr('class', 'smallDendogram');
                        
                        function Path(d) {
                            //Path function builder for TOP heatmap tree path attribute
                            
                            return "M" + ( d.target.x * dendogram.width )  + "," + ( (d.target.y * dendogram.height) + (padding*.5) ) +
                            "V" + ( (d.source.y * dendogram.height) + (padding*.5) ) +
                            "H" + ( d.source.x * dendogram.width );
                            

                          };
                    	  
                        function drawAnalysisTree(canvas, cluster, tree, type) {
                            
                            canvas.selectAll('*').remove();
                            var nodes = cluster.nodes(tree);
                            var links = cluster.links(nodes);
                            
                            canvas.selectAll("path")
                                .data(links)
                              .enter().append("path")
                                .attr("d", function(d) {
                                return Path(d)
                                })
                                .attr("stroke", function(){
                                  return "grey"
                                })
                                .attr("fill", "none"); 

                            canvas.selectAll("circle").data(nodes).enter().append("circle")
                               .attr("r", 2.5)
                               .attr("cx", function(d){
                                 return d.x * dendogram.width;
                               })
                               .attr("cy", function(d){
                                 return (d.y * dendogram.height) + (padding*.5);
                               })
                               .attr("fill", function(d){
                                 return "red"
                               })
                               .on("click", function(d){
                                 //
                               }); 

                          };
                        scope.$watch ('data', function (newval, oldval) {
                          if (newval) {
                            drawAnalysisTree (dendogramWindow, Cluster, newval.root, "horizontal");
                          }
                        });

                      } // end link
                    };

          }])
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
                    	  
                    	jq('div.fixed-height').css('height', jq ('#leftPanel').css ('height').slice (0, -2)* .8 )

                        var svgWidth = Math.floor (jq ('#rightPanel').css (
                            'width').slice (0, -2) * .9), svgHeight = Math //svgheight no longer!
                            .floor (jq ('#rightPanel').css ('height').slice (0, -2) * .9);

                        var heatmapMarginLeft = Math.floor (svgWidth * .15), 
                            heatmapMarginRight = Math.floor (svgWidth * .15), 
                            heatmapMarginTop = 200, 
                            heatmapMarginBottom = Math.floor (svgHeight * .15),
                            heatmapColumnSelectionsGutter = 0,
                            heatmapRowSelectionsGutter = 0;

                        var heatmapCellsWidth = svgWidth - heatmapMarginLeft
                            - heatmapMarginRight;

                        var heatmapCellsHeight = undefined;
                        var heatmapCellHeight = undefined;

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
                        
                        //Tree Scales
                        
                        var verticalTreeX = d3.scale.linear().domain([0, 1]);
                    
                        var verticalTreeY = d3.scale.linear().domain([0, 1]);
                    	
                        var horizontalTreeX = d3.scale.linear().domain([0, 100]);
                    
                        var horizontalTreeY = d3.scale.linear().domain([0, 100]);
                        
                        
                        var svg = window.append ("svg").attr ("class", "chart")
                        // .attr("pointer-events", "all")
                        .attr ("width", svgWidth);
                        

                        var vis = svg.append ("g");

                        var rects = vis.append ("g").attr ("class", "cells")
                            .selectAll ("rect");

                        var selections = vis.append("g").attr ("class", "selections")
                            .selectAll ("rect");
                        
                        var columnSelections = selections.append("g")
                            .attr ("class", "colSelections");
                        
                        var rowSelections = selections.append("g")
                            .attr ("class", "rowSelections");
                        
                        var clip = svg.append("defs").append("svg:clipPath")
                        	.attr("id", "columnLabelClip")
                        	.append("svg:rect")
                        	.attr("id", "clip-rect")
                       		.attr("x", heatmapMarginLeft)
                       		.attr("y", (2*heatmapMarginTop)/3)
                        	.attr("width", heatmapCellsWidth)
                        	.attr("height", (1*heatmapMarginTop)/3 )

                        var xlabels = vis.append ("g")
                            //.attr("clip-path", "url(#columnLabelClip)")
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
                                row: selection.name, 
                                col: key, 
                                color: selection.properties.selectionColor}); 
                            });
                          });
                          
                          rowData.selections.forEach(function(selection){
                            selection.keys.forEach(function(key){
                              
                              rowCells.push({
                                col: selection.name, 
                                row: key, 
                                color: selection.properties.selectionColor}); 
                            });
                          });
                          
                          //Clearing canvas
                          d3.selectAll(".columnSelection").remove();
                          d3.selectAll(".rowSelection").remove();
                          
                          //Canvas adding
                          
                          
                          columnSelections.data(columnCells).enter().append("rect")
                          .attr({"class" : "columnSelection",
                              "height" : function (d) {

                                return colSelectionsY.rangeBand();
                              },
                              "width" : function (d) {
                                return colSelectionsX.rangeBand();
                              },
                              "x" : function (d, i) {
                                return colSelectionsX(d.col);
                              },
                              "y" : function (d, i) {
                                return colSelectionsY(d.row);
                              },
                              "fill" : function (d) {
                                return d.color;
                              }
                          });
                          
                          rowSelections.data(rowCells).enter().append("rect")
                          .attr({"class" : "rowSelection",
                              "height" : function (d) {

                                return rowSelectionsY.rangeBand();
                              },
                              "width" : function (d) {
                                return rowSelectionsX.rangeBand();
                              },
                              "x" : function (d, i) {
                                return rowSelectionsX(d.col);
                              },
                              "y" : function (d, i) {
                                return rowSelectionsY(d.row);
                              },
                              "fill" : function (d) {
                                return d.color;
                              }
                          });
                       
                          
                        };

                        function drawLabels (xAxis, yAxis) {
                          
                          
                          var xband = (XIndex2Pixel(1) - XIndex2Pixel(0))/2;
                          
                          
                          
                          xlabels.selectAll("text").data( XLabel2Index.domain() )
                           .enter().append("text")
                          .attr('dx', -heatmapMarginTop)
                          .attr('y', function(d){ 
                            return XIndex2Pixel( XLabel2Index(d) ) + xband
                           })
                          //.style("text-anchor", "start")
                          .attr ("transform", "rotate(-90)")
                          
                          .text(function(d){return d.substr(0, 8)})
                          .style("font-size", "14px")
                        

                          

                          yAxis.attr (
                              "transform",
                              "translate("
                                  + (heatmapMarginLeft + heatmapCellsWidth + heatmapRowSelectionsGutter)
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
                            },
                          })
                          .append("title").text(function(d){return "Value: " + d.value + "\nRow: " + d.row + "\nColumn: " + d.column});
                          
                          

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
                            },
                            "fill" : function(d, i){
                              return cellColor (d.value)
                            }
                          });
                          
                          

                        }
                        ;

                        function scaleUpdates (cols, rows, min, max, avg) {
                        	
                          heatmapCellHeight = 30;
                          
                          heatmapCellsHeight = heatmapCellHeight*rows.keys.length;
                          dendogramLeft.height = heatmapCellsHeight;
                        	
                          svg.attr("height", heatmapCellsHeight + heatmapMarginTop + heatmapMarginBottom);

                          leftshifter.domain ([ min, avg ]); // Color Update

                          rightshifter.domain ([ avg, max ]) // Color Update

                          //Selection Scales update
                          
                          if (cols.selections.length > 0){
                        	  
                        	heatmapColumnSelectionsGutter = 50;
                        	
                            colSelectionsX.domain(cols.keys)
                              .rangeBands([ heatmapMarginLeft,
                                          heatmapMarginLeft + heatmapCellsWidth  ]);
                          
                            colSelectionsY.domain(cols.selections.map(function(d){return d.name}))
                              .rangeBands([ heatmapMarginTop,
                                          heatmapMarginTop + heatmapColumnSelectionsGutter  ]);
                          };
                          
                          if (rows.selections.length > 0) {
                            
                        	heatmapRowSelectionsGutter = .25 * heatmapMarginRight;
                        	  
                            rowSelectionsX.domain(rows.selections.map(function(d, i){return d.name}))
                              .rangeBands([ heatmapMarginLeft + heatmapCellsWidth,
                                          heatmapMarginLeft + heatmapCellsWidth + heatmapRowSelectionsGutter ]);
                          
                            rowSelectionsY.domain(rows.keys)
                              .rangeBands([ heatmapMarginTop,
                                          heatmapMarginTop + heatmapCellsHeight]);
                          };
                          
                          
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
                              [ heatmapMarginTop + heatmapColumnSelectionsGutter,
                                heatmapMarginTop + heatmapColumnSelectionsGutter + heatmapCellsHeight ]);

                          xAxisd3.scale (XIndex2Pixel).orient ("top").ticks (
                              cols.keys.length).tickFormat (function (d) {
                            if (d % 1 == 0 && d >= 0 && d < cols.keys.length) {
                              return XIndex2Label (d);

                            }
                          });

                          yAxisd3.scale (YIndex2Pixel).orient ("right").ticks (
                              rows.keys.length).tickFormat (function (d) {
                            if (d % 1 == 0 && d >= 0 && d < rows.keys.length) {
                              return YIndex2Label (d).substr(0, 15);
                            }
                          });
                          
                          verticalTreeX.range([4, heatmapMarginLeft]);
                      
                          verticalTreeY.range([heatmapMarginTop + heatmapColumnSelectionsGutter,
                              heatmapCellsHeight + heatmapMarginTop + heatmapColumnSelectionsGutter]);
                              
                          horizontalTreeX.range([heatmapMarginLeft, heatmapCellsWidth + heatmapMarginLeft]);
                      
                          horizontalTreeY.range([4, (2*heatmapMarginTop) / 3]);
                          
                        }
                        ;

                        function drawHeatmap (data) {
                          

                          heatmapcells = rects.data (data.values).enter ().append ("rect");
						              scope.theData=data;
                          scaleUpdates (data.column, data.row,
                              data.min, data.max, data.avg);
                          
                          drawSelections(data.column, data.row)

                          drawCells (heatmapcells);

                          drawLabels (xlabels, ylabels);

                        };
                        
                        function updateDrawHeatmap (data) {

                          scaleUpdates (data.column, data.row,
                              data.min, data.max, data.avg);

                          redrawCells (heatmapcells);

                          drawLabels (xlabels, ylabels);
                          
                          drawSelections(data.column, data.row);

                        };
                        
                        var heatmapcells = undefined;

                        scope.$watch('heatmapData', function(newval, oldval){

                            if (newval && !oldval) {
                              
                              $('#loading').modal('hide');
                              //redraw previously rendered tree if page loads
                              
                              if (newval.column.root) {
                                //heatmapMarginTop = 200;
                            	  scope.heatmapTopTree = newval.column.root;
                              }
                              
                              if (newval.row.root) {
                                //heatmapMarginLeft = Math.floor (svgWidth * .15);
                            	  scope.heatmapLeftTree = newval.row.root;
                              }
                              
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
                            height: (2*heatmapMarginTop) / 3,
                            width: heatmapCellsWidth
                        };
                        
                        var verticalTreeX = d3.scale.linear()
                        	.domain([0, 1])
                        	.range([4, heatmapMarginLeft]);
                        
                        var verticalTreeY = d3.scale.linear()
                        	.domain([0, 1])
                        	.range([heatmapMarginTop + heatmapColumnSelectionsGutter,
                                heatmapCellsHeight + heatmapMarginTop + heatmapColumnSelectionsGutter]);
                                
                        var horizontalTreeX = d3.scale.linear()
                        	.domain([0, 1])
                        	.range([heatmapMarginLeft, heatmapCellsWidth + heatmapMarginLeft]);
                        
                        var horizontalTreeY = d3.scale.linear()
                            .domain([0, 1])
                            .range([4, (2*heatmapMarginTop) / 3]);
                        
                        var dendogramLeftWindow = svg.append("g")
                            .attr('class', 'leftDendogram');
                        
                        var dendogramTopWindow = svg.append("g")
                            .attr('class', 'topDendogram');
                        
                        
                        //Left Dendogram Builder
                        scope.$watch('heatmapTopTree', function(newval, oldval){
                        
                          if (newval) {
                          
                            var tree = newval;
                            //heatmapMarginTop = 200;
                            //updateDrawHeatmap(scope.heatmapData);
                            drawTree(dendogramLeftWindow, Cluster, tree, 'horizontal' )

                            
                          }
                          
                        });
                        
                        scope.$watch('heatmapLeftTree', function(newval, oldval){
                          
                          if (newval) {

                            var tree = newval;
                            //heatmapMarginLeft = Math.floor (svgWidth * .15);
                            //updateDrawHeatmap(scope.heatmapData);
                            drawTree(dendogramTopWindow, Cluster, tree, 'vertical' )
                            
                            
                          }
                          
                        });
                        
                        scope.$watch('selectedColor', function(newval, oldval){
                          
                          if (newval) {

                            redrawCells (heatmapcells);
                            
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
                             .attr("r", 4)
                             .attr("cx", function(d){
     
                              return (type == 'vertical') ? verticalTreeX(d.y) : horizontalTreeX(d.x );
                              
                             })
                             .attr("cy", function(d){
                              return (type == 'vertical') ? verticalTreeY(d.x) : horizontalTreeY(d.y);
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
                          
                          return "M" + horizontalTreeX(d.target.x)  + "," + horizontalTreeY(d.target.y) +
                          "V" + horizontalTreeY(d.source.y) +
                          "H" + horizontalTreeX(d.source.x) ;

                        };
                        function verticalPath(d) {
                        	
                          //Path function builder for LEFT heatmap tree path attribute

                          return "M" + verticalTreeX(d.source.y)  + "," + verticalTreeY(d.source.x) +
                          "V" + verticalTreeY(d.target.x) +
                          "H" + verticalTreeX(d.target.y)

                        };



                        function cellColor (val) {

                          var color = {
                            red : 0,
                            blue : 0,
                            green : 0
                          }

                          if (scope.selectedColor == "Green-Black-Red" ) {

                            if (val <= leftshifter.domain()[1]) {
                              color.red = leftshifter (val);

                            } else {
                              
                              color.green = rightshifter (val);
                            }
                            ;

                          } else if (scope.selectedColor == "Red-White-Blue" ) {
                            
                            ls = d3.scale.linear ()
                              .domain([ leftshifter.domain()[0] , leftshifter.domain()[1] ])
                              .rangeRound ( [ leftshifter.range()[1] , leftshifter.range()[0] ] );
                            
                            rs = d3.scale.linear ()
                              .domain([ rightshifter.domain()[0] , rightshifter.domain()[1] ])
                              .rangeRound ( [ rightshifter.range()[1] , rightshifter.range()[0] ] );

                            if (val <= ls.domain()[1]) {
                              color.blue = "255";
                              color.red = ls (val);
                              color.green = ls (val);
                              

                            } else { 
                              
                              color.red = "255";
                              color.blue = rs (val);
                              color.green = rs (val);
                              
                            }
                            ;

                          } else { // default Yellow-Black-Blue
                            if (val <= leftshifter.domain()[1]) {
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