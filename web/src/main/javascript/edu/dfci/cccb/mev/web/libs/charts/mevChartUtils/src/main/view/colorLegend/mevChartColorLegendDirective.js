define(["lodash", "d3", "./mevChartColorLegend.tpl.html", "./mevChartColorLegend.less"], function(_, d3, template){
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
                       width: 80,
                       height: 190,
                       margin: [30, 50],
                       color: {
                           available: [
                               {
                                   label: "Blue,Yellow",
                                   range: ["blue", "yellow"]
                               },
                               {
                                   label: "Red,Green",
                                   range: ['red', 'green'],
                               },
                               {
                                   label: "Red,Blue",
                                   range: ['red', 'blue']
                               }
                           ],
                           find: function(color){
                               if(_.isArray(color))
                                   return _.find(this.available, function(item){
                                       return _.isEqual(color, item.range);
                                   });
                               else
                                   return _.find(this.available, function(item){
                                       return color===item.label;
                                   });
                           }
                       }
                   }
               });


           }],
           link: function(scope, elem, attr, ctrl){
               scope.vm = {
                   updateColor: function updateColor($event){
                       console.debug("udpatecolor", scope.vm.color);
                       scope.config.updateColor(scope.vm.color);
                       this.draw(scope.config, d3.select(elem[0]));
                   },
                   color: scope.config.legend.color.find(scope.config.colors),
                   draw: function(config, d3root){
                       var colorLegendConfig = config.legend;
                       d3root.select("svg").remove();
                       var svgColorValueLegend = d3root.insert("svg", ":first-child");
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

                       legend.append("stop").attr("offset", "0%").attr("stop-color", this.color.range[1]).attr("stop-opacity", 1);
                       legend.append("stop").attr("offset", "100%").attr("stop-color", this.color.range[0]).attr("stop-opacity", 1);

                       key.append("rect").attr("width", w - marginh).attr("height", h - marginv).style("fill", "url(#gradient)").attr("transform", "translate(0,10)");

                       var y = d3.scale.linear().range([h - marginv, 0]).domain([config.min, config.max]).nice();

                       var yAxis = d3.svg.axis().scale(y).orient("right");

                       key.append("g").attr("class", "y axis").attr("transform", "translate(31,10)")
                           .call(yAxis)
                           .append("text").attr("y", h - marginv).attr("dy", ".71em").style("text-anchor", "end").text(config.label);
                   }
               }

               scope.vm.draw(scope.config, d3.select(elem[0]));
           }
       }
   };
    directive.$name = "mevChartColorLegend";
    directive.$provider = "directive";
    directive.$inject=[];
    return directive;
});