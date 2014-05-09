define(['angular', 'jquery', 'd3', 'alertservice/AlertService'], function(angular, jq, d3){
	
	return angular.module('Mev.AnalysisAccordionCollection', ['Mev.AlertService'])
	.directive('analysisContentItem', ['$compile', function ($compile) {
        var heirarchicalTemplate = '<hierarchical-Accordion analysis="analysis" project="project"></hierarchical-Accordion>';
        var kMeansTemplate = '<k-Means-Accordion analysis="analysis" project="project"></k-Means-Accordion>';
        var anovaTemplate = '<anova-Accordion analysis="analysis" project="project"></anova-Accordion>';
        var tTestTemplate = '<t-Test-Accordion analysis="analysis" project="project"></t-Test-Accordion>';
        var limmaTemplate  = '<limma-Accordion analysis="analysis" project="project"></limma-Accordion>';
        
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
    
        return {
            restrict: "E",
            rep1ace: true,
            scope: {
                analysis : '=analysis',
                project : '=project',
            },
            link: function(scope, element, attrs) {

                element.append(getTemplate(scope.analysis.type));
        
                $compile(element.contents())(scope);
            }
        };
    }])
	.directive('kMeansAccordion', [function() {
        return {
            restrict : 'E',
            scope : {
            	analysis : "=analysis",
            	project : "=project"
            },
            templateUrl : '/container/view/elements/kmeansAccordion',
            link : function(scope) {
                
                function traverse(clusters){
                    
                    var labels = []
                    
                    for (var i = 0; i < clusters.length; i++) {
                        labels = labels.concat(clusters[i]);
                    };
                    
                    return labels
                }
                
                scope.applyToHeatmap=function(){
                    
                    var labels = traverse(scope.analysis.clusters);
                    
                    if (scope.analysis.dimension == "column"){

                        scope.project.generateView({
                            viewType:'heatmapView', 
                            labels:{
                                row:{keys:scope.project.dataset.row.keys}, 
                                column:{keys:labels}
                            },
                            expression:{
                                min: scope.project.dataset.expression.min,
                                max: scope.project.dataset.expression.max,
                                avg: scope.project.dataset.expression.avg,
                            },
                            panel : {top: scope.analysis}
                        });
                        
                    } else {
                        scope.project.generateView({
                            viewType:'heatmapView', 
                            labels:{
                                column:{keys:scope.project.dataset.column.keys}, 
                                row:{keys:labels}
                            },
                            expression:{
                                min: scope.project.dataset.expression.min,
                                max: scope.project.dataset.expression.max,
                                avg: scope.project.dataset.expression.avg,
                            },
                            panel: {side: scope.analysis}
                        });
                    }
                    
                };
                
            }
        };
    }])
    .directive('anovaAccordion',
    ['$filter', 'alertService', function($filter, alertService) {
        return {
            restrict : 'E',
            templateUrl : '/container/view/elements/anovaAccordion',
            scope : {
            	analysis : "=analysis",
            	project : '=project',
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
                    color: '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16),
                    dimension:'row'
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
                };
                    
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
                    
                    scope.project.dataset.selection.post({
                        datasetName : scope.project.dataset.datasetName,
                        dimension : scope.selectionParams.dimension

                    }, selectionsData,
                    function(response){
                            
                            scope.project.dataset.resetSelections('row')
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
                
                function traverse (results) {
                    var step1 = $filter('filter')(results, {
                        id: scope.filterParams.id
                    });

                    var step2 = $filter('filterThreshold')(step1, scope.filterParams.pValue, 'pValue')
                    
                    var step3 = step2.map(function(d){
                        return d.id
                    })
                    
                    return step3;
                }
                
                scope.applyToHeatmap=function(){
                    
                    var labels = traverse(scope.analysis.results);

                    scope.project.generateView({
                        viewType:'heatmapView', 
                        labels:{
                            column:{keys:scope.project.dataset.column.keys}, 
                            row:{keys:labels}
                        },
                        expression:{
                            min: scope.project.dataset.expression.min,
                            max: scope.project.dataset.expression.max,
                            avg: scope.project.dataset.expression.avg,
                        }
                    });

                    
                };
            }
        };
    }])
    .directive('tTestAccordion',
    ['$filter', 'alertService', function($filter, alertService) {
        return {
            restrict : 'E',
            templateUrl : '/container/view/elements/tTestAccordion',
            scope : {
            	project : "=project",
            	analysis : "=analysis"
            },
            link : function(scope) {

                scope.$watch('analysis', function(newval){
                    if (newval){
                        scope.tTest = scope.analysis;
                    }
                });
                
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
                    
                    scope.project.dataset.selections.post({
                        datasetName : dataset.datasetName,
                        dimension : "row"
                
                    }, selectionData, 
                    function(response){
                            scope.project.dataset.resetSelections('row')
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
                };
                
                function traverse (results) {
                    var step1 = $filter('filter')(results, {
                        id: scope.filterParams.id
                    });
                                                                
                    var step2= $filter('filterThreshold')(step1, scope.filterParams.pValueThreshold, 'pValue');
                    var step3= $filter('filterThreshold')(step2, scope.filterParams.logFoldChange, 'logFoldChange');
                    var step4 = step3.map(function(d){
                        return d.id
                    })
                    
                    return step4;
                }
                
                scope.applyToHeatmap=function(){
                    
                    var labels = traverse(scope.tTest.results);

                    scope.project.generateView({
                        viewType:'heatmapView', 
                        labels:{
                            column:{keys:scope.project.dataset.column.keys}, 
                            row:{keys:labels}
                        },
                        expression:{
                            min: scope.project.dataset.expression.min,
                            max: scope.project.dataset.expression.max,
                            avg: scope.project.dataset.expression.avg,
                        }
                    });

                    
                };
                
                
            }

        };
    }]).directive('limmaAccordion',
    ['$filter', 'alertService', function($filter, alertService) {
        return {
            restrict : 'E',
            templateUrl : '/container/view/elements/limmaAccordion',
            scope : {
                project : '=project',
            	analysis : "=analysis"
            },
            link : function(scope) {
                
                scope.headers = [
	               {'name':'ID', 'field': "id", 'icon': "search"},
	               {'name':'Log-Fold-Change', 'field': "logFoldChange", 'icon': ">="},
	               {'name':'Average Expression', 'field': "averageExpression", 'icon': "none"},
	               {'name':'P-Value', 'field': "pValue", 'icon': "<="},
	               {'name':'q-Value', 'field' : "qValue", 'icon': "none"}
               ];
                
                scope.filterParams = {
	                'id' : {
	                	field: 'id',
	                	value: undefined,
	                	op: "="
	                },
	                'logFoldChange' : {
	                	field: 'logFoldChange',
	                	value: undefined,
	                	op: '>='
	                },
	                'pValue' : {
	                	field: 'pValue',
	                	value: 0.05,
	                	op: '<='
	                },
	                'qValue':{
	                	field: 'qValue',
	                	value: undefined,
	                	op: '<='
	                }
                }
                scope.filteredResults=undefined;
                scope.selectionParams = {
                    name: undefined,
                    color: '#'+Math.floor(Math.random()*0xFFFFFF<<0).toString(16)
                }
                
                console.log
                scope.addSelections = function(){
                    
                    var userselections = scope.analysis.results;
                                        
                    var selectionData = {
                        name: scope.selectionParams.name,
                        properties: {
                            selectionDescription: '',
                            selectionColor:scope.selectionParams.color,                     
                        },
                        keys:scope.filteredResults
                    };
                    
                    
                    
                    scope.project.dataset.selection.post({
                        datasetName : scope.project.dataset.datasetName,
                        dimension : "row"
                        
                    }, selectionData, 
                    function(response){
                            scope.project.dataset.resetSelections('row')
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
                        scope.tableOrdering = header.field;
                    } else {
                        scope.tableOrdering = "-"
                                + header.field;
                    }
                }
                
                function traverse (results) {
                    var step1 = $filter('filter')(results, {
                        id: scope.filterParams.id.value
                    });
                    
                    var step2 = $filter('filterThreshold')(step1, scope.filterParams.logFoldChange.value, 'logFoldChange')
                    var step3= $filter('filterThreshold')(step2, scope.filterParams.pValue.value, 'pValue')
                    var step4 = step3.map(function(d){
                        return d.id
                    })
                    
                    return step4;
                }
                
                scope.applyFilter = function (results) {
                	
                    var filtered = $filter('filter')(results, {
                        id: scope.filterParams.id.value
                    });
                    
                    var filtered = $filter('filterThreshold')(filtered, scope.filterParams.logFoldChange.value, scope.filterParams.logFoldChange.field, scope.filterParams.logFoldChange.op);
                    filtered= $filter('filterThreshold')(filtered, scope.filterParams.pValue.value, scope.filterParams.pValue.field);
                    filtered= $filter('filterThreshold')(filtered, scope.filterParams.pValue.value, scope.filterParams.pValue.field);
                    filtered = $filter('orderBy')(filtered, scope.tableOrdering);
                    scope.filteredResults = filtered;
                    
                    console.debug("scope.filterParams", scope.filterParams);
                    console.debug("filteredResults.length", scope.filteredResults.length);
                    
                    return scope.filteredResults;
                }
                
                scope.applyToHeatmap=function(){
                    
                    var labels = scope.filteredResults.map(function(d){
                    	return d.id;
                    });

                    scope.project.generateView({
                        viewType:'heatmapView', 
                        labels:{
                            column:{keys:scope.project.dataset.column.keys}, 
                            row:{keys:labels}
                        },
                        expression:{
                            min: scope.project.dataset.expression.min,
                            max: scope.project.dataset.expression.max,
                            avg: scope.project.dataset.expression.avg,
                        }
                    });

                    
                };
            }

        };
    }])
    .directive('hierarchicalAccordion',
    [function() {

        return {
            restrict : 'E',
            scope : {
                analysis : "=analysis",
                project : "=project"

            },
            templateUrl : '/container/view/elements/hierarchicalAccordion',
            link : function(scope, elems, attr) {
                
                scope.applyToHeatmap=function(){
                    
                    var labels = traverse(scope.analysis.root);
                    
                    if (scope.analysis.dimension == "column"){

                        scope.project.generateView({
                            viewType:'heatmapView', 
                            labels:{
                                row:{keys:scope.project.dataset.row.keys}, 
                                column:{keys:labels}
                            },
                            expression:{
                                min: scope.project.dataset.expression.min,
                                max: scope.project.dataset.expression.max,
                                avg: scope.project.dataset.expression.avg,
                            },
                            panel : {top: scope.analysis}
                        });
                        
                    } else {
                        scope.project.generateView({
                            viewType:'heatmapView', 
                            labels:{
                                column:{keys:scope.project.dataset.column.keys}, 
                                row:{keys:labels}
                            },
                            expression:{
                                min: scope.project.dataset.expression.min,
                                max: scope.project.dataset.expression.max,
                                avg: scope.project.dataset.expression.avg,
                            },
                            panel: {side: scope.analysis}
                        });
                    }
                    
                };
                
                function traverse(tree) {
                    
                    var leaves = {
                        '0':[],
                        '1':[]
                    };
                        
                    if (tree.children.length > 0){
                        for (var i = 0; i < tree.children.length; i++){
                            leaves[i] = (!tree.children[i].children) ? [tree.children[i].name] : traverse(tree.children[i])
                        }
                    };

                    return leaves[0].concat(leaves[1]);
                };

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
                    
                    var nodes = cluster.nodes(tree);

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