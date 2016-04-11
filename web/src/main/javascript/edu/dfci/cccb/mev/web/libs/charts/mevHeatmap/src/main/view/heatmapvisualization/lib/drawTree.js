define(['d3', ], function(d3){
  
    var cluster = d3.layout.cluster().sort(null)
        .separation( function(a, b) {
            return (a.parent == b.parent)? 1 : 1;
        }).value(function(d) {
            return d.distance;
        })
        .children(function(d) {
            return d.children;
        });
    
    function nodeclick(d, canvas, type, self){
        
        var dimension = (type == 'horizontal') ? 'column' : 'row';

        var nColor = (type == 'horizontal')? 'blue' : 'red'; //Initial nonselected color of a node.
        var pColor = (type == 'horizontal')? 'blue' : 'red'; //Initial nonselected color of a branch.

        var cir = canvas //Selects all the circles representing nodes but only those which were the clicked circle, using datum as the equality filter.
            .selectAll("circle")
            .filter(function(db){
                return d === db ? 1 : 0;
            });

        var path = canvas.selectAll("path") //Selects all paths but only those which have the same source coordinates as the node clicked.
            .filter(function(dp){
                return (d.x === dp.source.x && d.y === dp.source.y) ? 1 : 0;
            });

        //Check the state of the clicked node. If 'active' (color is green) swap to inactive colors and pass those colors down to all children and vice versa.
        var isSelect = cir.style('fill') == '#00ff00' || cir.style('fill') == 'rgb(0, 255, 0)';
        if(isSelect){

            cir.style('fill', nColor)
                .transition().duration(500); //Change radius of nonactive nodes.

            path.transition().style('stroke', pColor).duration(500);

        } else {

            nColor = '#00ff00';
            pColor = '#00ff00';
            cir.style('fill', nColor)
                .transition().duration(500);
            path.transition().style('stroke', pColor).duration(500);

        }

        if(d.children){ //Check if the node clicked is not a leaf. If the node has children, travel down the three updating the colors to indicate selection.
            walk(d, nColor, pColor, canvas, type, self);
        } else {
            if(nColor == '#00ff00'){ //Check color to see if indicated action is a select/deselect
                d.selected = true;
                if(self.view.selectionParams[dimension].labels.indexOf(d.name) == -1){ //Check if gene already is in the array.
                    self.view.selectionParams[dimension].labels.push(d.name);
                }
            } else { //Algorithm for removing genes from the list on a deselect.
                d.selected = false;
                var index = self.view.selectionParams[dimension].labels.indexOf(d.name); //Get the index of the given gene in the gene array.
                self.view.selectionParams[dimension].labels.splice(index, 1); //Splice that gene out of the array using its gotten index.
            }
        }
        d.selected = nColor == '#00ff00';

    }
    
    function horizontalPath(d, scales) {
        // Path function builder for TOP
        // heatmap tree path attribute

        return "M" + scales.top.xScale(d.target.x) + 
        ","+ scales.top.yScale(d.target.y) + 
        "V" + scales.top.yScale(d.source.y) + 
        "H" + scales.top.xScale(d.source.x);

    }
    
    function verticalPath(d, scales) {

        // Path function builder for LEFT
        // heatmap tree path attribute

        return "M" + scales.side.xScale(d.source.y) + 
        "," + scales.side.yScale(d.source.x) + 
        "V" + scales.side.yScale(d.target.x) + 
        "H" + scales.side.xScale(d.target.y);

    }
    
    var walk = function(d, nColor, pColor,  canvas, type, self){
        
        var dimension = (type == 'horizontal') ? 'column' : 'row';

        d.children.forEach(function(dc){ //Loop through each child, recursively calling walk() as necessary.

            canvas.selectAll('circle')
                .filter(function(db){
                    return dc === db ? 1 : 0;
                })
                .transition().style("fill",nColor).duration(500)
                .transition().duration(500);

            canvas.selectAll("path")
                .filter(function(dp){
                    return (dc.x === dp.source.x && dc.y === dp.source.y) ? 1 : 0;
                })
                .transition().style("stroke", pColor).duration(500);

            if(dc.children){ //Check if children exist, if so, recurse the previous function.
                walk(dc, nColor, pColor, canvas, type, self);
            } else {
                if(nColor == '#00ff00'){
                    d.selected=true;
                    if(self.view.selectionParams[dimension].labels.indexOf(dc.name) == -1){
                        self.view.selectionParams[dimension].labels.push(dc.name);
                    }
                } else {
                    d.selected=false;
                    var index = self.view.selectionParams[dimension].labels.indexOf(dc.name);
                    self.view.selectionParams[dimension].labels.splice(index, 1);
                }
            }
        });
    };
    
    
    return function(canvas, tree, type, scope){
        
        var self = this;
        
        canvas.selectAll('*').remove();
        
        var nodes = cluster.nodes(tree);
        
        var links = cluster.links(nodes);
        
        canvas
        .selectAll("path")
        .data(links)
        .enter()
        .append("path")
        .attr("d",function(d) {
            return (type == 'horizontal') ? horizontalPath(d, self.scales.panel) : verticalPath(d, self.scales.panel);
        })
        .attr("stroke", function() {
            return (type == 'horizontal') ? "blue" : "red";
        }).attr("fill", "none");
        
        var fo = canvas.append("foreignObject")   
        .attr({
            width: 30,
            height: 10
        });        

        var div = fo.append("xhtml:div")  // declare the tooltip div 
        .attr("class", "d3tooltip")              // apply the 'tooltip' class
        .html("<a>save</a>")
        .style("opacity", 0);              // set the opacity to nil
        
        div.select("a").on("click", function(d){
            hideTooltip(true);
            $('div#'+ ((type == 'horizontal')? 'column': 'row') + 'SelectionsModal-' + self.view.id).modal();
        }).on("mouseout", function(d){
            hideTooltip();   
        });
        function showTooltip(){
            div.transition()
                        .duration(200)  
                        .style("display", "block")
                        .style("opacity", 0.9);  
            fo.attr("x", (d3.event.layerX - 40) + "px")          
                    .attr("y", (d3.event.layerY - 22) + "px"); 
        }
        function hideTooltip(fast){
            if(fast)
                div.style("opacity", 0);
            else
                div.transition()
                .duration(500)                
                .style("display", "none");            
        }
        canvas
        .selectAll("circle")
        .data(nodes)
        .enter()
        .append("circle")
        .attr('id', 'selectionCircle')
        .attr("r", 4)
        .attr("cx", function(d) {
            return (type == 'horizontal') ? self.scales.panel.top.xScale(d.x) : self.scales.panel.side.xScale(d.y);
        })
        .attr("cy", function(d) {
            return (type == 'horizontal') ? self.scales.panel.top.yScale(d.y, self.scales.panel) : self.scales.panel.side.yScale(d.x);
        })
        .attr("fill", function(d) {
            return (type == 'horizontal') ? "blue" : "red";
        })
        .on("mousedown", function(d) {
        	switch (d3.event.button){
        	case 0:
        		nodeclick(d, canvas, type, self);  
                if(d.selected){                    
                    showTooltip();
                }else{
                    div.transition()
                    .duration(200)  
                    .style("opacity", 0);                 
                }
                
        	}
        })
        .on("mouseover", function(d) {
                console.debug("d.selected", d.selected);
                if(d.selected){                    
                    showTooltip();
                }
            });     
        
        

//        d3.select("g#"+((type == 'horizontal')? 'top' : 'side')+"-Panel").on('contextmenu', function(){
        canvas.on('contextmenu', function(){
        	d3.event.preventDefault();
    		$('div#'+ ((type == 'horizontal')? 'column': 'row') + 'SelectionsModal-' + self.view.id).modal();
        });
        
    	
    };
    
});