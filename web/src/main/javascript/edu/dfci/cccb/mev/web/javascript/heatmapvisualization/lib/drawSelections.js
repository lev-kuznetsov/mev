define(['d3'], function(d3){

    //drawSelections :: ![HeatmapVisualization] Array[Selection] --> null
    //  Takes selections object and draws it on the heatmap visualization
    return function(selections, dimension){
        
        var self = this; //HeatmapVisualization
        
        //clear window
        self.DOM.selections[dimension].selectAll('*').remove()
        
        var groupScale = d3.scale().ordinal().domain(selections.map(function(group){return group.name}))
        
        if (dimension == 'column') {
            groupScale.rangeRoundBands([self.params.panel.top.height + self.params.labels.column.height,
                                        self.params.panel.top.height + self.params.labels.column.height
                                        + self.params.selections.column.height], 0, 0);
        } else {
            groupScale.rangeRoundBands([params.panel.side.width 
                                        + ( self.view.column.values.length * self.params.cell.width),
                                        params.panel.side.width
                                          + ( self.view.column.values.length * self.params.cell.width)
                                          + self.params.selections.row.width], 0, 0);
        }
        
        var cells = []; 
        //build selections array from selections array
        //for each selection
        selections.map(function(selection){
          //for each key in selection
            selection.keys.map(function(key){
              //make object with selection group, label, and color and push to cells
              cells.push({group: selection.name, label: key, color: selection.color})
            })
        })
            
                
        
        //bind cells to Dom
        var selectionsCells = self.DOM.selections[dimension].selectAll('rect').data(cells, function(d){return [d.group, d.label]})
        //draw enter selection
        selectionsCells.enter()
            .append('rect')
                .attr({
                    'x':function(d){
                        return (dimension == 'column') ? self.scales.cell.xScale(d.label) : groupScale(d.group)
                    },
                    'y':function(d){
                        return (dimension == 'column') ? groupScale(d.group) : self.scales.cell.yScale(d.label) 
                    },
                    'color':function(d){
                        return d.color
                    },
                    'height':
                    'width':
                })
        //remove exit selection
        selectionCells.exit().remove()
        
        return null
    };
    
});