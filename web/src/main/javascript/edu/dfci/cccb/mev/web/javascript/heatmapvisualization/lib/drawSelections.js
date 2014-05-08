define(['d3'], function(d3){

    //drawSelections :: ![HeatmapVisualization] Array[Selection] --> null
    //  Takes selections object and draws it on the heatmap visualization
    return function(selections, dimension){
        
        var self = this; //HeatmapVisualization
        
        //clear window
        self.DOM.selections[dimension].selectAll('*').remove();
        
        
        var groupScale = d3.scale.ordinal().domain(selections.map(function(group){return group.name}))
        
        if (dimension == 'column') {
            groupScale.rangeRoundBands([self.params.panel.top.height + self.params.labels.column.height,
                                        self.params.panel.top.height + self.params.labels.column.height
                                        + self.params.selections.column.height], 0, 0);
        } else {
            groupScale.rangeRoundBands([self.params.panel.side.width 
                                        + ( self.view.labels.column.keys.length * self.params.cell.width),
                                        self.params.panel.side.width
                                          + ( self.view.labels.column.keys.length * self.params.cell.width)
                                          + self.params.selections.row.width], 0, 0);
        }
        
        var cells = []; 
        //build selections array from selections array
        //for each selection
        selections.map(function(selection){
          //for each key in selection
            selection.keys.map(function(key){
              //make object with selection group, label, and color and push to cells
              cells.push({group: selection.name, label: key, color: selection.properties.selectionColor})
            })
        })
                
        
        //bind cells to Dom
        var selectionsCells = self.DOM.selections[dimension].selectAll('rect').data(cells, function(d){return [d.group, d.label]})
        //draw enter selection
        selectionsCells.enter()
            .append('rect')
                .attr({
                    'x':function(d){
                        return (dimension == 'column') ? self.scales.cells.xScale(d.label) : groupScale(d.group)
                    },
                    'y':function(d){
                        return (dimension == 'column') ? groupScale(d.group) : self.scales.cells.yScale(d.label) 
                    },
                    'fill':function(d){
                        return d.color
                    },
                    'height': (dimension == 'column') ?  groupScale.rangeBand() : self.scales.cells.yScale.rangeBand(),
                    'width': (dimension == 'column') ? self.scales.cells.xScale.rangeBand() : groupScale.rangeBand()
                })
        //remove exit selection
        selectionsCells.exit().remove()
        
        return null
    };
    
});