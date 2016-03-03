define(['angular', 'd3', 'jquery',
        './lib/HeatmapVisualizationClass', './lib/generateParams',
        'alertservice/AlertService', 'colorbrewer/ColorBrewer', 'jquery-ui'], 
function(angular, d3, jquery, HeatmapVisualizationClass, generateParams){
	return angular.module('Mev.heatmapvisualization', ['d3colorBrewer', 'Mev.AlertService'])
	.directive('heatmapSettings',[function() {

        return {
            restrict : 'E',
            scope : {
                currentColors : '=currentColors',
                availableColorGroups : '=availableColorGroups',
                colorEdge : '=colorEdge',
                applyNewRanges : '=applyNewRanges',
                applyDefaultRanges : '=applyDefaultRanges'
            },
            templateUrl : "/container/view/elements/heatmapSettingsModalBody",
        }
	}])
	.directive('visHeatmap',[ "$routeParams", "$http", "d3colors", "alertService", "$timeout",
         function($routeParams, $http, d3colors, alertService, $timeout) {
 
            return {

                restrict : 'E',
                templateUrl : "/container/view/elements/visHeatmap",
                scope: {
                	heatmapView: "=heatmapView",
                	heatmapDataset: "=heatmapDataset",
                	project : '=project'
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
                	
                	$scope.availableColorGroups = Object.getOwnPropertyNames(d3colors);
                	$scope.currentColors = {group:undefined};
                	
                	$scope.visualization = undefined;
                	
                	var svg = undefined;
                	
                	$scope.$watch('currentColors.group', function(newval, oldval){

                	    if(newval && $scope.heatmapDataset && (oldval != newval) && svg){

                                $scope.currentColors.low = d3colors[newval][3][0];
                                $scope.currentColors.mid = d3colors[newval][3][1];
                                $scope.currentColors.high = d3colors[newval][3][2];
                                
                                var params = new generateParams({colors:$scope.currentColors});

                                $scope.visualization = new HeatmapVisualizationClass($scope.heatmapView,svg, params);

                	    }
                	});
                	$scope.repaintView = function(){
                		var updatedView = Object.create($scope.heatmapView);	                      
	                    updatedView.expression.min = $scope.project.dataset.expression.min;
	                    updatedView.expression.max = $scope.project.dataset.expression.max;
	                    updatedView.expression.avg = $scope.project.dataset.expression.avg;
	                    updatedView.coloring = undefined;
	                    $scope.heatmapView = updatedView;
                	}
                	$scope.$on("mui:model:dataset:values:loaded", function(){
//                		$scope.repaintView();
                		var position = {
                    			top: scrollable.scrollTop(),
                    			height:scrollable.height(),
                    			left: scrollable.scrollLeft(),
                                width:scrollable.width()
                    	};
                		if($scope.visualization)
                			$scope.visualization.updateCells(position, $scope.heatmapDataset, {force: true});
                	});
                	
                    $scope.colorEdge = {

                      min: undefined,
                      max: undefined,
                      avg: undefined

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
                    	var updatedView = Object.create($scope.heatmapView);
                      	
                          if ( 
                    		  typeof parseFloat($scope.colorEdge.min) == 'number'
                    		  && typeof parseFloat($scope.colorEdge.avg) == 'number'
                    		  && typeof parseFloat($scope.colorEdge.max) == 'number'
                    		  && parseFloat($scope.colorEdge.min) <= parseFloat($scope.colorEdge.avg)
                    		  && parseFloat($scope.colorEdge.avg) <= parseFloat($scope.colorEdge.max)
                    		  && parseFloat($scope.colorEdge.min) >= parseFloat($scope.project.dataset.expression.min)
                    		  && parseFloat($scope.colorEdge.max) <= parseFloat($scope.project.dataset.expression.max) ){
                    	  
                    	    var updatedView = Object.create($scope.heatmapView);
                      	
                    	    updatedView.coloring = $scope.currentColors;
                            updatedView.expression.min = parseFloat($scope.colorEdge.min);
                            updatedView.expression.max = parseFloat($scope.colorEdge.max);
                            updatedView.expression.avg = parseFloat($scope.colorEdge.avg);
                            $scope.heatmapView = updatedView;
                            
                         } else {
                    	  
                    	    $scope.colorEdge.min = $scope.project.dataset.expression.min;
	                        $scope.colorEdge.max = $scope.project.dataset.expression.max;
	                        $scope.colorEdge.avg = $scope.project.dataset.expression.avg;
	                      
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
                			svg.attr('id', 'svg-Window-' + $scope.heatmapView.id);
//                        	svg = d3.select(elems[0]).selectAll('#svg-Window-'+$scope.heatmapView.id);
                        	
                            $scope.colorEdge.min = newval.expression.min;
                            $scope.colorEdge.avg = newval.expression.avg;
                            $scope.colorEdge.max = newval.expression.max;
                			var params = 
                				new generateParams((newval.coloring) ? {colors:newval.coloring}: undefined);
                			$scope.currentColors = params.colors;
                			$scope.visualization = new HeatmapVisualizationClass(newval,svg, params)
                		}
                		
                	});
                	
                	//When visualization information comes, print cells, selections,
                	//	and clear current top and side panes
                	$scope.$watch('visualization', function(newval){

                		if(newval){

                            var startAvg = ((newval.view.expression.avg - $scope.project.dataset.expression.min) 
                                 / ($scope.project.dataset.expression.max - $scope.project.dataset.expression.min) )* 1000;
                            var startMin = ((newval.view.expression.min - $scope.project.dataset.expression.min) 
                                    / ($scope.project.dataset.expression.max - $scope.project.dataset.expression.min) )* 1000;
                            var startMax = ((newval.view.expression.max - $scope.project.dataset.expression.min) 
                                    / ($scope.project.dataset.expression.max - $scope.project.dataset.expression.min) )* 1000;
                            
                            $scope.visualization.updateCells(position, $scope.heatmapDataset);
//TODO: Add j-query slider
                            $timeout(function(){
	                            jquery('div#heatmapColorSlider').slider({
	                        		min: 1, 
	                        		max: 1000, 
	                        		values: [startMin, startAvg, startMax],
	                                slide: function(event, ui) {
	    	                              var index = $(ui.handle).siblings('a').andSelf().index(ui.handle);
	    	                              var values = $(this).slider('values');
	    	        
	    	                              if ((index == 0 || ui.value > values[index - 1]) &&
	    	                                (index == values.length - 1 || ui.value < values[index + 1])) {
	    	            
	    	                                var dimension = null;
	    	             
	    	                                if (index == 0){
	    	                                  dimension = "min"    
	    	                                } else if (index == 1) {
	    	                                  dimension = "avg"
	    	                                } else if  (index == 2) {
	    	                                  dimension = "max"
	    	                                }
	    	            
	    	                                $scope.$apply(function(){
	    	                                	$scope.colorEdge[dimension] = ($scope.project.dataset.expression.min + (
	    	                                			$scope.project.dataset.expression.max - $scope.project.dataset.expression.min)*(ui.value/1000));
	    	                                
	    	                                })
	    	                                
	    	              
	    	                             }
	    	                             return (index == 0 || ui.value > values[index - 1]) &&
	    	                                (index == values.length - 1 || ui.value < values[index + 1]);
	    	    
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
                        
                        $scope.project.dataset.selection.post({
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
