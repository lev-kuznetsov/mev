define(["mui", "d3"], function(ng, d3){
    var service = function (){
        return function mevTooltipContent(config, item){
            if (item === null) return '';
            _.extend(config, {
                tooltip: {
                    title: function(config, item){
                        var key = config.x.label;
                        if(!key && _.isString(config.x.field)) key = config.x.field;
                        return (key ? key + ": " :  "") + config.x.get(item.data);
                    }
                }
            });

            function keyFormatter(d, i) { return d;};

            function isInTitle(sTitle, dimConfig, item){
                return sTitle.indexOf(dimConfig.get(item.data)) >= 0;
            }
            function findColorDim(config){
                var colorDim = _.find(config, function(item, key){
                    //it is a color dimension if it has a "colors" attribute
                    // or if it's under the "color" key
                    return item.colors || key === "color";
                })
                return colorDim;
            }
            function addDimRow(tbodyEnter, dimConfig, item){
                var trowEnter = tbodyEnter.selectAll("tr")
                    .data(function(p) { return p.series;})
                    .append("tr")
                    .classed("highlight", function(p) { return p.highlight;});

                trowEnter.append("td")
                    .append("div");

                trowEnter.append("td")
                    .classed("key",true)
                    .classed("total",function(p) { return !!p.total;})
                    .html(function(p, i) {
                            return keyFormatter(dimConfig.label, i)
                                + ": " + dimConfig.get(item.data);
                        }
                    );
            }
            function addFieldRow(tbodyEnter, item, field, key){
                var trowEnter = tbodyEnter.selectAll("tr")
                    .data(function(p) { return p.series;})
                    .append("tr")
                    .classed("highlight", function(p) { return p.highlight;});

                trowEnter.append("td")
                    .append("div");

                trowEnter.append("td")
                    .classed("key",true)
                    .classed("total",function(p) { return !!p.total;})
                    .html(function(p, i) {
                            return key + ": " + ( _.isFunction(field) ? field.call(item.data, item.data) : item.data[field]);
                        }
                    );
            }

            var tooltip = d3.select(document.createElement("div"))
                .data([item])
                .classed("mev-tooltip", true);

            //show the title
            var title = tooltip.append("div");
            title.classed("tooltip-title", true)
                .html(config.tooltip.title(config, item));

            var table = tooltip.append("table");
            var tbodyEnter = table.selectAll("tbody")
                .data([item])
                .enter().append("tbody");

            //under the title we show the color dimension first
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
                .html(function(p, i) {
                        var colorDim = findColorDim(config);
                        return colorDim ?
                            keyFormatter(colorDim.label, i) + ": " + colorDim.get(item.data)
                            : keyFormatter(p.key, i);
                    }
                );

            //now show the dimension data
            var sTitle = title.node().innerHTML;
            var colorDim = findColorDim(config);
            if(!isInTitle(sTitle, config.x, item)) addDimRow(tbodyEnter, config.x, item);
            if(!isInTitle(sTitle, config.y, item)) addDimRow(tbodyEnter, config.y, item);
            if(config.z && config.z !== colorDim && !isInTitle(sTitle, config.z, item)) addDimRow(tbodyEnter, config.z, item);
            if(config.size && config.size !== colorDim && !isInTitle(sTitle, config.size, item)) addDimRow(tbodyEnter, config.size, item);

            //finally append any additional fields
            var fields = config.tooltip ? config.tooltip.fields : {};
            _.forEach(fields, addFieldRow.bind(null, tbodyEnter, item));
            // trowEnter.append("td")
            // 	.classed("value",true)
            // 	.html(function(p, i) {
            // 		var fields = config.tooltip ? config.tooltip.fields : {};
            // 		return _.reduce(fields,
            // 			function(html, field, key){
            // 				return html += ", " + key
            // 					+ ": " +( _.isFunction(field) ? field.call(item.data, item.data) : item.data[field]);
            // 			},
            // 			config.y.label + ": " + config.y.get(item.data)
            // 			+ ", " + config.size.label + ": " + config.size.get(item.data));
            // 	});

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

            var html = tooltip.node().outerHTML;
            if (item.footer !== undefined)
                html += "<div class='footer'>" + d.footer + "</div>";
            return html;
        }


    };
    service.$name="mevTooltipContent";
    service.$inject=[];
    service.$provider="factory";
    return service;
})