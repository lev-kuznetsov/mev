(function(){

  define(['d3'], function(d3){

    d3.custom = d3.custom || {}
    
    d3.custom.scatterplot = function module() {
        var settings = {
            width: 600,
            height: 600,
            margin: {
                top:10,
                bottom: 10,
                left: 20,
                right: 10,
            },
            axis: {
                gutter: {
                    left: 40,
                    bottom: 40
                }
            }
        }
        
        var brush
        
        var dispatch = d3.dispatch('brushend', 'update', 'draw')
        
        function exports(_selection) {
            _selection.each(function(_data, i){
                
                var svg = d3.select(this)
                
                //Group definitions
                var points = svg.append('g').classed('points', true)
                var AxisG = svg.append('g').classed('axis', true)
                var xAxisG = AxisG.append("g").classed('xAxis', true)
                var yAxisG = AxisG.append("g").classed('yAxis', true)
                var labels = svg.append('g').classed('labels', true)
                var brushG = svg.append('g').classed("brush", true)
                
                //axis defs
                var axis = {
                    x: d3.svg.axis(),
                    y: d3.svg.axis()
                }
                
                var scale = {
                    x: d3.scale.linear(),
                    y: d3.scale.linear()
                }
                
                //brush defs
                brush = d3.svg.brush()
                
                //brushing
                brush
                    .on("brushend", function(){
                    
                        xRange = [brush.extent()[0][0],brush.extent()[1][0]]
                        yRange = [brush.extent()[0][1],brush.extent()[1][1]]
                        
                        selectedPoints = []
                        
                        svg.selectAll('text#tooltip')
                        .remove()
                        
                        points.selectAll('circle').attr('fill', function(point){
                            
                            if ( (point.x <= parseFloat(xRange[1]) ) && (point.x >= parseFloat(xRange[0]) )
                               && (point.y <= parseFloat(yRange[1]) ) && (point.y >= parseFloat(yRange[0]) ) ) {
                                selectedPoints.push(point)
                                return "orange"
                            }
                            
                            return point.fill
                        }).each(function(point){
                        	
                        	if ( (point.x <= parseFloat(xRange[1]) ) && (point.x >= parseFloat(xRange[0]) )
                                    && (point.y <= parseFloat(yRange[1]) ) && (point.y >= parseFloat(yRange[0]) ) ) {
                                     
	                        	var pointElem = d3.select(this)
	
	                            var xPosition = parseFloat(pointElem.attr("cx")) + parseFloat(pointElem.attr('r'))*(Math.random()+1);
	                            var yPosition = parseFloat(pointElem.attr("cy")) - parseFloat(pointElem.attr('r'))*(Math.random()+1);
	
	                            svg.append("text")
	                                .attr("id", "tooltip")
	                                .attr("x", xPosition)
	                                .attr("y", yPosition)
	                                .attr("text-anchor", "middle")
	                                .attr("font-family", "sans-serif")
	                                .attr("font-size", "11px")
	                                .attr("font-weight", "bold")
	                                .attr("fill", "black")
	                                .attr("fill-opacity", "0%")
	                                .text(point.id);
                        	}
                        })
                        
                        svg.selectAll('text#tooltip')
                        .transition()
                        .duration(2000)
                        .attr("fill-opacity", "100%")
                        
                        
                        dispatch.brushend(brush, selectedPoints)
                        
                    });

                brushG.selectAll("rect")
                    .attr('fill', 'red');
                
                
                dispatch.on('draw', function(newdata){
                    
                    //update scales
                    updateScales(newdata, axis, scale, xAxisG, yAxisG, settings)
                    
                    //setup brush
                    brush.x(scale.x)
                    brush.y(scale.y)
                    brushG.call(brush);
                    
                    //initial points loading
                    var circles = points.selectAll('circle').data(newdata.points, function(point){ return point.id})

                    circles.enter().append('circle')
                        .attr('cx', function(point){return scale.x(point.x)})
                        .attr('cy', function(point){ return scale.y(point.y)})
                        .attr('r', 5)
                        .attr('fill', function(point){ return point.fill})
                        .on('mouseover', function(point){

                            var pointElem = d3.select(this)

                            var xPosition = parseFloat(pointElem.attr("cx")) + parseFloat(pointElem.attr('r')) + 10;
                            var yPosition = parseFloat(pointElem.attr("cy")) - parseFloat(pointElem.attr('r')) - 10;

                            svg.append("text")
                                .attr("id", "tooltip")
                                .attr("x", xPosition)
                                .attr("y", yPosition)
                                .attr("text-anchor", "middle")
                                .attr("font-family", "sans-serif")
                                .attr("font-size", "11px")
                                .attr("font-weight", "bold")
                                .attr("fill", "black")
                                .text(point.id);
                        })
                        .on('mouseout', function(){
                            d3.select("#tooltip").remove();
                        })
                    
                    //Initial labels drawing
                    labels.append('text')
                        .attr("x", settings.margin.left + settings.axis.gutter.left + (.5* settings.width))
                        .attr("y", settings.margin.top + settings.height + (settings.axis.gutter.bottom) )
                        .attr("text-anchor", "middle")
                        .attr("font-family", "sans-serif")
                        .attr("font-size", "16px")
                        .attr("font-weight", "bold")
                        .attr("fill", "black")
                        .text(newdata.labels.x);
                    
                    labels.append('text')
                        .attr("x",settings.margin.left)
                        .attr("y", settings.margin.top + (.5*settings.height) )
                        .attr("text-anchor", "middle")
                        .attr("font-family", "sans-serif")
                        .attr("font-size", "16px")
                        .attr("font-weight", "bold")
                        .attr("fill", "black")
                        .text(newdata.labels.y);
                        
                })
                
                dispatch.on('update', function(newdata){
                    
                    var circles = points.selectAll('circle').data(newdata.points, function(point){ return point.id})
                    
                    //update scales
                    updateScales(newdata, axis, scale, xAxisG, yAxisG, settings)
                    
                    //update brushing
                    brush.clear()
                    brushG.selectAll("*").remove()
                    brush.x(scale.x)
                    brush.y(scale.y)
                    brushG.call(brush)
                    
                    var offpageX = settings.width + settings.margin.left + settings.axis.gutter.left + 100
                    
                    //add the entering circles off-page
                    circles.enter().append('circle')
                    .attr('cx', offpageX)
                    .attr('cy', function(point){ return scale.y(point.y)})
                    .attr('r', 5)
                    .attr('fill', function(point){ return point.fill})
                    .on('mouseover', function(point){

                        var pointElem = d3.select(this)

                        var xPosition = parseFloat(pointElem.attr("cx")) + parseFloat(pointElem.attr('r')) + 10;
                        var yPosition = parseFloat(pointElem.attr("cy")) - parseFloat(pointElem.attr('r')) - 10;

                        svg.append("text")
                            .attr("id", "tooltip")
                            .attr("x", xPosition)
                            .attr("y", yPosition)
                            .attr("text-anchor", "middle")
                            .attr("font-family", "sans-serif")
                            .attr("font-size", "11px")
                            .attr("font-weight", "bold")
                            .attr("fill", "black")
                            .text(point.id);
                    })
                    .on('mouseout', function(){
                        d3.select("#tooltip").remove();
                    })
                    
                    //transition entering circles
                    circles.transition()
                        .duration(2000)
                        .attr('fill', function(point){ return point.fill})
                        .attr('cx', function(point){ return scale.x(point.x)})
                        .attr('cy', function(point){ return scale.y(point.y)})
                    
                    //transition exiting circles
                    circles.exit().transition().duration(2000)
                        .attr('cx', offpageX)
                        .remove()
                        
                    //trasition exiting tooltips
                    svg.selectAll('text#tooltip')
                    .transition()
                    .duration(1000)
                    .attr("fill-opacity", "0%")
                    .remove()
                    
                    //update labels
                    labels.selectAll("*").remove()
                    
                    labels.append('text')
                        .attr("x", settings.margin.left + settings.axis.gutter.left + (.5* settings.width))
                        .attr("y", settings.margin.top + settings.height + (settings.axis.gutter.bottom) )
                        .attr("text-anchor", "middle")
                        .attr("font-family", "sans-serif")
                        .attr("font-size", "16px")
                        .attr("font-weight", "bold")
                        .attr("fill", "black")
                        .text(newdata.labels.x);
                    
                    labels.append('text')
                        .attr("x",settings.margin.left)
                        .attr("y", settings.margin.top + (.5*settings.height) )
                        .attr("text-anchor", "middle")
                        .attr("font-family", "sans-serif")
                        .attr("font-size", "16px")
                        .attr("font-weight", "bold")
                        .attr("fill", "black")
                        .text(newdata.labels.y);
                })
                
            })
        }
        
        exports.width = function(_width){
            if (arguments.length > 0){
                settings.width = _width
                return exports
            }
            return width
        }
        
        exports.height = function(_input){
            if (arguments.length > 0){
                settings.height = _input
                return exports
            }
            return height
        }
        
        exports.margin = function(_input){
            if (arguments.length > 0){
                settings.margin = _input
                return exports
            }
            return margin
        }
        
        exports.axis = function(_input){
            if (arguments.length > 0){
                settings.axis = _input
                return exports
            }
            return axis
        }
        
        exports.brush = function(){
            return brush
        }
        
        exports.dispatcher = function(_input){
            if (arguments.length > 0){
                dispatch = _input
                return exports
            }
            
            return dispatch
        }
        
        d3.rebind(exports, dispatch, 'on')
        return exports
    }
    
    function updateScales(_data, axis, scale, xAxisG, yAxisG, settings){
        xData = {
            min: d3.min(_data.points, function(point){ return point.x }),
            max: d3.max(_data.points, function(point){ return point.x }),
            extent: d3.extent(_data.points, function(point){ return point.x })
        }

        xData.range = xData.max - xData.min

        yData = {
            min: d3.min(_data.points, function(point){ return point.y }),
            max: d3.max(_data.points, function(point){ return point.y }),
            extent: d3.extent(_data.points, function(point){ return point.y })
        }

        yData.range = yData.max - yData.min

        scale.x.domain([xData.min - (xData.range*.1), xData.max + (xData.range*.1) ])
            .range([settings.margin.left + settings.axis.gutter.left, settings.margin.left + settings.axis.gutter.left + settings.width])

        scale.y.domain([yData.min - (yData.range*.1), yData.max + (yData.range*.1)])
            .range([settings.margin.top + settings.height, settings.margin.top])

        axis.x.orient("bottom").scale(scale.x)
        axis.y.orient("left").scale(scale.y)

        xAxisG.attr("transform", "translate(0," + (settings.margin.top + settings.height) + ")").call(axis.x)
        yAxisG.attr("transform", "translate(" + (settings.margin.left + settings.axis.gutter.left) + ")").call(axis.y)
    }


  })

})()
