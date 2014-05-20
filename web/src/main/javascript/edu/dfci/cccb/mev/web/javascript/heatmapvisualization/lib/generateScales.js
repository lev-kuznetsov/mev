define(['d3'], function(d3){
	
	
	//generateScales :: [!row.keys, !column.keys] || Params[, Object] -> Scales
	//	-Sets scales by calling on view method with columns and rows 
	//   labels taking default default params object
	//
	return function(params, view) {
		
		var scales = Object.create(null);
		
		scales.cells = {
			xScale : d3.scale.ordinal().domain(view.labels.column.keys)
				.rangeRoundBands([params.panel.side.width,
				    params.panel.side.width 
				 	+ (view.labels.column.keys.length * params.cell.width)], 0, 0),
			yScale : d3.scale.ordinal().domain(view.labels.row.keys)
				.rangeRoundBands([ params.panel.top.height
					 	+ params.selections.column.height
					 	+ params.labels.column.height , 
					 params.panel.top.height
					 	+ params.selections.column.height
					 	+ params.labels.column.height 
					 	+ (view.labels.row.keys.length * params.cell.height)], 0, 0),
			colorScale : d3.scale.linear().domain([view.expression.min, view.expression.avg, view.expression.max])
					.range([params.colors.low, params.colors.mid, params.colors.high])
			
			
		}
		
		scales.panel = {
		   top : {
		       xScale : d3.scale.linear().domain([0, 1]).range([params.panel.side.width,
		                                                                  params.panel.side.width 
		                                                                  + (view.labels.column.keys.length * params.cell.width)]),
		       yScale : d3.scale.linear().domain([0, 1]).range([5, params.panel.top.height]),
		   },
		   side : {
		       xScale :d3.scale.linear().domain([0, 1]).range([4, params.panel.side.width]),
		       yScale :d3.scale.linear().domain([0, 1]).range([params.panel.top.height
		                                                                 + params.selections.column.height
		                                                                 + params.labels.column.height , 
		                                                              params.panel.top.height
		                                                                 + params.selections.column.height
		                                                                 + params.labels.column.height 
		                                                                 + (view.labels.row.keys.length * params.cell.height)]),
		   }
		}
		
		return scales;
		
	};

});
