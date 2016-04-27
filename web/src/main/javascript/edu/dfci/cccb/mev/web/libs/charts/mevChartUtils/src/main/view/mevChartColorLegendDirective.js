define(["lodash", "d3"], function(_, d3){
   var directive = function(){
       return {
           restrict: "AEC",
           scope: {
            config: "=?mevChartColorLegend"
           },
           link: function(scope, elem, attr, ctrl){
               if(!scope.config)
                   throw new Error("Config is undefined");

               _.extend(scope.config, {
                   legend: {
                       width: 130,
                       height: 300
                   }
                });

               var colorLegendConfig = scope.config.legend;
               var d3root = d3.select(elem[0]);
               d3root.select("svg").remove();
               var svgColorValueLegend = d3root.append("svg");
               svgColorValueLegend.attr("width", colorLegendConfig.width).attr("height", colorLegendConfig.height);

               var w = colorLegendConfig.width, h = colorLegendConfig.height;
               var key = svgColorValueLegend;
               var legend = key.append("defs").append("svg:linearGradient")
                   .attr("id", "gradient")
                   .attr("x1", "100%")
                   .attr("y1", "0%")
                   .attr("x2", "100%")
                   .attr("y2", "100%")
                   .attr("spreadMethod", "pad");

               legend.append("stop").attr("offset", "0%").attr("stop-color", "yellow").attr("stop-opacity", 1);
               legend.append("stop").attr("offset", "100%").attr("stop-color", "blue").attr("stop-opacity", 1);

               key.append("rect").attr("width", w - 100).attr("height", h - 100).style("fill", "url(#gradient)").attr("transform", "translate(0,10)");

               var y = d3.scale.linear().range([h - 100, 0]).domain([scope.config.min, scope.config.max]).nice();

               var yAxis = d3.svg.axis().scale(y).orient("right");

               key.append("g").attr("class", "y axis").attr("transform", "translate(31,10)")
                   .call(yAxis)
                   .append("text").attr("y", h-100).attr("dy", ".71em").style("text-anchor", "end").text(scope.config.label);
           }
       }
   };
    directive.$name = "mevChartColorLegend";
    directive.$provider = "directive";
    directive.$inject=[];
    return directive;
});