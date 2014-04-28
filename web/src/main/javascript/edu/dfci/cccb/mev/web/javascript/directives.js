define(
        ['angular', 'jquery', 'd3', 'services', 'colorbrewer/ColorBrewer'],
        function(angular, jq, d3) {

            return angular
                    .module('myApp.directives', ['d3colorBrewer'])
                    .directive('appVersion',
                            ['appVersion', function(version) {
                                return function(scope, elm, attrs) {
                                    elm.text(version);
                                };
                            }])
                    .directive('appName', ['appName', function(name) {
                        return function(scope, elm, attrs) {
                            elm.text(name);
                        };
                    }])
                    .directive(
                            'mainNavigation',
                            [
                                    'mainMenuBarOptions',
                                    function(opts) {
                                        return {
                                            restrict : 'A',
                                            templateUrl : '/container/view/elements/mainNavigation',
                                            link : function(scope) {
                                                scope.menu = opts;
                                            }
                                        };
                                    }])
                    .directive(
                            'heatmapNavigation',
                            [function() {
                                return {
                                    restrict : 'A',
                                    templateUrl : '/container/view/elements/heatmapNavigation'
                                };
                            }])
                    .directive(
                            'heatmapPanels',
                            [
                                    '$routeParams',
                                    function($routeParams) {
                                        return {
                                            restrict : 'A',
                                            templateUrl : '/container/view/elements/heatmapPanels',
                                            link : function(scope, elems, attrs) {
                                                
                                                document.title = "MeV: "
                                                        + $routeParams.datasetName;

                                                

                                                var rightPanel = jq('#rightPanel'),
                                                    leftPanel = jq('#leftPanel'),
                                                    centerTab = jq('div.tab'),
                                                    leftTab = jq('div.tab#tab-left'),
                                                    rightTab = jq('div.tab#tab-right'),
                                                    pageWidth = jq('body').width() - 50,
                                                    panelCycle = 1;

                                                var isDragging = false;

                                                centerTab
                                                .mousedown(function(mouse) {
                                                    isDragging = true;
                                                    mouse
                                                            .preventDefault();
                                                });

                                                jq(document).mouseup(
                                                    function() {
                                                        isDragging = false;
                                                    })
                                                .mousemove(function(mouse) {
                                                    if (isDragging && mouse.pageX < pageWidth
                                                                    * (9 / 10)
                                                            && mouse.pageX > 0) {
                                                        showSidePanel = 1;
                                                        leftPanel.css("width", mouse.pageX);
                                                        rightPanel.css( "width", pageWidth
                                                                                - mouse.pageX);
                                                            
                                                        leftPanel.children().show();
                                                    }

                                                    if (isDragging && mouse.pageX < pageWidth * (1 / 7)
                                                            && mouse.pageX > 0) {
                                                        leftPanel.children().hide();
                                                        jq('div.tab#tab-left').click()
                                                    }

                                                });
                                                
                                                leftTab.on("click", function(e){
                                                    collapseSidePanel();
                                                });
                                                
                                                rightTab.on("click", function(e){
                                                    expandSidePanel();
                                                });

                                                
                                                function expandSidePanel() { //Double click to expand
                                                    if (panelCycle == 1) { //Middle value to left expand full
                                                        
                                                        
                                                        leftPanel
                                                            .css(
                                                                    "width",
                                                                    pageWidth* (9 / 10));
                                                        leftPanel
                                                            .children()
                                                            .show();
                                                        
                                                        rightPanel
                                                            .css(
                                                                    "width",
                                                                    pageWidth *(1- (9 / 10) ) );
                                                        
                                                        panelCycle = 2;
                                                        
                                                    } else if (panelCycle == 0){
                                                        
                                                        leftPanel
                                                            .css("width", pageWidth* (3 / 10));
                                                        
                                                        leftPanel
                                                            .children()
                                                            .show();
                                                        
                                                        rightPanel.css("width",
                                                                    pageWidth *(1- (3 / 10) ) );
                                                        
                                                        panelCycle = 1;
                                                    }
                                                };

                                                function collapseSidePanel() { //Click to close

                                                    if (panelCycle == 1) { //Middle value to left close
                                                        
                                                        
                                                        leftPanel
                                                                .css(
                                                                        "width",
                                                                        0);
                                                        leftPanel
                                                                .children()
                                                                .hide();
                                                        rightPanel
                                                                .css(
                                                                        "width",
                                                                        pageWidth - 30);
                                                        panelCycle = 0;
                                                        
                                                        
                                                    } else if (panelCycle == 2) { //Left expanded full to middle
 
                                                        leftPanel
                                                            .css("width", pageWidth* (3 / 10));
                                                        
                                                        leftPanel
                                                            .children()
                                                            .show();
                                                        
                                                        rightPanel.css("width",pageWidth *(1- (3 / 10) ) );
                                                        
                                                        panelCycle = 1;
                                                    }

                                                };

                                                scope.showLimmaTables = true;

                                            }
                                        };
                                    }])
                    .directive(
                            'sideNavigationBar',
                            [function() {
                                return {
                                    restrict : 'E',
                                    templateUrl : '/container/view/elements/sideNavigationBar',
                                    link : function(scope) {

                                        scope.clusterAnalysisClickOpen = function(id) {

                                            jq('a#clustersTabLink')
                                                    .trigger("click");

                                            jq(id.href).collapse(
                                                    "show");

                                            jq('div.fixed-height')
                                                    .animate(
                                                            {
                                                                scrollTop : jq(
                                                                        id.dataParent)
                                                                        .offset().top
                                                            }, 200);
                                        }

                                        scope.limmaAnalysisClickOpen = function(id) {

                                            jq('a#limmaTabLink')
                                                    .trigger("click");

                                            jq(id.href).collapse(
                                                    "show");

                                            jq('div.fixed-height')
                                                    .animate(
                                                            {
                                                                scrollTop : jq(
                                                                        id.dataParent)
                                                                        .offset().top
                                                            }, 200);
                                        }
                                    }
                                };
                            }])
                            .directive('limmaAccordionList',[function() {
                                    return {
                                        
                                        restrict : 'E',
                                        templateUrl : '/container/view/elements/limmaAccordionList'
                                        
                                    };

                            }])
                            .directive(
                            'limmaAccordion',
                            ['$filter', '$routeParams', '$http', 'alertService', function($filter, $routeParams, $http, alertService) {
                                return {
                                    restrict : 'E',
                                    templateUrl : '/container/view/elements/limmaAccordion',
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
                                            
                                            
                                            $http({
                                                method:"POST", 
                                                url:"/dataset/" + $routeParams.datasetName + "/" 
                                                + 'row' 
                                                + "/selection",
                                                data:{
                                                    name: scope.selectionParams.name,
                                                    properties: {
                                                        selectionDescription: '',
                                                        selectionColor:scope.selectionParams.color,                     
                                                    },
                                                    keys:step4
                                                }
                                        
                                            })
                                            .success(function(response){
                                                    scope.$emit('SeletionAddedEvent', 'row');
                                                    var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                                    var header = "Heatmap Selection Addition";
                                                     
                                                    alertService.success(message,header);
                                            })
                                            .error(function(data, status, headers, config) {
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
                            .directive('tTestAccordionList',[function() {
                                    return {
                                        
                                        restrict : 'E',
                                        templateUrl : '/container/view/elements/tTestAccordionList'
                                        
                                    };

                            }])
                            .directive(
                            'tTestAccordion',
                            ['$filter', '$routeParams', '$http', 'alertService', function($filter, $routeParams, $http, alertService) {
                                return {
                                    restrict : 'E',
                                    templateUrl : '/container/view/elements/tTestAccordion',
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
                                            
                                            
                                            $http({
                                                method:"POST", 
                                                url:"/dataset/" + $routeParams.datasetName + "/" 
                                                + 'row' 
                                                + "/selection",
                                                data:{
                                                    name: scope.selectionParams.name,
                                                    properties: {
                                                        selectionDescription: '',
                                                        selectionColor:scope.selectionParams.color,                     
                                                    },
                                                    keys:step4
                                                }
                                        
                                            })
                                            .success(function(response){
                                                    scope.$emit('SeletionAddedEvent', 'row');
                                                    var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                                    var header = "Heatmap Selection Addition";
                                                     
                                                    alertService.success(message,header);
                                            })
                                            .error(function(data, status, headers, config) {
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
                            }])
                            .directive(
                                'hclAccordionList',
                                [function() {
                                    return {
                                        restrict : 'E',
                                        templateUrl : '/container/view/elements/hclAccordionList'                                    
                                    };
                            }])
                            .directive(
                                'kmeansAccordionList',
                                [function() {
                                    return {
                                        restrict : 'E',
                                        templateUrl : '/container/view/elements/kmeansAccordionList'                                    
                                    };
                            }])
                            .directive(
                                'anovaAccordionList',
                                [function() {
                                    return {
                                        restrict : 'E',
                                        templateUrl : '/container/view/elements/anovaAccordionList'                                    
                                    };
                            }])
                            .directive(
                                'anovaAccordion',
                                ['$filter', '$routeParams', '$http', 'alertService', function($filter, $routeParams, $http, alertService) {
                                    return {
                                        restrict : 'E',
                                        templateUrl : '/container/view/elements/anovaAccordion',
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

                                                    $http({
                                                        method:"POST", 
                                                        url:"/dataset/" + $routeParams.datasetName + "/" 
                                                        + 'row' 
                                                        + "/selection",
                                                        data:{
                                                            name: scope.selectionParams.name,
                                                            properties: {
                                                                selectionDescription: '',
                                                                selectionColor:scope.selectionParams.color,                     
                                                            },
                                                            keys:step3
                                                        }

                                                    })
                                                    .success(function(response){
                                                            scope.$emit('SeletionAddedEvent', 'row');
                                                            var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                                            var header = "Heatmap Selection Addition";
                                                    
                                                            scope.selectionParams.color = '#'+Math
                                                                .floor(Math.random()*0xFFFFFF<<0)
                                                                .toString(16)

                                                            alertService.success(message,header);
                                                    })
                                                    .error(function(data, status, headers, config) {
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
                            
                            .directive(
                                'kmeansAccordion',
                                [function() {
                                    return {
                                        restrict : 'E',
                                        templateUrl : '/container/view/elements/kmeansAccordion',
                                        link: function(scope){

                                            return
                                        }
                                    };
                            }])
                            .directive(
                                    'expressionPanel',
                            [
                                    '$routeParams',
                                    function($routeParams) {
                                        return {
                                            restrict : 'AC',
                                            templateUrl : '/container/view/elements/expressionPanel',                                            
                                            link : function(scope) {

                                                scope.datasetName = $routeParams.datasetName;

                                            }
                                        };
                                    }])
                    .directive('bsprevanalysis', function() {

                        return {

                            restrict : 'C',
                            scope : {

                                bindid : '@',
                                parentid : '@',
                                header : '@',
                                data : '@'

                            }
                        };

                    })
                    .directive(
                            'bsTable',
                            function() {

                                return {
                                    scope : {
                                        data : "="
                                    },
                                    restrict : 'E',
                                    templateUrl : "/container/view/elements/table"

                                };

                            })
                    .directive(
                            'bsImgbutton',
                            function() {

                                return {
                                    scope : {
                                        icon : "@",
                                        title : "@",
                                        align : "@"
                                    },
                                    restrict : 'E',
                                    template : "<button class='btn btn-success pull-{{align}}' "
                                            + "title='{{title}}'>  "
                                            + "<i class='icon-{{icon}}'></i> Download"
                                            + "</button>"

                                };

                            })
                    .directive(
                            'prevlimma',
                            function() {

                                return {

                                    restrict : 'C',
                                    templateUrl : "/container/view/elements/prevlimmashell"

                                };

                            })
                    .directive(
                    'bsmodal',
                    [
                            '$compile',
                            function($compile) {

                                return {

                                    restrict : 'E',
                                    scope : {

                                        bindid : '@',
                                        header : '@',
                                        test : '@',
                                        func : '&'

                                    },
                                    transclude : true,
                                    templateUrl : "/container/view/elements/modal"

                                };

                    }])
                  
                    .directive(
                            'uploadsTable',
                            [function() {
                                return {
                                    restrict : 'A',
                                    scope : {
                                        uploads : '='
                                    },
                                    templateUrl : '/container/view/elements/uploadsTable',
                                    link : function(scope, elems, attrs) {

                                        scope.datasets = [];

                                        scope
                                                .$watch(
                                                        "uploads",
                                                        function(newVal, oldVal) {
                                                            if (oldVal != undefined) {
                                                                scope.datasets = newVal
                                                            }
                                                        })

                                    }
                                }
                            }])
                    .directive(
                            'uploadDrag',
                            [ function(){

                                return {
                                    restrict : 'C',
                                    templateUrl : '/container/view/elements/uploadDragAndDrop',
                                    link : function(scope, elems, attrs) {

                                        jq('#upload-button')
                                                .click(
                                                        function() {
                                                            jq(
                                                                    '#upload-input')
                                                                    .click();
                                                        });

                                        jq('#upload-input')
                                                .on(
                                                        "change",
                                                        function() {

                                                            var input = document
                                                                    .getElementById('upload-input'), files = new Array();

                                                            for ( var i = 0; i < input.files.length; i++) {
                                                                files
                                                                        .push(input.files[i]);

                                                                if (files.length == input.files.length) {
                                                                    files
                                                                            .map(function(file) {

                                                                                var formdata = new FormData;
                                                                                formdata
                                                                                        .append(
                                                                                                'upload',
                                                                                                file);
                                                                                formdata
                                                                                        .append(
                                                                                                'name',
                                                                                                file.name);
                                                                                var xhr = new XMLHttpRequest();

                                                                                xhr.upload
                                                                                        .addEventListener(
                                                                                                "progress",
                                                                                                function(e) {
                                                                                                    return;
                                                                                                });

                                                                                xhr.onreadystatechange = function() {
                                                                                    if (xhr.readyState == 4
                                                                                            && xhr.status == 200) {

                                                                                        scope
                                                                                                .loadUploads();

                                                                                    };
                                                                                };

                                                                                xhr
                                                                                        .open(
                                                                                                "POST",
                                                                                                "/dataset",
                                                                                                true);
                                                                                xhr
                                                                                        .send(formdata);
                                                                            });
                                                                };
                                                            };

                                                        });

                                    }
                                };

                            }])
                    .directive(
                            'datasetSummary',
                            function() {
                                return {
                                    restrict : 'A',
                                    scope : {
                                        datasetobj : "&"
                                    },
                                    templateUrl : '/container/view/elements/datasetSummary',
                                };

                            })
                    .directive(
                            'd3RadialTree',
                            [function() {

                                return {
                                    restrict : 'A',
                                    scope : {
                                        data : '=',
                                        diameter : '@'

                                    },
                                    templateUrl : '/container/view/elements/d3RadialTree',
                                    link : function(scope, elems, attr) {

                                        function noder(d) {

                                            var a = [];

                                            if (!d.children) {
                                                a.push(d);
                                            } else {
                                                d.children
                                                        .forEach(function(child) {
                                                            noder(
                                                                    child)
                                                                    .forEach(
                                                                            function(j) {
                                                                                a
                                                                                        .push(j)
                                                                            });
                                                        });
                                            };

                                            return a;
                                        };

                                        var padding = 20;
                                        var dendogram = {
                                            height : 200 + padding,
                                            width : pageWidth = jq(
                                                    'body').width()
                                                    * (2 / 5) // Nicely define
                                        // width
                                        };

//                                        d3
//                                                .select(elems[0])
//                                                .append("svg")
//                                                .attr(
//                                                        {
//                                                        	id : attr.id,
//                                                            width : dendogram.width,
//                                                            height : (dendogram.height + (padding))
//                                                        });

                                        var svg = d3.select(elems[0])
                                                .select("svg");
                                        svg.attr(
                                              {
                                            	id : attr.id,
                                                width : dendogram.width,
                                                height : (dendogram.height + (padding))
                                            });
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

                                        var labelsGutter = 50;
                                        var g = svg.append("g");
                                        g.attr('class',
                                        'smallDendogram');
                                        g.attr('opacity', '1');

                                        var dendogramWindow = d3
                                                .select(elems[0])
                                                .select("svg")
                                                .select(
                                                        "g.smallDendogram")

                                        var xPos = d3.scale
                                                .linear()
                                                .domain([0, 1])
                                                .range(
                                                        [
                                                                padding,
                                                                dendogram.width
                                                                        - padding])
                                        var yPos = d3.scale
                                                .linear()
                                                .domain([0, 1])
                                                .range(
                                                        [
                                                                padding,
                                                                dendogram.height
                                                                        - padding
                                                                        - labelsGutter])

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

                                            }

                                            canvas
                                                    .selectAll("path")
                                                    .data(links)
                                                    .enter()
                                                    .append("path")
                                                    .attr(
                                                            "d",
                                                            function(d) {
                                                                return Path(d)
                                                            })
                                                    .attr(
                                                            "stroke",
                                                            function() {
                                                                return "grey"
                                                            }).attr(
                                                            "fill",
                                                            "none");

                                            canvas
                                                    .selectAll(
                                                            "circle")
                                                    .data(nodes)
                                                    .enter()
                                                    .append("circle")
                                                    .attr("r", 2.5)
                                                    .attr(
                                                            "cx",
                                                            function(d) {
                                                                return xPos(d.x);
                                                            })
                                                    .attr(
                                                            "cy",
                                                            function(d) {
                                                                return yPos(d.y);
                                                            })
                                                    .attr(
                                                            "fill",
                                                            function(d) {
                                                                return "red"
                                                            })
                                                    .on(
                                                            "click",
                                                            function(d) {
                                                                //
                                                            });

                                        };
                                        scope
                                                .$watch(
                                                        'data',
                                                        function(newval, oldval) {
                                                            if (newval) {
                                                                drawAnalysisTree(
                                                                        dendogramWindow,
                                                                        Cluster,
                                                                        newval.root,
                                                                        "horizontal");
                                                            }
                                                        });

                                    } // end link
                                };

                            }])
                    .directive(
                            'bsTable',
                            function() {

                                return {
                                    scope : {
                                        data : "="
                                    },
                                    restrict : 'E',
                                    templateUrl : "/container/view/elements/table.html"

                                };

                            })
                    

});