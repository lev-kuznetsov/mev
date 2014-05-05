define([], function(){
   
  //updateCells :: ![Params] ![View], Object -> null
    //   Used to update shown cells using position object with
    //   top and height pixel properties
    return function(position) {
            
        var reference = this;
        
        var totalTop = this.params.labels.column.height
        + this.params.panel.top.height
        + this.params.selections.column.height;
        
        var heatmapCellHeight = this.params.cell.height;
        
        if (position.top < totalTop + (10 * heatmapCellHeight)) {
            
            var numRows = Math.floor( (position.top + position.height- totalTop ) / heatmapCellHeight ) 
                + 30
            var startRow = 0
            var endRow = numRows
        
        } else {
            var startRow = Math.floor( (position.top - totalTop) / heatmapCellHeight ) - 10
            var numRows =  ( (position.top + position.height - totalTop) / heatmapCellHeight) 
            var endRow = startRow + numRows + 30
        }
        
        
        var labels =  this.view.row.keys.slice(startRow, endRow);
        
        this.drawCells(labels)
        
    };
    
});