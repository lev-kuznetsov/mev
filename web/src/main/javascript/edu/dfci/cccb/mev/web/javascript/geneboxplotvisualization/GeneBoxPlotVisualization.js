define(['angular', 'd3'], function(angular, d3){
	
	return angular.module('Mev.GeneBoxPlotVisualization', [])
	.controller('testCtrl', ['$scope', function ($scope) {

        //An example of what groups need to look like
        //Make some random values
        var numberOfGroups = [0, 1, 2, 3, 4];
        var numberOfValues = [0, 1, 2, 3, 4, 5,6,7,8,9,10,11,12,13,14];

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
    .service('D3BoxPlots', ['greaterThan', 'extractValues', 'D3Service', 'extractQuantiles',

    function (gT, eV, d3, eQ) {
        
        //D3BoxPlots :: String, D3Selection -> Object
        //  Service to build a Box Plot visualization taking a string
        //  to unique ID the specific plot, and a D3 selection 
        return function (id, element) {

            var width = 30, //width of the box
                padding = 10, //spacing on one side of the box
                geneSpacing = 2 * (width + (padding * 2)); //2 times the space required for a box

            return {
                draw: function (groups) {
                    //assume groups = {
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
                        'width': groups.data.length * geneSpacing,
                            'height': 300,
                            'id': "svg-" + id
                    });

                    var svg = d3.select('svg#svg-' + id);

                    svg.append('g').attr('id', 'quantiles' + id);

                    var quantiles = d3.select('g#quantiles' + id);

                    yScale = d3.scale.linear().domain([groups.min, groups.max])
                        .range([280, 10]);

                    groups.data.map(function (group, index) {

                        quantiles.append('g').attr('id', 'quantile-' + index);

                        var box = quantiles.select('g#quantile-' + index);

                        drawQuantile(yScale, group, box, index * geneSpacing, groups);

                    });

                    function drawQuantile(scale, data, element, xposition, groups) {

                        console.log(groups)
                        var sortedControl = data.control.values.sort(gT),
                            sortedExperiment = data.experiment.values.sort(gT);

                        var extractedControlValues = eV(sortedControl),
                            extractedExperimentValues = eV(sortedExperiment);

                        var experimentQuantile = eQ(extractedExperimentValues),
                            controlQuantile = eQ(extractedControlValues);
                        
                        //Error checking
                        
                        errs = []
                        if (groups.max < experimentQuantile.max || 
                            groups.max < controlQuantile.max){
                            
                            errs.push(
                                    "A quantile max outside of data max!\n " 
                                    + "Experment Max: " + experimentQuantile.max +"\n"
                                    + "Control Max: " + controlQuantile.max +"\n"
                                    + "Group Max: " + groups.max);
                            
                        } else if (groups.min > experimentQuantile.min || 
                                   groups.min > controlQuantile.min) {
                            
                            errs.push(
                                "A quantile min outside of data min!\n " 
                                + "Experment Min: " + experimentQuantile.min +"\n"
                                + "Control Min: " + experimentQuantile.min +"\n"
                                + "Group Min: " + groups.min)
                        };
                        
                        if (errs.length > 0) {
                         
                            for(i=0; i<errs.length; i++){
                               throw new RangeError(errs[i]); 
                            }
                            
                            return
                            
                        };

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

                        //Median Line Draw
                        medianLine.selectAll("line")
                            .data([experimentQuantile.second, controlQuantile.second])
                            .enter().append("line")
                            .attr("class", "median")
                            .attr("x1", function (d, i) {
                            return xposition + padding + (
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
                            .data([experimentQuantile, controlQuantile]).enter()
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
                            .attr('style', 'fill:green;fill-opacity:.25;stroke:black;stroke-width:1.5;');

                        //min/max line draw

                        minLines.selectAll("line")
                            .data([experimentQuantile, controlQuantile])
                            .enter().append("line")
                            .attr("class", "min-Lines")
                            .attr("x1", function (d, i) {
                            return xposition + padding + (
                            i * (
                            width + (padding * 2)));
                        })
                            .attr("y1", function (d) {
                            return scale(d.min);
                        })
                            .attr("x2", function (d, i) {
                            return xposition + padding + width + (
                            i * (
                            width + (padding * 2)));
                        })
                            .attr("y2", function (d) {
                            return scale(d.min);
                        })
                            .attr('value', function (d) {
                            return d.min + ":" + scale(d.min);
                        });

                        maxLines.selectAll("line")
                            .data([experimentQuantile, controlQuantile])
                            .enter().append("line")
                            .attr("class", "max-Lines")
                            .attr("x1", function (d, i) {
                            return xposition + padding + (
                            i * (
                            width + (padding * 2)))
                        })
                            .attr("y1", function (d) {
                            return scale(d.max);
                        })
                            .attr("x2", function (d, i) {
                            return xposition + padding + width + (
                            i * (
                            width + (padding * 2)))
                        })
                            .attr("y2", function (d) {
                            return scale(d.max);
                        })
                            .attr('value', function (d) {
                            return d
                        });

                        //intermediate lines draw

                        intBottomLines.selectAll("line").data([
                        experimentQuantile, controlQuantile])
                            .enter().append("line")
                            .attr("class", "int-bottom-lines")
                            .attr("x1", function (d, i) {
                            return xposition + padding + (width / 2) + (
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
                            return scale(d.min);
                        })
                            .attr('value', function (d) {
                            return d.first + " , " + d.min + " : " + scale(d.first) + " , " + scale(d.min)
                        });

                        intTopLines.selectAll("line").data([
                        experimentQuantile, controlQuantile])
                            .enter().append("line")
                            .attr("class", "int-top-lines")
                            .attr("x1", function (d, i) {
                            return xposition + padding + (width / 2) + (
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
                            return scale(d.max);
                        })
                            .attr('value', function (d) {
                            return d.third + " , " + d.max + " : " + scale(d.third) + " , " + scale(d.max)
                        });

                    };

                },
                clear: function () {
                    element.selectAll('*').remove()
                },
                data: undefined
            };
        };
    }])
        .service('extractQuantiles', [function () {

        return function (extractedValues) {
            return {
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
        .directive('d3BoxPlotVisualization', ['D3BoxPlots', 'D3Service', function (D3BoxPlots, D3Service) {
        return {
            scope: {
                data: '=',
            },
            restrict: 'E',
            link: function (scope, elems, attrs) {

                scope.$watch('data', function (data, olddata) {

                    if (data) {
                        
                        try {
                            var svg = 
                                D3BoxPlots(data.id,
                                    D3Service.select(elems[0]));
                            svg.draw(data)
                        } catch (e) {
                            if (e instanceof RangeError){
                                //Handle rangeError. Maybe a pretty picture?
                            } else {
                                //Handle some other error
                            }
                        }
                    }

                });

            }
        };
    }]);
	
});