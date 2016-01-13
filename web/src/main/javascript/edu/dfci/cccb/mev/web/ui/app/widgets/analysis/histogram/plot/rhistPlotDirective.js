define(["ng", "d3", "d3-tip"], function(ng, d3){
	"use strict";
	var RHistPlotDirective = function RHistPlotDirective(){
		
		return {
			restrict: "AE",
			scope: {
				data: "=",
				muiXLabel: "@",
				yLabel: "@"				
			},
//this template uses the on-size-changed custom directive to trigger resize event
//the on-size-change event taps the window object, so it only works when the window is resized,
//not when the dashboard panels are resized independently of the window
//			template: "<div class='rhistPlot' on-size-changed='sizeChanged'><svg preserveAspectRatio='xMinYMin'></svg></div>",
//this template sets the preserveAspectRatio attribute which only works if the  viewBox is defined
//The viewBox allows the browser to shrink/expand the svg graphics as its container size changes
//This works but is not ideal because the graphics become hard to read
//			template: "<div class='rhistPlot'><svg preserveAspectRatio='xMinYMin'></svg></div>",
//Finally, this template relies on $animate events and updates the svg graphics manually
			template: "<div class='rhistPlot'><svg></svg></div>",
			link: function(scope, elem, attrs){
				
				function AspectRatio(curWidth, curHeight, targetRatio){
					var _self=this;
					_self.targetRatio = targetRatio || 5 / 3;
				    _self.curRatio = curWidth / curHeight;
				    if(_self.curRatio > _self.targetRatio)
				    	_self.targetWidth = _self.targetRatio * curHeight;
				    else if(_self.curRatio < _self.targetRatio)
				    	_self.targetHeight = curWidth / _self.targetRatio;
				    
				    _self.height = function(){
				    	return _self.targetHeight || curHeight;
				    };
				    _self.width = function(){
				    	return _self.targetWidth || curWidth;
				    };				    
				};
				
				console.debug("histogram", ng.element(elem[0]));
				var root = elem.find(".rhistPlot");
				var d3root = d3.select(root[0]);
			    var svg = d3root.select("svg");			    
			    d3root.on("resize", function(){});			    
			    
				var margin = {top: 10, right: 30, bottom: 50, left: 60};			    
			    var width = root.width() - margin.left - margin.right;
			    var height = root.height() - margin.top - margin.bottom;
			    var aspectRatio = new AspectRatio(width, height);
			    width = aspectRatio.width();
			    height = aspectRatio.height();
//			    var targetRatio = 5 / 3;
//			    var curRatio = width / height;
//			    if(curRatio > targetRatio)
//			    	width = targetRatio * height;
//			    else if(curRatio < targetRatio)
//			    	height = width / targetRation;
			    
			    
			    var binsize = scope.data.breaks[1]-scope.data.breaks[0];
			    var leftoffset = 0;
			    // whitespace on either side of the bars in units of MPG
			    var binmargin = .025; 
			    
			    // Set the limits of the x axis
			    var xmin = scope.data.breaks[0]-leftoffset;
			    var xmax = scope.data.breaks[scope.data.breaks.length-1];

			    var histdata = scope.data.counts;			 

			    // This scale is for determining the widths of the histogram bars
			    // Must start at 0 or else x(binsize a.k.a dx) will be negative
			    var x = d3.scale.linear()
				  .domain([0, (xmax - xmin)])
				  .range([0, width]);

			    // Scale for the placement of the bars
			    var x2 = d3.scale.linear()
				  .domain([xmin, xmax])
				  .range([0, width]);
				
			    var y = d3.scale.linear()
				  .domain([0, d3.max(histdata, function(d) { 
									return d; 
									})])
				  .range([height, 0]);

			    var xAxis = d3.svg.axis()
				  .scale(x2)
				  .tickValues(scope.data.breaks)
//				  .ticks(scope.data.breaks.length)
				  .tickFormat(d3.format("s"))
				  .orient("bottom");
			    
			    var yAxis = d3.svg.axis()
				  .scale(y)
				  .ticks(8)
				  .tickFormat(d3.format("s"))
				  .orient("left");

//			    var tip = d3.tip()
//				  .attr('class', 'd3-tip')
//				  .direction('e')
//				  .offset([0, 20])
//				  .html(function(d) {
//				    return '<table id="tiptable">' + d + "</table>";
//				});

			    // put the graph in the "mpg" div
			    var canvas = svg
//			      .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom))
				  .attr("width", width + margin.left + margin.right)
				  .attr("height", height + margin.top + margin.bottom)
				  .append("g")
				  .attr("transform", "translate(" + margin.left + "," + 
									margin.top + ")");

//			    svg.call(tip);

			    // set up the bars
			    var bar = canvas.selectAll(".bar")
				  .data(histdata)
				  .enter().append("g")
				  .attr("class", "bar")
				  .attr("transform", function(d, i) { return "translate(" + 
				       x2(scope.data.breaks[i]) + "," + y(d) + ")"; });
//				  .on('mouseover', tip.show)
//				  .on('mouseout', tip.hide);

			    // add rectangles of correct size at correct location
			    bar.append("rect")
				  .attr("x", x(binmargin))
				  .attr("width", x(binsize - 2 * binmargin))
				  .attr("height", function(d) { return height - y(d); });

			    // add the x axis and x-label
			    canvas.append("g")
				  .attr("class", "x axis")
				  .attr("transform", "translate(0," + height + ")")
				  .call(xAxis);
			    svg.append("text")
				  .attr("class", "x label")
				  .attr("text-anchor", "middle")
				  .attr("x", width / 2)
				  .attr("y", height + margin.bottom)
				  .text(scope.muiXLabel);

			    // add the y axis and y-label
			    canvas.append("g")
				  .attr("class", "y axis")
				  .attr("transform", "translate("+x(leftoffset)+",0)")
				  .call(yAxis);
			    svg.append("text")
				  .attr("class", "ylabel")
				  .attr("y", 0) // x and y switched due to rotation
				  .attr("x", -(height / 2))
				  .attr("dy", "1em")
				  .attr("transform", "rotate(-90)")
				  .style("text-anchor", "middle")
				  .text(scope.yLabel);		
			    
			    scope.sizeChanged = function($event, element){			    		
					console.debug("histogram wxh", root.width(), root.height());										
//					svg.attr("viewBox", "0 0 "+(root.width() + margin.left + margin.right)+" "+(root.height() + margin.top + margin.bottom));
					var el = root;
					console.debug("ANIMATE histogram sizeChanged", el.width(), el.height());
					var width = el.width() - margin.left - margin.right;
					var height = root.height() - margin.top - margin.bottom;
					var aspectRatio = new AspectRatio(width, height);
				    width = aspectRatio.width();
				    height = aspectRatio.height();
				    
					svg.attr("width", el.width());
					svg.attr("height", el.height());
					x.range([0, width]);
					x2.range([0, width]);
					y.range([height, 0]);
					canvas.select(".x.axis")
					.attr("transform", "translate(0," + height + ")")
					.call(xAxis);
					canvas.select(".y.axis").call(yAxis);
					canvas.selectAll(".bar")					  					  
					  .attr("transform", function(d, i) { return "translate(" + 
					       x2(scope.data.breaks[i]) + "," + y(d) + ")"; });
					canvas.selectAll("rect")					  					  
					  .attr("x", x(binmargin))
					  .attr("width", x(binsize - 2 * binmargin))
					  .attr("height", function(d) { return height - y(d); });
				};
				scope.$on("mui:dashboard:panel:rowMax", scope.sizeChanged);
				scope.$on("mui:dashboard:panel:rowMin", scope.sizeChanged);
				scope.$on("mui:dashboard:panel:max", scope.sizeChanged);
				scope.$on("mui:dashboard:panel:min", scope.sizeChanged);
			}
		};
	};
	RHistPlotDirective.$inject=[];
	RHistPlotDirective.$name="rHistPlotDirective";
	return RHistPlotDirective;
});