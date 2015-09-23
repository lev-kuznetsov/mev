define(['d3', 
        './generateScales', 
        './drawCells', 
        './drawSelections', 
        './drawAnalysis', 
        './updateCells', 
        './drawTree', 
        './drawCluster', 
        './drawTopPanel', 
        './drawSidePanel'], 
function(d3, generateScales, drawCells, drawSelections, 
		drawAnalysis, updateCells, drawTree, drawCluster, 
		drawTopPanel, drawSidePanel){
	
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
				+ (View.labels.row.keys.length * params.cell.height ) + 50 );
    	
		svg.attr('width', 
				params.panel.side.width
				+ params.labels.row.width
				+ params.selections.row.width
				+ (View.labels.column.keys.length * params.cell.width) + 50 )

		this.DOM.heatmapCells = this.DOM.svg.append('g').attr("id", "heatmap-Cells");
//		this.DOM.heatmapCells = d3.select('#heatmap-Cells');
		
		this.DOM.sidePanel = this.DOM.svg.append('g').attr("id", "side-Panel");
//		this.DOM.sidePanel = d3.select('#side-Panel');
		
		this.DOM.topPanel = this.DOM.svg.append('g').attr("id", "top-Panel");
//		this.DOM.topPanel = d3.select('#top-Panel');
		
//		this.DOM.svg.append('g').attr("id", "column-Selections");
//		this.DOM.svg.append('g').attr("id", "row-Selections");
		
		this.DOM.selections = {
			column : this.DOM.svg.append('g').attr("id", "column-Selections"),
			row : this.DOM.svg.append('g').attr("id", "row-Selections")
		}
		
//		this.DOM.svg.append('g').attr("id", "column-Labels");
//		this.DOM.svg.append('g').attr("id", "row-Labels");
		
		this.DOM.labels = {
			column : this.DOM.svg.append('g').attr("id", "column-Labels"),
			row : this.DOM.svg.append('g').attr("id", "row-Labels")
		};
		
		this.DOM.legend = this.DOM.svg.append('g').attr("id", 'heatmap-Legend');
//		this.DOM.legend = d3.select("#heatmap-Legend")
		
		this.params = params;
		
		this.scales = generateScales(params, View);
		
		
		this.view = View;
		
		this.drawCells = drawCells;
		this.drawSelections = drawSelections;
		this.drawAnalysis = drawAnalysis;
		this.drawTree = drawTree;
		this.drawCluster = drawCluster;
		this.drawTopPanel = drawTopPanel;
		this.drawSidePanel = drawSidePanel;
		
		this.updateCells = updateCells;

	
	}
});
