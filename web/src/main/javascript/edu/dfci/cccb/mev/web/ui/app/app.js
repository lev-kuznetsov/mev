define(["ng",
       "angular-animate",
       "angular-ui-router",        
       "bootstrap",
       "angular-ui-bootstrap", 
       "angular-route",  
       "app/views/views.module",
       "app/widgets/widgets.module",
       "app/utils/utils.module",
       "app/domain/domain.module",
       
       'ng-grid',
       'browser-filesaver',
       'angular-utils-pagination',
       'angular-utils-ui-breadcrumbs',        
       'js-data-angular',
       'pouchdb',
       'blob-util',
       'angular-nvd3',
       'ag-grid',
       'crossfilter',
       
       'js/directives', 
       'js/services', 
       'js/controllers', 
       'js/setmanager/SetManager',                
       'js/heatmap/Heatmap',
       'js/heatmapvisualization/HeatmapVisualization',
       'js/analysisaccordioncollection/AnalysisAccordionCollection',
       'js/analysismodalcollection/AnalysisModalCollection',
       'js/viewCollection/ViewCollection',
       'js/geneboxplotvisualization/GeneBoxPlotVisualization',
       'js/orefine/OrefineBridge',
       'mainmenu',
       "geods",
       "js/clinicalSummary/ClinicalSummary.package",
       'js/cohortanalysis/CohortAnalysis',        
       'js/setmanager/SetManager',
       'js/presets/PresetManager',
       'js/mainpanel/MainPanel'
        
        ], function(ng){
	"use strict";
	return ng.module("ngbootstrap-app", ["ngAnimate",
	                                     "ui.bootstrap",
	                                     "ngResource",
	                                     "ui.router",
	                                     "mui.domain",
	                                     "mui.views",
	                                     "mui.components",
	                                     "mui.utils",
	                                     "mui.domain.navigator",
	                                     
	                                     'ngGrid',
	                                     'angularUtils.directives.dirPagination',
	                                     'angularUtils.directives.uiBreadcrumbs',
	                                     'js-data',
	                                     'nvd3',
	                                     'agGrid',
	                                     
	                                     'myApp.directives', 
	                            	     'myApp.services',
	                            	     'myApp.controllers',
	                            	     'Mev.SetManager',
	                            	     'Mev.PresetManager',
	                            	     'Mev.heatmap',
	                            	     'Mev.heatmapvisualization',
	                            	     'Mev.AnalysisAccordionCollection',
	                            	     'Mev.AnalysisModalCollection',
	                            	     'Mev.ViewCollection',
	                            	     'Mev.MainMenuModule',
	                            	     'Mev.GeneBoxPlotVisualization',
	                            	     'Mev.GeodsModule',
	                            	     'Mev.ClinicalSummary',
	                            	     'Mev.CohortAnalysis'
	                            	     
	                                     ])
	.config(['$stateProvider', '$urlRouterProvider',
	function($stateProvider, $urlRouterProvider){
		
		//default page is the welcome page
		$urlRouterProvider.when("/", "/welcome");
		$urlRouterProvider.when("", "/welcome");
		
		$stateProvider
		.state("root", {
			templateUrl: "app/views/root/templates/root.tpl.html",
			controller: "RootCtrl",
			controllerAs: "RootCtrl",
			abstract: true, 			
 			breadcrumbProxy: "root.welcome"
 			
		})
		.state("root.about", {
			url: "/about",
			templateUrl: "app/views/about/templates/about.tpl.html",
			parent: "root"
		})
		.state("root.contact", {
			url: "/contact",
			templateUrl: "app/views/contact/templates/contact.tpl.html",
			parent: "root",
			data: {
				sidemenuUrl: "app/views/contact/templates/contact.sidemenu.tpl.html"
			}
		})
//		.state("root.issues", {
//			url: "/issues",
//			templateUrl: "app/views/issues/templates/issues.tpl.html",
//			parent: "root",
//			controller: "IssuesVM",
//			data: {
////				sidemenuUrl: "app/views/issues/templates/issues.sidemenu.tpl.html"
//				sidemenuUrl: "app/views/issues/templates/issues.sidemenu.accordion.tpl.html"
//			}
//		})
		;
	}])
	.config(function($provide) {
//	    $provide.decorator('$httpBackend', function($delegate) {
//	        var proxy = function(method, url, data, callback, headers) {
//	            var interceptor = function(status, data, headers) {
//	                var _this = this,
//	                    _arguments = arguments;
//	                
//	                //most likely returning a template, no delay
//	                if(typeof data==='string' && data.charAt(0)==="<"){		                
//		                callback.apply(_this, arguments);		                
//	                }else{
//	                	setTimeout(function() {
//		                    callback.apply(_this, _arguments);
//		                }, 700);	
//	                }	                
//	            };
//	            return $delegate.call(this, method, url, data, interceptor, headers);
//	        };
//	        for(var key in $delegate) {
//	            proxy[key] = $delegate[key];
//	        }
//	        return proxy;
//	    });
	})	
	.config(['$sceProvider', function($sceProvider) {		
	  $sceProvider.enabled(false);
	}])
	.config(["paginationTemplateProvider", function(paginationTemplateProvider){
		paginationTemplateProvider.setPath('/container/vendor/mbAngularUtils/pagination/dirPagination.tpl.html');
	}])	
	.run(["$rootScope", "$state", "$stateParams",
	function ($rootScope, $state, $stateParams) {
		
//		EndpointConfig();    
	    
	    $rootScope.$state = $state;
	    $rootScope.$stateParams = $stateParams;	    
	    
	    $rootScope.$on('$stateChangeStart', 
            function(event, toState, toParams, fromState, fromParams){ 
//                $("#ui-view").html("");
                $(".page-loading").removeClass("hidden");
        });

        $rootScope.$on('$stateChangeSuccess',
            function(event, toState, toParams, fromState, fromParams){ 
                $(".page-loading").addClass("hidden");
        });

	    
	    
	}]);
	
});