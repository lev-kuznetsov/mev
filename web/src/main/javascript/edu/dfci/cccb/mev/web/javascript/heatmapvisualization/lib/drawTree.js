define(['d3'], function(d3){
  
    var cluster = d3.layout.cluster().sort(null)
        .separation( function(a, b) {
            return (a.parent == b.parent)? 1 : 1
        }).value(function(d) {
            return d.distance;
        })
        .children(function(d) {
            return d.children;
        });
    
    function nodeclick(d, canvas, type){
        

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
        
        if(cir.style('fill') == '#00ff00' || cir.style('fill') == 'rgb(0, 255, 0)'){

            cir.style('fill', nColor)
                .transition().duration(500); //Change radius of nonactive nodes.

            path.transition().style('stroke', pColor).duration(500);

        } else {

            nColor = '#00ff00';
            pColor = '#00ff00';
            cir.style('fill', nColor)
                .transition().duration(500);
            path.transition().style('stroke', pColor).duration(500);

        };

        if(d.children){ //Check if the node clicked is not a leaf. If the node has children, travel down the three updating the colors to indicate selection.
            walk(d, nColor, pColor, canvas, type);
        } else {
            if(nColor == '#00ff00'){ //Check color to see if indicated action is a select/deselect
                if(scope.treeSelections[type].indexOf(d.name) == -1){ //Check if gene already is in the array.
                    scope.treeSelections[type].push(d.name)
                }
            } else { //Algorithm for removing genes from the list on a deselect.
                var index = scope.treeSelections[type].indexOf(d.name); //Get the index of the given gene in the gene array.
                scope.treeSelections[type].splice(index, 1); //Splice that gene out of the array using its gotten index.
            };
        };

    };
    
    function horizontalPath(d, scales) {
        // Path function builder for TOP
        // heatmap tree path attribute

        return "M"
                + scales.top.xScale(d.target.x)
                + ","
                + scales.top.yScale(d.target.y)
                + "V"
                + scales.top.yScale(d.source.y)
                + "H"
                + scales.top.xScale(d.source.x);

    };
    
    function verticalPath(d, scales) {

        // Path function builder for LEFT
        // heatmap tree path attribute

        return "M"
                + scales.side.xScale(d.source.y)
                + ","
                + scales.side.yScale(d.source.x)
                + "V"
                + scales.side.yScale(d.target.x)
                + "H"
                + scales.side.xScale(d.target.y)

    };
    
    var walk = function(d, nColor, pColor,  canvas, type, scope){

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
                walk(dc, nColor, pColor, canvas, type);
            } else {
                if(nColor == '#00ff00'){
                    if(scope.treeSelections[type].indexOf(dc.name) == -1){
                        scope.treeSelections[type].push(dc.name);
                    };
                } else {
                    var index = scope.treeSelections[type].indexOf(dc.name);
                    scope.treeSelections[type].splice(index, 1);
                }
            };
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
            return (type == 'horizontal') ? horizontalPath(d, self.scales.panel) : verticalPath(d, self.scales.panel)
        })
        .attr("stroke", function() {
            return (type == 'horizontal') ? "blue" : "red"
        }).attr("fill", "none");
        
        canvas
        .selectAll("circle")
        .data(nodes)
        .enter()
        .append("circle")
        .attr("r", 4)
        .attr("cx", function(d) {
            return (type == 'horizontal') ? self.scales.panel.top.xScale(d.x) : self.scales.panel.side.xScale(d.y);
        })
        .attr("cy", function(d) {
            return (type == 'horizontal') ? self.scales.panel.top.yScale(d.y, self.scales.panel) : self.scales.panel.side.yScale(d.x);
        })
        .attr("fill", function(d) {
            return (type == 'horizontal') ? "blue" : "red"
        })
        .on("click", function(d) {
            nodeclick(d, canvas, type, scope)
        });
    }
    
})