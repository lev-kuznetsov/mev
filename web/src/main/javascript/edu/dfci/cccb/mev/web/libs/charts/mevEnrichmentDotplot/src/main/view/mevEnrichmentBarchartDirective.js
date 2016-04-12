define(["lodash", "./mevEnrichmentBarchart.tpl.html"], function(_, template){"use strict";
	var directive = function mevEnrichmentDotplotDirective(){
		return {
			restrict: "AEC",
			scope: {
				config: "=mevEnrichmentDotplot"
			},
			template: template,
			controller: ["$scope", "mevDotplotNvd3Adaptor", function(scope, mevDotplotNvd3Adaptor, mevEnrcihmentDataAdaptor){

				var data = scope.config.data;
				scope.data=mevDotplotNvd3Adaptor(scope.config, data);

				var maxPvalueElm = _.maxBy(data, function(item){
					return item.getPValue();
				});

				if(maxPvalueElm)
					scope.maxPvalue = maxPvalueElm.getPValue();;

				var pValueScale = d3.scale.linear().domain([0, scope.maxPvalue || 1]).range(["blue", "yellow"]);
				scope.options = {
		            chart: {
		                type: 'dotPlotChart',
		                height: _.max([450, 10*scope.data.length]),
		                x: function(d){return d.getName();},
		                y: function(d){return d.getRatio();},
		                z: function(d){
		                	return d.getMatched();
		                },
		                showControls: false,
		                showValues: true,
		                duration: 500,
		                xAxis: {
		                    showMaxMin: false
		                },
		                valueFormat: function(d){
		                	return d3.format(',.4f')(d);
		                },
		                yAxis: {
		                    axisLabel: 'Ratio',
		                    tickFormat: function(d){
		                        return d3.format(',.4f')(d);
		                    }
		                },
		                barColor: function(d){
		                  return pValueScale(d.getPValue());
		                },
		                margin: {"left": 300},
		                tooltip: {
			                	contentGenerator: function(item) {
			                		// Format function for the tooltip values column.
								    var valueFormatter = function(d, i) {
								        return d;
								    };

								    // Format function for the tooltip header value.
								    var headerFormatter = function(d) {
								        return "" + ": " + d;
								    };

								    var keyFormatter = function(d, i) {
								        return d;
								    };

							        if (item === null) {
							            return '';
							        }

							        var table = d3.select(document.createElement("table"));
						            var theadEnter = table.selectAll("thead")
						                .data([item])
						                .enter().append("thead");

						            theadEnter.append("tr")
						                .append("td")
						                .attr("colspan", 3)
						                .append("strong")
						                .classed("id", true)
						                .html(item.data.getName());

						            
						        

							        var tbodyEnter = table.selectAll("tbody")
							            .data([item])
							            .enter().append("tbody");

							        var trowEnter = tbodyEnter.selectAll("tr")
							                .data(function(p) { return p.series;})
							                .enter()
							                .append("tr")
							                .classed("highlight", function(p) { return p.highlight;});

							        trowEnter.append("td")
							            .classed("legend-color-guide",true)
							            .append("div")
							            .style("background-color", function(p) { return p.color;});

							        trowEnter.append("td")
							            .classed("key",true)
							            .classed("total",function(p) { return !!p.total;})
							            .html(function(p, i) { return keyFormatter(p.key, i);});

							        trowEnter.append("td")
							            .classed("value",true)
							            .html(function(p, i) { return item.data.getMatched() + 
							            	", Match: " + item.data.getMatched() +
							            	", Total: " + item.data.getTotal() +
							            	", Ratio: " + item.data.getRatio();});

							        trowEnter.selectAll("td").each(function(p) {
							            if (p.highlight) {
							                var opacityScale = d3.scale.linear().domain([0,1]).range(["#fff",p.color]);
							                var opacity = 0.6;
							                d3.select(this)
							                    .style("border-bottom-color", opacityScale(opacity))
							                    .style("border-top-color", opacityScale(opacity))
							                ;
							            }
							        });

							        var html = table.node().outerHTML;
							        if (item.footer !== undefined)
							            html += "<div class='footer'>" + d.footer + "</div>";
							        return html;
							    }
			                },
		            }
		        };
			}],
			link: function(scope, elem, attr, ctrl){
				var pValueLegendConfig = {
					width: 130,
					height: 300
				};
				var d3root = d3.select(elem[0]);
				d3root.select(".controls svg").remove();
				var svgPValueLegend = d3.select("#pValueLegend").append("svg");
				svgPValueLegend.attr("width", pValueLegendConfig.width).attr("height", pValueLegendConfig.height);	

				var w = pValueLegendConfig.width, h = pValueLegendConfig.height;

				var key = svgPValueLegend;

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

				var y = d3.scale.linear().range([h - 100, 0]).domain([0, scope.maxPvalue]).nice();

				var yAxis = d3.svg.axis().scale(y).orient("right");

				key.append("g").attr("class", "y axis").attr("transform", "translate(31,10)")
				.call(yAxis)
				.append("text").attr("y", h-100).attr("dy", ".71em").style("text-anchor", "end").text("p-value");

			}
		};
	};
	directive.$name="mevEnrichmentDotplot";
	directive.$provider="directive";
	directive.$inject=["mevDotplotNvd3Adaptor"];
	return directive;
});
