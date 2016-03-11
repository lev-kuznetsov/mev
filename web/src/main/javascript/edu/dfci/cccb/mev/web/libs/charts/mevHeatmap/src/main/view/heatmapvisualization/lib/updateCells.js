define([], function(){
   
  //updateCells :: !params(Params), !view(View), Object -> null
    //   Used to update shown cells using position object with
    //   top and height pixel properties
    return function(position, dataset, options) {
            
        var self = this;
        var totalTop = this.params.labels.column.height
        + this.params.panel.top.height
        + this.params.selections.column.height;
        
        var heatmapCellHeight = this.params.cell.height;
        
        if (position.top < totalTop + (100 * heatmapCellHeight)) {
            
            var numRows = Math.floor( (position.top + position.height- totalTop ) / heatmapCellHeight ) 
                + 30
            var startRow = 0;
            var endRow = numRows;
        
        } else {
        	
            var startRow = Math.floor( (position.top - totalTop) / heatmapCellHeight ) - 10
            var numRows =  ( ( position.height) / heatmapCellHeight) 
            var endRow = startRow + numRows + 30
        }

        var totalLeft = this.params.panel.side.width
        + this.params.selections.row.width;
        if (position.left < totalLeft + (100 * heatmapCellHeight)) {
            
            var numCols = Math.floor( (position.left + position.width - totalLeft ) / heatmapCellHeight ) 
                + 30
            var startCol = 0;
            var endCol = numCols;
        
        } else {
        	
            var startCol = Math.floor( (position.left - totalLeft) / heatmapCellHeight ) - 10
            var numCols =  ( ( position.left) / heatmapCellHeight) 
            var endCol = startCol + numCols + 30
        }
        
        var labels = {
                row: self.view.labels.row.keys.slice(startRow, endRow),
//                column: self.view.labels.column.keys
                column: self.view.labels.column.keys.slice(startCol, endCol),
        }
        this.drawCells(labels, dataset, options && options.force);
        
    };
    
});