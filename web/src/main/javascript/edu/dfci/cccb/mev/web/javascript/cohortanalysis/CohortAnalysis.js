define(['d3', 'angular', 'colorbrewer/ColorBrewer'], function(d3, angular){
	
	return angular.module('Mev.CohortAnalysis', ['d3colorBrewer'])
    .service('d3Service', function () {
    return d3
})
    .service('histogramExtractor', ['d3Service', function (d3) {
    // histogramExtractor [Numbers] -> Object
    //    Function to extract values for histogram processing
    return function (data) {

		
		var extracted = data.filter(function(datum){
			return (isNaN(datum.value) ) ? false : true;
		}).map(function(datam){
			return parseFloat(datam.value)
		});

        return (extracted.length > 1) ? {
            'data': extracted,
                'min': d3.min(extracted),
                'max': d3.max(extracted)
        } : undefined;
    }
}])
.service('stringClean', function(){
	return function(string){
		
		var returnstring = "";
		var arr = string.split('_');
		arr.map(function(word){
			returnstring = returnstring.concat(word.charAt(0).toUpperCase() + word.slice(1) + " ")
		})
		
		return returnstring;
	}
})
.directive('cohortDashboard', ['$compile', function ($compile) {
	
	var pieChartTemplate = '<d3-Pie-Chart data="viewModel" height="300" width="500"></d3-Pie-Chart>';
    var histogramTemplate = '<d3-Histogram data="viewModel" height="300" width="500"></d3-Histogram>';
    
    var getTemplate = function(type) {
        var template = '';

        switch(type) {
            case 'histogram':
                template = histogramTemplate;
                break;
            case 'piechart':
                template = pieChartTemplate;
                break;
        }

        return template;
    };
    
    function compileView(model, type, element, compile, scope){
		
		element.append(getTemplate(scope.viewType));
        compile(element.contents())(scope);
        
	};
	
    return {
        'restrict': 'E',
            'scope': {
            	'viewModel': '=data',
            	'viewType': '=type'
            },
            'link': function(scope, element, attrs) {
            	
            		

            		scope.$watch('viewType', function(newval){
            			if (scope.viewModel && scope.viewType){
            				compileView(scope.viewModel, scope.viewType, element, $compile, scope)
            			
            			}
            		});
            		
            		scope.$watch('viewModel', function(newval){

            			if (scope.viewModel && scope.viewType){
            				compileView(scope.viewModel, scope.viewType, element, $compile, scope)
            			}
                        
            		});

            }
    };
    
}])
    .directive('d3Histogram', ['histogramExtractor', 'd3Service', 'd3colors', 'stringClean',
                               function (histogramExtractor, d3, d3colors, clean) {
    return {
        'restrict': 'E',
            'scope': {
            'histData': '=data',
                'height': '@height',
                'width': '@width',
        },
            'link': function (scope, elems, attrs) {

            var formatCount = d3.format(",.0f");

            var margin = {
                top: 10,
                right: 30,
                bottom: 50,
                left: 50
            },
            width = scope.width - margin.left - margin.right,
                height = scope.height - margin.top - margin.bottom;

            d3.select(elems[0]).append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .attr('class', 'histogram')

            var svg = d3.select(elems[0]).select("svg.histogram")

            svg.append("g")
            .attr('class','visualization')
            .attr("transform", 
                "translate(" + margin.left + "," + margin.top + ")");

            var viz = svg.select("g.visualization");

            scope.$watch('histData', function (newval, oldval) {

                if (newval) {

                	var histData = histogramExtractor(newval);
                	
                	if (histData === undefined || !newval[0].columnId ) { 

                        viz.selectAll("*").remove();
                        viz.append("text")
                        .attr("y", 10)
                        .attr("x", 1)
                        .style("text-anchor", "start")
                        .text(function(d) { 
                        	return "Cannot generate histogram for this cohort."
                        });
                		return
                	}
                	
                	var data = d3.layout.histogram()
	                    //.bins(x.ticks(20))
	                  (histData.data);

                	data = data.filter(function(d){
                		return (d.length > 0)? true:false
                	});
                	
                	if (data.length == 1){

                        viz.selectAll("*").remove();
                        viz.append("text")
                        .attr("y", 100)
                        .attr("x", 1)
                        .style("text-anchor", "start")
                        .text(function(d) { 
                        	return "Cohort data is uniform. Value: " 
                        	+ data[0][0] +" Count: " + data.length;
                        })
                        return
                	}
                	

                	var labels = [{'axis':'left', 'value':'# Of Samples'}, 
                	              {'axis':'bottom', 'value':clean(newval[0].columnId)}];
                	
                	viz.append('g').attr('class', 'labels')
                	
                	viz.select('g.labels').selectAll('text')
                		.data(labels).enter()
                		.append('text')
                		.attr({
                			'x':function(d,i){
                				if(d.axis === 'bottom'){
                					return width/2 
                				} else if (d.axis === 'left') {
                					return -height/2
                				};
                			},
                			'y':function(d,i){
                				if (d.axis === 'bottom'){
                					return height+margin.bottom/1.5
                				} else if (d.axis === 'left'){
                					return -40
                				};
                			},
                		})
                		.text(function(d){
                			return d.value
                		}).attr("text-anchor", function(d){
                			if (d.axis === 'bottom'){
                				return 'middle'
                			} else if (d.axis === 'left'){
                				return 'middle'
                			}
                		}).attr('transform', function(d){
                			return (d.axis === "left")? 'rotate(-90)':""
                		})
                    
                    var maxRange = d3.max(data.map(function(datum){
                    	return datum.dx;
                    }));

                    var x = d3.scale.linear()
                        .domain([histData.min-maxRange,histData.max+maxRange])
                        .range([0, width]);
                    
                    

                    var y = d3.scale.linear()
                        .domain([0, d3.max(data, function (d) {
                        return d.y;
                    })])
                        .range([height, margin.top]);

                    var xAxis = d3.svg.axis()
                        .scale(x)
                        .orient("bottom");
                    
                    var yAxis = d3.svg.axis()
                    .scale(y)
                    .tickFormat(d3.format('d'))
                    .innerTickSize('0')
                    .outerTickSize('3')
                    .orient("left");

                    var bar = viz.selectAll(".bar")
                        .data(data)
                        .enter().append("g")
                        .attr("class", "bar")
                        .attr("transform", function (d) {
                        	return "translate(" + x(d.x) + "," + y(d.y) + ")";
                        })
                        .attr('fill', function(d,i){
                        	return d3colors['Set3'][9][0]
                        });

                    bar.append("rect")
                        .attr("x", 1)
                        .attr("width", function(d){
                        	return x(d.dx + d.x) - x(d.x) -2
                        })
                        .attr("height", function (d) {
                        return height - y(d.y);
                    });
                    
                    viz.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + height + ")")
                        .call(xAxis);
                    
                    viz.append("g")
                    .attr("class", "y axis")
                    .attr("transform", "translate(0," + 0 + ")")
                    .call(yAxis);

                    //End of if statement    
                };


                //End of scope.data watcher  
            });



            //End of link function    
        },


        //End of return object
    };
}])
.service('PieChartExtractor', [function () {
	return function(data){
		
		var frequencies = {}, count = 0;
		
		data.map(function(datum){
			
			count = count + 1;
			
			if (frequencies[datum.value]){
				frequencies[datum.value] = frequencies[datum.value] +1
			} else {
				frequencies[datum.value] = 1;
			}
		});
		
		
		return {
			'slices': Object.keys(frequencies).map(function(prop){
				return {'key':prop, 'value':frequencies[prop]}
			}),
			'total': count
		}
	};
}])
.directive('d3PieChart', ['d3Service', 'PieChartExtractor', 'd3colors',
                          function(d3, PCExtract, d3colors){
    return {
        'restrict': 'E',
        'scope' : {
            'data' : '=data',
            'width' : '@width',
            'height' : '@height',
            'on': '@dimension'
        },
        'link': function(scope, elems, attrs){
            
            var width = scope.width,
                height = scope.height,
                radius = Math.min(scope.width, scope.height) / 2;
            
            var color = d3.scale.ordinal()
                .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);
            
            var arc = d3.svg.arc()
                .outerRadius(radius - 10)
                .innerRadius(0);
            
            var pie = d3.layout.pie()
                .sort(null)
                .value(function(d) { return d.value; });
            
            d3.select(elems[0]).append("svg")
                .attr("width", width)
                .attr("height", height);
            
            var svg = d3.select(elems[0]).select('svg');
            
            svg.append("g")
                .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
            var viz = svg.select('g');
            
            scope.$watch("data", function(newval, oldval) {
            
                if (newval){
                    
                	  var extract = PCExtract(scope.data);
                    
                      var g = viz.selectAll(".arc")
                          .data(pie(extract.slices))
                        .enter().append("g")
                          .attr("class", "arc");
                      
                      
                    
                      g.append("path")
                          .attr("d", arc)
                          .style("fill", function(d, i) { 
                              return d3colors['Set3'][9][(i%9)];
                          })
                          .on('mouseover', function(d,i){
                        	  d3.select(this).attr('style', 'fill:orange;');
                        	  
                        	  div.transition()        
	                              .duration(200)      
	                              .style("opacity", .99);      
                        	  
                        	  
	                          div.select('text')
	                          	.attr("transform", "translate(" + [arc.centroid(d)[0],arc.centroid(d)[1]+30] + ")")
	                          	.text(d.data.key + ': ' + d.value);
                          })
                          .on('mouseout', function(d,i){
                        	  d3.select(this).attr('style', 'fill:'+d3colors['Set3'][9][(i%9)]+ ";");
                        	  
		                      div.transition()        
	                              .duration(500)      
	                              .style("opacity", 0);   
                          })
                          
                      
                      viz.append("g")
	                  	.attr("class", "tooltip")               
	                  	.style("opacity", 0)
	                  	.append('text').text('hello');
                    
                      var div = viz.select('g.tooltip');
                      
                      
                      g.append("text")
                          .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
                          .attr("dy", ".35em")
                          .style("text-anchor", "middle")
                          .text(function(d) { 
                        	  
                        	  var returnstring = undefined;
                        	  
                        	  var percentage = (( (d.endAngle - d.startAngle)/(2*Math.PI) )*100);
                        	  
                        	  if ( (Math.ceil(percentage) -percentage ) >= .5){

                        		  percentage = Math.ceil(percentage)
                        	  } else {
                        		  percentage = Math.floor(percentage)
                        	  }
                        	  
                        	  if (d.data.key.length > 10){
                        		  returnstring = d.data.key.slice(0,10) + '... :' 
                            	  + percentage + "%" ;
                        	  } else {
                        		  returnstring = d.data.key+ ': ' 
                        		  	+ percentage + "%" ;
                        	  }
                        	  
                        	  console.log(returnstring.split('.'))
                        	  
                        	  
                        	  
                        	  return  returnstring;
                        	  
                          });
                          
                          
                 };
              
            
            });
            
        //End link function
        }
    //End restrict object
    }
    
//End of directive
}]);
	
});

