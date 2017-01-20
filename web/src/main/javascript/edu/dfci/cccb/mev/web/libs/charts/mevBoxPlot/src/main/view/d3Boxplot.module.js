define(['mui', 'd3', "../style/boxplot.less", "save-svg-as-png"], function(ng, d3, style, saveSvgAsPng){

    return angular.module('mev-d3-boxplot', [])
        //.service('GroupBuilder')
        .controller('mevBoxplotTestCtrl', ['$scope', 'mevBoxplotService', function ($scope, mevBoxplotService) {

            //An example of what groups need to look like
            //Make some random values
            var numberOfGroups = [0, 1, 2, 3, 4];
            var numberOfValues = [0, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3];

            $scope.groups = {
                'data': numberOfGroups.map(function (d, i) {
                    return {
                        'control': {
                            'values': numberOfValues.map(function (d, j) {
                                return {
                                    'row': i,
                                    'column': j,
                                    'value': Math.random()
                                }
                            })
                        },
                        'experiment': {
                            'values': numberOfValues.map(function (d, j) {
                                return {
                                    'row': i,
                                    'column': numberOfValues.length + j,
                                    'value': Math.random()
                                }
                            })
                        },
                        'geneName': i,
                        'pValue': 2
                    };
                }),
                'min': 0,
                'max': 1,
                'id' : "cool-Name"
            };

        }])
        .service('D3Service', [function () {
            //Untestable
            return d3;
        }])
        .service('quantileGenerator', [
            'greaterThan', 'extractValues','extractQuantiles', function(gT, eV, eQ){


                return function(data, params){

                    var quantiles = Object.keys(data.groups).map(function(groupName){
                        var group = data.groups[groupName];
                        var sortedGroup = group.values.sort(gT);
                        var extractedGroupValues = eV(sortedGroup);
                        var groupQuantile = eQ(extractedGroupValues);
                        groupQuantile.name=groupName;
                        groupQuantile.outliers = group.values.filter(function(value){
                            return (value.value >= groupQuantile.ninetyseventh ||
                            value.value <= groupQuantile.zerothird) ? true : false;
                        });
                        groupQuantile.color=group.color;
                        return groupQuantile;
                    });

                    var errs = [];
                    quantiles.map(function(quantile){
                        if (params.max < quantile.max){
                            errs.push(
                                "A quantile max outside of data max!\n "
                                + "Quantile Max: " + quantile.max +"\n"
                                + "Absolute Max: " + params.max);
                        }
                        if (params.min > quantile.min) {
                            errs.push(
                                "A quantile min outside of data min!\n "
                                + "Quantile Min: " + quantile.min +"\n"
                                + "Absolute Min: " + params.min)
                        };
                    });

                    if (errs.length > 0) {

                        for(i=0; i<errs.length; i++){
                            console.log(errs[i])
                            throw new RangeError(errs[i]);
                        }
                        return
                    };
                    return quantiles;
                }
            }])
        .service('D3BoxPlots', ['D3Service', 'quantileGenerator', function (d3, quantileGenerator) {

            //D3BoxPlots :: String, D3Selection -> Object
            //  Service to build a Box Plot visualization taking a string
            //  to unique ID the specific plot, and a D3 selection.
            //  D3 in this context is from the D3 service
            return function (id, element) {

                var width = 8, //width of the box
                    padding = 2, //spacing on one side of the box
                    geneSpacing = 10, //space in between genes
                    height = 300,
                    margin = {top:60, bottom:30,left:75,right:20},
                    geneWidth = undefined;
                return {
                    draw: function (params) {
                        console.debug("boxplot.draw", params, itemsPerGroup);
                        var groupNames = Object.keys(params.data[0].groups);
                        var itemsPerGroup = groupNames.length;
                        geneWidth = (padding*2*itemsPerGroup) + (itemsPerGroup*width) + geneSpacing; // total width of a group,

                        //assume params = {
                        // data : [
                        //   {'control':{
                        //       values:[{'row':String, 'column':String, 'value':Number}, ...],
                        //    'experiment':{
                        //       values:[{'row':String, 'column':String, 'value':Number}, ...]},
                        //    'geneName': String,
                        //    'pValue': Number
                        //   }, ...],
                        // min: Number,
                        // max: Number
                        //}

                        this.clear();

                        element.append('svg')
                            .attr({
                                'width': (params.data.length * geneWidth)
                                + margin.left + margin.right,
                                'height': height + margin.top + margin.bottom,
                                'id': "svg-" + id
                            });
                       
                        var svg = d3.select('svg#svg-' + id);
                        // svg.append("style").text(style.source.replace(/[\[]*[.]*mev-boxplot[\]]*/g, ""));
                        svg.append('g').attr('id', 'quantiles' + id);

                        var quantiles = d3.select('g#quantiles' + id);

                        yScale = d3.scale.linear().domain([params.min, params.max])
                            .range([height -margin.bottom, margin.top]);

                        params.data.map(function (item, index) {

                            quantiles.append('g').attr('id', 'quantile-' + index);

                            var box = quantiles.select('g#quantile-' + index);

                            drawQuantile(yScale, item, box, (index * geneWidth) + margin.left, params);

                        });

                        this.drawAxis(yScale, params, svg, params.data.length* geneSpacing);

                        this.drawLabels(svg, params.data[0].groups);

                        this.drawTools(svg);

                    },
                    drawTools: function(svg){
                        var fo = svg.append("foreignObject").attr({
                            x: svg.attr("width")-30,
                            y: 0,
                            height: 50,
                            width: 50
                        });

                        var divSave = fo.append('xhtml:div');
                        var link = divSave.append("div")
                            // .html("<span class=\"glyphicon glyphicon-cog\"></span>")
                            .append("a")
                            .html("<span class=\"glyphicon glyphicon-floppy-save\"></span>")
                        divSave.on("click", function(d, i){
                            console.debug(element, ng.element(element[0][0]).scope());
                            var name = ng.element(element[0][0]).scope
                                ? ng.element(element[0][0]).scope().analysis
                                    ? ng.element(element[0][0]).scope().analysis.name + '-boxplot.png'
                                    : "mev-boxplot.png"
                                : "mev-boxplot.png"
                            saveSvgAsPng.saveSvgAsPng(svg[0][0], name, {
                                selectorRemap: function(s) {
                                    return s.replace('mev-boxplot ', '');
                                }
                            })
                        });
                        var linkDom = link[0][0];
                        if(linkDom.offsetWidth===0 || linkDom.offsetHeight===0)
                            link.text("save");
                    },
                    clear: function () {
                        element.selectAll('*').remove();
                    },
                    drawLabels: function(svg, groups){
                        svg.append('g').attr('class','legend');
                        var legend = svg.select('g.legend');
                        var arGroups = Object.keys(groups).map(function(groupName){return groups[groupName]});

                        legend.selectAll('rect').data(arGroups)
                            .enter().append('rect')
                            .attr({
                                'x': 5,
                                'y': function(d,i){
                                    return 10+15*i;
                                },
                                'width': 15,
                                'height': 15
                            })
                            .attr('style', function(d,i){
                                return 'fill:'+d.color+';'
                                    + 'fill-opacity:.25;stroke:black;stroke-width:1.5;';
                            });

                        legend.selectAll('text').data(arGroups)
                            .enter().append('text')
                            .attr({
                                'x': 23,
                                'y': function(d,i){
                                    return 22+15*i;
                                }
                            })
                            .text(function(d,i){
                                return ' - ' + d.name;
                            });
                    },
                    drawAxis: function(yscale, groups, svg, width){

                        svg.append('g')
                            .attr('class', 'y axis')
                            .attr('id', 'svg-yaxis-'+id)
                            .attr("transform", "translate(" + margin.left + ",0)")

                        var yAxis = svg.select('g#svg-yaxis-'+id)

                        var yaxis = d3.svg.axis()
                            .scale(yscale)
                            .orient('left')
                            .ticks(10)

                        yAxis.call(yaxis);

                        svg.append('g')
                            .attr('class', 'x axis')
                            .attr('id', 'svg-xaxis-'+id)

                        var xAxis = svg.select('g#svg-xaxis-'+id)

                        xAxis.append('line')
                            .attr({
                                'x1':margin.left,
                                'x2':(groups.data.length * geneWidth) + width,
                                'y1':yscale.range()[0],
                                'y2':yscale.range()[0],
                            })
                            .attr('style', 'stroke-width:1;')



                    },
                    data: undefined
                };

                function drawQuantile(scale, data, element, xposition, params) {


//             	cx: xposition + padding + width + (padding * 2) + (width / 2) //ctrl
//		       	cx: xposition + padding 						+ (width / 2) //exp

                    var quantiles = quantileGenerator(data, params);
                    quantiles.map(function(quantile, index){
                        element.append('g').attr('id', quantile.name+'-outliers');
                        outliers = element.select('g#'+quantile.name+'-outliers');

                        outliers.selectAll('circle').data(quantile.outliers).enter()
                            .append('circle')
                            .attr({
                                cx: xposition + padding + (width * index) + (padding * 2 * index) + (width / 2) ,
                                cy:function(d){
                                    return scale(d.value);
                                },
                                r:2,
                                fill:'red'
                            });
                    });


                    element.append('g').attr('id', 'median-line');
                    medianLine = element.select('g#median-line');

                    element.append('g').attr('id', 'max-lines');
                    maxLines = element.select('g#max-lines');

                    element.append('g').attr('id', 'min-lines');
                    minLines = element.select('g#min-lines');

                    element.append('g').attr('id', 'first-third-lines');
                    firstThirdQuantileBox = element.select('g#first-third-lines');

                    element.append('g').attr('id', 'int-bottom-lines');
                    intBottomLines = element.select('g#int-bottom-lines');

                    element.append('g').attr('id', 'int-top-lines');
                    intTopLines = element.select('g#int-top-lines');

                    element.append('g').attr('id', 'label');
                    label = element.select('g#label');

                    //Median Line Draw

                    medianLine.selectAll("line")
                        .data(quantiles.map(function(quantile){
                            return quantile.second;
                        }))
                        .enter().append("line")
                        .attr("class", "median")
                        .attr("x1", function (d, i) {
                            return xposition + padding +(
                                i * (
                                width + (padding * 2)))
                        })
                        .attr("y1", function (d) {
                            return scale(d);
                        })
                        .attr("x2", function (d, i) {

                            return xposition + padding + width + (
                                i * (
                                (width) + (padding * 2)))
                        })
                        .attr("y2", function (d) {
                            return scale(d);
                        })
                        .attr('value', function (d) {
                            return d
                        });

                    //quantile box draw

                    firstThirdQuantileBox.selectAll('rect')
                        .data(quantiles).enter()
                        .append('rect')
                        .attr("class", "first-third")
                        .attr("x", function (d, i) {
                            return xposition + padding + (
                                i * (
                                width + (padding * 2)));
                        })
                        .attr("y", function (d) {
                            return scale(d.third);
                        })
                        .attr("height", function (d) {
                            return scale(d.first) - scale(d.third);
                        })
                        .attr("width", width)
                        .attr('value', function (d) {
                            return d.first + "," + d.third + ":" + scale(d.first) + "," + scale(d.third);
                        })
                        .attr('style', function(d,i){
                            return 'fill:'+d.color+';'
                                + 'fill-opacity:.25;stroke:black;stroke-width:1;'
                        });

                    //minimum line draw

                    minLines.selectAll("line")
                        .data(quantiles)
                        .enter().append("line")
                        .attr("class", "min-Lines")
                        .attr("x1", function (d, i) {
                            return xposition + padding  + (
                                i * (
                                width + (padding * 2)));
                        })
                        .attr("y1", function (d) {
                            return scale(d.zerothird);
                        })
                        .attr("x2", function (d, i) {
                            return xposition + padding + width  + (
                                i * (
                                width + (padding * 2)));
                        })
                        .attr("y2", function (d) {
                            return scale(d.zerothird);
                        })
                        .attr('value', function (d) {
                            return d.min + ":" + scale(d.min);
                        });

                    //maximum line draw

                    maxLines.selectAll("line")
                        .data(quantiles)
                        .enter().append("line")
                        .attr("class", "max-Lines")
                        .attr("x1", function (d, i) {
                            return xposition + padding  + (
                                i * (
                                width + (padding * 2)))
                        })
                        .attr("y1", function (d) {
                            return scale(d.ninetyseventh);
                        })
                        .attr("x2", function (d, i) {
                            return xposition + padding + width  + (
                                i * (
                                width + (padding * 2)))
                        })
                        .attr("y2", function (d) {
                            return scale(d.ninetyseventh);
                        })
                        .attr('value', function (d) {
                            return d
                        });

                    //intermediate bottom line draw

                    intBottomLines.selectAll("line").data(quantiles)
                        .enter().append("line")
                        .attr("class", "int-bottom-lines")
                        .attr("x1", function (d, i) {
                            return xposition + padding + (width / 2) +  (
                                i * (
                                width + (padding * 2)))
                        })
                        .attr("y1", function (d) {
                            return scale(d.first);
                        })
                        .attr("x2", function (d, i) {
                            return xposition + padding + (width / 2) + (
                                i * (
                                width + (padding * 2)))
                        })
                        .attr("y2", function (d) {
                            return scale(d.zerothird);
                        })
                        .attr('value', function (d) {
                            return d.first + " , " + d.min + " : " + scale(d.first) + " , " + scale(d.min)
                        });

                    //intermediate top line draw

                    intTopLines.selectAll("line").data(quantiles)
                        .enter().append("line")
                        .attr("class", "int-top-lines")
                        .attr("x1", function (d, i) {
                            return xposition + padding + (width / 2)  + (
                                i * (
                                width + (padding * 2)))
                        })
                        .attr("y1", function (d) {
                            return scale(d.third);
                        })
                        .attr("x2", function (d, i) {
                            return xposition + padding + (width / 2) + (
                                i * (
                                width + (padding * 2)))
                        })
                        .attr("y2", function (d) {
                            return scale(d.ninetyseventh);
                        })
                        .attr('value', function (d) {
                            return d.third + " , " + d.max + " : " + scale(d.third) + " , " + scale(d.max)
                        });

                    //Labels
                    var x = xposition + geneWidth/3 - 15;
                    var y = scale(params.min) + 10;
                    label.selectAll('text').data([data.name]).enter()
                        .append('text')
                        .attr({
//                	x: xposition + width + padding*2,
                            x: x,
                            y: y
                        })
                        .text(data.geneName)
                        .attr("font-family", "sans-serif")
                        .attr("text-anchor", "left")
                        .attr("font-size", "14px")
                        .attr("fill", "red")
                        .attr("transform", "rotate(35,"+x+","+y+")");

                };
            };
        }])
        .service('extractQuantiles', [function () {

            return function (extractedValues) {
                return {
                    ninetyseventh: d3.quantile(extractedValues, .97),
                    zerothird: d3.quantile(extractedValues, .03),
                    max: d3.quantile(extractedValues, 1),
                    min: d3.quantile(extractedValues, 0),
                    first: d3.quantile(extractedValues, .25),
                    second: d3.quantile(extractedValues, .5),
                    third: d3.quantile(extractedValues, .75),
                };
            };
        }])
        .service('extractValues', [function () {
            return function (arr) {
                return arr.map(function (elem) {
                    return elem.value
                });
            }
        }])
        .service('greaterThan', [function () {
            return function (a, b) {
                return (parseFloat(a.value) > parseFloat(b.value)) ? 1 : -1;
            };
        }])
        .directive('mevBoxplot',
            ['D3BoxPlots', 'D3Service', function (D3BoxPlots, D3Service) {
                return {
                    scope: {
                        data: '=',
                    },
                    restrict: 'E',
                    template: "<div>Loading ...</div>",
                    link: function (scope, elems, attrs) {

                        scope.$watch('data', function (dataPromise, olddata) {

                            if (dataPromise) {

//                        try {
                                dataPromise.then(function(data){
                                    var svg =  D3BoxPlots(data.id, D3Service.select(elems[0]));
                                    svg.draw(data);
                                });;

//                        } catch (e) {
//                            if (e instanceof RangeError){
//                            	raiseAlert.error(e.name + ': ' +e.message, "Box Plotting Error")
//                            } else {
//                            	raiseAlert.error(e.name + ': ' +e.message, "Error!")
//                            }
//                        }
                            }

                        });

                    }
                };
            }]);
});