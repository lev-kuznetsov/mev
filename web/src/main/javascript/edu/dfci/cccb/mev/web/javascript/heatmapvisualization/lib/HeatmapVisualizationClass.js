define(['d3', './generateScales', './drawCells', './drawSelections', './updateCells'], 
function(d3, generateScales, drawCells, drawSelections, updateCells){
	
	//Constructor :: View, D3selection, scrollableSelection, Params
	//	-> HeatmapVisualization
	return function(View, svg, params) {

		var self = this;
		
		this.DOM = {
			svg: svg
		}
		
		this.DOM.svg.selectAll("*").remove();
		
    	svg.attr('height', 
				params.panel.top.height
				+ params.labels.column.height
				+ params.selections.column.height
				+ (View.row.values.length * params.cell.height ) + 50 );
    	
		svg.attr('width', 
				params.panel.side.width
				+ params.labels.row.width
				+ params.selections.row.width
				+ (View.column.values.length * params.cell.width) + 50 )

		this.DOM.svg.append('g').attr("id", "heatmap-Cells");
		this.DOM.heatmapCells = d3.select('#heatmap-Cells');
		
		this.DOM.svg.append('g').attr("id", "side-Panel");
		this.DOM.sidePanel = d3.select('#side-Panel');
		
		this.DOM.svg.append('g').attr("id", "top-Panel");
		this.DOM.topPanel = d3.select('#top-Panel');
		
		this.DOM.svg.append('g').attr("id", "column-Selections");
		this.DOM.svg.append('g').attr("id", "row-Selections");
		
		this.DOM.selections = {
			column : d3.select('#column-Selections'),
			row : d3.select('#row-Selections')
		}
		
		this.DOM.svg.append('g').attr("id", "column-Labels");
		this.DOM.svg.append('g').attr("id", "row-Labels");
		
		this.DOM.labels = {
			column : d3.select('#column-Labels'),
			row : d3.select('#row-Labels')
		};
		
		this.params = params;
		
		this.scales = generateScales(params, View);
		
		this.view = View;
		
		this.drawCells = drawCells;
		this.drawSelections = drawSelections;
		
		this.updateCells = updateCells;

	
	}
});
