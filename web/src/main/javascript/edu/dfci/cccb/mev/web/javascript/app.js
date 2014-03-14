define(['angular', 
        'angularResource', 
        'angularRoute', 
        'directives', 
        'services', 
        'controllers', 
        'setmanager/SetManager', 
//        'uiBootstrap', 
        'uiBootstrapTpls'], function(angular){
	'use strict';
	return angular.module('myApp', [
	     'ngRoute',
	     'ngResource',
	     'myApp.directives', 
	     'myApp.services',
	     'myApp.controllers',
	     'Mev.SetManager',
	     'Mev.PresetManager',
	     'ui.bootstrap'])
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
		  .when('/home', {
              templateUrl: '/container/view/partials/importItems',
              controller: 'ImportsCtrl'
          });
		  
		  $routeProvider.otherwise({redirectTo: '/home'});
		  
		  //$locationProvider.html5Mode(true).hashPrefix('!');
		  
		}]);
	
});