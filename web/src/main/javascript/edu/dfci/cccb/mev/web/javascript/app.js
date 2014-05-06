define(['angular', 
        'angularResource', 
        'angularRoute', 
        'directives', 
        'services', 
        'controllers', 
        'setmanager/SetManager',
        'uiBootstrapTpls',
        'ngGrid',
        'heatmap/Heatmap',
        'heatmapvisualization/HeatmapVisualization',
        'analysisaccordioncollection/AnalysisAccordionCollection',
        'analysismodalcollection/AnalysisModalCollection',
        'mainmenu'
        ], function(angular){
	'use strict';
	return angular.module('myApp', [
	     'ngRoute',
	     'ngResource',	     
	     'myApp.directives', 
	     'myApp.services',
	     'myApp.controllers',
	     'Mev.SetManager',
	     'Mev.PresetManager',
	     'ui.bootstrap',
	     'ngGrid',
	     'Mev.heatmap',
	     'Mev.heatmapvisualization',
	     'Mev.AnalysisAccordionCollection',
	     'Mev.AnalysisModalCollection',
	     'Mev.MainMenuModule'
	     ])
	.config(['$routeProvider', '$locationProvider', '$sceProvider', function($routeProvider, $locationProvider, $sceProvider) {
		
	  $sceProvider.enabled(false);
	  $routeProvider
		  .when('/dataset', {
			  templateUrl: '/container/view/partials/heatmap', 
			  controller: 'HeatmapCtrl'
		  })
		  .when('/dataset/:datasetName', {
			  templateUrl: '/container/view/partials/heatmap', 
			  controller: 'HeatmapCtrl'
		  })
		  .when('/datasets', {
              templateUrl: '/container/view/partials/importItems',
              controller: 'ImportsCtrl'
          })
          .when('/home', {
              templateUrl:'/container/view/partials/home'
          });
		  
		  $routeProvider.otherwise({redirectTo: '/home'});
		  
		  //$locationProvider.html5Mode(true).hashPrefix('!');
		  
		}]);
	
});