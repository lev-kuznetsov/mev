define(['./cellFilter', 'd3', 'q', "jquery"], function(cellFilter, d3, q, $){
	
	//drawCells !View, !shownCells, !scales, Array [String], -> null
	//	draws cells on heatmapvisualization object
	return function(labels, ds, repaintAll){
		var self = this;

		var labelPairs = [];
		self.shownCells = []
		labels.row.map(function(row){
		    return labels.column.map(function(col){
		        labelPairs.push([row, col]);
		        self.shownCells.push({
		        	column: col,
		        	row: row,
		        	value: NaN
		        })
		    });
		});
		
		ds.expression.getSome(self.shownCells).then(function(shownCells){
			// DATA JOIN
			//Join new data with old elements, if any.
			var domCells = self.DOM.heatmapCells.selectAll('rect');
			var allCells = domCells.data(shownCells, function(k){
				return k.row+";"+k.column;
			});

			// UPDATE
			// Update old elements as needed here 
			//	... nothing to update for existing cells
			
			// ENTER
			// Create new elements as needed.
			var newCells = allCells.enter();
			
			var newDom = newCells.append('rect')
				.attr({
					x : function(d){ return self.scales.cells.xScale(d.column);},
					y : function(d){ return self.scales.cells.yScale(d.row);},
					height: self.params.cell.height - self.params.cell.padding,
					width: self.params.cell.width - self.params.cell.padding,
					fill: function(d){ return self.scales.cells.colorScale(d.value);},
//					fill: function(d){
//						var node = this;
//						ds.expression.tryGet([d.row, d.column]).then(function(value){						
//							node.setAttribute("fill", self.scales.cells.colorScale(value));
//						});
////						return self.scales.cells.colorScale(d.value);
//						return "";
//					},
					'cell-value': function(d) { return d.value;},
					'cell-column': function(d) { return d.column;},
					'cell-row': function(d) { return d.row;},
			});
			console.debug("swap:", newDom.size());
			
//			newCells.attr({fill: "blue"});
			// ENTER + UPDATE
			// Appending to the enter selection expands the update selection to include
			// entering elements; so, operations on the update selection after appending to
			// the enter selection will apply to both entering and updating nodes.
			//	... nothing to update for all cells
			if(repaintAll)
				domCells.attr({fill: function(d){ return self.scales.cells.colorScale(d.value);}});
//				domCells.attr({fill: "blue"});
			
	        // EXIT
	        // Remove old elements as needed.
			var deleteCells = allCells.exit(); 
			deleteCells.remove();
		})["catch"](function(e){
			throw e;
		});
//		self.shownCells = labelPairs.map(function(pair){ return ds.expression.get(pair);});
		
//		ds.expression.getAll(labelPairs).then(function(shownCells){
//			
//		});
//		
		
		
		var rowLabels = self.DOM.labels.row.selectAll('text').data(labels.row, function(k){return k;}),
		colLabels = self.DOM.labels.column.selectAll('text').data(labels.column, function(k){return k;});
		
		rowLabels.enter().append('text')
            .attr({
            	x: self.params.panel.side.width
              	+ ( self.view.labels.column.keys.length * self.params.cell.width)
              	+ self.params.selections.row.width,
            	y: function(d){return self.scales.cells.yScale(d) + self.params.cell.height;},
            	"text-anchor": "bottom"
            })
            .text(function(d){
            	return d;
            })
            .append('title').text(function(d){ return d;});
		
		colLabels.enter().append('text')
		    .attr('transform', 'rotate(-90)')
            .attr({
                x: - (self.params.panel.top.height
                + self.params.labels.column.height),
                y: function(d){return  self.scales.cells.xScale(d) + .68*self.params.cell.width;},
                "text-anchor": "start"
            })
            
            .text(function(d){
                return d.slice(0, 13);
            })
            .append('title').text(function(d){ return d });
		
        rowLabels.exit().remove();

		colLabels.exit().remove();
		
		
		//Legend stuff
		var rands = d3.range(0, 100);
		
		
		var labelYScale = d3.scale.linear().domain([0, rands.length-1])
			.range([300, 80]);
		
		var labelColorScale = d3.scale.linear().domain([0, rands.length-1])
			.range([self.view.expression.min, self.view.expression.max]);
			
		self.DOM.legend.selectAll("rect").remove();
		self.DOM.legend.selectAll("rect").data(rands).enter().append("rect")
		.attr({
			x : function(d, i){ return 30;},
			y : function(d){ return labelYScale(d);},
			height: labelYScale(0) - labelYScale(1) +1,
			width: 50,
			fill: function(d){return self.scales.cells.colorScale(labelColorScale(d));}
			
		}).on("click", function(d, i){
			$('#settingsModal-'+self.view.id).modal('show');
		});
		
		self.DOM.legend.selectAll("text").data(rands).enter()
        .append("text")
        .attr({
            x: 90,
            y:function(d,i){return labelYScale(d) + 7;},
            'style':'font-size:10',
            'text-anchor':'start'
        })
        .text(function(j, i){
        	
        	var d = labelColorScale(j);
            var returnstring = String(d).split(".");

            if (returnstring.length > 1){
                returnstring = returnstring[0] + "." + returnstring[1].slice(0,3);
            } else {
            	returnstring[0]
            }
            
            ticks = [0, Math.floor((rands.length-1)*.25), Math.floor((rands.length-1)*.5), Math.floor((rands.length-1)*.75), rands.length-1];
            
            return (ticks.indexOf(i)>-1)? returnstring :"";
        }).append("title")
            .text(function(d, i){
                return labelColorScale(d);
            });
            
		
	};
});