define(['d3','./colorBrewer', 'jquery'], function(d3, d3colors){
    
    return function (cluster, canvas){
        
        var self = this;
        
        canvas.selectAll('*').remove();
        
        var colors = d3.scale.ordinal()
        .domain(d3.range(cluster.clusters.length) )
        
        if (cluster.clusters.length > 2){
            colors.range(d3colors.Set1[cluster.clusters.length] );
        } else {
            colors.range(["blue", "yellow" ]);
        }
        
        if (cluster.dimension == "column"){
    
            cluster.clusters.map(function(group, index){
                
                canvas.selectAll('rect').data(group, function(d){return d}).enter()
                    .append('rect')
                    .attr({
                        'x': function(d, i){
                            return self.scales.cells.xScale(d);
                        },
                        'y': function(d, i){
                            return self.scales.panel.top.yScale(.5)
                        },
                        'width': function(d, i){
                            return self.params.cell.width - self.params.cell.padding
                        },
                        'height': function(d, i){
                            return self.scales.panel.top.yScale(.5) - self.scales.panel.top.yScale(0)
                        },
                        'fill':function(d, i){
                            return colors(index)
                        }
                    })
                    .on("mousedown", function(d) {
			        	switch (d3.event.button){
			        	case 0:
			        		self.view.selectionParams[cluster.dimension].labels = group
			        		$('div#'+ (cluster.dimension) + 'SelectionsModal').modal();
			        		break
			        	}
			            
			        })
            });
            
        } else if (cluster.dimension == "row") {
            
            cluster.clusters.map(function(group, index){
                
                var fill = '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16)
                
                canvas.selectAll('rect').data(group, function(d){return d}).enter()
                    .append('rect')
                    .attr({
                        'y': function(d, i){
                            return self.scales.cells.yScale(d);
                        },
                        'x': function(d, i){
                            return self.scales.panel.side.xScale(.5) -1
                        },
                        'height': function(d, i){
                            return self.params.cell.height - self.params.cell.padding
                        },
                        'width': function(d, i){
                            return self.scales.panel.side.xScale(.5) - self.scales.panel.side.xScale(0)
                        },
                        'fill':function(d, i){
                            return colors(index)
                        }
                    })
                    .on("mousedown", function(d) {
			        	switch (d3.event.button){
			        	case 0:
			        		self.view.selectionParams[cluster.dimension].labels = group
			        		$('div#'+ (cluster.dimension) + 'SelectionsModal').modal();
			        		break
			        	}
			            
			        })
            });
            
        }
    };
})