define(['d3'], function(d3){
	
	
	//generateScales :: [!row.values, !column.values] || Params[, Object] -> Scales
	//	-Sets scales by calling on view method with columns and rows 
	//   labels taking default default params object
	//
	return function(params, View) {
		
		var scales = Object.create(null);
		
		var self = View; //Only here cause I don't want to rewrite
		
		scales.cells = {
			xScale : d3.scale.ordinal().domain(self.column.values)
				.rangeRoundBands([params.panel.side.width,
				    params.panel.side.width 
				 	+ (self.column.values.length * params.cell.width)], 0, 0),
			yScale : d3.scale.ordinal().domain(self.row.values)
				.rangeRoundBands([params.panel.top.height
					 	+ params.selections.column.height
					 	+ params.labels.column.height , 
					 params.panel.top.height
					 	+ params.selections.column.height
					 	+ params.labels.column.height 
					 	+ (self.row.values.length * params.cell.height)], 0, 0),
			colorScale : d3.scale.linear().domain([self.expression.min, self.expression.avg, self.expression.max])
					.range([params.colors.low, params.colors.mid, params.colors.high])
			
			
		}
		
		return scales;
		
	};

});
