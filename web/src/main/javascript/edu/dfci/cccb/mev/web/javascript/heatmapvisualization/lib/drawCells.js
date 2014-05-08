define(['./cellFilter', 'd3', 'qtip'], function(cellFilter, d3, qtip){
	
	//drawCells !View, !shownCells, !scales, Array [String], -> null
	//	draws cells on heatmapvisualization object
	return function(labels, ds){
		var self = this;

		var labelPairs = [];
		
		labels.row.map(function(row){
		    return labels.column.map(function(col){
		        labelPairs.push([row, col]);
		    })
		});
		
		self.shownCells = labelPairs.map(function(pair){ return ds.expression.get(pair)});
		
		
		
		var newCells = self.DOM.heatmapCells.selectAll('rect').data(self.shownCells, function(k){
			return [k.row, k.column]
		})
		
		
		
		newCells.enter().append('rect')
			.attr({
				x : function(d){ return self.scales.cells.xScale(d.column) },
				y : function(d){ return self.scales.cells.yScale(d.row) },
				height: self.params.cell.height - self.params.cell.padding,
				width: self.params.cell.width - self.params.cell.padding,
				fill: function(d){return self.scales.cells.colorScale(d.value) }
				
			})
		
		newCells.exit().remove()
		
		var rowLabels = self.DOM.labels.row.selectAll('text').data(labels.row, function(k){return k}),
		colLabels = self.DOM.labels.column.selectAll('text').data(labels.column, function(k){return k});
			
		rowLabels.enter().append('text')
            .attr({
            	x: self.params.panel.side.width
              	+ ( self.view.labels.column.keys.length * self.params.cell.width)
              	+ self.params.selections.row.width,
            	y: function(d){return self.scales.cells.yScale(d) + self.params.cell.height },
            	"text-anchor": "bottom"
            })
            .text(function(d){
            	return d
            })
            .append('title').text(function(d){ return d });
		
		colLabels.enter().append('text')
		    .attr('transform', 'rotate(-90)')
            .attr({
                x: - (self.params.panel.top.height
                + self.params.labels.column.height),
                y: function(d){return  self.scales.cells.xScale(d) + .68*self.params.cell.width },
                "text-anchor": "start"
            })
            
            .text(function(d){
                return d.slice(0, 13);
            })
            .append('title').text(function(d){ return d });
        
        rowLabels.exit().remove();
		
		colLabels.exit().remove();
		
		//Legend stuff
		var rands = d3.range(self.view.expression.min, self.view.expression.max, 
				Math.round((self.view.expression.max + Math.abs(self.view.expression.min))/100));
		
		var labelXScale = d3.scale.linear().domain([0, rands.length-1])
			.range([d3.min(self.scales.cells.xScale.range()),
					 d3.max(self.scales.cells.xScale.range())]);
		
		var labelColorScale = d3.scale.linear().domain([self.view.expression.min, self.view.expression.avg, self.view.expression.max])
			.range(self.scales.cells.colorScale.range())
			
		self.DOM.legend.selectAll("rect").remove()
		self.DOM.legend.selectAll("rect").data(rands).enter().append("rect")
		.attr({
			x : function(d, i){ return labelXScale(i)},
			y : function(d){ return self.params.legend.height *.5 },
			height: self.params.legend.height *.5,
			width: labelXScale(1) - labelXScale(0)+1,
			fill: function(d){return labelColorScale (d) }
			
		})
		
		self.DOM.legend.selectAll("text").data(rands).enter()
        .append("text")
        .attr({
            x: function(d,i){return labelXScale(i)},
            y:self.params.legend.height *.45,
            'style':'font-size:10',
            'text-anchor':'middle'
        })
        .text(function(d, i){
        	
            var returnstring = String(d).split(".")[0]
            if (returnstring.length > 1){
                returnstring = returnstring + "." + String(d).split(".")[1].slice(0,3)
            }
            
            ticks = [0, Math.floor((rands.length-1)*.25), Math.floor((rands.length-1)*.5), Math.floor((rands.length-1)*.75), rands.length-1]
            
            return (ticks.indexOf(i)>-1)? returnstring :""
        }).append("title")
            .text(function(d, i){
                return d;
            })
		
	}
})