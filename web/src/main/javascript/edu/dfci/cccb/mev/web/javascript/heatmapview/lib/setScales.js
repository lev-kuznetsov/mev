deps = [];

define(deps, function(){
	
	return function() {
		
		var reference = this;
		
		this.cells.xScale.domain(reference.labels.column.values)
			.rangeRoundBands(
				[reference.params.panel.side.width,
				 reference.params.panel.side.width 
				 	+ (reference.labels.column.values.length * reference.params.cell.width)], 0, 0);
		
		this.cells.yScale.domain(reference.labels.row.values)
			.rangeRoundBands(
				[reference.params.panel.top.height
				 	+ reference.params.selections.column.height
				 	+ reference.params.labels.column.height , 
				 reference.params.panel.top.height
				 	+ reference.params.selections.column.height
				 	+ reference.params.labels.column.height + (reference.labels.row.values.length * reference.params.cell.height)], 0, 0);
		
		this.selections.column.xScale
			.domain(reference.labels.column.values)
			.rangeRoundBands(
					[reference.params.panel.side.width,
					 reference.params.panel.side.width
					 	+(reference.labels.column.values.length * reference.params.cell.width)], 0, 0);
		
		this.selections.column.yScale
			.domain(reference.selections.column.values.map(function(selection){
				return selection.name
			}))
			.rangeRoundBands(
					[reference.params.panel.top.height + reference.params.labels.column.height,
					 reference.params.panel.top.height + reference.params.labels.column.height
					 	+ reference.params.selections.column.height], 0, 0);

		this.selections.row.xScale
			.domain(reference.selections.row.values.map(function(selection){
				return selection.name
			}))
			.rangeRoundBands([reference.params.panel.side.width 
			                  	+ ( reference.labels.column.values.length * reference.params.cell.width),
			                  reference.params.panel.side.width
			                  	+ ( reference.labels.column.values.length * reference.params.cell.width)
			                  	+ reference.params.selections.row.width], 0, 0);
		
		this.selections.row.yScale
			.domain(reference.labels.row.values)
			.rangeRoundBands([reference.params.panel.top.height 
			                  	+ reference.params.labels.column.height
			                  	+ reference.params.selections.column.height,
            				  reference.params.panel.top.height 
			                  	+ reference.params.labels.column.height
			                  	+ reference.params.selections.column.height 
			                  	+ (reference.labels.row.values.length * reference.params.cell.height)], 0, 0);
		
		
	};

});
