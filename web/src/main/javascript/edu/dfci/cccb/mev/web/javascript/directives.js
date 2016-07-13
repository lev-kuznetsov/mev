define(
        ['angular', 'jquery', 'd3', 'colorbrewer/ColorBrewer'],
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
                                    templateUrl : "/container/view/elements/modal",
                                    compile: function(tElem, tAttrs){                                    	
                                    	return {
                                    		post: function(scope, elem, attrs, ctrl){
                                    			var rootElement = angular.element("body > ui-view");
                                    			
                                    			if(rootElement.length===0)
                                    				rootElement = angular.element("body > ng-view");
                                    			
                                    			if(rootElement.length===1){
                                    				var exists = rootElement.children("[bindid='"+scope.bindid+"']");                                    				
                                    				if(exists.length>0){                                    					                                    				
                                    					//we already have this modal, remove to avoid duplicates                                     					
                                    					exists.html('').remove();
                                    					console.debug("BSMODAL remove", attrs.bindid, rootElement.children("[bindid='"+scope.bindid+"']").length);
                                    				}
                                    				console.debug("BSMODAL appaned", attrs.bindid);
                                					rootElement.append(elem);
                                    			}
                                    			
                                    		}
                                    	};
                                    }
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
                                                                scope.datasets = newVal.map(function(dataset){
                                                                    return dataset.id;
                                                                });
                                                            }
                                                        })

                                    }
                                }
                            }])
                    .directive(
                            'uploadDrag',
                            [ "DatasetResourceService", function(DatasetResource){

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
                                                                    files.map(function(file){
                                                                        DatasetResource.uploadFile(file);
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