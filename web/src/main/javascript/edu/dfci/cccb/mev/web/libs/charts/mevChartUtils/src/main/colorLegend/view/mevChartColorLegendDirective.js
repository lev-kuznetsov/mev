define(["lodash", "d3", "./mevChartColorLegend.tpl.html", "./mevChartColorLegend.less"], function(_, d3, template){ "use strict";
   var directive = function(){
       return {
           restrict: "AEC",
           scope: {
            config: "=?mevChartColorLegend"
           },
           template: template,
           controller: ["$scope", function(scope){
               if(!scope.config)
                   throw new Error("Config is undefined");
               _.extend(scope.config, {
                   legend: {
                       width: 150,
                       height: 190,
                       margin: [30, 120],
                   }
               });
           }],
           link: function(scope, elem, attr, ctrl){
               scope.vm = {
                   updateColor: function updateColor(broadcast){
                       scope.config.updateColor(scope.vm.color);
                       this.draw(scope.config, d3.select(elem[0]));
                   },
                   color: scope.config.colors,
                   draw: function(config, d3root){
                       var colorLegendConfig = config.legend;
                       d3root.select("svg").remove();
                       var svgColorValueLegend = d3root.insert("svg", ":first-child");
                       // var svgColorValueLegend = d3root.append("svg");
                       svgColorValueLegend.attr("width", colorLegendConfig.width).attr("height", colorLegendConfig.height);

                       var w = colorLegendConfig.width, h = colorLegendConfig.height;
                       var marginv = _.isArray(colorLegendConfig.margin) ? colorLegendConfig.margin[0] : colorLegendConfig.margin;
                       var marginh = _.isArray(colorLegendConfig.margin) ? colorLegendConfig.margin[1] : colorLegendConfig.margin;
                       var key = svgColorValueLegend;
                       var legend = key.append("defs").append("svg:linearGradient")
                           .attr("id", "gradient")
                           .attr("x1", "100%")
                           .attr("y1", "0%")
                           .attr("x2", "100%")
                           .attr("y2", "100%")
                           .attr("spreadMethod", "pad");

                       legend.append("stop").attr("offset", "0%").attr("stop-color", config.colors.range[1]).attr("stop-opacity", 1);
                       legend.append("stop").attr("offset", "100%").attr("stop-color", config.colors.range[0]).attr("stop-opacity", 1);

                       key.append("rect").attr("width", w - marginh).attr("height", h - marginv).style("fill", "url(#gradient)").attr("transform", "translate(0,10)");

                       var y = d3.scale.linear().range([h - marginv, 0]).domain([config.min, config.max]).nice();

                       var yAxis = d3.svg.axis().scale(y).orient("right");

                       key.append("g").attr("class", "y axis").attr("transform", "translate(31,10)")
                           .call(yAxis)
                           .append("text").attr("y", h - marginv).attr("dy", ".71em").style("text-anchor", "end").text(config.label);
                       
                   }
               };
               scope.saveAsConfig = {
                   name: scope.config.chartConfig ? scope.config.chartConfig.name : "mev-chart.png",
                   selector: 'nvd3 svg'
               };
               scope.$on("mui:charts:color:updated", function($event, source, color){
                   if(source!==scope.config){
                       scope.vm.draw(scope.config, d3.select(elem[0]));
                       scope.vm.color = scope.config.colors;
                   }
               });
               scope.vm.draw(scope.config, d3.select(elem[0]));
           }
       }
   };
    directive.$name = "mevChartColorLegend";
    directive.$provider = "directive";
    directive.$inject=[];
    return directive;
});