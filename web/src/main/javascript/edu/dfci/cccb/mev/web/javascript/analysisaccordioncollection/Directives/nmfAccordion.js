(function(){

	var deps = ['d3', 'heatmapvisualization/lib/h3atmap', 'dendogram/lib/d3ndogram', 'jquery']
	
    define(deps, function(d3, h3atmap, d3ndogram, jquery){

       return function(module){

           module
           .directive('nmfTest', ['pathService', 'pcaTransform', 'pcaMulti', function(paths, transform, generatePCA){
        	   return {
                   restrict: 'E',
                   templateUrl: paths.module + '/templates/nmfAccordion.test.tpl.html',
                   scope:{
                  	 project: "="
                   },
                   link: function(scope){
                	   
                	   var cols = d3.range(5).map(function(ind){ return "sample" + ind })
                	   var rows = d3.range(100).map(function(ind){ return "gene" + ind })
                	   var values = cols.map(function(column, index){
                		   var column = rows.map(function(row){
                			   return {
                				   'column': column,
                				   'row': row,
                				   'value': Math.random() + (index * .01)
                			   }
                		   })
                		   
                		   return column
                	   }).reduce(function(aggregation, next){ return aggregation.concat(next)}, [])
                	   
						scope.analysis = {
							'name': "BLAH2", 
							'type':"Non-Negative Matrix Factorization",
							'randomId': "JDLSFJLKJ", 
							'analysis':{
								"type": "column",
								"row":{
									"type":"row",
									"keys": rows
								},
								"column":{
									"type":"column",
									"keys": cols
								},
								"values": values,
								"tree": {
						            distance: 0,
						            children: [{
						                distance: 0,
						                children: [{
						                    distance: 0,
						                    name: "Node2"
						                },{
						                    distance: 0,
						                    name: "Node1"
						                }]
						            },{
						                distance: 0,
						                children: [{
						                    distance: 0,
						                    children:[{
						                        distance: 0,
						                        name: "Node4"
						                    },{
						                        distance:0,
						                        name: "Node5"
						                    }]
						                },{
						                    distance: 0,
						                    name: "Node3"
						                }]

						            }]
						        }
							}
						}
				   }
          	   }
             }])
           .directive('nmfHeatmapVisualization', [function(){
        	   
        	   return {
                   restrict: 'C',
                   scope: {
                     data: "=data"
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
	                       'colors': d3.scale.linear().range(['#ffeda0', '#feb24c', '#f03b20'])
	                   }
		        	   
		        	   var totalRowsCanFit = parseInt(attr.height / scope.visualization.settings.cell.height)
		        	   var offsetRows = parseInt( (scope.h3atmap.settings.y['gutter-width']+ scope.h3atmap.settings.y.margin)
		        			   / scope.visualization.settings.cell.height)
		        	   
		        	   var heatmap = h3atmap()
		        	       
		        	   var dendogram = d3ndogram()
		        	   
		        	   scope.$watch('data', function(newval, oldval){
		        		   if (newval){
		        			   scope.visualization.svg.selectAll('*').remove()
			        		   scope.h3atmap.settings.x['axis-width'] = 
			        			   newval.analysis.column.keys.length * scope.visualization.settings.cell.width
		        			   scope.h3atmap.settings.y['axis-width'] =  
		        				   newval.analysis.row.keys.length * scope.visualization.settings.cell.height
		        			   
		        			   if (newval.analysis.type == 'row'){
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
		        			       .settings.invert(newval.analysis.type == 'row' ? true : false)
		        			   
		        			   
		        			   heatmap()
		        			   dendogram()
		        			   
		        			   var parsedPoints = newval.analysis.values.map(function(p){
		        				   return {
		        				       x: p.column,
		        				       y: p.row,
		        				       value: p.value
		        				   }
		        			   }).filter(function(point){
		        				   return newval.analysis.row.keys.slice(0,totalRowsCanFit).reduce(function(agg, next){
		        					   return next == point.y ? agg.concat([next]) : agg 
		        				   }, []).length > 0
		        			   })
		        			   
		        			   heatmap.draw({
		        			        points: parsedPoints,
		        			        rows: newval.analysis.row.keys,
		        			        columns: newval.analysis.column.keys,
		        			        min: d3.min(parsedPoints, function(d){return d.value}),
		        			        max: d3.max(parsedPoints, function(d){return d.value}),
		        			        average: d3.mean(parsedPoints, function(d){return d.value}),
		        			    })
		        			   
		        			   dendogram.draw({
		        				   root:newval.analysis.tree
		        			   })
		        		   }
		        	   });
		        	   
		        	   
		        	   scope.$on("UI:SCROLL", function($event, params){
		        		    		        		   
		        		   var numberOfRowsAbove = parseInt(params.scrollTop / scope.visualization.settings.cell.height) - offsetRows;
		        		   var totalRowsCanFit = parseInt(params.height / scope.visualization.settings.cell.height);		        		   
		        		   var shownrows = scope.data.analysis.row.keys.filter(function(row, index){
		        			   return index > numberOfRowsAbove-2 &&
		        			   		index < totalRowsCanFit + numberOfRowsAbove + 2
		        		   });
		        		   
		        		   var parsedPoints = scope.data.analysis.values.map(function(p){
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
	        			        rows: scope.data.analysis.row.keys,
	        			        columns: scope.data.analysis.column.keys,
	        			        min: d3.min(parsedPoints, function(d){return d.value}),
	        			        max: d3.max(parsedPoints, function(d){return d.value}),
	        			        average: d3.mean(parsedPoints, function(d){return d.value}),
	        			    })
		        	   });
		        	   
                   }
        	   }
        	   
           }])
           .directive('loquiScrollable', [function(){
        	   return {
        		   restrict: 'C',        		   
        		   link: function(scope, element, attributes){
        			   
        			   element.css({
        				   'height': parseFloat(attributes.height),
        				   'overflow': 'auto'
        			   })
        			   
        			   element.on('scroll', function(){
	    				   scope.$broadcast("UI:SCROLL", {
	    					   scrollTop: element.scrollTop(),
	    					   height: element.height()
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
                		 'dimension': {
                			 'x': undefined,
                             'y': undefined
                         },
                         'samples':[],
                         'color': '#' + Math
	                         .floor(Math.random() * 0xFFFFFF << 0)
	                         .toString(16)
                     }
                	 
                	 scope.$watch('analysis', function(newval, oldval){
                		 if (newval){
                			 scope.matrix = {
                		         'n': newval,
                		         'f': newval
                			 } 
                		 }
                	 })

                     scope.$watch('selectionParams.dimension.x', function(newval, oldval){
                         
                         return
                     }) 
                     
                     scope.$watch('selectionParams.dimension.y', function(newval, oldval){
                         
                         return
                     })
                     
                     scope.addSelections = function(){
                    	 
                         var selectionData = {
                             name: scope.selectionParams.name,
                             properties: {
                                 selectionDescription: '',
                                 selectionColor: scope.selectionParams.color,
                             },
                             keys: scope.selectionParams.samples.map(function(sample){return sample['id']})
                         };

                         scope.project.dataset.selection.post({
                                 datasetName: scope.project.dataset.datasetName,
                                 dimension: "column"

                             }, selectionData,
                             function (response) {
                                 scope.project.dataset.resetSelections('column')
                                 var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                 var header = "Heatmap Selection Addition";
                                 
                                 scope.selectionParams.color = '#' + Math
                                 .floor(Math.random() * 0xFFFFFF << 0)
                                 .toString(16)

                                 alertService.success(message, header);
                             },
                             function (data, status, headers, config) {
                                 var message = "Couldn't add new selection. If " + "problem persists, please contact us.";

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