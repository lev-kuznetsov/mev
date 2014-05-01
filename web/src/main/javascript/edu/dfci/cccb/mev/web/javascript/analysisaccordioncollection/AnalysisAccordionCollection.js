define(['angular', 'jquery', 'd3', 'alertservice/AlertService'], function(angular, jq, d3){
	
	return angular.module('Mev.AnalysisAccordionCollection', ['Mev.AlertService'])
	.directive('analysisContentItem', ['$compile', function ($compile) {
        var heirarchicalTemplate = '<hierarchical-Accordion analysis="analysis" heatmap-dataset="dataset"></hierarchical-Accordion>';
        var kMeansTemplate = '<k-Means-Accordion analysis="analysis" heatmap-dataset="dataset"></k-Means-Accordion>';
        var limmaTemplate = '<anova-Accordion analysis="analysis" heatmap-dataset="dataset"></anova-Accordion>';
        var anovaTemplate = '<t-Test-Accordion analysis="analysis" heatmap-dataset="dataset"></t-Test-Accordion>';
        var tTestTemplate = '<limma-Accordion analysis="analysis" heatmap-dataset="dataset"></limma-Accordion>';
        
        var getTemplate = function(analysisType) {
            var template = '';
    
            switch(analysisType) {
                case 'Hierarchical Clustering':
                    template = heirarchicalTemplate;
                    break;
                case 'K-means Clustering':
                    template = kMeansTemplate;
                    break;
                case 'LIMMA Differential Expression Analysis':
                    template = limmaTemplate;
                    break;
                case 'Anova Analysis':
                    template = anovaTemplate;
                    break;
                case 't-Test Analysis':
                    template = tTestTemplate;
                    break;
            }
    
            return template;
        }
    
        var linker = function(scope, element, attrs) {
    
            element.append(getTemplate(scope.analysis.type));
    
            $compile(element.contents())(scope);
        }
    
        return {
            restrict: "E",
            rep1ace: true,
            link: linker,
            scope: {
                analysis : '=analysis',
                dataset : '=heatmapDataset'
            }
        };
    }])
	.directive('kMeansAccordion', [function() {
        return {
            restrict : 'E',
            scope : {
            	analysis : "=analysis"
            },
            templateUrl : '/container/view/elements/kmeansAccordion',
        };
    }])
    .directive('anovaAccordion',
    ['$filter', 'alertService', function($filter, alertService) {
        return {
            restrict : 'E',
            templateUrl : '/container/view/elements/anovaAccordion',
            scope : {
            	analysis : "=analysis",
            	dataset : "&heatmapDataset"
            },
            link: function(scope){
                    scope.headers = [
                        {'name':'ID', 'value':"id"},
                        {'name':'P-Value', 'value':"pValue"},
                        {'name':'Pairwise LFC', 'value':'pairwise_log_fold_change'}
                    ]
                    
                    scope.filterParams = {
                            'id' : '',
                            'pValue' : undefined
                    };
                        
                    scope.selectionParams = {
                        name: undefined,
                        color: '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16)
                    };
                        
                    var ctr = -1;
                    scope.tableOrdering = undefined;
                    scope.reorderTable = function(header) {

                        ctr = ctr * (-1);
                        if (ctr == 1) {
                            scope.tableOrdering = header.value;
                        } else {
                            scope.tableOrdering = "-"
                                    + header.value;
                        }
                    }
                        
                    scope.addSelections = function(){
                
                        var userselections = scope.analysis.results;

                        var step1 = $filter('filter')(scope.analysis.results, {
                            id: scope.filterParams.id
                        });

                        var step2 = $filter('filterThreshold')(step1, scope.filterParams.pValue, 'pValue')
                        
                        var step3 = step2.map(function(d){
                            return d.id
                        })

                        var selectionsData = {
                            name: scope.selectionParams.name,
                            properties: {
                                selectionDescription: '',
                                selectionColor:scope.selectionParams.color,                     
                            },
                            keys:step3
                        };
                        
                        dataset.selections.post({
                            datasetName : dataset.datasetName,
                            dimension : scope.selectionParams.dimention

                        }. selectionsData,
                        function(response){
                                scope.$broadcast('SeletionAddedEvent', 'row');
                                var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                var header = "Heatmap Selection Addition";
                        
                                scope.selectionParams.color = '#'+Math
                                    .floor(Math.random()*0xFFFFFF<<0)
                                    .toString(16)

                                alertService.success(message,header);
                        },
                        function(data, status, headers, config) {
                            var message = "Couldn't add new selection. If "
                                + "problem persists, please contact us.";

                             var header = "Selection Addition Problem (Error Code: "
                                + status
                                + ")";

                             alertService.error(message,header);
                        });

                    }
            }
        };
    }])
    .directive('tTestAccordion',
    ['$filter', 'alertService', function($filter, alertService) {
        return {
            restrict : 'E',
            templateUrl : '/container/view/elements/tTestAccordion',
            scope : {
            	dataset : "&heatmapDataset",
            	analysis : "=analysis"
            },
            link : function(scope) {

                scope.headers = {
                    'ID' : {name: "id", sort: -1},
                    'P-Value' : {name: "pValue", sort: -1},
                    'Log Fold Change': {name: "logFoldChange", sort: -1}
                };
                
                scope.filterParams = {
                        'id' : '',
                        'pValueThreshold' : undefined,
                        'logFoldChange' : undefined
                };
                
                scope.selectionParams = {
                        name: undefined,
                        color: '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16)
                };
                
                scope.addSelections = function(){
                    
                    var userselections = scope.tTest.results;
                    
                    var step1 = $filter('filter')(scope.tTest.results, {
                        id: scope.filterParams.id
                    });
                                                                
                    var step2= $filter('filterThreshold')(step1, scope.filterParams.pValueThreshold, 'pValue');
                    var step3= $filter('filterThreshold')(step2, scope.filterParams.logFoldChange, 'logFoldChange');
                    var step4 = step3.map(function(d){
                        return d.id
                    })
                    
                    var selectionData = {
                        name: scope.selectionParams.name,
                        properties: {
                            selectionDescription: '',
                            selectionColor:scope.selectionParams.color,                     
                        },
                        keys:step4
                    };
                    
                    dataset.selections.post({
                        datasetName : dataset.datasetName,
                        dimension : "row"
                
                    }, selectionData, 
                    function(response){
                            scope.$broadcast('SeletionAddedEvent', 'row');
                            var message = "Added " + scope.selectionParams.name + " as new Selection!";
                            var header = "Heatmap Selection Addition";
                             
                            alertService.success(message,header);
                    }, 
                    function(data, status, headers, config) {
                        var message = "Couldn't add new selection. If "
                            + "problem persists, please contact us.";

                         var header = "Selection Addition Problem (Error Code: "
                            + status
                            + ")";
                         
                         alertService.error(message,header);
                    });
                    
                };
                
                scope.getCaretCss = function(header){                                        	
                	if(header.sort==1){
                		return "caret-up";
                	}else{
                		return "caret-down";
                	}
                }

                var ctr = -1;
                scope.tTestTableOrdering = undefined;                                        
                scope.reorderTTestTable = function(header, $event) {

                    ctr = ctr * (-1);
                    scope.headers[header].sort=scope.headers[header].sort*(-1);
                    if (scope.headers[header].sort == 1) {
                        scope.tTestTableOrdering = scope.headers[header].name;
                    } else {
                        scope.tTestTableOrdering = "-"
                                + scope.headers[header].name;
                    }
                }
            }

        };
    }]).directive('limmaAccordion',
    ['$filter', 'alertService', function($filter, alertService) {
        return {
            restrict : 'E',
            templateUrl : '/container/view/elements/limmaAccordion',
            scope : {
            	dataset : "&heatmapDataset",
            	analysis : "=analysis"
            },
            link : function(scope) {

                scope.headers = [
	               {'name':'ID', 'value': "id", 'icon': "search"},
	               {'name':'Log-Fold-Change', 'value': "logFoldChange", 'icon': ">="},
	               {'name':'Average Expression', 'value': "averageExpression", 'icon': "none"},
	               {'name':'P-Value', 'value': "pValue", 'icon': "<="},
	               {'name':'q-Value', 'value' : "qValue", 'icon': "none"}
               ];
                
                scope.filterParams = {
	                'id' : '',
	                'logFoldChange' : undefined,
	                'pValue' : undefined,
	                'qValue':undefined
                }
                
                scope.selectionParams = {
                    name: undefined,
                    color: '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16)
                }
                
                scope.addSelections = function(){
                    
                    var userselections = scope.limma.datar.results;
                    
                    var step1 = $filter('filter')(scope.limma.datar.results, {
                        id: scope.filterParams.id
                    });
                    
                    var step2 = $filter('filterThreshold')(step1, scope.filterParams.logFold, 'logFoldChange')
                    var step3= $filter('filterThreshold')(step2, scope.filterParams.pValue, 'pValue')
                    var step4 = step3.map(function(d){
                        return d.id
                    })
                    
                    var selectionData = {
                        name: scope.selectionParams.name,
                        properties: {
                            selectionDescription: '',
                            selectionColor:scope.selectionParams.color,                     
                        },
                        keys:step4
                    };
                    
                    dataset.selection.post({
                        datasetName : dataset.datasetName,
                        dimension : "row"
                        
                    }, selectionData, 
                    function(response){
                            scope.$broadcast('SeletionAddedEvent', 'row');
                            var message = "Added " + scope.selectionParams.name + " as new Selection!";
                            var header = "Heatmap Selection Addition";
                             
                            alertService.success(message,header);
                    }, 
                    function(data, status, headers, config) {
                        var message = "Couldn't add new selection. If "
                            + "problem persists, please contact us.";

                         var header = "Selection Addition Problem (Error Code: "
                            + status
                            + ")";
                         
                         alertService.error(message,header);
                    });
                    
                }

                var ctr = -1;
                scope.limmaTableOrdering = undefined;

                scope.reorderLimmaTable = function(header) {

                    ctr = ctr * (-1);
                    if (ctr == 1) {
                        scope.tableOrdering = header.value;
                    } else {
                        scope.tableOrdering = "-"
                                + header.value;
                    }
                }
            }

        };
    }])
    .directive('hierarchicalAccordion',
    [function() {

        return {
            restrict : 'E',
            scope : {
                dataset : "&heatmapDataset",
                analysis : "=analysis"

            },
            templateUrl : '/container/view/elements/hierarchicalAccordion',
            link : function(scope, elems, attr) {

                var padding = 20;
                
                var labelsGutter = 50;
                
                var panel = {
                    height : 200 + padding,
                    width : pageWidth = 700,
                };

                var Cluster = d3.layout
                    .cluster()
                    .sort(null)
                    .separation(
                            function(a, b) {
                                return a.parent == b.parent
                                        ? 1
                                        : 1
                            })
                    .value(
                            function(d) {
                                return d.distance;
                            })
                    .children(
                            function(d) {
                                return d.children;
                            });

                

                var xPos = d3.scale
                        .linear()
                        .domain([0, 1])
                        .range([padding, panel.width - padding]);
                
                var yPos = d3.scale
                        .linear()
                        .domain([0, 1])
                        .range([padding, panel.height - padding - labelsGutter]);

                function Path(d) {
                    // Path function builder for TOP
                    // heatmap tree path attribute

                    return "M"
                            + (xPos(d.target.x))
                            + ","
                            + (yPos(d.target.y))
                            + "V"
                            + (yPos(d.source.y))
                            + "H"
                            + (xPos(d.source.x));

                };
                
                function noder(d) {

                    var a = [];

                    if (!d.children) {
                        a.push(d);
                    } else {
                        d.children.forEach(function(child) {
                           noder(child).forEach(function(j) {
                               a.push(j)
                           });
                        });
                    };

                    return a;
                };

                function drawAnalysisTree(canvas, cluster, tree, type) {

                    canvas.selectAll('*')
                            .remove();
                    
                    var nodes = cluster
                            .nodes(tree);
                    var links = cluster
                            .links(nodes);

                    var labels = noder(tree);

                    if (labels.length <= 50) {
                        canvas
                                .selectAll(
                                        "text")
                                .data(labels)
                                .enter()
                                .append(
                                        "text")

                                .attr(
                                        "transform",
                                        function(d) {
                                            return "translate("
                                                    + xPos(d.x)
                                                    + ","
                                                    + yPos(d.y)
                                                    + ")rotate(90)"
                                        })
                                .attr(
                                        "text-anchor",
                                        "start")
                                .attr("dx", 5)
                                .attr("dy", 3)
                                .text(
                                        function(d) {
                                            return d.name
                                        })

                    };

                    canvas
                        .selectAll("path")
                        .data(links)
                        .enter()
                        .append("path")
                        .attr("d",function(d) {
                            return Path(d)
                        })
                        .attr("stroke", function() {
                            return "grey"
                        }).attr( "fill", "none");

                    canvas.selectAll( "circle").data(nodes)
                    .enter()
                    .append("circle")
                    .attr("r", 2.5)
                    .attr("cx",function(d) {
                        return xPos(d.x);
                    })
                    .attr("cy", function(d) {
                        return yPos(d.y);
                    })
                    .attr("fill", function(d) {
                        return "red"
                    })
                    .on("click", function(d) {
                        //
                    });

                };
                scope.$watch('analysis',  function(newval, oldval) {
                    if (newval) {
                        
                        d3.select(elems[0]).select('div#svgPlace').append('svg');
                        
                        var svg = d3.select(elems[0]).select('div#svgPlace').select('svg')

                        svg.attr({
                            width : panel.width,
                            height : panel.height + padding
                        });
                        
                        svg.append("g");
                        
                        var panelWindow = d3
                            .select(elems[0])
                            .select("svg")
                            .select("g");
                        
                        

                        drawAnalysisTree(
                                panelWindow,
                                Cluster,
                                newval.root,
                                "horizontal");
                    }
                });

            } // end link
        };

    }])
    
	
});