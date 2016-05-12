define(['mui', 'd3', 'jquery', "lodash", "./style/mev-heatmap.less",
        './lib/HeatmapVisualizationClass', './lib/generateParams', './directives/visHeatmap.tpl.html', "./directives/heatmapSettingsModalBody.tpl.html",
        '../colorBrewer/mevColorBrewer', '../alertService/mevHeatmapAlert', 'jquery-ui'], 
function(angular, d3, jquery, _, style, HeatmapVisualizationClass, generateParams, heatmapTemplate, heatmapSettingTemplate){ "use strict";
	return angular.module('mevHeatmapVisualization', ['mevColorBrewer'])
	.directive('mevHeatmapSettings',["mevD3colors", function(d3colors) {
        return {
            restrict : 'E',
            scope : {
                currentColors : '=currentColors',
                availableColorGroups : '=availableColorGroups',
                colorEdge : '=colorEdge',
                applyNewRanges : '=applyNewRanges',
                applyDefaultRanges : '=applyDefaultRanges',
                heatmapView: "=mevHeatmapView"
            },            
            link: function(scope, elm, attr){
                scope.currentColors.group = d3colors.current();
            },
            template : heatmapSettingTemplate,
            
        };
	}])
	.directive('mevHeatmap',[ "$http", "mevD3colors", "mevHeatmapAlertService", "$timeout",
         function($http, d3colors, alertService, $timeout) {
 
            return {

                restrict : 'E',
                // templateUrl : "/container/javascript/heatmapvisualization/templates/visHeatmap.tpl.html",
                template: heatmapTemplate,
                scope: {
                	heatmapView: "=mevHeatmapView",
                	heatmapDataset: "=mevDataset"                	
                },
                controller: function($scope){
                    $scope.currentColors = { 
                        group: d3colors.current()
                    };
                    $scope.availableColorGroups = _.keys(d3colors);
                    $scope.currentColors = d3colors.coloring;
                    
                    $scope.visualization = undefined;
                    $scope.$on("ui:d3colors:change", function($event, data){
                        if($scope.currentColors.group !== data){
                            $scope.currentColors.group = data;
                            var updatedView = _.extend({}, $scope.heatmapView);
                            if(!updatedView.coloring) updatedView.coloring = {};
                            // updatedView.coloring.group = data;
                            // updatedView.coloring.low = d3colors[updatedView.coloring.group][3][0];
                            // updatedView.coloring.mid = d3colors[updatedView.coloring.group][3][1];
                            // updatedView.coloring.high = d3colors[updatedView.coloring.group][3][2];

                            updatedView.coloring = d3colors.coloring;
                            $scope.heatmapView = updatedView;                            
                        }
                    });
                    this.onDataLoaded = $scope.$on("mui:model:dataset:values:loaded", function(){
//                      $scope.repaintView();
                        var position = {
                                top: $scope.scrollable.scrollTop(),
                                height:$scope.scrollable.height(),
                                left: $scope.scrollable.scrollLeft(),
                                width:$scope.scrollable.width()
                        };
                        if($scope.visualization){
                            $scope.heatmapView.refresh();                            
                            // $scope.visualization.updateScales();
                            // $scope.visualization.updateCells(position, $scope.heatmapDataset, {force: true});
                            $scope.colorEdge.refresh();
                            $scope.applyNewRanges();
                        }
                    });
                },
                link : function($scope, elems, attr) {
                	//use jquery to get the nearest scrollable parent
                    var $ = jquery;
                	var scrollable = $(elems).scrollParent();
                	//if that didnt' work use the scrollableContainer attribute if supplied
                	if(!scrollable && $scope.heatmapView.scrollableContainer)
                		scrollable = $($scope.heatmapView.scrollableContainer);
                	//finally, default to 'div.tab-content' for legacy code
                	if(!scrollable)
                		scrollable = $("div.tab-content");
                	$scope.scrollable = scrollable;
                	var delay = 50, timer = null;
//                	if(!$scope.heatmapView.scrollableContainer)
//                		$scope.heatmapView.scrollableContainer="div.tab-content";
//                	var scrollable = $($scope.heatmapView.scrollableContainer), delay = 50, timer = null;
                	console.debug("HeatmapVis scrollable", $scope.heatmapView.scrollableContainer, scrollable);
                       
                    var position = {
                			top: scrollable.scrollTop(),
                			height:scrollable.height(),
                			left: scrollable.scrollLeft(),
                            width:scrollable.width()
                	};
                	
                	
                	
                	var svg = undefined;
                	
                	$scope.repaintView = function(){
                		var updatedView = _.extend({}, $scope.heatmapView);	                      
	                    updatedView.expression.min = $scope.heatmapDataset.expression.min;
	                    updatedView.expression.max = $scope.heatmapDataset.expression.max;
	                    updatedView.expression.avg = $scope.heatmapDataset.expression.avg;
	                    updatedView.coloring = d3colors.current();
	                    $scope.heatmapView = updatedView;
                	}
                	
                    $scope.colorEdge = {

                      min: undefined,
                      max: undefined,
                      avg: undefined,
                      refresh: function(){
                        $scope.colorEdge.min = $scope.heatmapView.expression.min;
                        $scope.colorEdge.max = $scope.heatmapView.expression.max;
                        $scope.colorEdge.avg = $scope.heatmapView.expression.avg;
                          
                      }

                    };
                    var colorSlider = jquery('div#heatmapColorSlider')
                    $timeout( function(){
                    	
                    }, 1000);
                    
                    $scope.$watchCollection('colorEdge', function(colorEdge, oldval){

                       if (colorEdge.min && colorEdge.max &&colorEdge.avg) {
//TODO: Add j-query slider
//Currently the heatmap colormap is not adjustable, so I commented out - it was often throwing an error on page load                    	   
//                         colorSlider.slider('option', 'values', [colorEdge.min, colorEdge.avg, colorEdge.max]);
                       }
                    });

                    $scope.applyDefaultRanges = function (){
                    	
                    	$('#settingsModal').modal('hide');
                    	$scope.repaintView();

                    };
                    
                    $scope.applyNewRanges = function(){
                    	
                    	$('#settingsModal').modal('hide');
                    	
                          if ( 
                    		  typeof parseFloat($scope.colorEdge.min) == 'number'
                    		  && typeof parseFloat($scope.colorEdge.avg) == 'number'
                    		  && typeof parseFloat($scope.colorEdge.max) == 'number'
                    		  && parseFloat($scope.colorEdge.min) <= parseFloat($scope.colorEdge.avg)
                    		  && parseFloat($scope.colorEdge.avg) <= parseFloat($scope.colorEdge.max)
                    		  && parseFloat($scope.colorEdge.min) >= parseFloat($scope.heatmapView.expression.min)
                    		  && parseFloat($scope.colorEdge.max) <= parseFloat($scope.heatmapView.expression.max) ){
                    	  
                    	    var updatedView = _.extend({}, $scope.heatmapView);
                      	    
                    	    // updatedView.coloring = ;
                         //    updatedView.coloring.low = d3colors[updatedView.coloring.group][3][0];
                         //    updatedView.coloring.mid = d3colors[updatedView.coloring.group][3][1];
                         //    updatedView.coloring.high = d3colors[updatedView.coloring.group][3][2];                            
                            updatedView.coloring = d3colors.current($scope.currentColors.group);                             

                            updatedView.expression.min = parseFloat($scope.colorEdge.min);
                            updatedView.expression.max = parseFloat($scope.colorEdge.max);
                            updatedView.expression.avg = parseFloat($scope.colorEdge.avg);
                            $scope.heatmapView = updatedView;
                            
                         } else {
                    	  
                    	    $scope.colorEdge.min = $scope.heatmapDataset.expression.min;
	                        $scope.colorEdge.max = $scope.heatmapDataset.expression.max;
	                        $scope.colorEdge.avg = $scope.heatmapDataset.expression.avg;
	                      
                    	    alertService.error("Ranges must be real numbers!",
                    	    		"Range Update Error");
                    	    $scope.applyDefaultRanges();
                    	    
                         }
                      
                    	
                    };
            	
                    var eventQ=[];
                    scrollable.on("scroll", function(e){
                    	//remove unhandled scholl events from the queue
                        while(eventQ.length>0){
                        	clearTimeout(eventQ.pop());
                        }
                        
                        //create a new event handler and put it on the work queue
                        timer = setTimeout(function(){
                        	//we are handling the event, so remove it from the queue
                        	eventQ.pop();                        	
                    		position = {
                                    top: scrollable.scrollTop(),
                                    height:scrollable.height(),
                                    left: scrollable.scrollLeft(),
                                    width:scrollable.width()
                          	    };
                    		$scope.visualization.updateCells(position, $scope.heatmapDataset);
	                    	
                        }, 100);                        
                        eventQ.push(timer);	                    
                    });
                	
                	
                	
                	
                	
                	
                	//When dataset information comes, generate new visualization.
                	$scope.$watch('heatmapView', function(newval, oldval){
                		console.debug("heatmapView change", oldval.note, newval.note);
                		if (newval){
                			
                			console.debug("watch heatmapView ", newval.note);
                			if (d3.select(elems[0]).selectAll('svg')){

                    			d3.select(elems[0]).selectAll('svg').remove()
                			}
                			var svg = d3.select(elems[0]).append('svg');
                            //svg.append("style").text(style.source);
                			svg.attr('id', 'svg-Window-' + $scope.heatmapView.id);
//                        	svg = d3.select(elems[0]).selectAll('#svg-Window-'+$scope.heatmapView.id);
                        	
                            $scope.colorEdge.min = newval.expression.min;
                            $scope.colorEdge.avg = newval.expression.avg;
                            $scope.colorEdge.max = newval.expression.max;
                            var params = new generateParams((newval.coloring) ? {colors:newval.coloring}: {colors: d3colors.coloring});
                			$scope.visualization = new HeatmapVisualizationClass(newval,svg, params)
                		}
                		
                	});
                	
                	//When visualization information comes, print cells, selections,
                	//	and clear current top and side panes
                	$scope.$watch('visualization', function(newval){

                		if(newval){

                            var startAvg = ((newval.view.expression.avg - newval.view.expression.min) / (newval.view.expression.max - newval.view.expression.min) )* 10000;
                            var startMin = ((newval.view.expression.min - newval.view.expression.min) / (newval.view.expression.max - newval.view.expression.min) )* 10000;
                            var startMax = ((newval.view.expression.max - newval.view.expression.min) / (newval.view.expression.max - newval.view.expression.min) )* 10000;
                            
                            $scope.visualization.updateCells(position, $scope.heatmapDataset);
//TODO: Add j-query slider
                            $timeout(function(){
	                            jquery('div#heatmapColorSlider-'+newval.view.id).slider({
	                        		min: 0, 
	                        		max: 10000, 
	                        		values: [startMin, startAvg, startMax],
	                                slide: function(event, ui) {
	    	                              var index = $(ui.handle).siblings('a').andSelf().index(ui.handle);
	    	                              var values = $(this).slider('values');
                                          function domainToRange(min, max, input){
                                            var output = min+(max - min)/10000*input;
                                            return output;
                                          }
                                          function applyValues(values){
                                                $scope.colorEdge.min=domainToRange($scope.heatmapView.expression.min, $scope.heatmapView.expression.max, values[0]);
                                                $scope.colorEdge.avg=domainToRange($scope.heatmapView.expression.min, $scope.heatmapView.expression.max, values[1]);
                                                $scope.colorEdge.max=domainToRange($scope.heatmapView.expression.min, $scope.heatmapView.expression.max, values[2]);
                                          }
	    	        
	    	                              // if ((index == 0 || ui.value > values[index - 1]) &&
	    	                              //   (index == values.length - 1 || ui.value < values[index + 1])) {
	    	            
	    	                                var dimension = null;
	    	             
	    	                                if (index == 0){
	    	                                  dimension = "min"    
	    	                                } else if (index == 1) {
	    	                                  dimension = "avg"
	    	                                } else if  (index == 2) {
	    	                                  dimension = "max"
	    	                                }
	    	            
	    	                                $scope.$apply(function(){
                                                applyValues(values);                                                
	    	                                
	    	                                })
	    	                                
	    	              
	    	                             // }
	    	                             // var ret = (index == 0 || ui.value > values[index - 1]) &&
	    	                             //    (index == values.length - 1 || ui.value < values[index + 1]);
                                         var ret = true;
                                         var margin = 100;
                                         if(values[0] >= values[1]-margin/2){
                                            values[0]=values[1]-margin;
                                            applyValues(values);
                                            ret = false;      
                                            $timeout(function(){
                                                console.debug("slide reset", values);
                                                $(this).slider("values", values);
                                            }.bind(this));
                                         }else if(values[2] <= values[1]+margin/2){
                                            values[2]=values[1]+margin;
                                            applyValues(values);                                            
                                            ret = false;
                                            $timeout(function(){
                                                var slider = $(this).slider("values", values);                                                
                                                console.debug("slide reset", slider.values);
                                            }.bind(this));
                                         }                                         
                                        console.debug("slide ret", ret, values, $scope.colorEdge);
                                        return ret;
	    	                           } 
	                        	});
                            }, 2000);

                		}
                		
                		if ($scope.heatmapDataset.selections.column){
                            $scope.visualization.drawSelections($scope.heatmapDataset.selections.column, "column");
                		}
                		if ($scope.heatmapDataset.selections.row){
                            $scope.visualization.drawSelections($scope.heatmapDataset.selections.row, "row");
                		}
                		
                		if ($scope.visualization.view.panel && $scope.visualization.view.panel.top) {
                		    $scope.visualization.drawTopPanel($scope.visualization.view.panel.top);
                		    
                		    
                		}
                		
                		if ($scope.visualization.view.panel && $scope.visualization.view.panel.side) {
                            $scope.visualization.drawSidePanel($scope.visualization.view.panel.side);
                        }
                		
                	});
                	
                	
                	//When new selections come, print them
                	
                	$scope.$watchCollection('heatmapDataset.selections.column', function(newval){

                	    if (newval && $scope.visualization){
                	        $scope.visualization.drawSelections(newval, "column")
                	    };
                	});
                	
                	$scope.$watchCollection('heatmapDataset.selections.row', function(newval){

                	    if (newval && $scope.visualization){
                            $scope.visualization.drawSelections(newval, "row")
                        };
                    });

                	
                	//When panel elements come in, print them
                	$scope.$watch('visualization.view.panel.top', function(newval, oldval) {
 
                        if(newval && $scope.visualization){
                            $scope.visualization.drawTopPanel($scope.visualization.view.panel.top);
                        }
                    });
                	
                	$scope.$watch('visualization.view.panel.side', function(newval, oldval) {

                        if(newval && $scope.visualization){
                            $scope.visualization.drawSidePanel($scope.visualization.view.panel.side);
                        }
                    });

                	$scope.clearSelections = function(dimension){
                		
                		$scope.visualization.view.selectionParams[dimension].labels=[];

                		switch (dimension){
                		case ('row'):
                			$scope.visualization.drawSidePanel($scope.visualization.view.panel.side)
                			break
                		case ("column"):
                			$scope.visualization.drawTopPanel($scope.visualization.view.panel.top)
                			break
                		}
                	};
                	//addSelection [Selection] --> null
                	$scope.addSelection = function(dimension){
                		console.debug("HeatmaVisualization: addSelection", dimension, $scope.visualization.view.selectionParams[dimension].labels, $scope.visualization.view.selectionParams[dimension])
                		var selectionsData = {
                            name: $scope.visualization.view.selectionParams[dimension].name,
                            properties: {
                                selectionDescription: '',
                                selectionColor: $scope.visualization.view.selectionParams[dimension].color,                     
                            },
                            keys:$scope.visualization.view.selectionParams[dimension].labels
                        };
                		
                		if (selectionsData.keys.length == 0){
                			var message = "Cannot add empty selection. Plese try again.";

                            var header = "Selection Addition Problem (Error Code: 0)";

                            alertService.error(message,header);
                			return
                		}
                        
                        $scope.heatmapDataset.selection.post({
                            datasetName : $scope.heatmapDataset.datasetName,
                            dimension : dimension

                        }, selectionsData,
                        function(response){
                                var message = "Added " + $scope.visualization.view.selectionParams[dimension].name + " as new Selection!";
                                var header = "Heatmap Selection Addition";
                        
                                $scope.$emit('SeletionAddedEvent', dimension);
                                $scope.visualization.view.selectionParams[dimension].color = '#'+Math
                                    .floor(Math.random()*0xFFFFFF<<0)
                                    .toString(16);
                                $scope.visualization.view.selectionParams[dimension].name = undefined;

                                alertService.success(message,header);
                        },
                        function(data, status, headers, config) {
                            var message = "Couldn't add new selection. If "
                                + "problem persists, please contact us.";

                             var header = "Selection Addition Problem (Error Code: "
                                + status
                                + ")";

                             alertService.error(message,header);
                        });
                	};

                }

            }; // End return obj
        }])
})
