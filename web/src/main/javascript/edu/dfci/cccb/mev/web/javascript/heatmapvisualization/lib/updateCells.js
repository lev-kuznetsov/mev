define([], function(){
   
  //updateCells :: !params(Params), !view(View), Object -> null
    //   Used to update shown cells using position object with
    //   top and height pixel properties
    return function(position, dataset) {
            
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

        var labels = {
                row: self.view.labels.row.keys.slice(startRow, endRow),
                column: self.view.labels.column.keys
        }
        this.drawCells(labels, dataset)
        
    };
    
});