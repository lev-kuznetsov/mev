define(['d3'], function(d3){
	return function(svg, scrollable, params) {
		
	//returns view layer object with bindings to access and modify view.
	//Needs svg selection object on init
		
		return {
			params: params,
        	labels : {
        		row : []
        	},
        	cells : [],
        	position : {
        		top : scrollable.scrollTop(),
                height : scrollable.height(),
            },
        	updateRows: function(fullLabels) {
                
        		var reference = this;
        		var totalTop = this.params.labels.column.height
        		+ this.params.panel.top.height
        		+ this.params.selections.column.height;
        		
        		var heatmapCellHeight = this.params.cell.height;
        		
                if (reference.position.top < totalTop + (10 * heatmapCellHeight)) {
                    
                    var numRows = Math.floor( (reference.position.top + reference.position.height- totalTop ) / heatmapCellHeight ) 
                    	+ 30
                    var startRow = 0
                    var endRow = numRows
                
                } else {
                    var startRow = Math.floor( (reference.position.top - totalTop) / heatmapCellHeight ) - 10
                    var numRows =  ( (reference.position.top + reference.position.height - totalTop) / heatmapCellHeight) 
                    var endRow = startRow + numRows + 30
                }
                
                
                this.labels.row =  fullLabels.slice(startRow, endRow) ;
                
            },
            d3Selections: {
            	svg: svg
            },
            setSVG : function (fullLabels){
            	
            	var reference = this;
            	
            	this.d3Selections.svg.selectAll("*").remove();
        		
            	this.d3Selections.svg.attr('height', 
        				reference.params.panel.top.height
        				+ reference.params.labels.column.height
        				+reference.params.selections.column.height
        				+(fullLabels.length * reference.params.cell.height ) + 50 )
        		this.d3Selections.svg.attr('width', 
        				reference.params.panel.side.width
        				+ reference.params.labels.row.width
        				+ reference.params.selections.row.width
        				+ (fullLabels.length * reference.params.cell.width) + 50 )
        		
		
				this.d3Selections.svg.append('g').attr("id", "heatmap-Cells");
        		this.d3Selections.heatmapCells = d3.select('#heatmap-Cells');
				
				this.d3Selections.svg.append('g').attr("id", "side-Panel");
				this.d3Selections.sidePanel = d3.select('#side-Panel');
				
				svg.append('g').attr("id", "top-Panel");
				this.d3Selections.topPanel = d3.select('#top-Panel');
				
				svg.append('g').attr("id", "column-Selections")
				this.d3Selections.columnSelections = d3.select('#column-Selections');
				
				svg.append('g').attr("id", "column-Labels")
				this.d3Selections.columnLabels = d3.select('#column-Labels');
				
				svg.append('g').attr("id", "row-Selections")
				this.d3Selections.rowSelections = d3.select('#row-Selections');
				
				svg.append('g').attr("id", "row-Labels")
				this.d3Selections.rowLabels = d3.select('#row-Labels');
                				
        		this.updateRows(fullLabels);
        	}
        };
	
	}
});
