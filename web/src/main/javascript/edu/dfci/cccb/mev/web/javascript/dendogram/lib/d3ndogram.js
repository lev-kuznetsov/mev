(function(){

    define(['d3'], function(d3){


        d3ndogram = function(){

            var settings = {
                'interpolate': false,
                'invert': false,
                'x':{
                    'margin':10,
                    'axis-width':90,
                    'gutter-width':90
                },
                'y':{
                    'margin':10,
                    'axis-width':500,
                    'gutter-width':90
                },
                'labels': true,
                'style': {
                    'path':{
                        'color': '#ccc',
                        'width': '1.5px',
                        'selected': {
                            'color': '#0f0'
                        }
                    },
                    'node':{
                        'color': '#f00',
                        'radius': 3,
                        'selected': {
                            'color': '#0f0',
                            'radius': 5
                        }

                    }
                },
            }

            var axis = {
                'x':{
                    'group': undefined,
                    'scale': d3.scale.linear(),
                    'axis': d3.svg.axis().orient('bottom')
                },
                'y':{
                    'group': undefined,
                    'scale': d3.scale.linear(),
                    'axis': d3.svg.axis().orient('right')
                }
            }

            var dispatch = d3
                .dispatch('draw', 'clean', 'selected', 'drawSubtree')

            var selection, dendogram

            var cluster = undefined,
                shownTree = undefined,
                selectedTree = {
                    'root': undefined
                },
                dendogramTree = undefined

            var childrenAccessor = function(node){
                return node.children
            }
            var valueAccessor = null

            var diagonalPath = d3.svg.diagonal()
            var rightAnglePath = function(path){
                //Function to build right angle paths under
                //inverting conditions

                //initial moveto point
                var initialPoint = !settings.invert ? 
                    [axis.x.scale(path.source.x), 
                        axis.y.scale(path.source.y) ] : 
                    [axis.x.scale(path.source.y), 
                        axis.y.scale(path.source.x) ] 

                //first LineTo
                var firstPoint = !settings.invert ? 
                    [axis.x.scale(path.target.x), 
                        axis.y.scale(path.source.y) ] : 
                    [axis.x.scale(path.source.y), 
                        axis.y.scale(path.target.x) ] 

                //second LineTo
                var endPoint = !settings.invert ? 
                    [axis.x.scale(path.target.x), 
                        axis.y.scale(path.target.y) ] : 
                    [axis.x.scale(path.target.y), 
                        axis.y.scale(path.target.x) ] 

                return "M " + initialPoint[0] + " " + initialPoint[1] + 
                       "L " + firstPoint[0] + " " + firstPoint[1] + " " +
                              endPoint[0] + " " + endPoint[1] 

            }

            var exports = function(){

                dendogram = selection.append('g')
                    .classed('d3ndogram', true) 

                axis.x.group = dendogram.append('g')
                    .classed('d3ndogram-x-axis', true)

                axis.y.group = dendogram.append('g')
                    .classed('d3ndogram-y-axis', true)

                dendogramTree = dendogram.append('g')
                    .classed('d3ndogram-tree', true) 
                
                cluster = d3.layout.cluster()
                    .separation(function(){ return 1 })

                diagonalPath
                    .projection(function(d) { 
                        if (!settings.invert){
                            return [axis.y.scale(d.x), axis.x.scale(d.y)] 
                        } 
                        return [axis.x.scale(d.y),axis.y.scale(d.x)] 
                    }) 

                axis.x.scale.domain([0,1])
                axis.y.scale.domain([0,1])

                axis.x.scale.range([
                    settings.x.margin + settings.x['gutter-width'],
                    settings.x.margin + settings.x['gutter-width'] + 
                        settings.x['axis-width'] 
                ])

                axis.y.scale.range([
                    settings.y.margin + settings.y['gutter-width'],
                    settings.y.margin + settings.y['gutter-width'] + 
                        settings.y['axis-width']
                ])

                dispatch.on('draw', function(data){

                    if (valueAccessor){
                        cluster.value(valueAccessor)
                    }

                    cluster.children(childrenAccessor)

                    shownTree = Object.create(data.root)
                    shownLeafLabels = getSelectedNodes(data.root, {}, true)
                    .filter(function(node) { 
                        return typeof node.children == 'undefined'
                    })

                    var nodes = cluster.nodes(shownTree)
                    var svgNodes = dendogramTree
                        .selectAll('circle').data(nodes)

                    var paths = cluster.links(nodes)
                    var svgPaths = dendogramTree
                        .selectAll('path').data(paths)

                    svgPaths.enter().append('path')
                    .attr({ 
                        'd': settings.interpolate ? diagonalPath : rightAnglePath
                    })
                    .style({
                        'stroke': settings.style.path.color,
                        'stroke-width': settings.style.path.width,
                        'fill': 'none'
                    })

                    svgNodes.enter()
                    .append('circle')
                        .attr({ 
                            'cx': function(node){ 
                                if (!settings.invert){
                                    return axis.x.scale(node.x)
                                }
                                return axis.x.scale(node.y)
                            },
                            'cy': function(node){ 
                                if (!settings.invert){
                                    return axis.y.scale(node.y) 
                                }
                                return axis.y.scale(node.x) 
                            },
                            'r': settings.style.node.radius,
                            'fill': settings.style.node.color,
                            'name': function(node){ return node.name }
                        })
                    .on('mousedown', function(d){

                        var self = d3.select(this)

                        if (self.attr('selected-node') == 'true'){
                            self.attr('selected-node', 'false')
                            selectedTree.root = undefined
                            dispatch.selected(selectedTree)
                            dispatch.drawSubtree()
                            
                            return
                        }


                        var selected = {
                            'x': parseFloat(self.attr('cx')),
                            'y': parseFloat(self.attr('cy'))
                        }

                        self.attr('selected-node', 'true')
                        selectedTree.root = getSubtree(shownTree, selected)
                        dispatch.selected(selectedTree)
                        dispatch.drawSubtree()

                    })

                    if (!settings.invert && settings.labels){

                        xLabel = d3.scale.ordinal()
                            .domain()

                        
                    }

                })

                dispatch.on('clean', function(){
                    dendogram.selectAll('*').remove()
                    axis.x.group.selectAll('*').remove()
                    axis.y.group.selectAll('*').remove()
                })

                dispatch.on('drawSubtree', function(){

                    if (!selectedTree.root){

                        dendogramTree.selectAll('circle')
                            .transition().duration(800)
                            .attr('fill', settings.style.node.color)

                        dendogramTree.selectAll('path')
                            .transition().duration(800)
                            .attr('fill', settings.style.node.color)

                        return
                    }

                    if (valueAccessor){
                        cluster.value(valueAccessor)
                    }

                    cluster.children(childrenAccessor)

                    var subTree = Object.create(selectedTree.root)
                    var nodes = getSelectedNodes(subTree, {}, true)
                    .map(function(node){

                        var copy = Object.create(node)

                        if (!settings.invert){
                            copy.x = axis.x.scale(node.x)
                            copy.y = axis.y.scale(node.y) 
                        } else {
                            copy.x = axis.x.scale(node.y)
                            copy.y = axis.y.scale(node.x) 
                        }

                        return copy

                    })

                    dendogram.selectAll('circle')
                    .transition().duration(800)
                    .attr('fill', function(circle){
                        var self = d3.select(this)

                        var element = nodes.reduce(function(acc, next){
                            if (next.x == parseFloat(self.attr('cx')) && 
                                next.y == parseFloat(self.attr('cy'))){
                                return acc.concat([next])
                            }
                            return acc
                        }, [])

                        if (element.length == 1){
                            return settings.style.node.selected.color
                        }

                        return settings.style.node.color

                    })

                })

            }

            exports.selection = function(){
                if (arguments.length > 0){
                    selection = arguments[0]
                    return exports
                }
                return selection
            }

            exports.dispatch = function(){
                if (arguments.length > 0){
                    dispatch = arguments[0]
                    return exports
                }
                return dispatch
            }

            exports.valueAccessor = function(){
                if (arguments.length > 0){
                    valueAccessor = arguments[0]
                    return exports
                }
                return valueAccessor
            }

            exports.selectedTree = function(){
                if (arguments.length > 0){
                    selectedTree.root = arguments[0]
                    return exports
                }
                return selectedTree.root
            }

            exports.childrenAccessor = function(){
                if (arguments.length > 0){
                    childrenAccessor = arguments[0]
                    return exports
                }
                return childrenAccessor
            }

            exports.settings = function(){
                if (arguments.length > 0){
                    settings = arguments[0]
                    return exports
                }
                return settings
            }

            exports.settings.x = function(){
                if (arguments.length > 0){
                    settings.x = arguments[0]
                    return exports
                }
                return settings.x
            }

            exports.settings.y = function(){
                if (arguments.length > 0){
                    settings.y = arguments[0]
                    return exports
                }
                return settings.y
            }

            exports.settings.labels = function(){
                if (arguments.length > 0){
                    settings.labels = arguments[0]
                    return exports
                }
                return settings.labels
            }

            exports.settings.invert = function(){
                if (arguments.length > 0){
                    settings.invert = arguments[0]
                    return exports
                }
                return settings.invert
            }

            exports.settings.interpolate = function(){
                if (arguments.length > 0){
                    settings.interpolate = arguments[0]
                    return exports
                }
                return settings.interpolate
            }

            exports.settings.style = function(){
                if (arguments.length > 0){
                    settings.style = arguments[0]
                    return exports
                }
                return settings.style
            }

            exports.settings.style.path = function(){
                if (arguments.length > 0){
                    settings.style.path = arguments[0]
                    return exports
                }
                return settings.style.path 
            }

            exports.settings.style.node = function(){
                if (arguments.length > 0){
                    settings.style.node = arguments[0]
                    return exports
                }
                
                return settings.style.node 
            }

            function getSubtree(root, selectedNode){
                //Returns subtree given a selected node specified
                //    as the new root

                position ={
                    'x': undefined,
                    'y': undefined    
                }

                if (!settings.invert){                            
                    position.x = axis.x.scale(root.x)                   
                } else {
                    position.x = axis.x.scale(root.y)                   
                }

                if (!settings.invert){                            
                    position.y = axis.y.scale(root.y)                   
                } else {
                    position.y = axis.y.scale(root.x)                   
                }

                //If node isn't selected and isn't in tree
                if (position.x != selectedNode.x || 
                    position.y != selectedNode.y){

                    var rest = undefined
                    if (typeof childrenAccessor(root) != 'undefined'){
                        rest = childrenAccessor(root).map(function(childTree){
                            return getSubtree(childTree, selectedNode)
                        })
                        .reduce(function(a, b){ 
                            return a ? a : b 
                        })
                    }
                    return rest
                }

                //If node is the selected node
                if (position.x == selectedNode.x && 
                    position.y == selectedNode.y){

                    return root
                }

            }

            function getSelectedNodes(root, selectedNode, inTree){
                //returns a list of nodes below and including selected 
                //node using x and y as id

                if (inTree){
                    var rest = []
                    if (typeof childrenAccessor(root) != 'undefined'){
                        rest = childrenAccessor(root).map(function(childTree){
                            return getSelectedNodes(childTree, selectedNode, true)
                        })
                        .reduce(function(a, b){ return a.concat(b) })
                    }
                    return [root].concat(rest)
                }

                position ={
                    'x': undefined,
                    'y': undefined    
                }

                if (!settings.invert){                            
                    position.x = axis.x.scale(root.x)                   
                } else {
                    position.x = axis.x.scale(root.y)                   
                }

                if (!settings.invert){                            
                    position.y = axis.y.scale(root.y)                   
                } else {
                    position.y = axis.y.scale(root.x)                   
                }

                //If node isn't selected and isn't in tree
                if (position.x != selectedNode.x || 
                    position.y != selectedNode.y){

                    var rest = []
                    if (typeof childrenAccessor(root) != 'undefined'){
                        rest = childrenAccessor(root).map(function(childTree){
                            return traverseSelected(childTree, selectedNode, false)
                        })
                        .reduce(function(a, b){ return a.concat(b) })
                    }
                    return rest
                }

                //If node is the selected node
                if (position.x == selectedNode.x && 
                    position.y == selectedNode.y){

                    //If it is not a leaf
                    var rest = []
                    if (typeof childrenAccessor(root) != 'undefined'){

                        rest = childrenAccessor(root)
                        .map(function(childTree){
                            return traverseSelected(childTree, selectedNode, true)
                        })
                        .reduce(function(a, b){ 
                            return a.concat(b) 
                        })

                    }

                    return [root]
                        .concat(rest)
                }

            }

            d3.rebind(exports, dispatch, 'on', 'draw', 'clean', 'selected')
            return exports
        }

        return d3ndogram

    })

})()
