(function(){

	
    define(['d3', 'heatmapvisualization/lib/h3atmap', 'dendogram/lib/d3ndogram', 'jquery'], function(d3, h3atmap, d3ndogram, jquery){

       return function(module){

           module
           .factory('removeDuplicates', [function(){
        	   return function(acc, next){
	    			
	    			var nextInAcc = false
	    			
	    			for (var i = 0; i < acc.length; i++){
	    				if (acc[i] == next){
	    					nextInAcc = true
	    					break
	    				}
	    			}
	    			
	    			if (nextInAcc){
	    				return acc
	    			}
	    			
	    			return acc.concat([next])
	    		}
           }])
           .directive('nmfHeatmapVisualization', ['removeDuplicates', function(removeDuplicates){
        	   
        	   return {
                   restrict: 'C',
                   scope: {
                     data: "=data",
                     selectionParams: "=selectionParams",
                     selectionDimension: "@selectionDimension"
                   },
                   link: function(scope, element, attr){
                	   
		        	   scope.visualization = {
		        		   'svg': d3.select(element[0]).append('svg'),
		        		   'settings': {
		        			   'cell':{
		        				   'width': 15,
		        				   'height': 15
		        			   }
		        		   }
		               }
		        	   
		        	   scope.d3ndogram = {}
		        	   scope.d3ndogram.settings = {
		                       'interpolate': false,
		                       'invert': false,
		                       'x':{
		                           'margin':undefined,
		                           'axis-width':undefined,
		                           'gutter-width':undefined
		                       },
		                       'y':{
		                           'margin':undefined,
		                           'axis-width':undefined,
		                           'gutter-width':undefined
		                       },
		                       'labels': true,
		                       'style': {
		                           'path':{
		                               'color': '#ccc',
		                               'width': '1.5px',
		                               'selected': {
		                                   'color': '#0f0'
		                               }
		                           },
		                           'node':{
		                               'color': '#f00',
		                               'radius': 3,
		                               'selected': {
		                                   'color': '#0f0',
		                                   'radius': 5
		                               }

		                           }
		                       },
		                   }
		        	   
		        	   scope.h3atmap = {}
		        	   scope.h3atmap.settings = {
	                       'x':{
	                           'margin': 10,
	                           'axis-width': undefined ,
	                           'gutter-width': 200,
	                       },
	                       'y':{
	                           'margin': 10,
	                           'axis-width': undefined,
	                           'gutter-width': 200,
	                       },
	                       'extent': {},
	                       'expand': false,
	                       'labels': true,
	                       'cell': {
	                           'width': scope.visualization.settings.cell.width,
	                           'height': scope.visualization.settings.cell.height,
	                           'padding-percentage': .10 
	                       },
	                       'colors': d3.scale.linear().range(['#ffeda0', '#feb24c', '#f03b20']),
	                       'selection': {'color': 'green'}
	                   }
		        	   
		        	   var totalRowsCanFit = 40
		        	   var offsetRows = parseInt( (scope.h3atmap.settings.y['gutter-width']+ scope.h3atmap.settings.y.margin)
		        			   / scope.visualization.settings.cell.height)
		        	   
		        	   var heatmap = h3atmap()
		        	       
		        	   var dendogram = d3ndogram()
		        	   
		        	   scope.$watch('data', function(newval, oldval){
		        		   if (newval){
		        			   scope.visualization.svg.selectAll('*').remove()
			        		   scope.h3atmap.settings.x['axis-width'] = 
			        			   scope.data.column.keys.length * scope.visualization.settings.cell.width
		        			   scope.h3atmap.settings.y['axis-width'] =  
		        				   scope.data.row.keys.length * scope.visualization.settings.cell.height
		        			   
		        			   if (attr.selectionDimension == 'row'){
		        				   scope.d3ndogram.settings.x.margin = scope.h3atmap.settings.x.margin
			        			   scope.d3ndogram.settings.x['axis-width'] = scope.h3atmap.settings.x['gutter-width']
			        			   scope.d3ndogram.settings.x['gutter-width'] = 0
			        			   scope.d3ndogram.settings.y.margin = scope.h3atmap.settings.y.margin
			        			   scope.d3ndogram.settings.y['axis-width'] = scope.h3atmap.settings.y['axis-width']
			        			   scope.d3ndogram.settings.y['gutter-width'] = scope.h3atmap.settings.y['gutter-width']
		        			   } else {
		        				   scope.d3ndogram.settings.y.margin = scope.h3atmap.settings.y.margin
			        			   scope.d3ndogram.settings.y['axis-width'] = scope.h3atmap.settings.x['gutter-width'] - 70
			        			   scope.d3ndogram.settings.y['gutter-width'] = 0
			        			   scope.d3ndogram.settings.x.margin = scope.h3atmap.settings.x.margin
			        			   scope.d3ndogram.settings.x['axis-width'] = scope.h3atmap.settings.x['axis-width']
			        			   scope.d3ndogram.settings.x['gutter-width'] = scope.h3atmap.settings.x['gutter-width']
		        			   }
		        			   
			        		   scope.visualization.svg
		        			       .attr('height', 2 * scope.h3atmap.settings.y.margin +
		        			    		   2 * scope.h3atmap.settings.y['gutter-width'] + 
		        			    		   scope.h3atmap.settings.y['axis-width'])
		        			        .attr('width', 2 * scope.h3atmap.settings.x.margin +
		        			    		   2 * scope.h3atmap.settings.x['gutter-width'] + 
		        			    		   scope.h3atmap.settings.x['axis-width'])
			        			   
		        			   heatmap.selection(scope.visualization.svg)
		        			   	   .settings(scope.h3atmap.settings)
		        			   	   .settings.expand(true)
		        			   	   
		        			   	   
		        			   dendogram.selection(scope.visualization.svg)
		        			       .settings(scope.d3ndogram.settings)
		        			       .settings.invert(attr.selectionDimension == 'row' ? true : false)
		        			   
		        			   
		        			   heatmap()
		        			   dendogram()
		        			   
		        			   var parsedPoints = scope.data.values.map(function(p){
		        				   return {
		        				       x: p.column,
		        				       y: p.row,
		        				       value: p.value
		        				   }
		        			   }).filter(function(point){
		        				   return scope.data.row.keys.slice(0,totalRowsCanFit).reduce(function(agg, next){
		        					   return next == point.y ? agg.concat([next]) : agg 
		        				   }, []).length > 0
		        			   })
		        			   
		        			   heatmap.draw({
		        			        points: parsedPoints,
		        			        rows: scope.data.row.keys,
		        			        columns: scope.data.column.keys,
		        			        min: scope.data.min,
		        			        max: scope.data.max,
		        			        average: scope.data.avg,
		        			    })
		        			    
		        			    heatmap.on('brushend', function(extent, selectedCells){
		        			    	
		        			    	
		        			    	if (attr.selectionDimension == 'row'){
		        			    		
		        			    		scope.selectionParams.selections = selectedCells.map(function(cell){
		        			    			return cell.y
		        			    		}).reduce(removeDuplicates, [])

		        			    		scope.selectionParams.dimension = 'row'
		        			    		
	        			    			heatmap.axis.y.group().selectAll('text')
	        			    			.text(function(d, i){
	        			    				if (scope.selectionParams.selections.indexOf(d) > -1){
	        			    					d3.select(this)
	        			    						.style('fill', scope.h3atmap.settings.selection.color)
	        			    						.style('font-weight', 'bold')
	        			    				} else {
	        			    					d3.select(this)
	        			    						.style('fill', 'black')
	        			    						.style('font-weight', 'normal')
	        			    				}
	        			    				return d
	        			    			})
		        			    		
		        			    	} else {
		        			    		
		        			    		scope.selectionParams.selections = selectedCells.map(function(cell){
		        			    			return cell.x
		        			    		}).reduce(removeDuplicates, [])
		        			    		
			        			    	heatmap.axis.x.group().selectAll('text')
			        			    	.text(function(d, i){
			        			    		if (scope.selectionParams.selections.indexOf(d) > -1){
	        			    					d3.select(this)
	        			    						.style('fill', scope.h3atmap.settings.selection.color)
	        			    						.style('font-weight', 'bold')
	        			    				} else {
	        			    					d3.select(this)
	        			    						.style('fill', 'black')
	        			    						.style('font-weight', 'normal')
	        			    				}
	        			    				return d
	        			    			})
		        			    		
		        			    		scope.selectionParams.dimension = 'column'
		        			    	}
		        			    	
		        			    	scope.$apply()
		        			    })
		        			   
		        			   if  (scope.data.root){
		        				   dendogram.draw({
			        				   root:scope.data.root
			        			   })
		        			   }
		        			   
		        		   }
		        	   });
		        	   
		        	   
		        	   scope.$on("UI:SCROLL", function($event, params){
		        		   
		        		   if(attr.id!=params.id || scope.data == undefined){
		        			   return
		        		   }
		        		   
		        		   var numberOfRowsAbove = parseInt(params.scrollTop / scope.visualization.settings.cell.height) - offsetRows;
		        		   var totalRowsCanFit = parseInt(params.height / scope.visualization.settings.cell.height);		        		   
		        		   var shownrows = scope.data.row.keys.filter(function(row, index){
		        			   return index > numberOfRowsAbove-2 &&
		        			   		index < totalRowsCanFit + numberOfRowsAbove + 2
		        		   });
		        		   
		        		   var parsedPoints = scope.data.values.map(function(p){
		        				   return {
		        				       x: p.column,
		        				       y: p.row,
		        				       value: p.value
		        				   }
		        			   }).filter(function(point){
		        				   
		        				   for (var k = 0; k < shownrows.length; k++){
		        					   if (shownrows[k] == point.y){
		        						   return true
		        					   }
		        				   }
		        				   return false
		        			   })
		        		   
		        		   heatmap.update({
	        			        points: parsedPoints,
	        			        rows: scope.data.row.keys,
	        			        columns: scope.data.column.keys,
	        			        min: scope.data.min,
	        			        max: scope.data.max,
	        			        average: scope.data.avg,
	        			    })
	        			    
	        			    if (attr.selectionDimension == 'row'){
        			    		
    			    			heatmap.axis.y.group().selectAll('text')
    			    			.text(function(d, i){
    			    				if (scope.selectionParams.selections.indexOf(d) > -1){
    			    					d3.select(this)
				    						.style('fill', scope.h3atmap.settings.selection.color)
				    						.style('font-weight', 'bold')
    			    				} else {
    			    					d3.select(this)
    			    						.style('fill', 'black')
	    			    					.style('font-weight', 'normal')
    			    				}
    			    				return d
    			    			})
        			    		
        			    	} else {
        			    		
	        			    	heatmap.axis.x.group().selectAll('text')
	        			    	.text(function(d, i){
	        			    		if (scope.selectionParams.selections.indexOf(d) > -1){
	        			    			d3.select(this)
				    						.style('fill', scope.h3atmap.settings.selection.color)
				    						.style('font-weight', 'bold')
    			    				} else {
    			    					d3.select(this)
	    			    					.style('fill', 'black')
	    			    					.style('font-weight', 'normal')
    			    				}
    			    				return d
    			    			})
    			    			
        			    	}
		        	   });
		        	   
                   }
        	   }
        	   
           }])
           .directive('loquiScrollable', [function(){
        	   return {
        		   restrict: 'C',
        		   link: function(scope, element, attributes){
        			   
        			   var scrollable = $(element).scrollParent();
        			   if(!scrollable){
        				   scrollable=element;
        				   element.css({
            				   'height': parseFloat(attributes.height),
            				   'overflow': 'auto'
            			   });
        			   }
        			   scrollable.on('scroll', function(){
	    				   scope.$broadcast("UI:SCROLL", {
	    					   scrollTop: scrollable.scrollTop(),
	    					   height: scrollable.height(),
	    					   id: attributes.id
	    				   });
        			   }); 
        			   
        		   }
        	   }
           }])
           .directive('nmfAccordion', ['pathService', 'alertService', function(paths, alertService){

               return {
                 restrict: 'E',
                 templateUrl: paths.module + '/templates/nmfAccordion.tpl.html',
                 scope: {
                   analysis: "=analysis",
                   project: "=project"
                 },
                 link: function(scope, element, attr){
                     
                     scope.selectionParams = {
                         'name':undefined,
                		 'dimension': "column",
                         'selections':[],
                         'color': '#' + Math
	                         .floor(Math.random() * 0xFFFFFF << 0)
	                         .toString(16)
                     }
                	 
                	 scope.$watch('analysis', function(newval, oldval){
                		 if (newval){
                			 scope.heatmap = {
                		         'w': newval.w,
                		         'h': {
                		        	 'root': newval.h.root,
                		        	 'values': newval.h.matrix.values,
                		        	 'column': newval.h.matrix.column,
                		        	 'row': newval.h.matrix.row,
                		        	 'max': newval.h.matrix.max,
                		        	 'min': newval.h.matrix.min,
                		        	 'avg': newval.h.matrix.avg,
                		         }
                			 } 
                		 }
                	 })
                     
                     scope.addSelections = function(){
                    	 
                         var selectionData = {
                             name: scope.selectionParams.name,
                             properties: {
                                 selectionDescription: '',
                                 selectionColor: scope.selectionParams.color,
                             },
                             keys: scope.selectionParams.selections
                         };
                         
                         scope.project.dataset.selection.post({
                                 datasetName: scope.project.dataset.datasetName,
                                 dimension: scope.selectionParams.dimension

                             }, selectionData,
                             function (response) {
                                 var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                 var header = "Heatmap Selection Addition";
                                 
                                 
                                 scope.project.dataset.resetSelections(scope.selectionParams.dimension)

                                 scope.selectionParams['color'] = '#' + 
                                 	Math.floor(Math.random() * 0xFFFFFF << 0)
            	                         .toString(16)
                                 
                                 alertService.success(message, header);
                             },
                             function (data, status, headers, config) {
                                 var message = "Couldn't add new selection. If problem persists, please contact us.";

                                 var header = "Selection Addition Problem (Error Code: " + status + ")";

                                 alertService.error(message, header);
                             });
                     }
                	 
                 }
              }

           }])

       } 


    })


})()