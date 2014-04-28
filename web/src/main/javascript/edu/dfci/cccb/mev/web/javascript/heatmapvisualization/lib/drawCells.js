define(['./cellFilter'], function(cellFilter){
	
	//drawCells !View, !shownCells, !scales, Array [String], -> null
	//	draws cells on heatmapvisualization object
	return function(labels){
		var self = this;
		
		self.shownCells = cellFilter(labels, self.view);
		
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
		
		var labels = self.DOM.labels.row.selectAll('text').data(labels, function(k){return k})
			
		labels.enter().append('text')
				.attr({
					x: self.params.panel.side.width
	              	+ ( self.view.column.values.length * self.params.cell.width)
	              	+ self.params.selections.row.width,
					y: function(d){return self.scales.cells.yScale(d) + self.params.cell.height },
					"text-anchor": "bottom"
				})
				.text(function(d){
					return d
				});
		
		labels.exit().remove()
	}
})